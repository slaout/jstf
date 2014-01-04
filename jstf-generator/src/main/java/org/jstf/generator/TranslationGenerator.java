package org.jstf.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jstf.generator.parser.FormatterParser;
import org.jstf.generator.parser.MessageFormatParser;
import org.jstf.generator.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TranslationGenerator {

	private static final Pattern XML_FILE_NAME_PATTERN = Pattern.compile("^[^_]+(_([a-z]{2}(_[A-Z]{2})?))?\\.xml$");
	// @see http://docs.oracle.com/javase/7/docs/api/java/util/Locale.html
	// TODO private static final Pattern XML_FILE_NAME_PATTERN = Pattern.compile("^[^_]+(_([a-zA-Z]{2,8}(_[a-zA-Z]{2})?))?\\.xml$");

	// Line Separator
	private static final String LS = "\r\n"; // TODO Option ?

	// Generated classes encoding (no need for this to be an option because they are English written and the translation non-ISO-8859-1 characters are \u0000 escaped)
	private static final String CLASS_ENCODING = "UTF-8";

	private static final String[] JAVA_RESERVED_KEYWORDS = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while" };

	private static final String[] QUANTITY_IDS = { "zero", "one", "two", "few", "many", "other" };

	public static void generate(String xmlFolder, String outClassPathFolder) throws IOException {
		generate(xmlFolder, outClassPathFolder, null);
	}

	/**
	 * See http://docs.oracle.com/javase/1.4.2/docs/api/java/text/MessageFormat.html
	 * 
	 * @param xmlFolder eg. projectFolder + "/res"
	 * @param outClassPathFolder eg. projectFolder + "/src/org.jstf/messages"
	 * @throws IOException
	 */
	public static void generate(String xmlFolder, String outClassPathFolder, TranslationGeneratorOptions options) throws IOException {

		// If options is null, use default options
		TranslationGeneratorOptions filledOptions = options;
		if (options == null) {
			filledOptions = new TranslationGeneratorOptions();
		}
		if (filledOptions.packageName == null || filledOptions.packageName.length() == 0) {
			filledOptions.packageName = "org.jstf.messages";
		}

		// TODO Also allow .properties source files instead of XML?
		// (for backward compatibility with other projects than Android ones)

		// TODO Test HTML in translations

		// TODO Profile execution to optimize for compilation speed?

		// TODO Use Locale class everywhere because some languages can have 3 letters
		Set<String> locales = new TreeSet<String>();

		// Map<stringMessageKey, Map<locale, translatedString>>
		Map<String, Map<String, String>> strings = new TreeMap<String, Map<String, String>>();

		// Map<pluralMessageKey, Map<locale, translatedPlurals>>
		Map<String, Map<String, String[]>> plurals = new TreeMap<String, Map<String, String[]>>();

		// Load all XML files for all languages
		File[] files = new File(xmlFolder).listFiles();
		File defaultFile = null;
		for (File file : files) {
			Matcher matcher = XML_FILE_NAME_PATTERN.matcher(file.getName());
			if (matcher.find()) {
				// Load this language XML file in the memory structure
				String locale = matcher.group(2);
				if (locale != null && locale.length() > 0) {
					locales.add(locale);
					parseXmlFile(locale, file.getPath(), strings, plurals);
				} else {
					// File with no locale information (eg. "strings.xml" is the default locale file): process it later when we will be sure to know the default locale
					defaultFile = file;
				}
			}
		}

		// Make sure the developer-provided default locale exists
		// (if there is a default file, the developer-provided default locale will exist, no matter what)
		if (filledOptions.defaultLocale != null && defaultFile == null && !locales.contains(filledOptions.defaultLocale)) {
			if (filledOptions.defaultLocale.length() == 5 && locales.contains(filledOptions.defaultLocale.substring(0, 2))) {
				filledOptions.defaultLocale = filledOptions.defaultLocale.substring(0, 2);
			} else {
				filledOptions.defaultLocale = null;
			}
		}

		// If no default valid locale is provided, find one
		if (filledOptions.defaultLocale == null) {
			if (locales.contains("en") || defaultFile != null) {
				// Use "en" as the default locale (if there is a default file, it is inferred to be English)...
				filledOptions.defaultLocale = "en";
			} else if (locales.contains("en_US")) {
				// ... or "en_US" if there is no "en"...
				filledOptions.defaultLocale = "en_US";
			} else if (locales.size() > 0) {
				// ... or the first available locale in last resort
				filledOptions.defaultLocale = locales.iterator().next();
				if (locales.size() > 1) {
					System.err.println("WARN: The default locale has been randomly chosen to \"" + filledOptions.defaultLocale + "\": it may be different on another build.");
				}
			}
		}

		// Now, we know the default locale for sure: parse the file with default locale, if any
		if (defaultFile != null) {
			locales.add(filledOptions.defaultLocale);
			parseXmlFile(filledOptions.defaultLocale, defaultFile.getPath(), strings, plurals);
		}

		// Search for duplicate keys
		// TODO "component.label", "component_label", and "component-label" are duplicate keys too!

		// A key cannot describe a string and a plurals at the same time
		Set<String> duplicateKeys = new TreeSet<String>(strings.keySet());
		duplicateKeys.retainAll(plurals.keySet());
		if (duplicateKeys.size() > 0) {
			for (String duplicateKey : duplicateKeys) {
				System.err.println("ERROR: The key \"" + duplicateKey + "\" describes both a string and plurals.");
			}
			return;
		}

		// TODO JUnit to test all cases

		// Fill missing specific translations (eg. "fr_BE") with parent ones (eg. "fr"), for instance, or from the default "en_US" if it is not translated
		String inheritedLocale = filledOptions.inheritDefaultLocale ? filledOptions.defaultLocale : null;
		inheritMessages(strings, locales, inheritedLocale);
		inheritPlurals(plurals, locales, inheritedLocale);

		// Ensure all messages are translated into all languages
		ensureAllStringsAreTranslated(strings, locales);
		ensureAllPluralsAreTranslated(plurals, locales);

		// Clean the output folder
		cleanOutputPackage(outClassPathFolder, filledOptions.packageName);

		// Generate the I18n class with all string and plural codes
		generateMessageCodesClass(strings, plurals, filledOptions.packageName, outClassPathFolder, filledOptions);

		// Generate the MessageXxXx classes, listing all translations of a locale
		for (String locale : locales) {
			generateLocaleClass(locale, strings, plurals, filledOptions.packageName, outClassPathFolder);
		}

		// Generate the class MessageLoader
		generateMessageLoaderClass(locales, filledOptions.defaultLocale, filledOptions.useMessageFormatInsteadOfFormatter, filledOptions.packageName, outClassPathFolder);
	}

	private static void cleanOutputPackage(String outClassPathFolder, String packageName) {
		String folderPath = outClassPathFolder + File.separator + packageName.replace(".", File.separator);

		// Create directory if absent
		System.out.println("Removing " + folderPath + File.separator + "*.java");
		File folder = new File(folderPath);
		folder.mkdirs();

		// Remove generated Java files if present
		for (String fileName : folder.list()) {
			if (fileName.endsWith(".java")) {
				new File(folderPath + File.separator + fileName).delete();
			}
		}
	}

	private static void parseXmlFile(String locale, String xmlFileName, Map<String, Map<String, String>> strings, Map<String, Map<String, String[]>> plurals) {
		try {
			// Load XML file
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFileName);

			Element root = document.getDocumentElement();

			// Parse all <string/> tags
			NodeList stringNodes = root.getElementsByTagName("string");
			int stringCount = stringNodes.getLength();
			for (int i = 0; i < stringCount; ++i) {
				Node stringNode = stringNodes.item(i);
				String key = stringNode.getAttributes().getNamedItem("name").getTextContent();
				String value = stringNode.getTextContent();

				// Map<locale, translatedString>
				Map<String, String> mapForKey = strings.get(key);
				if (mapForKey == null) {
					mapForKey = new TreeMap<String, String>();
					strings.put(key, mapForKey);
				}

				mapForKey.put(locale, value);
			}

			// Parse all <plurals/> tags
			NodeList pluralNodes = root.getElementsByTagName("plurals");
			int pluralCount = pluralNodes.getLength();
			for (int i = 0; i < pluralCount; ++i) {
				String[] quantityStrings = new String[QUANTITY_IDS.length];
				Node pluralNode = pluralNodes.item(i);
				NodeList itemNodes = pluralNode.getChildNodes();
				for (int j = 0; j < QUANTITY_IDS.length; j++) {
					quantityStrings[j] = getQuantityString(itemNodes, QUANTITY_IDS[j]);
				}

				String key = pluralNode.getAttributes().getNamedItem("name").getTextContent();
				// Map<locale, translatedPlurals>
				Map<String, String[]> mapForKey = plurals.get(key);
				if (mapForKey == null) {
					mapForKey = new TreeMap<String, String[]>();
					plurals.put(key, mapForKey);
				}

				mapForKey.put(locale, quantityStrings);
			}
		}
		catch (Exception e) {
			System.out.println("Error loading strings file " + xmlFileName);
		}
	}

	private static String getQuantityString(NodeList itemNodes, String quantity) {
		for (int i = 0; i < itemNodes.getLength(); i++) {
			Node itemNode = itemNodes.item(i);
			if (itemNode.getAttributes() != null && quantity.equals(itemNode.getAttributes().getNamedItem("quantity").getNodeValue())) {
				return itemNode.getTextContent();
			}
		}
		return null;
	}

	private static void inheritMessages(Map<String, Map<String, String>> strings, Set<String> locales, String defaultLocale) {
		for (Map<String, String> stringsForOneKey : strings.values()) {
			for (String locale : locales) {
				if (!stringsForOneKey.containsKey(locale)) {
					String language = locale.substring(0, 2);
					if (locale.length() == 5 && stringsForOneKey.containsKey(language)) {
						stringsForOneKey.put(locale, stringsForOneKey.get(language));
					} else if (defaultLocale != null && stringsForOneKey.containsKey(defaultLocale)) {
						stringsForOneKey.put(locale, stringsForOneKey.get(defaultLocale));
					}
				}
			}
		}
	}

	private static void inheritPlurals(Map<String, Map<String, String[]>> plurals, Set<String> locales, String defaultLocale) {
		for (Map<String, String[]> pluralsForOneKey : plurals.values()) {
			for (String locale : locales) {
				if (!pluralsForOneKey.containsKey(locale)) {
					String language = locale.substring(0, 2);
					if (locale.length() == 5 && pluralsForOneKey.containsKey(language)) {
						pluralsForOneKey.put(locale, pluralsForOneKey.get(language));
					} else if (defaultLocale != null && pluralsForOneKey.containsKey(defaultLocale)) {
						pluralsForOneKey.put(locale, pluralsForOneKey.get(defaultLocale));
					}
				}
			}
		}
	}

	private static void ensureAllStringsAreTranslated(Map<String, Map<String, String>> strings, Set<String> locales) {
		int localeCount = locales.size();

		for (Entry<String, Map<String, String>> entry : strings.entrySet()) {
			if (entry.getValue().size() < localeCount) {
				List<String> missingLocales = new ArrayList<String>(locales);
				missingLocales.removeAll(entry.getValue().keySet());
				System.err.println("ERROR: String key \"" + entry.getKey() + "\" is missing value" + (missingLocales.size() > 1 ? "s" : "") + " for locale" + (missingLocales.size() > 1 ? "s" : "") + " " + missingLocales);
				// TODO: add all errors in a list, for better processing later
				// And it allows to output Maven plugin errors, or Eclipse markers...
			}
		}
	}

	private static void ensureAllPluralsAreTranslated(Map<String, Map<String, String[]>> plurals, Set<String> locales) {
		int localeCount = locales.size();

		for (Entry<String, Map<String, String[]>> entry : plurals.entrySet()) {
			if (entry.getValue().size() < localeCount) {
				List<String> missingLocales = new ArrayList<String>(locales);
				missingLocales.removeAll(entry.getValue().keySet());
				System.err.println("ERROR: Plural key \"" + entry.getKey() + "\" is missing value" + (missingLocales.size() > 1 ? "s" : "") + " for locale" + (missingLocales.size() > 1 ? "s" : "") + " " + missingLocales);
			}
			// For each locale, check that the default category (mainly OTHER) is not null
		}
	}

	private static void generateMessageLoaderClass(Set<String> locales, String defaultLocale, boolean useMessageFormatInsteadOfFormatter, String packageName, String outClassPathFolder) throws IOException {
		String className = "MessageLoader";

		StringBuilder builder = new StringBuilder();

		builder.append("package ").append(packageName).append(";").append(LS);
		builder.append(LS);
		builder.append("import org.jstf.lib.MessageBase;").append(LS);
		builder.append(LS);
		builder.append("class ").append(className).append(" implements org.jstf.lib.MessageLoader {").append(LS);
		builder.append(LS);

		builder.append("\tpublic MessageBase getMessages(String language, String country) {").append(LS);
		boolean isFirst = true;
		// Priority to specialized language+country
		for (String locale : locales) {
			if (locale.length() == 5) {
				generateMessageLoaderForLocale(builder, isFirst, locale);
				isFirst = false;
			}
		}
		// And then the general language
		for (String locale : locales) {
			if (locale.length() == 2) {
				generateMessageLoaderForLocale(builder, isFirst, locale);
				isFirst = false;
			}
		}

		builder.append("\t\t} else {").append(LS);
		String newLocaleClass = "null";
		if (defaultLocale != null) {
			newLocaleClass = camelCaseLocale(defaultLocale.substring(0, 2));
			if (defaultLocale.length() == 5) {
				newLocaleClass += camelCaseLocale(defaultLocale.substring(3, 5).toLowerCase());
			}
			newLocaleClass = "new Message" + newLocaleClass + "()";
		}
		builder.append("\t\t\treturn ").append(newLocaleClass).append(";").append(LS);
		builder.append("\t\t}").append(LS);
		builder.append("\t}").append(LS);
		builder.append(LS);

		builder.append("\tpublic boolean isUsingMessageFormatInsteadOfFormatter() {").append(LS);
		builder.append("\t\treturn ").append(useMessageFormatInsteadOfFormatter ? "true" : "false").append(";").append(LS);
		builder.append("\t}").append(LS);
		builder.append(LS);

		builder.append("}").append(LS);

		saveClassFile(builder, outClassPathFolder, packageName, className);
	}

	private static void generateMessageLoaderForLocale(StringBuilder builder, boolean isFirst, String locale) {
		String language = locale.substring(0, 2).toLowerCase();
		String country = (locale.length() >=5 ? locale.substring(3, 5).toUpperCase() : null);

		builder.append("\t\t");
		if (!isFirst) {
			builder.append("} else ");
		}
		builder.append("if (\"").append(language).append("\".equals(language)");
		if (country != null && country.length() == 2) {
			builder.append(" && \"" + country + "\".equals(country)");
		}
		builder.append(") {").append(LS);
		String lc = camelCaseLocale(language);
		if (country != null && country.length() == 2) {
			lc += camelCaseLocale(country.toLowerCase());
		}
		builder.append("\t\t\treturn new Message").append(lc).append("();").append(LS);
	}

	private static void generateMessageCodesClass(Map<String, Map<String, String>> strings, Map<String, Map<String, String[]>> plurals, String packageName, String outClassPathFolder, TranslationGeneratorOptions options) throws IOException {
		String className = (options.i18nClassName != null && options.i18nClassName.length() > 0 ? options.i18nClassName : "I18n");

		StringBuilder builder = new StringBuilder();

		builder.append("package ").append(packageName).append(";").append(LS);
		builder.append(LS);
		builder.append("import org.jstf.lib.I18nAccess;").append(LS);
		builder.append(LS);
		builder.append("public class ").append(className).append(" {").append(LS);
		builder.append(LS);

		builder.append("\tprivate static I18nAccess i18nAccess = new I18nAccess(new MessageLoader());").append(LS);
		builder.append(LS);

		if (!options.useConstantsInsteadOfMethods) {
			// TODO Javadoc
			builder.append("\tpublic static String get(char keyCode, Object... params) {").append(LS);
			builder.append("\t\treturn i18nAccess.get(keyCode, params);").append(LS);
			builder.append("\t}").append(LS);
			builder.append(LS);

			// TODO Javadoc
			builder.append("\tpublic static String get(short keyCode, int quantity, Object... params) {").append(LS);
			builder.append("\t\treturn i18nAccess.get(keyCode, quantity, params);").append(LS);
			builder.append("\t}").append(LS);
			builder.append(LS);
		}

		int i = 0;
		for (String key : strings.keySet()) {
			if (i > Character.MAX_VALUE) {
				System.err.println("ERROR: There are more than " + Character.MAX_VALUE + " strings in your XML file.");
				return;
			}
			String normalizedKey = toJavaIdentifier(key);
			if (checkKeyName(normalizedKey)) {
				if (options.useConstantsInsteadOfMethods) {
					String constantName = (options.toStandardJavaConstantNaming ? toConstantCase(normalizedKey) : normalizedKey);
					builder.append("\tpublic static final char ").append(constantName).append(" = ").append(i).append(";").append(LS);
				} else {
					builder.append("\tpublic static String ").append(normalizedKey).append("(");
					String[] argumentTypes = getArgumentTypes(strings.get(key), options.useMessageFormatInsteadOfFormatter);
					boolean isFirst = true;
					// MessageFormat: "{0} {1}" / Formatter: "%1$s %2$s"
					final int firstArgNumber = (options.useMessageFormatInsteadOfFormatter ? 0 : 1);
					int argNumber = firstArgNumber;
					for (String argumentType : argumentTypes) {
						if (isFirst) {
							isFirst = false;
						} else {
							builder.append(", ");
						}
						builder.append(argumentType).append(" $").append(argNumber);
						argNumber++;
					}
					builder.append(") {").append(LS);
					builder.append("\t\treturn i18nAccess.get((char) ").append(i);
					for (int j = 0; j < argumentTypes.length; j++) {
						builder.append(", $").append(j + firstArgNumber);
					}
					builder.append(");").append(LS);
					builder.append("\t}").append(LS);
					builder.append(LS);
				}
				i++;
			} else {
				System.err.println("WARN: String key name \"" + key + "\" contains illegal characters.");
			}
		}

		if (strings.size() > 0 && plurals.size() > 0) {
			builder.append(LS);
		}

		i = Short.MIN_VALUE;
		for (String key : plurals.keySet()) {
			if (i > Short.MAX_VALUE) {
				System.err.println("ERROR: There are more than " + Character.MAX_VALUE + " plurals in your XML file.");
				return;
			}
			String normalizedKey = toJavaIdentifier(key);
			if (checkKeyName(normalizedKey)) {
				if (options.useConstantsInsteadOfMethods) {
					String constantName = (options.toStandardJavaConstantNaming ? toConstantCase(normalizedKey) : normalizedKey);
					builder.append("\tpublic static final short ").append(constantName).append(" = ").append(i).append(";").append(LS);
				} else {
					builder.append("\tpublic static String ").append(normalizedKey).append("(int quantity");
					String[] argumentTypes = getArgumentTypes(plurals.get(key), options.useMessageFormatInsteadOfFormatter);
					// MessageFormat: "{0} {1}" / Formatter: "%1$s %2$s"
					final int firstArgNumber = (options.useMessageFormatInsteadOfFormatter ? 0 : 1);
					int argNumber = firstArgNumber;
					for (String argumentType : argumentTypes) {
						builder.append(", ").append(argumentType).append(" $").append(argNumber);
						argNumber++;
					}
					builder.append(") {").append(LS);
					builder.append("\t\treturn i18nAccess.get((short) ").append(i).append(", quantity");
					for (int j = 0; j < argumentTypes.length; j++) {
						builder.append(", $").append(j + firstArgNumber);
					}
					builder.append(");").append(LS);
					builder.append("\t}").append(LS);
					builder.append(LS);
				}
				i++;
			} else {
				System.err.println("WARN: Plural key name \"" + key + "\" contains illegal characters.");
			}
		}

		builder.append(LS);
		builder.append("}").append(LS);

		saveClassFile(builder, outClassPathFolder, packageName, className);
	}

	private static String[] getArgumentTypes(Map<String, ?> messagesByLocale, boolean useMessageFormatInsteadOfFormatter) {
		String message = null;
		findMessage:
		for (Object value : messagesByLocale.values()) {
			if (value instanceof String[]) {
				String[] stringArray = (String[]) value;
				for (String subValue : stringArray) {
					if (subValue.length() > 0) {
						message = subValue;
						break findMessage;
					}
				}
			} else if (value instanceof String) {
				String stringValue = (String) value;
				if (stringValue.length() > 0) {
					message = stringValue;
					break;
				}
			}
		}

		if (message == null) {
			return new String[0];
		}

		if (useMessageFormatInsteadOfFormatter) {
			return MessageFormatParser.getArgumentTypes(message);
		} else {
			return FormatterParser.getArgumentTypes(message);
		}
	}

	private static boolean checkKeyName(String key) {
		if (key == null || key.length() == 0) {
			return false;
		}

		if (!Character.isJavaIdentifierStart(key.charAt(0))) {
			return false;
		}

		int keyLength = key.length();
		for (int i = 1; i < keyLength; i++) {
			if (!Character.isJavaIdentifierPart(key.charAt(i))) {
				return false;
			}
		}

		for(int i = 0; i < JAVA_RESERVED_KEYWORDS.length; i++) {
			String reservedKeyword = JAVA_RESERVED_KEYWORDS[i];
			if (reservedKeyword.equals(key)) {
				return false;
			}
		}

		return true;
	}

	private static void generateLocaleClass(String locale, Map<String, Map<String, String>> strings, Map<String, Map<String, String[]>> plurals, String packageName, String outClassPathFolder) throws IOException {
		String className = "Message" + camelCaseLocale(locale);

		StringBuilder builder = new StringBuilder();

		builder.append("package ").append(packageName).append(";").append(LS);
		builder.append(LS);
		builder.append("import org.jstf.lib.MessageBase;").append(LS);
		builder.append(LS);
		builder.append("class ").append(className).append(" implements MessageBase {").append(LS);
		builder.append(LS);

		builder.append("\tprivate static final String[] strings = {").append(LS);
		int keySetSize = strings.keySet().size();
		int i = 0;
		for (Entry<String, Map<String, String>> entry : strings.entrySet()) {
			String value = StringUtils.escapeJava(entry.getValue().get(locale));
			builder.append("\t\t").append(value == null ? "null" : "\"" + value + "\"").append(i < keySetSize - 1 ? "," : "").append(LS);
			i++;
		}
		builder.append("\t};").append(LS);
		builder.append(LS);

		builder.append("\tprivate static final String[][] plurals = {").append(LS);
		keySetSize = plurals.keySet().size();
		i = 0;
		for (Entry<String, Map<String, String[]>> entry : plurals.entrySet()) {
			builder.append("\t\t{").append(LS);
			String[] values = entry.getValue().get(locale);
			if (values != null) {
				for(int j = 0; j < values.length; j++) {
					String value = StringUtils.escapeJava(values[j]);
					builder.append("\t\t\t").append(value == null ? "null" : "\"" + value + "\"").append(j < values.length - 1 ? "," : "").append(LS);
				}
			}
			builder.append("\t\t}").append(i < keySetSize - 1 ? "," : "").append(LS);
			i++;
		}
		builder.append("\t};").append(LS);
		builder.append(LS);

		builder.append("\tpublic final String getLanguage() {").append(LS);
		builder.append("\t\treturn \"").append(locale.substring(0, 2)).append("\";").append(LS);
		builder.append("\t}").append(LS);
		builder.append(LS);

		String countryString = (locale.length() == 5 ? "\"" + locale.substring(3, 5) + "\"" : "null");
		builder.append("\tpublic final String getCountry() {").append(LS);
		builder.append("\t\treturn ").append(countryString).append(";").append(LS);
		builder.append("\t}").append(LS);
		builder.append(LS);

		builder.append("\tpublic final String[] getStrings() {").append(LS);
		builder.append("\t\treturn ").append(className).append(".strings;").append(LS);
		builder.append("\t}").append(LS);
		builder.append(LS);

		builder.append("\tpublic final String[][] getPlurals() {").append(LS);
		builder.append("\t\treturn ").append(className).append(".plurals;").append(LS);
		builder.append("\t}").append(LS);
		builder.append(LS);

		builder.append("}").append(LS);

		saveClassFile(builder, outClassPathFolder, packageName, className);
	}

	/**
	 * "en_US" => "EnUs" ; "en" => "En"
	 * @param locale
	 * @return
	 */
	private static String camelCaseLocale(String locale) {
		String camelCase = locale.substring(0, 1).toUpperCase() + locale.substring(1);
		if (camelCase.length() == 5) {
			camelCase = camelCase.substring(0, 2) + camelCase.substring(3, 4).toUpperCase() + camelCase.substring(4).toLowerCase();
		}
		return camelCase;
	}

	private static void saveClassFile(StringBuilder builder, String outClassPathFolder, String packageName, String className) throws IOException {
		String fileName = outClassPathFolder + File.separator + packageName.replace(".", File.separator) + File.separator + className + ".java";
		System.out.println("Generated " + fileName);

		File classFile = new File(fileName);
		classFile.getParentFile().mkdirs();

		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), CLASS_ENCODING));
		try {
			out.write(builder.toString());
		} finally {
			out.close();
		}
	}

	private static String toJavaIdentifier(String key) {
		String javaIdentifier = key;
		javaIdentifier = javaIdentifier.replace('.', '_');
		javaIdentifier = javaIdentifier.replace('-', '_');
		return javaIdentifier;
	}

	/**
	 * "CamelCase42ToSomethingElseX84" => "CAMEL_CASE_42_TO_SOMETHING_ELSE_X_84"
	 *
	 * @param key
	 * @return
	 */
	private static String toConstantCase(String key) {
		String regex = "([a-z])([A-Z0-9])";
		String replacement = "$1_$2";
		String constant = key.replaceAll(regex, replacement);

		String regex2 = "([0-9])([A-Za-z])";
		constant = constant.replaceAll(regex2, replacement);

		String regex3 = "([A-Z])([0-9])";
		constant = constant.replaceAll(regex3, replacement);

		return constant.toUpperCase();
	}

}
