package jamopp.parser.jdt.injector;

import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import jamopp.parser.jdt.JaMoPPJDTParser;

public class OtherModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(Logger.class).toInstance(Logger.getLogger(JaMoPPJDTParser.class.getSimpleName()));
		bind(String.class).annotatedWith(Names.named("DEFAULT_JAVA_VERSION")).toInstance("14");
		bind(String.class).annotatedWith(Names.named("DEFAULT_ENCODING")).toInstance(StandardCharsets.UTF_8.toString());

	}

}
