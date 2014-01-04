//package org.jstf.test;
//
//import java.util.Locale;
//
//import org.jstf.test.messages.pack1.I18n;
//import org.jstf.test.messages.pack2.I18nPack2;
//import org.jstf.test.messages.packmethods.I18nMethods;
//import org.jstf.test.messages.stresstest.I18nStressTest;
//import org.jstf.test.messages.stresstestmethods.I18nStressTestMethods;
//
//public class TestProject {
//
//	public static void main(String[] args) {
//		Locale.setDefault(new Locale("en", ""));
//
//		// Pack 1
//
//		System.out.println(I18n.get(Character.MAX_VALUE, "test", 42));
//		System.out.println(I18n.get(I18n.keyNeedingJavaEscaping));
//		System.out.println(I18n.get(I18n.keyWithMissingNonDefaultTranslations));
//		System.out.println(I18n.get(I18n.keyWithNoEnUkTranslation));
//		System.out.println(I18n.get(I18n.keyWithNoFrTranslation));
//		System.out.println(I18n.get(I18n.normalKeyWith1Parameter, 42));
//		System.out.println(I18n.get(I18n.normalKeyWith2Parameters, "test", 42));
//		System.out.println(I18n.get(I18n.normalSimpleKey));
//		System.out.println(I18n.get(I18n.keyWithEmptyMessage));
//		// System.out.println(I18n.get(I18n.unusedKey));
//
//		System.out.println("--");
//
//		System.out.println(I18n.get(I18n.pluralKey, 0, 0));
//		System.out.println(I18n.get(I18n.pluralKey, 1, 1));
//		System.out.println(I18n.get(I18n.pluralKey, 42, 42));
//
//		System.out.println("--");
//
//		// Pack 2
//		System.out.println(I18nPack2.get(I18nPack2.STRING_TEST_PACK_2, 42));
//		System.out.println(I18nPack2.get(I18nPack2.PLURAL_TEST_PACK_2, 42, 42));
//
//		System.out.println("--");
//
//		// Methods
//		System.out.println(I18nMethods.keyNeedingJavaEscaping());
//		System.out.println(I18nMethods.normalKeyWith2Parameters("test", 42));
//		System.out.println(I18nMethods.pluralKey(42, 42));
//
//		System.out.println("--");
//
//		// Stress-test
//		long startTime = System.nanoTime();
//		I18nStressTest.get(I18nStressTest.key1000);
//		long stopTime = System.nanoTime();
//		I18nStressTest.get(I18nStressTest.key1000);
//		long stopTime2 = System.nanoTime();
//		System.out.println("Stress-test first-translation: " + (stopTime - startTime) + " ns");
//		System.out.println("Stress-test second-translation: " + (stopTime2 - stopTime) + " ns");
//
//		System.out.println("--");
//
//		// Stress-test with methods
//		startTime = System.nanoTime();
//		I18nStressTestMethods.key950();
//		stopTime = System.nanoTime();
//		I18nStressTestMethods.key950();
//		stopTime2 = System.nanoTime();
//		System.out.println("With method: Stress-test first-translation: " + (stopTime - startTime) + " ns");
//		System.out.println("With method: Stress-test second-translation: " + (stopTime2 - stopTime) + " ns");
//	}
//
//}
