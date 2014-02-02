package org.jstf.generator.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FormatterParser {

	// %[argument_index$][flags][width][.precision][t]conversion
	private static Pattern formatterPattern = Pattern.compile(
		/**/"" +
		/**/"%" +
		/**/"((\\d+)\\$)?" + // 2:argument_index
		/**/"([\\<#\\-+ 0,(]*)?" + // 3:flags
		/**/"(\\d+)?" + // 4:width
		/**/"(\\.\\d+)?" + // 5:precision
		/**/"([tT])?" + // 6:t
		/**/"([a-zA-Z%])"); // 7:conversion

	public static String[] getArgumentTypes(String string) {
		// http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax

		// lasPosition starts at 0 for argument "%1$"
		int lastPosition = -1;

		List<String> arguments = new ArrayList<String>();

		// Find all "%...conversion" sub-strings
		Matcher matcher = formatterPattern.matcher(string);
		for (int i = 0; i < string.length(); ) {
			if (matcher.find(i)) {
				// Found one
				String argumentIndex = matcher.group(2);
				String flags = matcher.group(3);
				String conversion = matcher.group(7);

				// Compute argument position - 1
				if (!"%".equals(conversion) && !"n".equals(conversion)) {
					int position;
					if (flags != null && flags.contains("<")) {
						position = lastPosition;
					} else if (argumentIndex == null) {
						position = lastPosition + 1;
					} else {
						position = Integer.valueOf(argumentIndex) - 1;
					}
					if (position == -1) {
						position = 0;
					}
					lastPosition = position;

					String argumentType = "Object";
					// TODO The right type!

					// Add the argument at the right position
					while (position > arguments.size() - 1) {
						arguments.add(null);
					}
					arguments.set(position, argumentType);
				}

				i = matcher.end();
			} else {
				break;
			}
		}

		String[] parameters = arguments.toArray(new String[0]);
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] == null) {
				parameters[i] = "Object";
			}
		}
		return parameters;
	}

}
