package org.jstf.test;

import java.util.Locale;

import org.jstf.messages.I18n;

public class TestTest {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("xx", "XX"));

		System.out.println("Using locale " + Locale.getDefault());
		System.out.println();
		System.out.println(I18n.normalKeyWith1Parameter(42));
		System.out.println(I18n.pluralKey(42, 42));
	}

}
