Java Static Translation Framework (JSTF)
====

This is version 0.0.1: it is still a work in progress.
If you are interested, please contact me.

Aim of the Framework
----

JSTF was first created as a very lightweight, yet complete, tool to translate libGdx multi-platform applications (Android being the main target).
It was mainly required to not heavy-parsing free, to be able to startup as quick as possible on low-end phone hardwares.
The second requirement was to have a modern feature-set, with plural support as much as good as Android's, and being able to detect unused and untranslated keys.

Here are all the criteria used to evaluate a translation solution.
None of the projects found on Internet fulfilled the requirements:

- No parsing at startup (costly on small JVMs like Android ones)
- Fast run-time (no or few&fast string comparisons for the keys, or if string comparisons are required, cache values)
- No or minimal dependency tree (eg. no Android dependency for libGdx multi-platform projects; if possible only add one or two JARs to ease the task of people managing dependencies by hand)
- Unicode translations (prefer UTF-8; it is easy with XML files, and hard for .properties that are always ISO-8859-1 and need one more step to transform them from Unicode to ISO-8859-1)
- Messages can have parameters (formatted with MessageFormat or Formatter, or a similar solution)
- Plurals support for any Unicode language (one/two/zero/few/many/other Unicode forms, eg. 0 is plural in English but singular in French : "0 files" in English vs. "0 fichier" in French)
- Easy retrieving of the translations in Java (one simple method call: no need to use a factory, complex configuration, etc.)
- Do not crash if asking for an unknown key (return "!theUnknownKey!", for instance)
- Automatically get the system language/locale ("en" vs. "en_US") and allow to use any locale at runtime
	TODO Implement dynamic locale change in JSTF
- A system to detect non-existant keys (or generate constants or methods, so compilation will fail when using unknown keys)
- A system to detect unused keys (if constants or methods are generated, it is possible to use the UCDetector Eclipse plugin quite easily)
- No compiler-warning by using hard-coded Strings in the code (if keys are String, it would be better if developers do not have to use $NON-NLS-x$ comments)
- Allow developers and translators to use whatever platforms they prefer (Windows, Linux, Mac OS X...)

Evaluated Solutions
----

Given the requirements described above, here are the studied existing translation solutions and why they did not fit so the creation of JSTF was needed:

* **Java bridge to GNU gettext**

	https://code.google.com/p/gettext-commons/wiki/Tutorial

	My solution of choice when developping on C++/Qt.
	Has a very good support for plural forms.
	But is widely unused in Java projects: all Java projects use a key/value mechanism instead of the methodCall("String to translate") idiom.
	It is heavily based on String comparisons.
	The build tools need to be run on a Unix system: it is hard to make it work in a multi-platform way (see the comment section of the tutorial).

* **XML libGdx**

	http://siondream.com/blog/games/internationalization-for-libgdx-projects/

	It is very basic.
	It uses string keys.
	All languages in one big XML file that needs to be parsed and stored in memory.
	The XML format does not allow to use existing translation tools.

* **C10N**

	https://github.com/rodionmoiseev/c10n/wiki/Overview

	Quite seducing for its nice annotation-based system where every translation is a method (good key and parameters checking)
	But translators need to edit the Java files by hand! And we are not talking about string escaping...
	Or one can use properties instead of Java annotations, but then we are back with a good-old-Java-properties system.

* **Eclipse**

	http://stackoverflow.com/questions/3886978/configuring-string-externalization-in-eclipse-to-use-key-as-field-name

	http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fosgi%2Futil%2FNLS.html

	http://help.eclipse.org/helios/index.jsp?topic=/org.eclipse.jdt.doc.user/reference/ref-wizard-externalize-strings.htm

	http://eclipse.org/articles/Article-Internationalization/how2I18n.html

	http://docs.oracle.com/javase/tutorial/i18n/intro/index.html

	http://docs.oracle.com/javase/6/docs/api/java/text/MessageFormat.html

	NLS.bind(Messages.key_two, "example usage")

	Keys are String that needs to be loaded in memory and generate a lot of String comparisons.
	The system and its configuration is hard because there lacks a  good and complete documentation.
	Plural support with ChoiceFormat is a syntax nightmare for translators.

* **Android String resources**

	http://developer.android.com/guide/topics/resources/string-resource.html

	Very nice tool, with a good workflow for developers and translators.
	But it is tied to Android: we need to be library-agnostic.

* **Other ideas of translation frameworks**

	http://stackoverflow.com/questions/10248824/l18n-framework-with-compiletime-checking

Requirements to Use JSTF
----

Your project needs to target minimum Java 5.
Maven/Ant are supported but not required.

How Does the Framework Work for Your Project?
----

1. You create a development-time resource file (not deployed in JAR or APK):
```xml
	<?xml version="1.0" encoding="utf-8"?>
	<resources>
		<string name="normalKeyWith1Parameter">Normal key with one parameter: %d</string>
		<plurals name="pluralKey">
			<item quantity="one">The is %d item</item>
			<item quantity="other">There are %d items</item>
		</plurals>
	</resources>
```
There is one file per translated locale.
It is the same format as Android strings.xml files.

2. At build-time you run a Java class, or a script, or a Maven plugin (or in the future a Gradle task+plugin or an Ant task) that will parse the XML files.
	This class/script/plugin will generate several classes containing the translations.
	For instance, it will generate:

	* I18n.java (the access class)
	* MessageEnUs.java (the class storing messages for the en_US locale)
	* MessageFr.java (the class storing messages for the fr locale)
	* MessageLoader.java (the class that loads the correct Message* class depending on the user locale)

	For more information about this step, see the next sections.

3. At run-time, all you have to do to get translations is:
```java
	I18n.normalKeyWith1Parameter(42);
```
or:
```java
	I18n.pluralKey(itemNumber, itemNumber, parameter2);
```
And voilà!

How to Translate Your Project Using a Main Program
----

See the previous section "How Does the Framework Work for Your Project?".

This section describes the step of generating translation classes with only a Main class (without any Maven or Ant plugins).

* Include the jstf-lib.jar file in the classpath of the project that will display translated messages (let us name it "myproject").
* In your Java project, add a folder "res" with a subfolder "messages" (for instance).
  In this folder, put your files "strings_en.xml", "string_en_UK.xml", "strings_fr.xml"...
  See section "XML Format of Translations" below for the format of these files.
* Make sure you exclude this "res" from exporting to JAR.
  It is useless to have these XML files in your deliverable (because Java classes will ).
* Create another projet with the suffix "-generator" side to side with your main project (the one that needs to be translated).
  For instance, if your project is "myproject", create a project "myproject-generator".
* Include the jstf-lib.jar and jstf-generator.jar files in the classpath of the "myproject-generator" project.
* In the "myproject-generator" project, create a MainGeneration class with the following method:
```java
	public static void main(String[] args) throws IOException {
		String xmlFolder = "../myproject/res/messages";
		String outClassPathFolder = "../myproject/src";
		String packageName = "org.myproject.i18n";
		TranslationGenerator.generate(xmlFolder, outClassPathFolder, packageName);
	}
```
* Whenever the translations change, run that MainGeneration class in the "myproject-generator" project.
  This will generate classes in 
  You can use the generated class I18n to access translations in "myproject".
* There is an optional parameter in TranslationGenerator.generate(): you can use the options described in the "Advanced Uses" section of this manual.

How to Translate Your Project Using Maven
----

See the previous section "How Does the Framework Work for Your Project?".

This section describes the step of generating translation classes with the Maven plugin.

There is an example is the JSTF sources.
This is the project "jstf-test".

* By default, you place your string*.xml files in the folder /src/main/resources/jstf of the project.
  Eg. /src/main/resources/jstf/strings.xml
  or  /src/main/resources/jstf/strings_fr.xml
* Edit your pom.xml to include these sections:

	Add this part in the <dependencies></dependencies> section:

```xml
		<dependency>
			<groupId>org.jstf</groupId>
			<artifactId>jstf-lib</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
```

	Add this part in the <build><plugins></plugins></build> section:

```xml
		<plugin>
			<groupId>org.jstf</groupId>
			<artifactId>jstf-maven-plugin</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<executions>
				<execution>
					<goals>
						<goal>generate</goal>
					</goals>
					<configuration>
						<!-- See optional options below -->
					</configuration>
				</execution>
			</executions>
		</plugin>
```

	If you use Eclipse, add this part in the <build><pluginManagement><plugins></plugins></pluginManagement></build> section:

```xml
		<!-- http://stackoverflow.com/questions/8393447/is-maven-eclipse-plugin-no-longer-needed-with-the-new-m2eclipse-in-indigo -->
		<!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself. -->
		<plugin>
			<groupId>org.eclipse.m2e</groupId>
			<artifactId>lifecycle-mapping</artifactId>
			<version>1.0.0</version>
			<configuration>
				<lifecycleMappingMetadata>
					<pluginExecutions>
						<pluginExecution>
							<pluginExecutionFilter>
								<groupId>org.jstf</groupId>
								<artifactId>jstf-maven-plugin</artifactId>
								<versionRange>[0.0,)</versionRange>
								<goals>
									<goal>generate</goal>
								</goals>
							</pluginExecutionFilter>
							<action>
								<execute>
									<runOnConfiguration>true</runOnConfiguration>
									
									<!--runOnIncremental>true</runOnIncremental-->
									<!-- Infinite Eclipse compilation loop -->
									<!-- TODO Use BuildContext: http://wiki.eclipse.org/M2E_compatible_maven_plugins -->
								</execute>
							</action>
						</pluginExecution>
					</pluginExecutions>
				</lifecycleMappingMetadata>
			</configuration>
		</plugin>
```

* By default, the Maven plugin build will generate access classes in the package "org.jstf.messages".

* In the <configuration></configuration> part of the jstf-maven-plugin plugin, you can use the options described in the "Advanced Uses" section of this manual.

XML Format of Translations
----

Basically, here is an example of strings.xml file in order to understand the format:

```xml
	<?xml version="1.0" encoding="utf-8"?>
	<resources>
		<string name="normalKeyWith1Parameter">Normal key with one parameter: %d</string>
		<string name="anotherKey">Another key</string>
		<plurals name="firstPluralKey">
			<item quantity="one">The is %d item</item>
			<item quantity="other">There are %d items</item>
		</plurals>
		<plurals name="secondPluralKey">
			<item quantity="one">The is another plural translation</item>
			<item quantity="other">The is another %d translations</item>
		</plurals>
	</resources>
```

* The root node of the XML file is <resources/>.

* A simple translation is simply a <string/> node.
  With the translation key in the name="" attribute.
  And the translated message in the value of the <string/> node.
  Beware that you must escape apostrophes and quotation marks.

* A translation needing special treatment for plurals is in a <plurals/> node.
  With the translation key in the name="" attribute.
  The translations depending on a quantity are in <item/> sub-nodes.
  Their quantity="" attribute is one of these values: one/two/zero/few/many/other.
  They describe Unicode plural forms.

* To decide which quantity qualifier to use depending on the language, please see:
  http://cldr.unicode.org/index/cldr-spec/plural-rules
  http://unicode.org/repos/cldr-tmp/trunk/diff/supplemental/language_plural_rules.html
  To make simple, here are rules for English and French:
  English:
    - if the message describe one item, use quantity="one"
    - for other quantities (including 0 items), use quantity="other"
  French:
    - if the message describe zero or one item, use quantity="one"
    - for other quantities, use quantity="other"

For more information about the XML syntax, please see (there is no String Array concept in JSTF):
- http://developer.android.com/guide/topics/resources/string-resource.html#String
- http://developer.android.com/guide/topics/resources/string-resource.html#Plurals

Advanced Uses
----

TODO Options
TODO Fragmented packages

Formatter vs. MessageFormat
MessageFormat:
	More familiar to those coming from Spring
	Usually "{0} {1}"
	But can be eg. "{0} {1,number} {2,number,integer} {3,date,short}"

Tools and Workflow for Translators
----

TODO Document Android tools like:
http://stackoverflow.com/questions/10462247/is-there-a-way-how-to-edit-multiple-localised-string-xml-files-in-one-window-in
or find an easier one for translators to not have to install Eclipse!

"Android Localization Files Editor" is now part of Eclipse Tools (was Sequoyah):
http://help.eclipse.org/helios/index.jsp?topic=%2Forg.eclipse.sequoyah.localization.android.help%2Ftopics%2Fr_localization-string-editor.html
http://download.eclipse.org/sequoyah/updates/2.1/

This one too?:
http://www.qweas.com/downloads/development/components-libraries/overview-android-localizer-ailocalizer.html

This one also allows to open Android XML strings files:
https://poeditor.com/

In order to use Android translation tools, be careful to not enable the option to use MessageFormat instead of Formatter.

The Android "App Translation Service", part of the Google Play Developer Console:
http://android-developers.blogspot.fr/2013/11/app-translation-service-now-available.html

Eclipse Plugin for that
http://developer.android.com/sdk/installing/installing-adt.html#tmgr

Find Hardcoded Messages to Translate
----

In order to test if every user-visible texts in your application are coming from a translation resource instead of being hard-coded, use the language code "xx".
For instance, you can use this line of code (to be run before the very first message gets translated):

```java
Locale.setDefault(new Locale("xx", "XX"));
```

Configuring a Tool to Check for Unused Messages
----

This is the only requirement that was not meet by this JSTF solution.

Fortunately, there are tools that can check for unused methods in a project, allowing you to find message keys to remove.

We recommend to use the tool "UCDetector": http://www.ucdetector.org/

Install UCDetector with the following update-site (if it hasn't changed at the moment of you reading this document): http://ucdetector.sourceforge.net/update
Restart Eclipse
Right click on the generated I18n.java file, choose "UCDetector" and then click "Detect unnecessary code (Alt+Shift+U"
In the I18n class, you get warnings like "Method "I18n.unusedKey()" has 0 references", so you know what message keys to remove
To get rid of the warnings, right click on the generated I18n.java file, choose "UCDetector" and then click "Clean markers"

Alternatively, you can also use this project (not tested, as it seems quite complex for such a simple task of discovering unused methods or constants):
https://developers.google.com/java-dev-tools/codepro/doc/features/features

Downsides & Solutions
----

The speed benefit of JSTF comes with less flexibility when deploying updated translations: the whole application has to be repackaged & redeployed.
This is not a problem for Android/libgdx applications, but it may be problematic for web applications.
You cannot just upload new .properties or .xml files to eg. a server and trigger a method that will dynamically reload the file.

TODO Search for hot-code swap methods and document it + add a reset()/reload() method to class I18n
http://zeroturnaround.com/software/jrebel/features/comparison-matrix/
http://stackoverflow.com/questions/148681/unloading-classes-in-java

But wait... If a translation is missing in, say, locale "fr_CA", it is copied for the parent locale "fr" or default "en" locale into the class MessagesFrCa.
Strings can be duplicated several times?!
Yes, if only a few strings differ between "fr" and "fr_CA", then a lot of strings are duplicated in several classes.
It *could* be a problem on low resource machines like Android systems...
But at the same time, the DEX cache created by Android groups several .class files and thus agregate duplicate constants, so they are not duplicate anymore.
At runtime, there is no difference.

Stress-Test Results
----

TODO All strings will be interned, isn't it? Is it bad?
http://www.codeinstructions.com/2009/01/busting-javalangstringintern-myths.html
Test program in Myth 2!
"The problem is that the internalized strings go to the Permanent Generation, which is an area of the JVM that is reserved for non-user objects, like Classes, Methods and other internal JVM objects. The size of this area is limited, and is usually much smaller than the heap."

If you are worried about performances or JVM limits when generating classes with thousand of constants or methods, so was I.
In the test generator project, there is a Main class generating an XML file with thousands of strings and plurals.
Here are the results:
TODO Jar size, class loading time on computer and Android devices.

TODO Format the tests and results in an intelligible way!

	GENERATED CONSTANTS
		STRINGS:
			String text = "abcdefghijklmno " + i;
				Character.MAX_VALUE: = 65536
					I18n: Too many fields for type I18nStressTest. Maximum is 65535
					MessageEn: Too many constants, the constant pool for MessageFr would exceed 65536 entries
				Character.MAX_VALUE / 7: = 9362
					I18n: OK
					MessageEn: The code for the static initializer is exceeding the 65535 bytes limit
				Character.MAX_VALUE / 8:
					Compilation time is fast
					WARNING: auto-completion in Eclipse takes a few seconds
					I18n: OK
					MessageEn: OK
			String text = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz " + i;
				Character.MAX_VALUE / 8: = 8192
					Compilation is fast
					I18n: OK
					MessageEn: OK
					Class sizes:
						I18nStressTest.class (  253 805 bytes)
						MessageEn.class      (1 039 842 bytes)
						MessageLoader.class  (      858 bytes)
					Stress-test first-translation: 212793245 ns  =>  2.12793245 seconds
					Stress-test second-translation: 2766 ns
					Stress-test first-translation: 42603061 ns  =>  0.42603061 seconds
					Stress-test second-translation: 1975 ns
			String text = "abcdefghijklmno " + i;
				Idem, but MessageEn.class (310 843 bytes)
					Stress-test first-translation: 110722360 ns  =>  0.11072236 seconds
					Stress-test second-translation: 3161 ns  =>  0.00003161 seconds
					Stress-test first-translation: 35910178 ns  => 0.035910178 seconds
					Stress-test second-translation: 3556 ns  =>  0.00003556 seconds
			=> Max 8221: BY DICHOTOMY, MAXIMUM NUMBER OF STRINGS (+ 0 PLURAL) IS 8221 FOR MessageEn to be bellow static initializer limit
				"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz " + i
					strings.xml: 754 179 bytes
					*.class:     871 753 bytes  (without lib; adds up 25 897 bytes)
				"abcdefghijklmno " + i
					strings.xml: 450 002 bytes
					*.class:     567 576 bytes  (without lib; adds up 25 897 bytes)
			TODO: Compare with loading time for a .properties file
			TODO: & compare on Android!
		PLURALS:
			With 2 non-null strings per plural: Max 2987
			With 3 non-null strings per plural: Max 2348
			With 4 non-null strings per plural: Max 1931
			With 5 non-null strings per plural: Max 1644
			With 6 non-null strings per plural: Max 1429
			strings.xml: 470 167 bytes
			*.class:     344 482 bytes  (without lib; adds up 25 897 bytes)
	FOR INFORMATION, a big e-commerce website on one of my clients was having roughly 4600 messages. Will fit in 1, 2 or 3 packs. BTW, it would benefit such a partitioning.
	GENERATED METHODS
		TODO

TODO What about JSP? Access with key, using reflection

http://stackoverflow.com/questions/10209952/java-constant-pool
http://en.wikipedia.org/wiki/Java_class_file
	Per class:
		Number of constants is limited to 2^16 (containing items such as literal numbers, strings, and references to classes or methods)
			This includes values such as numbers of all sorts, strings, identifier names, references to classes and methods, and type descriptors
		Number of fields is limited to 2^16
		Number of methods is limited to 2^16
		Number of attributes is limited to 2^16 ???

TODO
	* Option: inheriting:
		- 'static' for performance  (but one Constant Pool per class: duplicate constant strings: TODO: coalesced when in JAR?)
		- 'dynamic' for JAR size optimization
	* MessageLoader: @SuppressWarnings("unused") String country
	* Automatically split fields and translations in X sub-classes?

Hacking the Framework
----

Fetch the project from GitHub.
In Eclipse, import existing Maven projects for sub-modules.
In Eclipse, in "jstf" parent project, right click each "jstf-*" folders and choose Properties, check "Derived".

TODO
TODO JUnit to check generated classes
TODO JUnit to check working translations

Mail to be sent to libGdx guys (in progress)
----

Hello guys,

I've searched the net and found no translation mechanism for libGdx.

The Android strings and plurals mechanism is an excellent translation system.
But libGdx is multi-platform, so we cannot depend on Android libraries.

Moreover, the Android mechanism parses XML at runtime, which is time and memory consuming.
I develop a game that once loaded level data using JSON at startup but found it very very slow to load because of heavy usage of the garbage collector on Android (ran fine on the desktop, but was still not as snappy as I wanted).
So I resorted to parse JSON files and generate Java classes at compilation time.
The game now starts up instantly, that's pure magic ;-)
I wanted a similar system for translations: no parsing required at startup, everything in Java and constants, the Java class is loaded in memory with exactly 0 garbage collection.

These are two requirements I had for such translation mechanism.
The solutions I found on Internet did not meet these requirements.
So I made a framework for myself.
I made it generic, so it could be reused in other projects.

Here is a mail to present my solution to you and discuss these questions:
* Are you interested in a translation system for libGdx?
* What would be your requirements for an official libGdx translation system?
* What would you change on my framework in order to make it fit better for libGdx users?
* Would you include it in libGdx or recommend it as the official libGdx translation system (one can hope ;-) )?
* Do you have questions regarding its internal working or how to use it? I will reply and add them to the FAQ.

I called this framework "Java Static Translation Framework (JSTF)".

Here is a link to the project I just started:
https://github.com/slaout/jstf

It contains:
* the library,
* a README.txt file explaining everything and
* a sample project.

Here is a short example of how to use it:

1. You create a development-time resource file (not deployed in JAR or APK):

	<?xml version="1.0" encoding="utf-8"?>
	<resources>
		<string name="normalKeyWith1Parameter">Normal key with one parameter: %d</string>
		<plurals name="pluralKey">
			<item quantity="one">The is %d item</item>
			<item quantity="other">There are %d items</item>
		</plurals>
	</resources>

There is one file per translated locale.
It is the same format as Android strings.xml files.
LibGdx being mainly targeted to Android developers, I tought it would be a nice requirement.
The XML files can be used by the same tools as Android projects, and sent to translators on Google translation services, for instance.
This is very convenient, and your project is still platform-independent.

2. At build-time you run a Java class, or a script, or a Maven plugin (or in the future a Gradle task+plugin or an Ant task) that will parse the XML files.
This class/script/plugin will generate several classes containing the translations.
For instance, it will generate:

	I18n.java (the access class)
	MessageEnUs.java (the class storing messages for the en_US locale)
	MessageFr.java (the class storing messages for the fr locale)
	MessageLoader.java (the class that loads the correct Message* class depending on the user locale)

3. At run-time, all you have to do to get translations is:

	I18n.normalKeyWith1Parameter(42);

or:

	I18n.pluralKey(itemNumber, itemNumber, parameter2);

And voilà!

I'm waiting for your comments.

Best regards,
Sébastien Laoût.
