package jamopp.parser.jdt.injection;

import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import jamopp.parser.jdt.JaMoPPJDTParser;
import jamopp.parser.jdt.implementation.jamopp.JamoppClasspathEntriesSearcherImpl;
import jamopp.parser.jdt.implementation.jamopp.JamoppCompilationUnitsFactoryImpl;
import jamopp.parser.jdt.implementation.jamopp.JamoppFileWithJDTParserImpl;
import jamopp.parser.jdt.implementation.jamopp.JamoppJavaParserFactoryImpl;
import jamopp.parser.jdt.interfaces.jamopp.JamoppClasspathEntriesSearcher;
import jamopp.parser.jdt.interfaces.jamopp.JamoppCompilationUnitsFactory;
import jamopp.parser.jdt.interfaces.jamopp.JamoppFileWithJDTParser;
import jamopp.parser.jdt.interfaces.jamopp.JamoppJavaParserFactory;

public class JamoppModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(Logger.class).toInstance(Logger.getLogger(JaMoPPJDTParser.class.getSimpleName()));
		bind(String.class).annotatedWith(Names.named("DEFAULT_JAVA_VERSION")).toInstance("14");
		bind(String.class).annotatedWith(Names.named("DEFAULT_ENCODING")).toInstance(StandardCharsets.UTF_8.toString());

		bind(JamoppClasspathEntriesSearcher.class).to(JamoppClasspathEntriesSearcherImpl.class);
		bind(JamoppCompilationUnitsFactory.class).to(JamoppCompilationUnitsFactoryImpl.class);
		bind(JamoppFileWithJDTParser.class).to(JamoppFileWithJDTParserImpl.class);
		bind(JamoppJavaParserFactory.class).to(JamoppJavaParserFactoryImpl.class);
	}

}
