package jamopp.parser.jdt.other;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class JamoppFileWithJDTParser {

	private final String javaVersion;
	private final JamoppJavaParserFactory jamoppJavaParserFactory;

	@Inject
	public JamoppFileWithJDTParser(JamoppJavaParserFactory jamoppJavaParserFactory,
			@Named("DEFAULT_JAVA_VERSION") String javaVersion) {
		this.javaVersion = javaVersion;
		this.jamoppJavaParserFactory = jamoppJavaParserFactory;
	}

	public ASTNode parseFileWithJDT(String fileContent, String fileName) {
		final ASTParser parser = jamoppJavaParserFactory.getJavaParser(javaVersion);
		parser.setUnitName(fileName);
		parser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		parser.setSource(fileContent.toCharArray());
		return parser.createAST(null);
	}

}
