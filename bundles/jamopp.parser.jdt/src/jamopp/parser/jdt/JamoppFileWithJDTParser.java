package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.inject.Inject;

class JamoppFileWithJDTParser {

	private final String javaVersion;
	private final JamoppJavaParserFactory jamoppJavaParserFactory;

	@Inject
	JamoppFileWithJDTParser(JamoppJavaParserFactory jamoppJavaParserFactory, String javaVersion) {
		this.javaVersion = javaVersion;
		this.jamoppJavaParserFactory = jamoppJavaParserFactory;
	}

	ASTNode parseFileWithJDT(String fileContent, String fileName) {
		final ASTParser parser = jamoppJavaParserFactory.getJavaParser(javaVersion);
		parser.setUnitName(fileName);
		parser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		parser.setSource(fileContent.toCharArray());
		return parser.createAST(null);
	}

}
