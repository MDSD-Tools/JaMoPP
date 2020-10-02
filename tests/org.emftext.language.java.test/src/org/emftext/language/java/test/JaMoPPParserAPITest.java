package org.emftext.language.java.test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.statements.ExpressionStatement;
import org.emftext.language.java.statements.Statement;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Class for testing the Parser API
 */

public class JaMoPPParserAPITest extends AbstractJaMoPPTests {
	
	private JaMoPPJDTParser parser;
	private static final String JAVA_FILE_EXTENSION = ".java";
	protected static final String TEST_INPUT_FOLDER = "src-input";
	
	@Before
	public void setUp() {
		parser = new JaMoPPJDTParser();
		
	}
	
	// 
	//Bekomme ich aus dem resource set irgendwie wieder classifier raus? 
	// oder die Assert Parser nutzen aus dem old JamoPP project? 
	
	@Ignore
	public void testIsClass() throws Exception {
		//ResourceSet set = parser.parseDirectory(Paths.get("scr-input/ClassA"));
		//set.getResources().get(index)
		String typename = "ClassA";
		//String filename = typename + JAVA_FILE_EXTENSION;
		org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		
		this.assertIsClass(clazz);
		//this.assertParsesToType(typename, "Class")
	}
	
	@Ignore
	void testNameOfClass() {
		
		//this.assertClassifierName(declaration, expectedName);
	}
	
	@Test 
	public void testMethodOverwriting()throws Exception {
		//System.out.print("setup");
		String filenameParent = "scr-input/ClassB" + JAVA_FILE_EXTENSION;
		String filenameChild = "scr-input/ClassA" + JAVA_FILE_EXTENSION;
		CompilationUnit cu = (CompilationUnit) parseResource(filenameParent,filenameChild);
		System.out.print("setup");
		System.out.print(cu.getClassifiers().get(1).getName());
		System.out.print(cu.getClassifiers().get(2).getName());
		
		
		//assertEquals(clazz.getMembers().get(1), target);
		
		//ConcreteClassifier clazz = cu.getClassifiers().get(1);
		//assertEquals(clazz.getMembers().get(1), target);
		
		ConcreteClassifier clazz = cu.getClassifiers().get(2);
				
				Statement s = ((ClassMethod) clazz.getMembers().get(2)).getStatements().get(2);
		ClassMethod target = (ClassMethod) ((MethodCall) (
				(ExpressionStatement) s).getExpression()).getTarget();
		assertEquals(clazz.getMembers().get(1), target);
		
		
		//parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
		
		
		//ResourceSet set = parser.parseDirectory(Paths.get("scr-input/ClassA","scr-input/ClassB"));
		
	}
	
	
	@Ignore
	public void testSrcSevenAndUp() {
		ResourceSet set = parser.parseDirectory(Paths.get("src-sevenandup"));
		this.assertModelValid(set);
	}

	@Ignore
	protected boolean isExcludedFromReprintTest(String filename) {
		return true;
	}

	@Ignore
	protected String getTestInputFolder() {
		return "";
	}

}


