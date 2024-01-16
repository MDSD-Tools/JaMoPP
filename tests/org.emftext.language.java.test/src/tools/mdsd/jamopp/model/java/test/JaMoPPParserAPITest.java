package tools.mdsd.jamopp.model.java.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Class for testing the Parser API
 */

public class JaMoPPParserAPITest extends AbstractJaMoPPTests {

	private static final String JAVA_FILE_EXTENSION = ".java";
	protected static final String TEST_INPUT_FOLDER = "src-input";
	private JaMoPPJDTParser parser;

	@Override
	protected String getTestInputFolder() {
		return "";
	}

	@Override
	protected boolean isExcludedFromReprintTest(String filename) {
		return true;
	}

	@BeforeEach
	public void setUp() {
		super.initResourceFactory();
		parser = new JaMoPPJDTParser();

	}

	@Disabled
	public void testIsClass() throws Exception {
		final String typename = "ClassA";
		final tools.mdsd.jamopp.model.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertIsClass(clazz);
	}

	@Test
	@Disabled
	public void testMethodOverwriting() throws Exception {
		final String filenameParent = "scr-input/ClassB" + JAVA_FILE_EXTENSION;
		final String filenameChild = "scr-input/ClassA" + JAVA_FILE_EXTENSION;
		final CompilationUnit cu = (CompilationUnit) parseResource(filenameParent, filenameChild);
		System.out.print("setup");
		System.out.print(cu.getClassifiers().get(1).getName());
		System.out.print(cu.getClassifiers().get(2).getName());

		final ConcreteClassifier clazz = cu.getClassifiers().get(2);

		final Statement s = ((ClassMethod) clazz.getMembers().get(2)).getStatements().get(2);
		final ClassMethod target = (ClassMethod) ((MethodCall) ((ExpressionStatement) s).getExpression()).getTarget();
		assertEquals(clazz.getMembers().get(1), target);

	}

	@Disabled
	public void testSrcSevenAndUp() {
		final ResourceSet set = parser.parseDirectory(Paths.get("src-sevenandup"));
		AbstractJaMoPPTests.assertModelValid(set);
	}
}
