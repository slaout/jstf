package org.jstf.generator.parser.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jstf.generator.parser.FormatterParser;


// TODO To JUnit
public class FormatterParserTest {

	public static void main(String[] args) {
		
		Calendar c = new GregorianCalendar();
		String s1 = String.format("Duke's Birthday: %1$tm %1$te,%1$tY", c);
		String s2 = String.format("Duke's Birthday: %1$tm %<te,%<tY", c);
		System.out.println(s1);
		System.out.println(s2);
		
		test("Message %d !", new String[] { "Object" });

		test("Message %s %d !", new String[] { "String", "Object" });

		test("Message %3$s %2$s!", new String[] { "Object", "String", "String" });

		test("Message %1$s %<s!", new String[] { "String" });

		test("Message %3$s %<s!", new String[] { "Object", "Object", "String" });

		test("Message %% %n!", new String[] { });

		test("Message %s %% (oh oh oh)", new String[] { "String" });

//		MessageFormat msgFmt = new MessageFormat("This is a {0} 'message with {10}' + At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.");
//
//		msgFmt.getFormats();
//		null
//		java.text.SimpleDateFormat@8140d380
//		java.text.SimpleDateFormat@a299500f
//		null
//		java.text.DecimalFormat@674dc

	}

	private static void test(String string, String[] expectedResult) {
		System.out.println();
		System.out.println(string);

		String[] result = FormatterParser.getArgumentTypes(string);
		System.out.print("  ");
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + " ");
		}
		System.out.println();

		// TODO Check all parameter values
		assert expectedResult.length == result.length;
	}

}
