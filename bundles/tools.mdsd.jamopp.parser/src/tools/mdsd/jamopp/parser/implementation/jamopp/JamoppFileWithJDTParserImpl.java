package tools.mdsd.jamopp.parser.implementation.jamopp;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppFileWithJDTParser;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppJavaParserFactory;

public class JamoppFileWithJDTParserImpl implements JamoppFileWithJDTParser {

	private final String javaVersion;
	private final JamoppJavaParserFactory jamoppJavaParserFactory;

	@Inject
	public JamoppFileWithJDTParserImpl(final JamoppJavaParserFactory jamoppJavaParserFactory,
			final String javaVersion) {
		this.javaVersion = javaVersion;
		this.jamoppJavaParserFactory = jamoppJavaParserFactory;
	}

	@Override
	public ASTNode parseFileWithJDT(final String fileContent, final String fileName) {
		final ASTParser parser = jamoppJavaParserFactory.getJavaParser(javaVersion);
		parser.setUnitName(fileName);
		parser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		parser.setSource(fileContent.toCharArray());
		return parser.createAST(null);
	}

}
