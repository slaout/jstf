package org.jstf.lib;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * We use char for strings and short for plurals.
 * char : 0 to 65535
 * short : -32768 to +32767
 * @see http://stackoverflow.com/questions/4049398/memory-space-required-for-a-static-string-constant-in-java/4049678?noredirect=1#comment29735987_4049678
 * @see http://stackoverflow.com/questions/6570343/maximum-size-of-a-method-in-java
 * @see http://developer.android.com/guide/topics/resources/string-resource.html#Plurals
 * 
 * @author Sébastien Laoût
 */
public class I18nAccess {

	private final MessageLoader messageLoader;

	private MessageBase messageBase;

	private Locale locale; // TODO Remove if/when MessageBase provides it own getLocale()

	private PluralRules pluralRules;

	private int defaultPluralIndex;

	public I18nAccess(MessageLoader messageLoader) {
		this.messageLoader = messageLoader;
	}

	/**
	 * Get one of the <string/> translated values.
	 * 
	 * @param keyCode
	 * @param params
	 * @return
	 */
	public String get(char keyCode, Object... params) {
		lazyInitialisation();

		// Get the string and...
		if (messageBase.getStrings() != null && keyCode >= 0 && keyCode < messageBase.getStrings().length) {
			String string = messageBase.getStrings()[keyCode];
			// ... apply parameters
			if (string != null && params != null && params.length > 0) {
				return applyParameters(string, params);
			} else if (string != null) {
				return string;
			}
		}

		// Key not found
		return keyNotFoundMessage(keyCode, params);
	}

	/**
	 * Get one of the <plurals/> translated values.
	 * 
	 * @param keyCode
	 * @param quantity
	 * @param params
	 * @return
	 */
	public String get(short keyCode, int quantity, Object... params) {
		lazyInitialisation();

		// Get the plural array and...
		int index = keyCode - Short.MIN_VALUE;
		if (messageBase.getPlurals() != null && index >= 0 && index < messageBase.getPlurals().length) {
			String[] plural = messageBase.getPlurals()[index];
			// ... the plural string and...
			int pluralIndex = pluralRules.getCategoryForQuantity(quantity);
			// (use the default category if the message is not translated in the right category)
			if (plural == null || pluralIndex < 0 || pluralIndex >= plural.length || plural[pluralIndex] == null) {
				pluralIndex = defaultPluralIndex;
			}
			if (plural != null && pluralIndex >= 0 && pluralIndex < plural.length) {
				String string = plural[pluralIndex];
				// ... apply parameters
				if (string != null && params != null && params.length > 0) {
					return applyParameters(string, params);
				} else if (string != null) {
					return string;
				}
			}
		}

		// Key not found
		return keyNotFoundMessage(keyCode, params);
	}

	private void lazyInitialisation() {
		// TODO What to do with multi-thread access to translations? Not a big deal because concurrent initializations will lead to the same state, but we need to think about a synchronization mechanism or not
		if (messageBase == null) {
			Locale defaultLocale = Locale.getDefault();
			messageBase = messageLoader.getMessages(defaultLocale.getLanguage(), defaultLocale.getCountry());
			if (messageBase != null) {
				// See http://docs.oracle.com/javase/7/docs/api/java/util/Locale.html#getLanguage()
				if (defaultLocale.getLanguage().equals(new Locale(messageBase.getLanguage()))) {
					// The available language is the same as the user language.
					// Keep the use locale to format numbers correctly
					locale = defaultLocale;
				} else {
					// The user language is not available: use the default local translations and number formatting too: otherwise it would lead to awkward results (eg. user is French but only English is translated: messages will be in English, so numbers should be formatted with the "." decimal character and not the French's "," to be coherent)
					String country = (messageBase.getCountry() == null ? "" : messageBase.getCountry());
					locale = new Locale(messageBase.getLanguage(), country);
				}
				pluralRules = PluralRules.getPluralRulesForLanguage(locale.getLanguage());
				defaultPluralIndex = PluralRules.getDefaultCategory(locale.getLanguage());
			} else {
				// TODO throw RuntimeException or IllegalStateException, isn't it?
			}
		}
	}

	private String applyParameters(String string, Object... params) {
		if (messageLoader.isUsingMessageFormatInsteadOfFormatter()) {
			// Always create a new instance of MessageFormat, because MessageFormat.applyPattern() would create a few object instances anyway.
			// And most importantly, it allows concurrent access by several threads!
			MessageFormat messageFormat = new MessageFormat(string, locale);
			return messageFormat.format(params);
		} else {
			// This method will create a new instance of Formatter.
			// So this is thread-safe too!
			return String.format(locale, string, params);
		}
	}

	private String keyNotFoundMessage(int keyCode, Object... params) {
		String string;
		string = "!" + keyCode + "!";
		if (params != null && params.length > 0) {
			for (Object param : params) {
				string += param + "!";
			}
		}
		return string;
	}

}
