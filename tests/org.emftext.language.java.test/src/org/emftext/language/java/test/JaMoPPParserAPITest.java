package org.emftext.language.java.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.statements.ExpressionStatement;
import org.emftext.language.java.statements.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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

    //
    // Bekomme ich aus dem resource set irgendwie wieder classifier raus?
    // oder die Assert Parser nutzen aus dem old JamoPP project?

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
        // ResourceSet set = parser.parseDirectory(Paths.get("scr-input/ClassA"));
        // set.getResources().get(index)
        final String typename = "ClassA";
        // String filename = typename + JAVA_FILE_EXTENSION;
        final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

        assertIsClass(clazz);
        // this.assertParsesToType(typename, "Class")
    }

    @Test
    @Disabled
    public void testMethodOverwriting() throws Exception {
        // System.out.print("setup");
        final String filenameParent = "scr-input/ClassB" + JAVA_FILE_EXTENSION;
        final String filenameChild = "scr-input/ClassA" + JAVA_FILE_EXTENSION;
        final CompilationUnit cu = (CompilationUnit) parseResource(filenameParent, filenameChild);
        System.out.print("setup");
        System.out.print(cu.getClassifiers()
            .get(1)
            .getName());
        System.out.print(cu.getClassifiers()
            .get(2)
            .getName());

        // assertEquals(clazz.getMembers().get(1), target);

        // ConcreteClassifier clazz = cu.getClassifiers().get(1);
        // assertEquals(clazz.getMembers().get(1), target);

        final ConcreteClassifier clazz = cu.getClassifiers()
            .get(2);

        final Statement s = ((ClassMethod) clazz.getMembers()
            .get(2)).getStatements()
                .get(2);
        final ClassMethod target = (ClassMethod) ((MethodCall) ((ExpressionStatement) s).getExpression()).getTarget();
        assertEquals(clazz.getMembers()
            .get(1), target);

        // parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);

        // ResourceSet set =
        // parser.parseDirectory(Paths.get("scr-input/ClassA","scr-input/ClassB"));

    }

    @Disabled
    void testNameOfClass() {

        // this.assertClassifierName(declaration, expectedName);
    }

    @Disabled
    public void testSrcSevenAndUp() {
        final ResourceSet set = parser.parseDirectory(Paths.get("src-sevenandup"));
        AbstractJaMoPPTests.assertModelValid(set);
    }
}
