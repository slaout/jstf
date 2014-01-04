//package org.jstf.test;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//
//import org.jstf.generator.TranslationGenerator;
//import org.jstf.generator.TranslationGeneratorOptions;
//
//public class GenerateStressTest {
//
//	// Line Separator
//	private static final String LS = "\r\n";
//
//	private static final String CLASS_ENCODING = "UTF-8";
//
//	public static void main(String[] args) throws IOException {
//		String outClassesFolder = "../jstf-test/src";
//
//		// Stress-test
//		generateStringsFile(8221, 0, "../jstf-test/res/stresstest");
//		{
//			String xmlFolder = "../jstf-test/res/stresstest";
//			String packageName = "org.jstf.test.messages.stresstest";
//			TranslationGeneratorOptions options = new TranslationGeneratorOptions();
//			options.i18nClassName = "I18nStressTest";
//			TranslationGenerator.generate(xmlFolder, outClassesFolder, packageName, options);
//		}
//
////		// Stress-test plural
////		generateStringsFile(0, 1644, "../jstf-test/res/stresstestplural");
////		{
////			String xmlFolder = "../jstf-test/res/stresstestplural";
////			String packageName = "org.jstf.test.messages.stresstestplural";
////			TranslationGeneratorOptions options = new TranslationGeneratorOptions();
////			options.i18nClassName = "I18nStressTestPlural";
////			TranslationGenerator.generate(xmlFolder, outClassesFolder, packageName, options);
////		}
//
//		// Stress-test methods
//		generateStringsFile(8221, 0, "../jstf-test/res/stresstestmethods");
//		{
//			String xmlFolder = "../jstf-test/res/stresstestmethods";
//			String packageName = "org.jstf.test.messages.stresstestmethods";
//			TranslationGeneratorOptions options = new TranslationGeneratorOptions();
//			options.i18nClassName = "I18nStressTestMethods";
//			options.useConstantsInsteadOfMethods = false;
//			TranslationGenerator.generate(xmlFolder, outClassesFolder, packageName, options);
//		}
//	}
//
//	private static void generateStringsFile(int stringCount, int pluralCount, String outClassPathFolder) throws IOException {
//		String fileName = "strings.xml";
//
//		StringBuilder builder = new StringBuilder();
//
//		builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + LS);
//		builder.append("<resources>" + LS);
//
//		for (int i = 0; i < stringCount; i++) {
//			String text = "abcdefghijklmno " + i;
//			builder.append("\t<string name=\"key" + i + "\">" + text + "</string>" + LS);
//		}
//
//		for (int i = 0; i < pluralCount; i++) {
//			String text = "" +
//					/**/"\t<plurals name=\"plural" + i + "\">" + LS +
//					/**///"\t\t<item quantity=\"zero\">No %d item " + i + "</item>" + LS +
//					/**/"\t\t<item quantity=\"one\">One %d item " + i + "</item>" + LS +
//					/**///"\t\t<item quantity=\"two\">Two %d items " + i + "</item>" + LS +
//					/**///"\t\t<item quantity=\"few\">Few %d items " + i + "</item>" + LS +
//					/**///"\t\t<item quantity=\"many\">Many %d items " + i + "</item>" + LS +
//					/**/"\t\t<item quantity=\"other\">Other %d items " + i + "</item>" + LS +
//					/**/"\t</plurals>" + LS;
//			builder.append(text);
//		}
//
//		builder.append("</resources>" + LS);
//
//		saveFile(builder, outClassPathFolder + File.separator + fileName);
//	}
//
//	
//	private static void saveFile(StringBuilder builder, String fileName) throws IOException {
//		System.out.println("Generated " + fileName);
//
//		File classFile = new File(fileName);
//		classFile.getParentFile().mkdirs();
//
//		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), CLASS_ENCODING));
//		try {
//			out.write(builder.toString());
//		} finally {
//			out.close();
//		}
//	}
//
//}
