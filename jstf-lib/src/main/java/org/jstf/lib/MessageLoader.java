package org.jstf.lib;

public interface MessageLoader {

	MessageBase getMessages(String language, String country);

	boolean isUsingMessageFormatInsteadOfFormatter();

}
