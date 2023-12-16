package jamopp.parser.jdt.implementation.jamopp;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.jamopp.JamoppFileWithJDTParser;
import jamopp.parser.jdt.interfaces.jamopp.JamoppJavaParserFactory;

public class JamoppFileWithJDTParserImpl implements JamoppFileWithJDTParser {

	private final String javaVersion;
	private final JamoppJavaParserFactory jamoppJavaParserFactory;

	@Inject
	public
	JamoppFileWithJDTParserImpl(JamoppJavaParserFactory jamoppJavaParserFactory, String javaVersion) {
		this.javaVersion = javaVersion;
		this.jamoppJavaParserFactory = jamoppJavaParserFactory;
	}

	@Override
	public ASTNode parseFileWithJDT(String fileContent, String fileName) {
		final ASTParser parser = jamoppJavaParserFactory.getJavaParser(javaVersion);
		parser.setUnitName(fileName);
		parser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		parser.setSource(fileContent.toCharArray());
		return parser.createAST(null);
	}

}
