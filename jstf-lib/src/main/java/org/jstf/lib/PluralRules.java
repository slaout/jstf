package org.jstf.lib;

import java.util.Locale;


/**
 * Implementing these rules:
 * http://unicode.org/repos/cldr-tmp/trunk/diff/supplemental/language_plural_rules.html
 * Last updated 2012-03-01 03:27:30 GMT
 */
public abstract class PluralRules {

	public static final int CATEGORY_ZERO = 0;
	public static final int CATEGORY_ONE = 1;
	public static final int CATEGORY_TWO = 2;
	public static final int CATEGORY_FEW = 3;
	public static final int CATEGORY_MANY = 4;
	public static final int CATEGORY_OTHER = 5;

	private static final String LANGUAGE_AFRIKAANS = "af";
	private static final String LANGUAGE_AKAN = "ak";
	private static final String LANGUAGE_ALBANIAN = "sq";
	private static final String LANGUAGE_AMHARIC = "am";
	private static final String LANGUAGE_ARABIC = "ar";
	private static final String LANGUAGE_ASU = "asa";
	private static final String LANGUAGE_AZERBAIJANI = "az";
	private static final String LANGUAGE_BAMBARA = "bm";
	private static final String LANGUAGE_BASQUE = "eu";
	private static final String LANGUAGE_BELARUSIAN = "be";
	private static final String LANGUAGE_BEMBA = "bem";
	private static final String LANGUAGE_BENA = "bez";
	private static final String LANGUAGE_BENGALI = "bn";
	private static final String LANGUAGE_BIHARI = "bh";
	private static final String LANGUAGE_BODO = "brx";
	private static final String LANGUAGE_BOSNIAN = "bs";
	private static final String LANGUAGE_BRETON = "br";
	private static final String LANGUAGE_BULGARIAN = "bg";
	private static final String LANGUAGE_BURMESE = "my";
	private static final String LANGUAGE_CATALAN = "ca";
	private static final String LANGUAGE_CENTRAL_MOROCCO_TAMAZIGHT = "tzm";
	private static final String LANGUAGE_CHEROKEE = "chr";
	private static final String LANGUAGE_CHIGA = "cgg";
	private static final String LANGUAGE_CHINESE = "zh";
	private static final String LANGUAGE_COLOGNIAN = "ksh";
	private static final String LANGUAGE_CORNISH = "kw";
	private static final String LANGUAGE_CROATIAN = "hr";
	private static final String LANGUAGE_CZECH = "cs";
	private static final String LANGUAGE_DANISH = "da";
	private static final String LANGUAGE_DIVEHI = "dv";
	private static final String LANGUAGE_DUTCH = "nl";
	private static final String LANGUAGE_DZONGKHA = "dz";
	private static final String LANGUAGE_ENGLISH = "en";
	private static final String LANGUAGE_ESPERANTO = "eo";
	private static final String LANGUAGE_ESTONIAN = "et";
	private static final String LANGUAGE_EWE = "ee";
	private static final String LANGUAGE_FAROESE = "fo";
	private static final String LANGUAGE_FILIPINO = "fil";
	private static final String LANGUAGE_FINNISH = "fi";
	private static final String LANGUAGE_FRENCH = "fr";
	private static final String LANGUAGE_FRIULIAN = "fur";
	private static final String LANGUAGE_FULAH = "ff";
	private static final String LANGUAGE_GALICIAN = "gl";
	private static final String LANGUAGE_GANDA = "lg";
	private static final String LANGUAGE_GEORGIAN = "ka";
	private static final String LANGUAGE_GERMAN = "de";
	private static final String LANGUAGE_GREEK = "el";
	private static final String LANGUAGE_GUJARATI = "gu";
	private static final String LANGUAGE_GUN = "guw";
	private static final String LANGUAGE_HAUSA = "ha";
	private static final String LANGUAGE_HAWAIIAN = "haw";
	private static final String LANGUAGE_HEBREW = "he";
	private static final String LANGUAGE_HINDI = "hi";
	private static final String LANGUAGE_HUNGARIAN = "hu";
	private static final String LANGUAGE_ICELANDIC = "is";
	private static final String LANGUAGE_IGBO = "ig";
	private static final String LANGUAGE_INARI_SAMI = "smn";
	private static final String LANGUAGE_INDONESIAN = "id";
	private static final String LANGUAGE_INUKTITUT = "iu";
	private static final String LANGUAGE_IRISH = "ga";
	private static final String LANGUAGE_ITALIAN = "it";
	private static final String LANGUAGE_JAPANESE = "ja";
	private static final String LANGUAGE_JAVANESE = "jv";
	private static final String LANGUAGE_JJU = "kaj";
	private static final String LANGUAGE_KABUVERDIANU = "kea";
	private static final String LANGUAGE_KABYLE = "kab";
	private static final String LANGUAGE_KALAALLISUT = "kl";
	private static final String LANGUAGE_KANNADA = "kn";
	private static final String LANGUAGE_KAZAKH = "kk";
	private static final String LANGUAGE_KHMER = "km";
	private static final String LANGUAGE_KOREAN = "ko";
	private static final String LANGUAGE_KOYRABORO_SENNI = "ses";
	private static final String LANGUAGE_KURDISH = "ku";
	private static final String LANGUAGE_LANGI = "lag";
	private static final String LANGUAGE_LAO = "lo";
	private static final String LANGUAGE_LATVIAN = "lv";
	private static final String LANGUAGE_LINGALA = "ln";
	private static final String LANGUAGE_LITHUANIAN = "lt";
	private static final String LANGUAGE_LULE_SAMI = "smj";
	private static final String LANGUAGE_LUXEMBOURGISH = "lb";
	private static final String LANGUAGE_MACEDONIAN = "mk";
	private static final String LANGUAGE_MACHAME = "jmc";
	private static final String LANGUAGE_MAKONDE = "kde";
	private static final String LANGUAGE_MALAGASY = "mg";
	private static final String LANGUAGE_MALAY = "ms";
	private static final String LANGUAGE_MALAYALAM = "ml";
	private static final String LANGUAGE_MALTESE = "mt";
	private static final String LANGUAGE_MANX = "gv";
	private static final String LANGUAGE_MARATHI = "mr";
	private static final String LANGUAGE_MASAI = "mas";
	private static final String LANGUAGE_MOLDAVIAN = "mo";
	private static final String LANGUAGE_MONGOLIAN = "mn";
	private static final String LANGUAGE_NAHUATL = "nah";
	private static final String LANGUAGE_NAMA = "naq";
	private static final String LANGUAGE_NEPALI = "ne";
	private static final String LANGUAGE_NORTH_NDEBELE = "nd";
	private static final String LANGUAGE_NORTHERN_SAMI = "se";
	private static final String LANGUAGE_NORTHERN_SOTHO = "nso";
	private static final String LANGUAGE_NORWEGIAN = "no";
	private static final String LANGUAGE_NORWEGIAN_BOKMAL = "nb";
	private static final String LANGUAGE_NORWEGIAN_NYNORSK = "nn";
	private static final String LANGUAGE_NYANJA = "ny";
	private static final String LANGUAGE_NYANKOLE = "nyn";
	private static final String LANGUAGE_ORIYA = "or";
	private static final String LANGUAGE_OROMO = "om";
	private static final String LANGUAGE_PAPIAMENTO = "pap";
	private static final String LANGUAGE_PASHTO = "ps";
	private static final String LANGUAGE_PERSIAN = "fa";
	private static final String LANGUAGE_POLISH = "pl";
	private static final String LANGUAGE_PORTUGUESE = "pt";
	private static final String LANGUAGE_PUNJABI = "pa";
	private static final String LANGUAGE_ROMANIAN = "ro";
	private static final String LANGUAGE_ROMANSH = "rm";
	private static final String LANGUAGE_ROMBO = "rof";
	private static final String LANGUAGE_ROOT = "root";
	private static final String LANGUAGE_RUSSIAN = "ru";
	private static final String LANGUAGE_RWA = "rwk";
	private static final String LANGUAGE_SAHO = "ssy";
	private static final String LANGUAGE_SAKHA = "sah";
	private static final String LANGUAGE_SAMBURU = "saq";
	private static final String LANGUAGE_SAMI_LANGUAGE = "smi";
	private static final String LANGUAGE_SANGO = "sg";
	private static final String LANGUAGE_SCOTTISH_GAELIC = "gd";
	private static final String LANGUAGE_SENA = "seh";
	private static final String LANGUAGE_SERBIAN = "sr";
	private static final String LANGUAGE_SERBO_CROATIAN = "sh";
	private static final String LANGUAGE_SHAMBALA = "ksb";
	private static final String LANGUAGE_SHONA = "sn";
	private static final String LANGUAGE_SICHUAN_YI = "ii";
	private static final String LANGUAGE_SKOLT_SAMI = "sms";
	private static final String LANGUAGE_SLOVAK = "sk";
	private static final String LANGUAGE_SLOVENIAN = "sl";
	private static final String LANGUAGE_SOGA = "xog";
	private static final String LANGUAGE_SOMALI = "so";
	private static final String LANGUAGE_SOUTH_NDEBELE = "nr";
	private static final String LANGUAGE_SOUTHERN_SAMI = "sma";
	private static final String LANGUAGE_SOUTHERN_SOTHO = "st";
	private static final String LANGUAGE_SPANISH = "es";
	private static final String LANGUAGE_SWAHILI = "sw";
	private static final String LANGUAGE_SWATI = "ss";
	private static final String LANGUAGE_SWEDISH = "sv";
	private static final String LANGUAGE_SWISS_GERMAN = "gsw";
	private static final String LANGUAGE_SYRIAC = "syr";
	private static final String LANGUAGE_TACHELHIT = "shi";
	private static final String LANGUAGE_TAGALOG = "tl";
	private static final String LANGUAGE_TAMIL = "ta";
	private static final String LANGUAGE_TELUGU = "te";
	private static final String LANGUAGE_TESO = "teo";
	private static final String LANGUAGE_THAI = "th";
	private static final String LANGUAGE_TIBETAN = "bo";
	private static final String LANGUAGE_TIGRE = "tig";
	private static final String LANGUAGE_TIGRINYA = "ti";
	private static final String LANGUAGE_TONGAN = "to";
	private static final String LANGUAGE_TSONGA = "ts";
	private static final String LANGUAGE_TSWANA = "tn";
	private static final String LANGUAGE_TURKISH = "tr";
	private static final String LANGUAGE_TURKMEN = "tk";
	private static final String LANGUAGE_TYAP = "kcg";
	private static final String LANGUAGE_UKRAINIAN = "uk";
	private static final String LANGUAGE_URDU = "ur";
	private static final String LANGUAGE_VENDA = "ve";
	private static final String LANGUAGE_VIETNAMESE = "vi";
	private static final String LANGUAGE_VUNJO = "vun";
	private static final String LANGUAGE_WALLOON = "wa";
	private static final String LANGUAGE_WALSER = "wae";
	private static final String LANGUAGE_WELSH = "cy";
	private static final String LANGUAGE_WESTERN_FRISIAN = "fy";
	private static final String LANGUAGE_WOLOF = "wo";
	private static final String LANGUAGE_XHOSA = "xh";
	private static final String LANGUAGE_YORUBA = "yo";
	private static final String LANGUAGE_ZULU = "zu";

	public static int getDefaultCategory(String languageCode) {
		String language = normalizeLanguage(languageCode);

		if (
				/**/LANGUAGE_BELARUSIAN.equals(language) || 
				/**/LANGUAGE_BOSNIAN.equals(language) || 
				/**/LANGUAGE_CROATIAN.equals(language) ||
				/**/LANGUAGE_RUSSIAN.equals(language) ||
				/**/LANGUAGE_SERBIAN.equals(language) ||
				/**/LANGUAGE_SERBO_CROATIAN.equals(language) ||
				/**/LANGUAGE_UKRAINIAN.equals(language) ||
				/**/LANGUAGE_POLISH.equals(language)
				) {
			return CATEGORY_MANY;
		}

		return CATEGORY_OTHER;
	}

	public abstract int getCategoryForQuantity(int quantity);

	// TODO FractionPluralRules
	// public abstract int getCategoryForQuantity(double quantity);

	public static PluralRules getPluralRulesForLanguage(String languageCode) {
		String language = normalizeLanguage(languageCode);

		if (
				/**/LANGUAGE_AZERBAIJANI.equals(language) ||
				/**/LANGUAGE_BAMBARA.equals(language) ||
				/**/LANGUAGE_BURMESE.equals(language) ||
				/**/LANGUAGE_CHINESE.equals(language) ||
				/**/LANGUAGE_DZONGKHA.equals(language) ||
				/**/LANGUAGE_GEORGIAN.equals(language) ||
				/**/LANGUAGE_HUNGARIAN.equals(language) ||
				/**/LANGUAGE_IGBO.equals(language) ||
				/**/LANGUAGE_INDONESIAN.equals(language) ||
				/**/LANGUAGE_JAPANESE.equals(language) ||
				/**/LANGUAGE_JAVANESE.equals(language) ||
				/**/LANGUAGE_KABUVERDIANU.equals(language) ||
				/**/LANGUAGE_KANNADA.equals(language) ||
				/**/LANGUAGE_KHMER.equals(language) ||
				/**/LANGUAGE_KOREAN.equals(language) ||
				/**/LANGUAGE_KOYRABORO_SENNI.equals(language) ||
				/**/LANGUAGE_LAO.equals(language) ||
				/**/LANGUAGE_MAKONDE.equals(language) ||
				/**/LANGUAGE_MALAY.equals(language) ||
				/**/LANGUAGE_PERSIAN.equals(language) ||
				/**/LANGUAGE_ROOT.equals(language) ||
				/**/LANGUAGE_SAKHA.equals(language) ||
				/**/LANGUAGE_SANGO.equals(language) ||
				/**/LANGUAGE_SICHUAN_YI.equals(language) ||
				/**/LANGUAGE_THAI.equals(language) ||
				/**/LANGUAGE_TIBETAN.equals(language) ||
				/**/LANGUAGE_TONGAN.equals(language) ||
				/**/LANGUAGE_TURKISH.equals(language) ||
				/**/LANGUAGE_VIETNAMESE.equals(language) ||
				/**/LANGUAGE_WOLOF.equals(language) ||
				/**/LANGUAGE_YORUBA.equals(language)
				/**/) {
			return new Other();
		}

		if (
				/**/LANGUAGE_MANX.equals(language)
				/**/) {
			return new Manx();
		}

		if (
				/**/LANGUAGE_CENTRAL_MOROCCO_TAMAZIGHT.equals(language)
				/**/) {
			return new CentralMoroccoTamazight();
		}

		if (
				/**/LANGUAGE_MACEDONIAN.equals(language)
				/**/) {
			return new Macedonian();
		}

		if (
				/**/LANGUAGE_AKAN.equals(language) ||
				/**/LANGUAGE_AMHARIC.equals(language) ||
				/**/LANGUAGE_BIHARI.equals(language) ||
				/**/LANGUAGE_FILIPINO.equals(language) ||
				/**/LANGUAGE_FRENCH.equals(language) ||
				/**/LANGUAGE_FULAH.equals(language) ||
				/**/LANGUAGE_GUN.equals(language) ||
				/**/LANGUAGE_HINDI.equals(language) ||
				/**/LANGUAGE_KABYLE.equals(language) ||
				/**/LANGUAGE_LINGALA.equals(language) ||
				/**/LANGUAGE_MALAGASY.equals(language) ||
				/**/LANGUAGE_NORTHERN_SOTHO.equals(language) ||
				/**/LANGUAGE_TAGALOG.equals(language) ||
				/**/LANGUAGE_TIGRINYA.equals(language) ||
				/**/LANGUAGE_WALLOON.equals(language)
				/**/) {
			return new OneOneOther();
		}

		if (
				/**/LANGUAGE_AFRIKAANS.equals(language) ||
				/**/LANGUAGE_ALBANIAN.equals(language) ||
				/**/LANGUAGE_ASU.equals(language) ||
				/**/LANGUAGE_BASQUE.equals(language) ||
				/**/LANGUAGE_BEMBA.equals(language) ||
				/**/LANGUAGE_BENA.equals(language) ||
				/**/LANGUAGE_BENGALI.equals(language) ||
				/**/LANGUAGE_BODO.equals(language) ||
				/**/LANGUAGE_BULGARIAN.equals(language) ||
				/**/LANGUAGE_CATALAN.equals(language) ||
				/**/LANGUAGE_CHEROKEE.equals(language) ||
				/**/LANGUAGE_CHIGA.equals(language) ||
				/**/LANGUAGE_DANISH.equals(language) ||
				/**/LANGUAGE_DIVEHI.equals(language) ||
				/**/LANGUAGE_DUTCH.equals(language) ||
				/**/LANGUAGE_ENGLISH.equals(language) ||
				/**/LANGUAGE_ESPERANTO.equals(language) ||
				/**/LANGUAGE_ESTONIAN.equals(language) ||
				/**/LANGUAGE_EWE.equals(language) ||
				/**/LANGUAGE_FAROESE.equals(language) ||
				/**/LANGUAGE_FINNISH.equals(language) ||
				/**/LANGUAGE_FRIULIAN.equals(language) ||
				/**/LANGUAGE_GALICIAN.equals(language) ||
				/**/LANGUAGE_GANDA.equals(language) ||
				/**/LANGUAGE_GERMAN.equals(language) ||
				/**/LANGUAGE_GREEK.equals(language) ||
				/**/LANGUAGE_GUJARATI.equals(language) ||
				/**/LANGUAGE_HAUSA.equals(language) ||
				/**/LANGUAGE_HAWAIIAN.equals(language) ||
				/**/LANGUAGE_HEBREW.equals(language) ||
				/**/LANGUAGE_ICELANDIC.equals(language) ||
				/**/LANGUAGE_ITALIAN.equals(language) ||
				/**/LANGUAGE_JJU.equals(language) ||
				/**/LANGUAGE_KALAALLISUT.equals(language) ||
				/**/LANGUAGE_KAZAKH.equals(language) ||
				/**/LANGUAGE_KURDISH.equals(language) ||
				/**/LANGUAGE_LUXEMBOURGISH.equals(language) ||
				/**/LANGUAGE_MACHAME.equals(language) ||
				/**/LANGUAGE_MALAYALAM.equals(language) ||
				/**/LANGUAGE_MARATHI.equals(language) ||
				/**/LANGUAGE_MASAI.equals(language) ||
				/**/LANGUAGE_MONGOLIAN.equals(language) ||
				/**/LANGUAGE_NAHUATL.equals(language) ||
				/**/LANGUAGE_NEPALI.equals(language) ||
				/**/LANGUAGE_NORTH_NDEBELE.equals(language) ||
				/**/LANGUAGE_NORWEGIAN.equals(language) ||
				/**/LANGUAGE_NORWEGIAN_BOKMAL.equals(language) ||
				/**/LANGUAGE_NORWEGIAN_NYNORSK.equals(language) ||
				/**/LANGUAGE_NYANJA.equals(language) ||
				/**/LANGUAGE_NYANKOLE.equals(language) ||
				/**/LANGUAGE_ORIYA.equals(language) ||
				/**/LANGUAGE_OROMO.equals(language) ||
				/**/LANGUAGE_PAPIAMENTO.equals(language) ||
				/**/LANGUAGE_PASHTO.equals(language) ||
				/**/LANGUAGE_PORTUGUESE.equals(language) ||
				/**/LANGUAGE_PUNJABI.equals(language) ||
				/**/LANGUAGE_ROMANSH.equals(language) ||
				/**/LANGUAGE_ROMBO.equals(language) ||
				/**/LANGUAGE_RWA.equals(language) ||
				/**/LANGUAGE_SAHO.equals(language) ||
				/**/LANGUAGE_SAMBURU.equals(language) ||
				/**/LANGUAGE_SENA.equals(language) ||
				/**/LANGUAGE_SHAMBALA.equals(language) ||
				/**/LANGUAGE_SHONA.equals(language) ||
				/**/LANGUAGE_SOGA.equals(language) ||
				/**/LANGUAGE_SOMALI.equals(language) ||
				/**/LANGUAGE_SOUTH_NDEBELE.equals(language) ||
				/**/LANGUAGE_SOUTHERN_SOTHO.equals(language) ||
				/**/LANGUAGE_SPANISH.equals(language) ||
				/**/LANGUAGE_SWAHILI.equals(language) ||
				/**/LANGUAGE_SWATI.equals(language) ||
				/**/LANGUAGE_SWEDISH.equals(language) ||
				/**/LANGUAGE_SWISS_GERMAN.equals(language) ||
				/**/LANGUAGE_SYRIAC.equals(language) ||
				/**/LANGUAGE_TAMIL.equals(language) ||
				/**/LANGUAGE_TELUGU.equals(language) ||
				/**/LANGUAGE_TESO.equals(language) ||
				/**/LANGUAGE_TIGRE.equals(language) ||
				/**/LANGUAGE_TSONGA.equals(language) ||
				/**/LANGUAGE_TSWANA.equals(language) ||
				/**/LANGUAGE_TURKMEN.equals(language) ||
				/**/LANGUAGE_TYAP.equals(language) ||
				/**/LANGUAGE_URDU.equals(language) ||
				/**/LANGUAGE_VENDA.equals(language) ||
				/**/LANGUAGE_VUNJO.equals(language) ||
				/**/LANGUAGE_WALSER.equals(language) ||
				/**/LANGUAGE_WESTERN_FRISIAN.equals(language) ||
				/**/LANGUAGE_XHOSA.equals(language) ||
				/**/LANGUAGE_ZULU.equals(language)
				) {
			return new OtherOneOther();
		}

		if (
				/**/LANGUAGE_LATVIAN.equals(language)
				/**/) {
			return new Latvian();
		}

		if (
				/**/LANGUAGE_COLOGNIAN.equals(language) ||
				/**/LANGUAGE_LANGI.equals(language)
				/**/) {
			return new ZeroOneOther();
		}

		if (
				/**/LANGUAGE_CORNISH.equals(language) ||
				/**/LANGUAGE_INARI_SAMI.equals(language) ||
				/**/LANGUAGE_INUKTITUT.equals(language) ||
				/**/LANGUAGE_LULE_SAMI.equals(language) ||
				/**/LANGUAGE_NAMA.equals(language) ||
				/**/LANGUAGE_NORTHERN_SAMI.equals(language) ||
				/**/LANGUAGE_SAMI_LANGUAGE.equals(language) ||
				/**/LANGUAGE_SKOLT_SAMI.equals(language) ||
				/**/LANGUAGE_SOUTHERN_SAMI.equals(language)
				/**/) {
			return new OtherOneTwoOther();
		}

		if (
				/**/LANGUAGE_BELARUSIAN.equals(language) ||
				/**/LANGUAGE_BOSNIAN.equals(language) ||
				/**/LANGUAGE_CROATIAN.equals(language) ||
				/**/LANGUAGE_RUSSIAN.equals(language) ||
				/**/LANGUAGE_SERBIAN.equals(language) ||
				/**/LANGUAGE_SERBO_CROATIAN.equals(language) ||
				/**/LANGUAGE_UKRAINIAN.equals(language)
				/**/) {
			return new ManyOneFewManyOneFewMany();
		}

		if (
				/**/LANGUAGE_POLISH.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_LITHUANIAN.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_TACHELHIT.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_MOLDAVIAN.equals(language) ||
				/**/LANGUAGE_ROMANIAN.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_CZECH.equals(language) ||
				/**/LANGUAGE_SLOVAK.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_SCOTTISH_GAELIC.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_BRETON.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_SLOVENIAN.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_MALTESE.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_IRISH.equals(language)
				/**/) {
			// TODO
			throw new UnsupportedOperationException("TODO");
		}

		if (
				/**/LANGUAGE_ARABIC.equals(language)
				/**/) {
			return new Arabic();
		}

		if (
				/**/LANGUAGE_WELSH.equals(language)
				/**/) {
			return new Welsh();
		}

		throw new UnsupportedOperationException(
			/**/"Language code \"" + language + "\" is unsupporred by the translation framework. " +
			/**/"Either it is an error, or the framework's PluralRules class needs to be slightly modified. " +
			/**/"Please see http://unicode.org/repos/cldr-tmp/trunk/diff/supplemental/language_plural_rules.html to quickly correct the framework.");
	}

	private static String normalizeLanguage(String languageCode) {
		// According to Locale.getLanguage():
		// BAD:
		// if (locale.getLanguage().equals("he"))
		// INSTEAD, DO:
		// if (locale.getLanguage().equals(new Locale("he").getLanguage()))
		return new Locale(languageCode).getLanguage();
	}

	protected static class Other extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			return CATEGORY_OTHER;
		}
	}

	protected static class Manx extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if ((quantity % 10 >= 1 && quantity % 10 <= 2) || quantity % 20 == 0) {
				return CATEGORY_ONE;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class CentralMoroccoTamazight extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if ((quantity >= 0 && quantity <= 1) || (quantity >= 11 && quantity <= 99)) {
				return CATEGORY_ONE;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class Macedonian extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity % 10 == 1 && quantity != 11) {
				return CATEGORY_ONE;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class OtherOneOther extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			return (quantity == 1 ? CATEGORY_ONE : CATEGORY_OTHER);
		}
	}

	protected static class Latvian extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity == 0) {
				return CATEGORY_ZERO;
			} else if (quantity % 10 == 1 && quantity % 100 != 11) {
				return CATEGORY_ONE;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class ZeroOneOther extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity == 0) {
				return CATEGORY_ZERO;
			} else if (quantity == 1) {
				return CATEGORY_ONE;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class OtherOneTwoOther extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity == 1) {
				return CATEGORY_ONE;
			} if (quantity == 2) {
				return CATEGORY_TWO;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class ManyOneFewManyOneFewMany extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity % 10 == 1 && quantity % 100 != 11) {
				return CATEGORY_ONE;
			} else if ((quantity % 10 >= 2 && quantity % 10 <= 4) && !(quantity % 100 >= 12 && quantity % 100 <=14)) {
				return CATEGORY_FEW;
			} else if (quantity % 10 == 0 || (quantity % 10 >= 5 && quantity % 10 <= 9) || (quantity % 100 >= 11 && quantity % 100 <= 14)) {
				return CATEGORY_MANY;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class OneOneOther extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			return (quantity == 0 || quantity == 1 ? CATEGORY_ONE : CATEGORY_OTHER);
		}
	}

	protected static class Arabic extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity == 0) {
				return CATEGORY_ZERO;
			} else if (quantity == 1) {
				return CATEGORY_ONE;
			} else if (quantity == 2) {
				return CATEGORY_TWO;
			} else if (quantity % 100 >= 3 && quantity % 100 <= 10) {
				return CATEGORY_FEW;
			} else if (quantity % 100 >= 11 && quantity % 100 <= 99) {
				return CATEGORY_MANY;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

	protected static class Welsh extends PluralRules {
		@Override
		public int getCategoryForQuantity(int quantity) {
			if (quantity == 0) {
				return CATEGORY_ZERO;
			} else if (quantity == 1) {
				return CATEGORY_ONE;
			} else if (quantity == 2) {
				return CATEGORY_TWO;
			} else if (quantity == 3) {
				return CATEGORY_FEW;
			} else if (quantity == 6) {
				return CATEGORY_MANY;
			} else {
				return CATEGORY_OTHER;
			}
		}
	}

}
