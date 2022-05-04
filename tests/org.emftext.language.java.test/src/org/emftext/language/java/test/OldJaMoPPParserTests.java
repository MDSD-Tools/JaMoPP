/*******************************************************************************
 * Copyright (c) 2006-2014
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany;
 *      - initial API and implementation
 *   Yves Kirschner - KIT, Germany
 *   	- penultimate implementation
 ******************************************************************************/

package org.emftext.language.java.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.imports.ClassifierImport;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.StaticImport;
import org.emftext.language.java.literals.BooleanLiteral;
import org.emftext.language.java.literals.CharacterLiteral;
import org.emftext.language.java.literals.DecimalDoubleLiteral;
import org.emftext.language.java.literals.DecimalFloatLiteral;
import org.emftext.language.java.literals.DecimalIntegerLiteral;
import org.emftext.language.java.literals.DecimalLongLiteral;
import org.emftext.language.java.literals.HexDoubleLiteral;
import org.emftext.language.java.literals.HexFloatLiteral;
import org.emftext.language.java.literals.HexIntegerLiteral;
import org.emftext.language.java.literals.HexLongLiteral;
import org.emftext.language.java.literals.IntegerLiteral;
import org.emftext.language.java.literals.LongLiteral;
import org.emftext.language.java.literals.OctalIntegerLiteral;
import org.emftext.language.java.literals.OctalLongLiteral;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.operators.LessThan;
import org.emftext.language.java.parameters.VariableLengthParameter;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.StringReference;
import org.emftext.language.java.resources.NumberLiterals;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.ExpressionStatement;
import org.emftext.language.java.statements.ForEachLoop;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.types.TypeReference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * JUnit test suite to test the JaMoPP parser. New tests should by added by:
 * <ul>
 * <li>putting a Java source file that contains valid Java classes to parse to
 * the <code>src-input</code> folder of this plug-in</li>
 * <li>declaring a test case in this path of Java source relative to the input
 * folder file to the method parseResource(String relativePath)</li>
 * <li>checking the returned CompilationUnit for correctness</li>
 * </ul>
 *
 * @author Christian Wende
 * @author Yves Kirschner
 */
public class OldJaMoPPParserTests extends AbstractJaMoPPTests {

	private static final String JAVA_FILE_EXTENSION = ".java";

	private static final String RESOLVING_FOLDER = "resolving/";

	protected static final String TEST_INPUT_FOLDER = "src-input";

	private static final String UNICODE_FOLDER = "unicode/";

	private static <T extends NamedElement> T findElementByName(List<T> elements, String name) {
		for (final T next : elements) {
			if (name.equals(next.getName())) {
				return next;
			}
		}
		return null;
	}

	private void assertIsBooleanField(Member member, boolean expectedInitValue) {
		assertType(member, Field.class);
		final Field booleanField = (Field) member;
		final Expression initValueForBoolean = booleanField.getInitialValue();

		final BooleanLiteral literal = (BooleanLiteral) initValueForBoolean;

		assertType(literal, BooleanLiteral.class);
		final BooleanLiteral initLiteralForBoolean = literal;
		assertEquals(expectedInitValue, initLiteralForBoolean.isValue());
	}

	private void assertIsCharField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final Field charField = (Field) member;
		final Expression initValue = charField.getInitialValue();

		final CharacterLiteral literal = (CharacterLiteral) initValue;

		assertType(literal, CharacterLiteral.class);
		final CharacterLiteral initLiteral = literal;
		assertEquals(expectedInitValue, initLiteral.getValue());
	}

	private void assertIsDecimalIntegerField(Member member, int expectedInitValue) {
		assertType(member, Field.class);
		final Field longField = (Field) member;
		final Expression initValue = longField.getInitialValue();

		final IntegerLiteral literal = (IntegerLiteral) initValue;

		assertType(literal, DecimalIntegerLiteral.class);
		final DecimalIntegerLiteral initLiteralForBoolean = (DecimalIntegerLiteral) literal;
		assertEquals(BigInteger.valueOf(expectedInitValue), initLiteralForBoolean.getDecimalValue());
	}

	private void assertIsDecimalLongField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final Field longField = (Field) member;
		final Expression initValue = longField.getInitialValue();

		final LongLiteral literal = (LongLiteral) initValue;

		assertType(literal, DecimalLongLiteral.class);
		final DecimalLongLiteral initLiteralForBoolean = (DecimalLongLiteral) literal;
		BigInteger expected;
		if (expectedInitValue.toLowerCase().startsWith("0x")) {
			expected = new BigInteger(expectedInitValue.substring(2), 16);
		} else {
			expected = new BigInteger(expectedInitValue);
		}
		assertEquals(expected, initLiteralForBoolean.getDecimalValue());
	}

	private void assertIsDoubleField(Member member, double expectedInitValue) {
		assertType(member, Field.class);
		final Field charField = (Field) member;
		final Expression initValue = charField.getInitialValue();

		final DecimalDoubleLiteral literal = (DecimalDoubleLiteral) initValue;

		assertNotNull(literal, member.getName() + " is not a double field.");
		assertType(literal, DecimalDoubleLiteral.class);
		final DecimalDoubleLiteral initLiteral = literal;
		assertEquals(expectedInitValue, initLiteral.getDecimalValue(), 0.0);
	}

	private void assertIsHexIntegerField(Member member, int expectedInitValue) {
		assertType(member, Field.class);
		final Field longField = (Field) member;
		final Expression initValue = longField.getInitialValue();

		final IntegerLiteral literal = (IntegerLiteral) initValue;

		assertType(literal, HexIntegerLiteral.class);
		final HexIntegerLiteral initLiteralForBoolean = (HexIntegerLiteral) literal;
		assertEquals(BigInteger.valueOf(expectedInitValue), initLiteralForBoolean.getHexValue());
	}

	private void assertIsHexLongField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final Field longField = (Field) member;
		final Expression initValue = longField.getInitialValue();

		final LongLiteral literal = (LongLiteral) initValue;

		assertType(literal, HexLongLiteral.class);
		final HexLongLiteral initLiteralForBoolean = (HexLongLiteral) literal;
		BigInteger expected;
		if (expectedInitValue.toLowerCase().startsWith("0x")) {
			expected = new BigInteger(expectedInitValue.substring(2), 16);
		} else {
			expected = new BigInteger(expectedInitValue);
		}
		assertEquals(expected, initLiteralForBoolean.getHexValue());
	}

	private void assertIsNumericField(List<Member> members, String name, Object expectedValue) {
		final NamedElement field = findElementByName(members, name);
		assertNotNull(field);
		assertType(field, Field.class);
		final Field unicode = (Field) field;
		final Expression value = unicode.getInitialValue();
		assertNotNull(value);

		Object initValue = null;
		if (value instanceof DecimalIntegerLiteral) {
			initValue = ((DecimalIntegerLiteral) value).getDecimalValue();
		}
		if (value instanceof DecimalLongLiteral) {
			initValue = ((DecimalLongLiteral) value).getDecimalValue();
		}
		if (value instanceof DecimalFloatLiteral) {
			initValue = ((DecimalFloatLiteral) value).getDecimalValue();
		}
		if (value instanceof DecimalDoubleLiteral) {
			initValue = ((DecimalDoubleLiteral) value).getDecimalValue();
		}
		if (value instanceof HexIntegerLiteral) {
			initValue = ((HexIntegerLiteral) value).getHexValue();
		}
		if (value instanceof HexLongLiteral) {
			initValue = ((HexLongLiteral) value).getHexValue();
		}
		if (value instanceof HexFloatLiteral) {
			initValue = ((HexFloatLiteral) value).getHexValue();
		}
		if (value instanceof HexDoubleLiteral) {
			initValue = ((HexDoubleLiteral) value).getHexValue();
		}
		if (value instanceof OctalIntegerLiteral) {
			initValue = ((OctalIntegerLiteral) value).getOctalValue();
		}
		if (value instanceof OctalLongLiteral) {
			initValue = ((OctalLongLiteral) value).getOctalValue();
		}
		assertNotNull(initValue, "Init value for field " + name + " is null.");
		assertEquals(expectedValue, initValue, "Field " + name);
	}

	private void assertIsOctalLongField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final Field longField = (Field) member;
		final Expression initValue = longField.getInitialValue();

		final LongLiteral literal = (LongLiteral) initValue;

		assertType(literal, OctalLongLiteral.class);
		final OctalLongLiteral initLiteralForBoolean = (OctalLongLiteral) literal;
		BigInteger expected;
		if (expectedInitValue.toLowerCase().startsWith("0x")) {
			expected = new BigInteger(expectedInitValue.substring(2), 16);
		} else {
			expected = new BigInteger(expectedInitValue);
		}
		assertEquals(expected, initLiteralForBoolean.getOctalValue());
	}

	private void assertIsStringField(List<Member> members, String name, String expectedValue) {
		final NamedElement field = findElementByName(members, name);
		assertNotNull(field);
		assertType(field, Field.class);
		final Field unicode = (Field) field;
		final Expression value = unicode.getInitialValue();

		final StringReference literal = (StringReference) value;

		assertType(literal, StringReference.class);
		final StringReference stringValue = literal;
		assertEquals("Unescaped value expected for field \"" + name + "\".", expectedValue, stringValue.getValue());
	}

	private void assertIsStringField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final Field charField = (Field) member;
		final Expression initValue = charField.getInitialValue();

		final StringReference literal = (StringReference) initValue;

		assertType(literal, StringReference.class);
		final StringReference initLiteral = literal;
		assertEquals(expectedInitValue, initLiteral.getValue());
	}

	private void assertParsableAndReprintable(String filename) throws Exception {
		final JavaRoot root = parseResource(filename);
		assertType(root, CompilationUnit.class);
		final CompilationUnit unit = (CompilationUnit) root;
		assertNotNull(unit);

		parseAndReprint(filename);
	}

	private void assertParsesToEnumAndReprints(final String typeName) throws Exception {
		final String filename = typeName + JAVA_FILE_EXTENSION;
		final CompilationUnit model = (CompilationUnit) parseResource(filename);
		assertNumberOfClassifiers(model, 1);
		final Classifier declaration = model.getClassifiers().get(0);
		assertClassifierName(declaration, typeName);
		assertType(declaration, Enumeration.class);

		parseAndReprint(filename);
	}

	@Override
	protected Map<Object, Object> getLoadOptions() {
		return new HashMap<>();
	}

	@Override
	protected String getTestInputFolder() {
		return TEST_INPUT_FOLDER;
	}

	@Override
	protected boolean isExcludedFromReprintTest(String filename) {
		return false;
	}

	@Test
	public void test$InClassName() throws Exception {
		parseAndReprint("ClassWith$InName" + JAVA_FILE_EXTENSION);
		parseAndReprint("ClassWith$$InName" + JAVA_FILE_EXTENSION);
		parseAndReprint("Class$$$$With$$$$In$$$$Name$$$$$" + JAVA_FILE_EXTENSION);
		parseAndReprint("pkg/ClassWith$In$$Pkg" + JAVA_FILE_EXTENSION);
		parseAndReprint("pkg/inner/ClassWith$In$$Inner$Pkg" + JAVA_FILE_EXTENSION);

		final String typename = "ClassWithDollarReferenced";
		final String filename = typename + JAVA_FILE_EXTENSION;

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationInstances() throws Exception {
		final String typename = "AnnotationInstances";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotations() throws Exception {
		final String typename = "Annotations";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Annotation annotation = assertParsesToAnnotation(typename);
		assertMemberCount(annotation, 11);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsAsAnnotationArguments() throws Exception {
		final String typename = "AnnotationsAsAnnotationArguments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 8);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsBetweenKeywords() throws Exception {
		final String typename = "AnnotationsBetweenKeywords";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 7);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForAnnotations() throws Exception {
		final String typename = "AnnotationsForAnnotations";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForEnums() throws Exception {
		final String typename = "AnnotationsForEnums";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration eenum = assertParsesToEnumeration(typename);
		assertMemberCount(eenum, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForInnerTypes() throws Exception {
		final String typename = "AnnotationsForInnerTypes";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForMethods() throws Exception {
		final String typename = "AnnotationsForMethods";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 6);

		parseAndReprint(filename);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testAnnotationsForParameters() throws Exception {
		final String typename = "AnnotationsForParameters";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 15);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForStatements() throws Exception {
		final String typename = "AnnotationsForStatements";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testAnonymousEnum() throws Exception {
		final String typename = "AnonymousEnum";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration enumeration = assertParsesToEnumeration(typename);
		// assert no members because enumeration constants are not members
		assertMemberCount(enumeration, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testAnonymousEnumWithArguments() throws Exception {
		final String typename = "AnonymousEnumWithArguments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration enumeration = assertParsesToEnumeration(typename);
		// assert one member (the constructor) because enumeration constants are not
		// members
		assertMemberCount(enumeration, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testAnonymousInner() throws Exception {
		final String typename = "AnonymousInner";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testArguments() throws Exception {
		final String typename = "Arguments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 5);

		parseAndReprint(filename);
	}

	@Test
	public void testArrayInitializers() throws Exception {
		final String typename = "ArrayInitializers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 10);

		parseAndReprint(filename);
	}

	@Test
	public void testArraysInDeclarationsComplex() throws Exception {
		final String typename = "ArraysInDeclarationsComplex";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 6);
		final List<Member> members = clazz.getMembers();
		assertType(members.get(0), Field.class);
		assertType(members.get(1), Field.class);
		assertType(members.get(2), Field.class);
		assertType(members.get(3), Method.class);
		assertType(members.get(4), Method.class);

		parseAndReprint(filename);
	}

	@Test
	public void testArraysInDeclarationsSimple() throws Exception {
		final String typename = "ArraysInDeclarationsSimple";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 5);
		final List<Member> members = clazz.getMembers();

		assertType(members.get(0), Field.class);
		assertType(members.get(1), Field.class);
		assertType(members.get(2), Field.class);
		assertType(members.get(3), Method.class);
		assertType(members.get(4), Method.class);

		parseAndReprint(filename);
	}

	@Test
	public void testAssignments() throws Exception {
		final String typename = "Assignments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);
		final List<Member> members = clazz.getMembers();

		assertType(members.get(0), Field.class);
		assertType(members.get(1), Block.class);

		parseAndReprint(filename);
	}

	@Test
	public void testBasicEnums() throws Exception {
		assertParsesToEnumAndReprints("BasicEnum");
		assertParsesToEnumAndReprints("BasicEnumWithCommaAndSemicolonAtTheEnd");
		assertParsesToEnumAndReprints("BasicEnumWithCommaAtTheEnd");
		assertParsesToEnumAndReprints("BasicEnumWithSemicolonAtTheEnd");
	}

	@Test
	public void testBlocks() throws Exception {
		final String typename = "Blocks";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testBooleanAssignments() throws Exception {
		final String typename = "BooleanAssignments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testBooleanExpressions() throws Exception {
		final String typename = "BooleanExpressions";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testBug1695() throws Exception {
		final String typename = "Bug1695";
		final String filename = "bugs" + File.separator + typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass("bugs", typename);

		assertEquals("Bug1695", clazz.getName());
		assertEquals("InnerClass", clazz.getMembers().get(0).getName());

		parseAndReprint(filename);
	}

	@Test
	public void testCasting() throws Exception {
		final String typename = "Casting";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testChainedCalls() throws Exception {
		final String typename = "ChainedCalls";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 27);

		parseAndReprint(filename);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testClassSemicolonOnly() throws Exception {
		final String typename = "ClassSemicolonOnly";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testClassWithEnumeratingFieldDeclaration() throws Exception {
		final String typename = "ClassWithEnumeratingFieldDeclaration";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		final List<Member> members = clazz.getMembers();
		assertType(members.get(0), Field.class);

		parseAndReprint(filename);
	}

	@Test
	public void testComments() throws Exception {
		final String typename = "Comments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsAtEOF() throws Exception {
		final String typename = "CommentsAtEOF";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 0);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenCaseStatements() throws Exception {
		final String typename = "CommentsBetweenCaseStatements";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenCatchClauses() throws Exception {
		final String typename = "CommentsBetweenCatchClauses";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenConstructorArguments() throws Exception {
		final String typename = "CommentsBetweenConstructorArguments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenMethodArguments() throws Exception {
		final String typename = "CommentsBetweenMethodArguments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenReferenceSequenceParts() throws Exception {
		final String typename = "CommentsBetweenReferenceSequenceParts";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsInArrayInitializers() throws Exception {
		final String typename = "CommentsInArrayInitializers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsInFieldDeclaration() throws Exception {
		final String typename = "CommentsInFieldDeclaration";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsInParExpression() throws Exception {
		final String typename = "CommentsInParExpression";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testConditionalExpressions() throws Exception {
		final String typename = "ConditionalExpressions";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testConstructorCalls() throws Exception {
		final String typename = "ConstructorCalls";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testControlZ() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "ControlZ.java");
	}

	@Test
	public void testCrazyUnicode() throws Exception {
		final String typename = "CrazyUnicode";
		final String file = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass("pkg", typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(file);
	}

	@Test
	public void testEmptyClass() throws Exception {
		final String typename = "EmptyClass";
		final String filename = typename + JAVA_FILE_EXTENSION;
		assertParsesToClass(typename);

		parseAndReprint(filename);
	}

	@Test
	public void testEmptyEnum() throws Exception {
		final String typename = "EmptyEnum";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration enumeration = assertParsesToEnumeration(typename);
		assertEquals(2, enumeration.getMembers().size(), typename + " should have no members.");

		parseAndReprint(filename);
	}

	@Test
	public void testEmptyEnumWithSemicolon() throws Exception {
		assertParsesToEnumAndReprints("EmptyEnumWithSemicolon");
	}

	@Test
	public void testEmptyInterface() throws Exception {
		final String typename = "EmptyInterface";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);
		assertEquals(0, interfaze.getMembers().size(), typename + " should have no members.");

		parseAndReprint(filename);
	}

	@Test
	public void testEnumImplementingTwoInterfaces() throws Exception {
		final String typename = "EnumImplementingTwoInterfaces";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration enumeration = assertParsesToEnumeration(typename);
		assertEquals(2, enumeration.getImplements().size(), typename + " implements two interfaces.");

		registerInClassPath("EmptyInterface" + JAVA_FILE_EXTENSION);
		registerInClassPath("IOneMethod" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testEnumValueMethodsUse() throws Exception {
		final String typename = "EnumValueMethodsUse";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		final Enumeration enumeration = (Enumeration) clazz.getMembers().get(0);
		assertMemberCount(enumeration, 2);
		parseAndReprint(filename);
	}

	@Test
	public void testEnumWithConstructor() throws Exception {
		final String typename = "EnumWithConstructor";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration enumeration = assertParsesToEnumeration(typename);
		assertMemberCount(enumeration, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testEnumWithMember() throws Exception {
		final String typename = "EnumWithMember";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Enumeration enumeration = assertParsesToEnumeration(typename);
		assertMemberCount(enumeration, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testEqualityExpression() throws Exception {
		final String typename = "EqualityExpression";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testEscapedStrings() throws Exception {
		final String typename = "EscapedStrings";
		final String file = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass("pkg", typename);
		assertMemberCount(clazz, 9);

		parseAndReprint(file);
	}

	@Test
	public void testExceptionThrowing() throws Exception {
		final String typename = "ExceptionThrowing";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 7);

		final List<Member> members = clazz.getMembers();
		assertConstructorThrowsCount(members.get(1), 1);
		assertConstructorThrowsCount(members.get(2), 2);
		assertMethodThrowsCount(members.get(3), 1);
		assertMethodThrowsCount(members.get(4), 3);
		assertMethodThrowsCount(members.get(5), 1);

		parseAndReprint(filename);
	}

	@Test
	public void testExplicitConstructorCalls() throws Exception {
		final String typename = "ExplicitConstructorCalls";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testExplicitGenericConstructorCalls() throws Exception {
		final String typename = "ExplicitGenericConstructorCalls";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 4);

		registerInClassPath("ConstructorCalls" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testExplicitGenericInvocation() throws Exception {
		final String typename = "ExplicitGenericInvocation";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testExpressions() throws Exception {
		final String typename = "Expressions";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testExpressionsAsMethodArguments() throws Exception {
		final String typename = "ExpressionsAsMethodArguments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testForEachLoop() throws Exception {
		final String typename = "ForEachLoop";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		final Member simpleForEach = clazz.getMembers().get(1);
		assertType(simpleForEach, ClassMethod.class);
		final ClassMethod simpleForEachMethod = (ClassMethod) simpleForEach;
		final Statement forEach = simpleForEachMethod.getStatements().get(0);
		assertType(forEach, ForEachLoop.class);
		parseAndReprint(filename);
	}

	@Test
	public void testFullQualifiedNameReferences() throws Exception {
		final String typename = "FullQualifiedNameReferences";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertResolveAllProxies(clazz);

		assertEquals(1, clazz.getMembers().size());
		final Member firstMember = clazz.getMembers().get(0);
		assertType(firstMember, Method.class);
		final Method method = (Method) firstMember;

		final ExpressionStatement statement = (ExpressionStatement) method.getStatements().get(0);
		IdentifierReference ref = (IdentifierReference) statement.getExpression();
		assertType(ref.getTarget(), org.emftext.language.java.containers.Package.class);
		org.emftext.language.java.containers.Package p1 = (org.emftext.language.java.containers.Package) ref
				.getTarget();
		assertEquals(1, p1.getNamespaces().size());
		assertEquals("java", p1.getNamespaces().get(0));

		ref = (IdentifierReference) ref.getNext();
		assertType(ref.getTarget(), org.emftext.language.java.containers.Package.class);
		p1 = (org.emftext.language.java.containers.Package) ref.getTarget();
		assertEquals(2, p1.getNamespaces().size());
		assertEquals("java", p1.getNamespaces().get(0));
		assertEquals("lang", p1.getNamespaces().get(1));

		ref = (IdentifierReference) ref.getNext();
		assertType(ref.getTarget(), org.emftext.language.java.containers.Package.class);
		p1 = (org.emftext.language.java.containers.Package) ref.getTarget();
		assertEquals(3, p1.getNamespaces().size());
		assertEquals("annotation", p1.getNamespaces().get(2));

		parseAndReprint(filename);
	}

	@Test
	public void testGenericConstructorCalls() throws Exception {
		final String typename = "GenericConstructorCalls";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 5);

		parseAndReprint(filename);
	}

	@Test
	public void testGenericConstructors() throws Exception {
		final String typename = "GenericConstructors";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		final List<Member> members = clazz.getMembers();
		assertConstructorTypeParameterCount(members.get(0), 1);
		assertConstructorTypeParameterCount(members.get(1), 2);
		assertConstructorTypeParameterCount(members.get(2), 1);
		assertConstructorTypeParameterCount(members.get(3), 2);

		parseAndReprint(filename);
	}

	@Test
	public void testGenericMethods() throws Exception {
		final String typename = "GenericMethods";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 5);

		final List<Member> members = clazz.getMembers();
		assertMethodTypeParameterCount(members.get(0), 1);
		assertMethodTypeParameterCount(members.get(1), 1);
		assertMethodTypeParameterCount(members.get(2), 2);
		assertMethodTypeParameterCount(members.get(3), 2);
		assertMethodTypeParameterCount(members.get(4), 3);

		parseAndReprint(filename);
	}

	@Test
	public void testGenericSuperConstructors() throws Exception {
		final String typename = "GenericSuperConstructors";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		registerInClassPath("GenericConstructors" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testIExtendsMultiple() throws Exception {
		testEmptyInterface();
		testIOneMethod();

		final String typename = "IExtendsMultiple";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);

		assertEquals(2, interfaze.getExtends().size(), "IExtendsMultiple extends two interfaces.");

		parseAndReprint(filename);
	}

	@Test
	public void testIGenericMembers() throws Exception {
		final String typename = "IGenericMembers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 3);
		final List<Member> members = interfaze.getMembers();
		assertType(members.get(0), Method.class);
		assertType(members.get(1), Interface.class);
		assertType(members.get(2), org.emftext.language.java.classifiers.Class.class);

		assertMethodTypeParameterCount(members.get(0), 1);
		assertInterfaceTypeParameterCount(members.get(1), 1);
		assertClassTypeParameterCount(members.get(2), 1);

		parseAndReprint(filename);
	}

	@Test
	public void testIMembers() throws Exception {
		final String typename = "IMembers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);

		assertMemberCount(interfaze, 5);

		final List<Member> members = interfaze.getMembers();
		assertType(members.get(0), Field.class);
		assertType(members.get(1), Method.class);
		assertType(members.get(2), Interface.class);
		assertType(members.get(3), org.emftext.language.java.classifiers.Class.class);
		assertType(members.get(4), Enumeration.class);

		parseAndReprint(filename);
	}

	@Test
	public void testImport1() throws Exception {
		final String typename = "Import1";
		final String filename = typename + JAVA_FILE_EXTENSION;

		registerInClassPath("Import2" + JAVA_FILE_EXTENSION);

		final CompilationUnit model = (CompilationUnit) parseResource(filename);
		assertNumberOfClassifiers(model, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testImport2() throws Exception {
		final String typename = "Import2";
		final String filename = typename + JAVA_FILE_EXTENSION;

		final CompilationUnit model = (CompilationUnit) parseResource(filename);
		assertNumberOfClassifiers(model, 1);

		registerInClassPath("Import1" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testInstanceOfArrayType() throws Exception {
		final String typename = "InstanceOfArrayType";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testInterfaces() throws Exception {
		final String filename1 = "Interface1" + JAVA_FILE_EXTENSION;
		final String filename2 = "Interface2" + JAVA_FILE_EXTENSION;
		final String filename3 = "Interface3" + JAVA_FILE_EXTENSION;

		parseAndReprint(filename1);
		parseAndReprint(filename2);
		parseAndReprint(filename3);

		final String typename = "InterfaceUse";
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);
		final Statement s = ((Block) clazz.getMembers().get(0)).getStatements().get(1);
		final ConcreteClassifier target = ((MethodCall) ((IdentifierReference) ((ExpressionStatement) s)
				.getExpression()).getNext()).getTarget().getContainingConcreteClassifier();
		// should point at interface2 with the most concrete type as return type of
		// getX()
		assertEquals("SyntheticContainerClass", target.getName());
		parseAndReprint(typename + JAVA_FILE_EXTENSION);
	}

	@Test
	public void testIOneMethod() throws Exception {
		final String typename = "IOneMethod";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 1);

		parseAndReprint(filename);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testISemicolonOnly() throws Exception {
		final String typename = "ISemicolonOnly";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testITwoPublicVoidMethods() throws Exception {
		final String typename = "ITwoPublicVoidMethods";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 2);

		final List<Member> members = interfaze.getMembers();
		final Member member1 = members.get(0);
		final Member member2 = members.get(1);
		final Method method1 = (Method) member1;
		final Method method2 = (Method) member2;
		assertModifierCount(method1, 1);
		assertModifierCount(method2, 1);
		assertIsPublic(method1);
		assertIsPublic(method2);

		parseAndReprint(filename);
	}

	@Test
	public void testIWithComments() throws Exception {
		final String typename = "IWithComments";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Interface interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testJavadoc1() throws Exception {
		final String filename1 = "JavaDocCommentBlock" + JAVA_FILE_EXTENSION;
		parseAndReprint(filename1);
	}

	@Test
	public void testJavadoc2() throws Exception {
		final String filename2 = "JavaDocCommentInClass" + JAVA_FILE_EXTENSION;
		parseAndReprint(filename2);
	}

	@Test
	public void testJavadoc3() throws Exception {
		final String filename3 = "JavaDocCommentInField" + JAVA_FILE_EXTENSION;
		parseAndReprint(filename3);
	}

	@Test
	public void testLegalIdentifiers() throws Exception {
		final String typename = "LegalIdentifiers";
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 1);

		// parseAndReprint(typename + JAVA_FILE_EXTENSION);
	}

	@Test
	public void testLiterals() throws Exception {
		final String typename = "Literals";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 27);

		final List<Member> members = clazz.getMembers();
		// check the fields and their initialization values
		assertIsDecimalIntegerField(findElementByName(members, "i1"), 3);
		assertIsHexIntegerField(members.get(2), 1);
		assertIsOctalLongField(members.get(3), "8");
		assertIsOctalLongField(members.get(4), "0");
		assertIsDoubleField(members.get(9), 1.5);
		assertIsCharField(members.get(10), "a");
		assertIsStringField(members.get(11), "abc");
		assertIsBooleanField(members.get(12), false);
		assertIsBooleanField(members.get(13), true);

		final Member maxLongField = findElementByName(members, "maxLong");
		assertNotNull(maxLongField);
		assertIsHexLongField(maxLongField, "0xffffffffffffffff");

		final Member i7Field = findElementByName(members, "i7");
		assertNotNull(i7Field);
		assertIsHexIntegerField(i7Field, 0xff);

		final Member i8Field = findElementByName(members, "i8");
		assertNotNull(i8Field);
		assertIsDecimalLongField(i8Field, "10");

		parseAndReprint(filename);
	}

	@Test
	public void testLocalVariableDeclarations() throws Exception {
		final String typename = "LocalVariableDeclarations";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testLocation() throws Exception {
		final String filename = "locations/Location.java";
		assertParsableAndReprintable(filename);
	}

	@Test
	public void testMembers() throws Exception {
		final String typename = "Members";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 6);

		final List<Member> members = clazz.getMembers();
		assertType(members.get(0), Field.class);
		assertType(members.get(1), Constructor.class);
		assertType(members.get(2), Method.class);
		assertType(members.get(3), Interface.class);
		assertType(members.get(4), org.emftext.language.java.classifiers.Class.class);
		assertType(members.get(5), Enumeration.class);

		parseAndReprint(filename);
	}

	@Test
	public void testMethodCallsWithLocalTypeReferences() throws Exception {
		final String typename = "MethodCallsWithLocalTypeReferences";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testMethodOverloading() throws Exception {
		final String filename = "resolving_new/methodOverloading_2/MethodOverloading" + JAVA_FILE_EXTENSION;
		final CompilationUnit cu = (CompilationUnit) parseResource(filename);
		final ConcreteClassifier clazz = cu.getClassifiers().get(0);

		final Statement s = ((ClassMethod) clazz.getMembers().get(2)).getStatements().get(2);
		final ClassMethod target = (ClassMethod) ((MethodCall) ((ExpressionStatement) s).getExpression()).getTarget();
		assertEquals(clazz.getMembers().get(1), target);
		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testMethodOverride() throws Exception {
		final String typename = "MethodOverride";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		final Statement s = ((Block) clazz.getMembers().get(0)).getStatements().get(1);
		final ConcreteClassifier target = ((MethodCall) ((IdentifierReference) ((ExpressionStatement) s)
				.getExpression()).getNext()).getTarget().getContainingConcreteClassifier();
		assertEquals("StringBuffer", target.getName());
		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testModifiers() throws Exception {
		final String typename = "Modifiers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 29);

		parseAndReprint(filename);
	}

	@Test
	public void testMoreUnicodeCharacters() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "MoreUnicodeCharacters.java");
	}

	@Test
	public void testMultipleImplements() throws Exception {
		testEmptyInterface();
		testIOneMethod();

		final String typename = "MultipleImplements";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 0);
		final List<TypeReference> implementedInterfaces = clazz.getImplements();
		assertEquals(2, implementedInterfaces.size());

		registerInClassPath("ISemicolonOnly" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testMultiplications() throws Exception {
		final String typename = "Multiplications";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		final List<Member> members = clazz.getMembers();

		final Field longField = (Field) members.get(1);
		final Expression initValue = longField.getInitialValue();

		final TreeIterator<EObject> iter = initValue.eAllContents();
		DecimalIntegerLiteral literal1 = null;
		DecimalIntegerLiteral literal2 = null;
		while (iter.hasNext()) {
			final Object obj = iter.next();
			if (obj instanceof DecimalIntegerLiteral) {
				if (literal1 == null) {
					literal1 = (DecimalIntegerLiteral) obj;
				} else {
					literal2 = (DecimalIntegerLiteral) obj;
				}
			}
		}
		assertNotNull(literal1, "no IntegerLiteral found");
		assertNotNull(literal2, "no second IntegerLiteral found");
		assertEquals(BigInteger.valueOf(3), literal1.getDecimalValue());
		assertEquals(BigInteger.valueOf(4), literal2.getDecimalValue());

		parseAndReprint(filename);
	}

	@Test
	public void testNestedPkgPackageInfo() throws Exception {
		// deep nested package with annotation in SAME package
		parseAndReprint("pkg2/pkg3/pkg4/PackageAnnotation.java");

		parseAndReprint("pkg2/pkg3/pkg4/package-info.java");
	}

	@Test
	public void testNoTypeArgument() throws Exception {
		final String typename = "NoTypeArgument";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);
		final Field b = (Field) clazz.getMembers().get(2);
		final RelationExpression exp = (RelationExpression) b.getInitialValue();
		assertTrue(exp.getRelationOperators().size() == 1);
		assertTrue(exp.getRelationOperators().get(0) instanceof LessThan,
				exp.getRelationOperators().get(0).eClass().getName());
		assertTrue(exp.getChildren().get(1) instanceof ShiftExpression);

		parseAndReprint(filename);
	}

	@Test
	public void testNumberLiterals() throws Exception {
		final String typename = "NumberLiterals";
		final String file = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass("pkg", typename);
		assertMemberCount(clazz, 46);
		// iterate over all fields, get their value using reflection and
		// compare this value with the one from the Java parser
		final java.lang.reflect.Field[] fields = NumberLiterals.class.getDeclaredFields();
		for (final java.lang.reflect.Field field : fields) {
			final Object value = field.get(null);
			Object bigValue = value;
			if (value instanceof Integer) {
				bigValue = BigInteger.valueOf(((Integer) value).longValue());
			}
			if (value instanceof Long) {
				bigValue = BigInteger.valueOf((Long) value);
			}
			assertIsNumericField(clazz.getMembers(), field.getName(), bigValue);
		}
		parseAndReprint(file);
	}

	@Test
	public void testParametersWithModifiers() throws Exception {
		final String typename = "ParametersWithModifiers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testPkgEmptyClass() throws Exception {
		final CompilationUnit model = (CompilationUnit) parseResource("pkg/EmptyClass.java");
		assertNumberOfClassifiers(model, 1);
		final Classifier declaration = model.getClassifiers().get(0);
		assertEquals("EmptyClass", declaration.getName(), "The name of the declared class equals 'EmptyClass'");
		assertEquals("pkg", model.getNamespaces().get(0), "pkg.Empty is located in a package 'pkg'");
		parseAndReprint("pkg/EmptyClass.java");
	}

	@Test
	public void testPkgInnerEmptyClass() throws Exception {
		final CompilationUnit model = (CompilationUnit) parseResource("pkg/inner/Inner.java");
		assertNumberOfClassifiers(model, 1);
		final Classifier declaraction = model.getClassifiers().get(0);
		assertEquals("Inner", declaraction.getName(), "The name of the declared class equals 'Inner'");
		assertEquals("inner", model.getNamespaces().get(1), "pkg.inner.Inner is located in a package 'inner'");
		assertEquals("pkg", model.getNamespaces().get(0), "Package 'Inner' is located in a package 'pkg'");
		parseAndReprint("pkg/inner/Inner.java");
	}

	@Test
	public void testPkgPackageAnnotation() throws Exception {
		final CompilationUnit model = (CompilationUnit) parseResource("pkg/PackageAnnotation.java");
		assertNumberOfClassifiers(model, 1);
		parseAndReprint("pkg/PackageAnnotation.java");
	}

	@Test
	public void testPkgPackageInfo() throws Exception {
		parseAndReprint("pkg2/pkg3/Pkg2Enum.java");
		parseAndReprint("pkg2/pkg3/PackageAnnotation.java");

		parseAndReprint("pkg2/package-info.java");
	}

	@Test
	public void testPkgPackageInfoWithRegisterClass() throws Exception {
		registerInClassPath("pkg/PackageAnnotation.java");

		parseAndReprint("pkg/package-info.java");
	}

	@Test
	public void testPrimitiveTypeArrays() throws Exception {
		final String typename = "PrimitiveTypeArrays";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testResolving() throws Exception {
		registerInClassPath(RESOLVING_FOLDER + "MethodCallsWithoutInheritance.java");

		assertParsableAndReprintable(RESOLVING_FOLDER + "MethodCalls.java");
		assertParsableAndReprintable(RESOLVING_FOLDER + "MethodCallsWithLocalTypeReferences.java");
		assertParsableAndReprintable(RESOLVING_FOLDER + "MethodCallsWithoutInheritance.java");
		assertParsableAndReprintable(RESOLVING_FOLDER + "ReferenceToInheritedMethod.java");
		assertParsableAndReprintable(RESOLVING_FOLDER + "VariableReferencing.java");
	}

	@Test
	public void testRoundedLiterals() throws Exception {
		final String typename = "RoundedLiterals";
		final String file = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 26);
		parseAndReprint(file);
	}

	@Test
	public void testSemicolonAfterExpressions() throws Exception {
		final String typename = "SemicolonAfterExpressions";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testSemicolonAfterMembers() throws Exception {
		final String typename = "SemicolonAfterMembers";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2 + 4);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testSimpleAnnotations() throws Exception {
		final String typename = "SimpleAnnotations";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final Annotation annotation = assertParsesToAnnotation(typename);
		assertEquals(2, annotation.getMembers().size(), typename + " should have 2 members.");

		parseAndReprint(filename);
	}

	@Test
	public void testSimpleMethodCalls() throws Exception {
		final String typename = "SimpleMethodCalls";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testSpecialCharacters() throws Exception {
		final String typename = "SpecialCharacters";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testSpecialHierarchy() {
		try {
			final CompilationUnit model = (CompilationUnit) parseResource(
					"spechier" + File.separator + "SubClass.java");
			assertNumberOfClassifiers(model, 1);
			registerInClassPath("spechier" + File.separator + "ClassC.java");
			parseAndReprint("spechier" + File.separator + "SubClass.java");
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStatements() throws Exception {
		registerInClassPath("ConditionalStatements" + JAVA_FILE_EXTENSION);

		assertParsesToClass("ConditionalStatements", 4);
		assertParsesToClass("TryCatchStatements", 4);
		assertParsesToClass("AssertStatements", 1);
		assertParsesToClass("ThrowStatements", 1);
		assertParsesToClass("SynchronizedStatements", 3);
		assertParsesToClass("SwitchStatements", 12);
		assertParsesToClass("DeclarationStatements", 1);
		assertParsesToClass("JumpLabelStatements", 4);
		assertParsesToClass("LoopStatements", 11);
	}

	@Test
	public void testStaticImports() throws Exception {
		final String typename = "StaticImports";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final CompilationUnit unit = (CompilationUnit) parseResource(filename, getTestInputFolder());
		final List<Import> imports = unit.getImports();
		assertEquals(2, imports.size());
		assertTrue(imports.get(0) instanceof StaticImport, "first import is not static");
		assertTrue(imports.get(1) instanceof ClassifierImport, "second import is static");
		registerInClassPath("pkg/EmptyClass" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/EscapedStrings" + JAVA_FILE_EXTENSION);
		parseAndReprint(filename);
	}

	@Test
	public void testStringLiteralReferencing() throws Exception {
		final String typename = "StringLiteralReferencing";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testSuperKeyword() throws Exception {
		final String typename = "SuperKeyword";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		final Member method = clazz.getMembers().get(0);
		assertType(method, Constructor.class);

		parseAndReprint(filename);
	}

	@Test
	public void testSynchronized() throws Exception {
		final String typename = "Synchronized";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testTempLiterals() throws Exception {
		final String typename = "TempLiterals";
		final String file = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 9);
		parseAndReprint(file);
	}

	@Test
	public void testTypeParameters() throws Exception {
		testIOneMethod();

		final String typename = "TypeParameters";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 14);

		parseAndReprint(filename);
	}

	@Test
	public void testTypeReferencing() throws Exception {
		final String typename = "TypeReferencing";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		registerInClassPath("pkg/EmptyClass" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/inner/Inner" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testTypeReferencingExternal() throws Exception {
		final String typename = "TypeReferencingExternal";
		final String filename = typename + JAVA_FILE_EXTENSION;
		assertParsesToClass(typename);

		registerInClassPath("TypeReferencing" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/EmptyClass" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/inner/Inner" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testUnaryExpressions() throws Exception {
		final String typename = "UnaryExpressions";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testUnicode() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "Unicode.java");
	}

	@Test
	public void testUnicodeIdentifiers() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "UnicodeIdentifiers.java");
	}

	@Test
	public void testUnicodeSurrogateCharacter() throws Exception {
		final String typename = "UnicodeSurrogateCharacters";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		final Member m1 = clazz.getMembers().get(0);
		assertTrue(m1 instanceof Field);
		final Expression value = ((Field) m1).getInitialValue();
		assertTrue(value instanceof CharacterLiteral);
		final String c = ((CharacterLiteral) value).getValue();
		assertEquals("\\uD800", c);
		parseAndReprint(filename);
	}

	@Test
	public void testUsingAnnotations() throws Exception {
		final String typename = "UsingAnnotations";
		final String filename = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		assertParsesToClass("pkg", typename);

		parseAndReprint(filename);
	}

	@Test
	public void testVariableLengthArgumentList() throws Exception {
		final String typename = "VariableLengthArgumentList";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 4);
		final Member firstMember = clazz.getMembers().get(0);
		final Constructor constructor = assertIsConstructor(firstMember);
		assertEquals(1, constructor.getParameters().size(), "Constructor of " + typename + " should habe 1 parameter.");
		assertType(constructor.getParameters().get(0), VariableLengthParameter.class);

		parseAndReprint(filename);
	}

	@Test
	public void testVariableReferencing() throws Exception {
		final String typename = "VariableReferencing";
		final String filename = typename + JAVA_FILE_EXTENSION;
		final org.emftext.language.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}
}
