package jamopp.parser.jdt.implementation.jamopp;

import org.eclipse.jdt.core.dom.ASTNode;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.interfaces.jamopp.JamoppFileWithJDTParser;
import jamopp.parser.jdt.interfaces.jamopp.JamoppJavaParserFactory;

public class JamoppFileWithJDTParserImpl implements JamoppFileWithJDTParser {

	private final JamoppJavaParserFactory jamoppJavaParserFactoryImpl;
	private final String javaVersion;

	@Inject
	public JamoppFileWithJDTParserImpl(JamoppJavaParserFactory jamoppJavaParserFactoryImpl,
			@Named("DEFAULT_JAVA_VERSION") String javaVersion) {
		this.javaVersion = javaVersion;
		this.jamoppJavaParserFactoryImpl = jamoppJavaParserFactoryImpl;
	}

	@Override
	public ASTNode parseFileWithJDT(String fileContent, String fileName) {
		final var parser = this.jamoppJavaParserFactoryImpl.getJavaParser(this.javaVersion);
		parser.setUnitName(fileName);
		parser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		parser.setSource(fileContent.toCharArray());
		return parser.createAST(null);
	}

}
