package org.jstf.test;


import java.io.IOException;

import org.jstf.generator.TranslationGenerator;
import org.jstf.generator.TranslationGeneratorOptions;

public class MainGeneration {

	public static void main(String[] args) throws IOException {
		String xmlFolder = "src/main/resources/jstf";
		String outClassPathFolder = "target/generated-sources/jstf";
		TranslationGeneratorOptions options = new TranslationGeneratorOptions();
		options.packageName = "org.jstf.messages";
		TranslationGenerator.generate(xmlFolder, outClassPathFolder, options);

//		// Pack 1
//		{
//			String xmlFolder = "../jstf-test/res/pack1";
//			String packageName = "org.jstf.test.messages.pack1";
//			TranslationGeneratorOptions options = new TranslationGeneratorOptions();
//			TranslationGenerator.generate(xmlFolder, outClassesFolder, packageName, options);
//		}
//
//		// Pack 2
//		{
//			String xmlFolder = "../jstf-test/res/pack2";
//			String packageName = "org.jstf.test.messages.pack2";
//			TranslationGeneratorOptions options = new TranslationGeneratorOptions();
//			options.i18nClassName = "I18nPack2";
//			options.defaultLocale = "fr";
//			options.toStandardJavaConstantNaming = true;
//			options.useMessageFormatInsteadOfFormatter = true;
//			TranslationGenerator.generate(xmlFolder, outClassesFolder, packageName, options);
//		}
//
//		// Pack with methods
//		{
//			String xmlFolder = "../jstf-test/res/packmethods";
//			String packageName = "org.jstf.test.messages.packmethods";
//			TranslationGeneratorOptions options = new TranslationGeneratorOptions();
//			options.i18nClassName = "I18nMethods";
//			options.useConstantsInsteadOfMethods = false;
//			TranslationGenerator.generate(xmlFolder, outClassesFolder, packageName, options);
//		}
	}

}
