package org.jstf.generator;

public class TranslationGeneratorOptions {

	/**
	 * The package where to generate the message classes (I18n, Message* and MessageLoader) for the currently processed XML files.<br>
	 * If null or empty, it will default to "org.jstf.messages".
	 */
	public String packageName;

	/**
	 * When having several XML message sources, they are generated into different packages.<br>
	 * By default, each package contains the I18n access class.<br>
	 * You are encouraged to rename this class so you can nicely import two or more I18n classes to use several message sources in one Java class.
	 * @see http://stackoverflow.com/questions/4468388/maximum-number-of-enum-elements-in-java for technical reasons to split translations into several packages. But it is also better from the business point of view to use separate message bundles.
	 */
	public String i18nClassName;

	/**
	 * The default locale to use for users having a locale that does not match any XML translation files.<br>
	 * If null or empty, default is either "en" if a message XML file exists for this language or "en_US" if a message XML file exists for this locale, and in last resort the first found file will be used, although it means that a random file will be chosen at build time, and a different one could be chosen on another build. If none exist, there is no default locale, since there is no translation available.
	 */
	public String defaultLocale;

	/**
	 * Set to true (the default value) to eg. use "en" messages for "fr" untranslated messages.<br>
	 * Set to false to get a warning at build-time and a null translations for missing messages at run-time.<br>
	 * Default is true for production convenience; consider using false from time to time to check for missing translations.
	 */
	public boolean inheritDefaultLocale = true;

	/**
	 * If false (the default value), the generated I18n class contains one static method per message, the name of the method being the message key and the parameters matching the message parameters (the %s, %d or {1}, for instance).<br>
	 * If true, the generated I18n class contains two static get(...) methods to get translations, with the first parameter being the message key: one of the generated I18n constants.
	 */
	public boolean useConstantsInsteadOfMethods;

	/**
	 * Useful only when useConstantsInsteadOfMethods is true.<br>
	 * Generated I18n.* constants will be named as the <string name="..."/> are named in XML files.<br>
	 * Some Checkstyle or PMD might complain about the final fields not being all upper-case.<br>
	 * You can set this option to true (default is false) to eg. transform key name "keyName42" to a constant named "KEY_NAME_42"
	 */
	public boolean toStandardJavaConstantNaming;

	/**
	 * Generate code with "MessageFormat" syntax (eg. "Message {1}") instead of the default "Formatter" syntax (eg. "Message %s").
	 * @see http://docs.oracle.com/javase/1.5.0/docs/api/java/text/MessageFormat.html
	 * @see http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html
	 */
	public boolean useMessageFormatInsteadOfFormatter;

}
