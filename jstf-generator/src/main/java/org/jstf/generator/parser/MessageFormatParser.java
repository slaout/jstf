package org.jstf.generator.parser;

import java.text.Format;
import java.text.MessageFormat;

public class MessageFormatParser {

	public static String[] getArgumentTypes(String message) {
		// http://docs.oracle.com/javase/1.5.0/docs/api/java/text/MessageFormat.html#patterns
		MessageFormat messageFormat = new MessageFormat(message);

		// Get formatters for all parameters by argument index the numbers in the "{X}" 's
		Format[] formatsByArgumentIndex = messageFormat.getFormatsByArgumentIndex();

		// Determining argument type would not always lead to the correct result...
		//
		// "{X}" => Object
		// The formatter is null
		// It can be any type.
		//
		// "{X,date}" => Date
		// The formatter is a DateFormat (or subclass SimpleDateFormat)
		// The easy way, but used seldomly, and what if the developer is using Calendar or one of the Java 8 classes?
		// http://howtodoinjava.com/2013/05/15/date-and-time-api-changes-in-java-8-lambda/
		//
		// "{X,number}" => long or double
		// The formatter is a NumberFormat (or subclass DecimalFormat)
		// But inadvertently converting an argument to long will loose decimal digits.
		// And inadvertently converting an argument to double will add a ".0" suffix for integers (unless a SubformatPattern is specified, but it is seldomly the case)
		// Anyway, when present, we could parse the SubformatPattern to create a method with the right argument type:
		// "{X,number,integer}"  NumberFormat.getIntegerInstance(getLocale())  => long
		// "{X,number,currency}" NumberFormat.getCurrencyInstance(getLocale()) => double
		// "{X,number,percent}"  NumberFormat.getPercentInstance(getLocale())  => double
		// "{X,number,#.##}"     SubformatPattern! => needs parsing to infer integer or decimal type!
		//
		// "{X,choice}" => long or double
		// The formatter is a ChoiceFormat (be careful: it is an instance of NumberFormat)
		// The inferred type is depending on the SubformatPattern and can be quite tricky to compute!
		//
		// ... So we use Object for all arguments:
		int count = formatsByArgumentIndex.length;
		String[] argumentTypes = new String[count];
		for (int i = 0; i < count; i++) {
			argumentTypes[i] = "Object";
		}

		// TODO Make sure every translations have the same number of arguments, or at least group all types
		// TODO If one message generates a NumberFormat and another message generates a 'null', convert to Number or Date?
		// TODO DateFormat: option to generate Date, Calendar or one of the Java 8 classes: http://howtodoinjava.com/2013/05/15/date-and-time-api-changes-in-java-8-lambda/
		return argumentTypes;
	}

}
