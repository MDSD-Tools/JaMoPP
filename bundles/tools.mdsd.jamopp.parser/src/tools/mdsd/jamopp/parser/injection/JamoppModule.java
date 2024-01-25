package tools.mdsd.jamopp.parser.injection;

import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import tools.mdsd.jamopp.parser.implementation.jamopp.JamoppClasspathEntriesSearcherImpl;
import tools.mdsd.jamopp.parser.implementation.jamopp.JamoppCompilationUnitsFactoryImpl;
import tools.mdsd.jamopp.parser.implementation.jamopp.JamoppFileWithJDTParserImpl;
import tools.mdsd.jamopp.parser.implementation.jamopp.JamoppJavaParserFactoryImpl;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppClasspathEntriesSearcher;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppCompilationUnitsFactory;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppFileWithJDTParser;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppJavaParserFactory;

public class JamoppModule extends AbstractModule {

	private final String loggerName;

	public JamoppModule(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	protected void configure() {
		super.configure();

		bind(Logger.class).toInstance(Logger.getLogger(loggerName));
		bind(String.class).annotatedWith(Names.named("DEFAULT_JAVA_VERSION")).toInstance("14");
		bind(String.class).annotatedWith(Names.named("DEFAULT_ENCODING")).toInstance(StandardCharsets.UTF_8.toString());

		bind(JamoppClasspathEntriesSearcher.class).to(JamoppClasspathEntriesSearcherImpl.class);
		bind(JamoppCompilationUnitsFactory.class).to(JamoppCompilationUnitsFactoryImpl.class);
		bind(JamoppFileWithJDTParser.class).to(JamoppFileWithJDTParserImpl.class);
		bind(JamoppJavaParserFactory.class).to(JamoppJavaParserFactoryImpl.class);
	}

}