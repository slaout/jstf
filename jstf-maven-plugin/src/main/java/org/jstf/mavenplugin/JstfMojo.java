package org.jstf.mavenplugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jstf.generator.TranslationGenerator;
import org.jstf.generator.TranslationGeneratorOptions;

/**
 * TEST MOJO DESCRIPTION
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute()
public class JstfMojo extends AbstractMojo {

	// Or ${project.build.resources[0].directory}/jstf
	@Parameter(defaultValue = "${basedir}/src/main/resources/jstf", required = false)
	private File xmlFolder;

	@Parameter(defaultValue = "${project.build.directory}/generated-sources/jstf", required = false)
	private File outClassPathFolder;

	/**
	 * The package where to generate the message classes (I18n, Message* and MessageLoader) for the currently processed XML files.<br>
	 * If null or empty, it will default to "org.jstf.messages".
	 */
	@Parameter(defaultValue = "org.jstf.messages", required = false)
	private String packageName;

	/**
	 * When having several XML message sources, they are generated into different packages.<br>
	 * By default, each package contains the I18n access class.<br>
	 * You are encouraged to rename this class so you can nicely import two or more I18n classes to use several message sources in one Java class.
	 * @see http://stackoverflow.com/questions/4468388/maximum-number-of-enum-elements-in-java for technical reasons to split translations into several packages. But it is also better from the business point of view to use separate message bundles.
	 */
	@Parameter(defaultValue = "I18n", required = false)
	private String i18nClassName;

	/**
	 * The default locale to use for users having a locale that does not match any XML translation files.<br>
	 * If null or empty, default is either "en" if a message XML file exists for this language or "en_US" if a message XML file exists for this locale, and in last resort the first found file will be used, although it means that a random file will be chosen at build time, and a different one could be chosen on another build. If none exist, there is no default locale, since there is no translation available.
	 */
	@Parameter(defaultValue = "", required = false)
	private String defaultLocale;

	/**
	 * Set to true (the default value) to eg. use "en" messages for "fr" untranslated messages.<br>
	 * Set to false to get a warning at build-time and a null translations for missing messages at run-time.<br>
	 * Default is true for production convenience; consider using false from time to time to check for missing translations.
	 */
	@Parameter(defaultValue = "true", required = false)
	private final boolean inheritDefaultLocale = true;

	/**
	 * If false (the default value), the generated I18n class contains one static method per message, the name of the method being the message key and the parameters matching the message parameters (the %s, %d or {1}, for instance).<br>
	 * If true, the generated I18n class contains two static get(...) methods to get translations, with the first parameter being the message key: one of the generated I18n constants.
	 */
	@Parameter(defaultValue = "false", required = false)
	private boolean useConstantsInsteadOfMethods;

	/**
	 * Useful only when useConstantsInsteadOfMethods is true.<br>
	 * Generated I18n.* constants will be named as the <string name="..."/> are named in XML files.<br>
	 * Some Checkstyle or PMD might complain about the final fields not being all upper-case.<br>
	 * You can set this option to true (default is false) to eg. transform key name "keyName42" to a constant named "KEY_NAME_42"
	 */
	@Parameter(defaultValue = "false", required = false)
	private boolean toStandardJavaConstantNaming;

	/**
	 * Generate code with "MessageFormat" syntax (eg. "Message {1}") instead of the default "Formatter" syntax (eg. "Message %s").
	 * @see http://docs.oracle.com/javase/1.5.0/docs/api/java/text/MessageFormat.html
	 * @see http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html
	 */
	@Parameter(defaultValue = "false", required = false)
	private boolean useMessageFormatInsteadOfFormatter;

	public void execute() throws MojoFailureException
	{
		getLog().info("Generating JSTF package " + packageName);

		Map<?, ?> context = getPluginContext();
		MavenProject project = (MavenProject) context.get("project");

		// Get the options
		TranslationGeneratorOptions options = new TranslationGeneratorOptions();
		options.packageName = packageName;
		options.i18nClassName = i18nClassName;
		options.defaultLocale = defaultLocale;
		options.inheritDefaultLocale = inheritDefaultLocale;
		options.useConstantsInsteadOfMethods = useConstantsInsteadOfMethods;
		options.toStandardJavaConstantNaming = toStandardJavaConstantNaming;
		options.useMessageFormatInsteadOfFormatter = useMessageFormatInsteadOfFormatter;

		// Run the generator
		try {
			TranslationGenerator.generate(xmlFolder.getAbsolutePath(), outClassPathFolder.getAbsolutePath(), options);
			// TODO getLog.error(errorList);
		} catch(IOException e) {
			throw new MojoFailureException("Failed to generate translations", e);
		}

		// Add the output class-path to the project
		String generatedSourcesDirectory = outClassPathFolder.getAbsolutePath();
		new File(generatedSourcesDirectory).mkdirs(); // TODO return false ?
		project.addCompileSourceRoot( generatedSourcesDirectory );
	}

}
