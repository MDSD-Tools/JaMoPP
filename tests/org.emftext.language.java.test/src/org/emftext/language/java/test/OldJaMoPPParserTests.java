/*******************************************************************************
 * Copyright (c) 2006-2014 Software Technology Group, Dresden University of Technology DevBoost
 * GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Software Technology Group - TU Dresden, Germany; DevBoost GmbH - Berlin, Germany; -
 * initial API and implementation Yves Kirschner - KIT, Germany - penultimate implementation
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

import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.containers.CompilationUnit;
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
import org.emftext.language.java.types.TypeReference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.osgi.service.component.annotations.Deactivate;

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

	private static void assertIsBooleanField(Member member, boolean expectedInitValue) {
		assertType(member, Field.class);
		final var booleanField = (Field) member;
		final var initValueForBoolean = booleanField.getInitialValue();

		final var literal = (BooleanLiteral) initValueForBoolean;

		assertType(literal, BooleanLiteral.class);
		final var initLiteralForBoolean = literal;
		assertEquals(expectedInitValue, initLiteralForBoolean.isValue());
	}

	private static void assertIsCharField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final var charField = (Field) member;
		final var initValue = charField.getInitialValue();

		final var literal = (CharacterLiteral) initValue;

		assertType(literal, CharacterLiteral.class);
		final var initLiteral = literal;
		assertEquals(expectedInitValue, initLiteral.getValue());
	}

	private static void assertIsDecimalIntegerField(Member member, int expectedInitValue) {
		assertType(member, Field.class);
		final var longField = (Field) member;
		final var initValue = longField.getInitialValue();

		final var literal = (IntegerLiteral) initValue;

		assertType(literal, DecimalIntegerLiteral.class);
		final var initLiteralForBoolean = (DecimalIntegerLiteral) literal;
		assertEquals(BigInteger.valueOf(expectedInitValue), initLiteralForBoolean.getDecimalValue());
	}

	private static void assertIsDecimalLongField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final var longField = (Field) member;
		final var initValue = longField.getInitialValue();

		final var literal = (LongLiteral) initValue;

		assertType(literal, DecimalLongLiteral.class);
		final var initLiteralForBoolean = (DecimalLongLiteral) literal;
		BigInteger expected;
		if (expectedInitValue.toLowerCase().startsWith("0x")) {
			expected = new BigInteger(expectedInitValue.substring(2), 16);
		} else {
			expected = new BigInteger(expectedInitValue);
		}
		assertEquals(expected, initLiteralForBoolean.getDecimalValue());
	}

	private static void assertIsDoubleField(Member member, double expectedInitValue) {
		assertType(member, Field.class);
		final var charField = (Field) member;
		final var initValue = charField.getInitialValue();

		final var literal = (DecimalDoubleLiteral) initValue;

		assertNotNull(literal, member.getName() + " is not a double field.");
		assertType(literal, DecimalDoubleLiteral.class);
		final var initLiteral = literal;
		assertEquals(expectedInitValue, initLiteral.getDecimalValue(), 0.0);
	}

	private static void assertIsHexIntegerField(Member member, int expectedInitValue) {
		assertType(member, Field.class);
		final var longField = (Field) member;
		final var initValue = longField.getInitialValue();

		final var literal = (IntegerLiteral) initValue;

		assertType(literal, HexIntegerLiteral.class);
		final var initLiteralForBoolean = (HexIntegerLiteral) literal;
		assertEquals(BigInteger.valueOf(expectedInitValue), initLiteralForBoolean.getHexValue());
	}

	private static void assertIsHexLongField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final var longField = (Field) member;
		final var initValue = longField.getInitialValue();

		final var literal = (LongLiteral) initValue;

		assertType(literal, HexLongLiteral.class);
		final var initLiteralForBoolean = (HexLongLiteral) literal;
		BigInteger expected;
		if (expectedInitValue.toLowerCase().startsWith("0x")) {
			expected = new BigInteger(expectedInitValue.substring(2), 16);
		} else {
			expected = new BigInteger(expectedInitValue);
		}
		assertEquals(expected, initLiteralForBoolean.getHexValue());
	}

	private static <T extends NamedElement> T findElementByName(List<T> elements, String name) {
		for (final T next : elements) {
			if (name.equals(next.getName())) {
				return next;
			}
		}
		return null;
	}

	private void assertIsNumericField(List<Member> members, String name, Object expectedValue) {
		final NamedElement field = findElementByName(members, name);
		assertNotNull(field);
		assertType(field, Field.class);
		final var unicode = (Field) field;
		final var value = unicode.getInitialValue();
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
		final var longField = (Field) member;
		final var initValue = longField.getInitialValue();

		final var literal = (LongLiteral) initValue;

		assertType(literal, OctalLongLiteral.class);
		final var initLiteralForBoolean = (OctalLongLiteral) literal;
		BigInteger expected;
		if (expectedInitValue.toLowerCase().startsWith("0x")) {
			expected = new BigInteger(expectedInitValue.substring(2), 16);
		} else {
			expected = new BigInteger(expectedInitValue);
		}
		assertEquals(expected, initLiteralForBoolean.getOctalValue());
	}

	private void assertIsStringField(Member member, String expectedInitValue) {
		assertType(member, Field.class);
		final var charField = (Field) member;
		final var initValue = charField.getInitialValue();

		final var literal = (StringReference) initValue;

		assertType(literal, StringReference.class);
		final var initLiteral = literal;
		assertEquals(expectedInitValue, initLiteral.getValue());
	}

	private void assertParsableAndReprintable(String filename) throws Exception {
		final var root = parseResource(filename);
		assertType(root, CompilationUnit.class);
		final var unit = (CompilationUnit) root;
		assertNotNull(unit);

		parseAndReprint(filename);
	}

	private void assertParsesToEnumAndReprints(final String typeName) throws Exception {
		final var filename = typeName + JAVA_FILE_EXTENSION;
		final var model = (CompilationUnit) parseResource(filename);
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

		final var typename = "ClassWithDollarReferenced";
		final var filename = typename + JAVA_FILE_EXTENSION;

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationInstances() throws Exception {
		final var typename = "AnnotationInstances";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotations() throws Exception {
		final var typename = "Annotations";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var annotation = assertParsesToAnnotation(typename);
		assertMemberCount(annotation, 11);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsAsAnnotationArguments() throws Exception {
		final var typename = "AnnotationsAsAnnotationArguments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 8);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsBetweenKeywords() throws Exception {
		final var typename = "AnnotationsBetweenKeywords";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 7);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForAnnotations() throws Exception {
		final var typename = "AnnotationsForAnnotations";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForEnums() throws Exception {
		final var typename = "AnnotationsForEnums";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var eenum = assertParsesToEnumeration(typename);
		assertMemberCount(eenum, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForInnerTypes() throws Exception {
		final var typename = "AnnotationsForInnerTypes";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForMethods() throws Exception {
		final var typename = "AnnotationsForMethods";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 6);

		parseAndReprint(filename);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testAnnotationsForParameters() throws Exception {
		final var typename = "AnnotationsForParameters";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 15);

		parseAndReprint(filename);
	}

	@Test
	public void testAnnotationsForStatements() throws Exception {
		final var typename = "AnnotationsForStatements";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testAnonymousEnum() throws Exception {
		final var typename = "AnonymousEnum";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var enumeration = assertParsesToEnumeration(typename);
		// assert no members because enumeration constants are not members
		assertMemberCount(enumeration, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testAnonymousEnumWithArguments() throws Exception {
		final var typename = "AnonymousEnumWithArguments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var enumeration = assertParsesToEnumeration(typename);
		// assert one member (the constructor) because enumeration constants are not
		// members
		assertMemberCount(enumeration, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testAnonymousInner() throws Exception {
		final var typename = "AnonymousInner";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testArguments() throws Exception {
		final var typename = "Arguments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 5);

		parseAndReprint(filename);
	}

	@Test
	public void testArrayInitializers() throws Exception {
		final var typename = "ArrayInitializers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 10);

		parseAndReprint(filename);
	}

	@Test
	public void testArraysInDeclarationsComplex() throws Exception {
		final var typename = "ArraysInDeclarationsComplex";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "ArraysInDeclarationsSimple";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "Assignments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "Blocks";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testBooleanAssignments() throws Exception {
		final var typename = "BooleanAssignments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testBooleanExpressions() throws Exception {
		final var typename = "BooleanExpressions";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testBug1695() throws Exception {
		final var typename = "Bug1695";
		final var filename = "bugs" + File.separator + typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass("bugs", typename);

		assertEquals("Bug1695", clazz.getName());
		assertEquals("InnerClass", clazz.getMembers().get(0).getName());

		parseAndReprint(filename);
	}

	@Test
	public void testCasting() throws Exception {
		final var typename = "Casting";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testChainedCalls() throws Exception {
		final var typename = "ChainedCalls";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 27);

		parseAndReprint(filename);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testClassSemicolonOnly() throws Exception {
		final var typename = "ClassSemicolonOnly";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testClassWithEnumeratingFieldDeclaration() throws Exception {
		final var typename = "ClassWithEnumeratingFieldDeclaration";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		final List<Member> members = clazz.getMembers();
		assertType(members.get(0), Field.class);

		parseAndReprint(filename);
	}

	@Test
	public void testComments() throws Exception {
		final var typename = "Comments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsAtEOF() throws Exception {
		final var typename = "CommentsAtEOF";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 0);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenCaseStatements() throws Exception {
		final var typename = "CommentsBetweenCaseStatements";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenCatchClauses() throws Exception {
		final var typename = "CommentsBetweenCatchClauses";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenConstructorArguments() throws Exception {
		final var typename = "CommentsBetweenConstructorArguments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenMethodArguments() throws Exception {
		final var typename = "CommentsBetweenMethodArguments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsBetweenReferenceSequenceParts() throws Exception {
		final var typename = "CommentsBetweenReferenceSequenceParts";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsInArrayInitializers() throws Exception {
		final var typename = "CommentsInArrayInitializers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsInFieldDeclaration() throws Exception {
		final var typename = "CommentsInFieldDeclaration";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testCommentsInParExpression() throws Exception {
		final var typename = "CommentsInParExpression";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testConditionalExpressions() throws Exception {
		final var typename = "ConditionalExpressions";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testConstructorCalls() throws Exception {
		final var typename = "ConstructorCalls";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testControlZ() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "ControlZ.java");
	}

	@Test
	public void testCrazyUnicode() throws Exception {
		final var typename = "CrazyUnicode";
		final var file = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass("pkg", typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(file);
	}

	@Test
	public void testEmptyClass() throws Exception {
		final var typename = "EmptyClass";
		final var filename = typename + JAVA_FILE_EXTENSION;
		assertParsesToClass(typename);

		parseAndReprint(filename);
	}

	@Test
	public void testEmptyEnum() throws Exception {
		final var typename = "EmptyEnum";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var enumeration = assertParsesToEnumeration(typename);
		assertEquals(2, enumeration.getMembers().size(), typename + " should have no members.");

		parseAndReprint(filename);
	}

	@Test
	public void testEmptyEnumWithSemicolon() throws Exception {
		assertParsesToEnumAndReprints("EmptyEnumWithSemicolon");
	}

	@Test
	public void testEmptyInterface() throws Exception {
		final var typename = "EmptyInterface";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);
		assertEquals(0, interfaze.getMembers().size(), typename + " should have no members.");

		parseAndReprint(filename);
	}

	@Test
	public void testEnumImplementingTwoInterfaces() throws Exception {
		final var typename = "EnumImplementingTwoInterfaces";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var enumeration = assertParsesToEnumeration(typename);
		assertEquals(2, enumeration.getImplements().size(), typename + " implements two interfaces.");

		registerInClassPath("EmptyInterface" + JAVA_FILE_EXTENSION);
		registerInClassPath("IOneMethod" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testEnumValueMethodsUse() throws Exception {
		final var typename = "EnumValueMethodsUse";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		final var enumeration = (Enumeration) clazz.getMembers().get(0);
		assertMemberCount(enumeration, 2);
		parseAndReprint(filename);
	}

	@Test
	public void testEnumWithConstructor() throws Exception {
		final var typename = "EnumWithConstructor";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var enumeration = assertParsesToEnumeration(typename);
		assertMemberCount(enumeration, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testEnumWithMember() throws Exception {
		final var typename = "EnumWithMember";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var enumeration = assertParsesToEnumeration(typename);
		assertMemberCount(enumeration, 4);

		parseAndReprint(filename);
	}

	@Test
	public void testEqualityExpression() throws Exception {
		final var typename = "EqualityExpression";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testEscapedStrings() throws Exception {
		final var typename = "EscapedStrings";
		final var file = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass("pkg", typename);
		assertMemberCount(clazz, 9);

		parseAndReprint(file);
	}

	@Test
	public void testExceptionThrowing() throws Exception {
		final var typename = "ExceptionThrowing";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

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
		final var typename = "ExplicitConstructorCalls";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testExplicitGenericConstructorCalls() throws Exception {
		final var typename = "ExplicitGenericConstructorCalls";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 4);

		registerInClassPath("ConstructorCalls" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testExplicitGenericInvocation() throws Exception {
		final var typename = "ExplicitGenericInvocation";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testExpressions() throws Exception {
		final var typename = "Expressions";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testExpressionsAsMethodArguments() throws Exception {
		final var typename = "ExpressionsAsMethodArguments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testForEachLoop() throws Exception {
		final var typename = "ForEachLoop";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		final var simpleForEach = clazz.getMembers().get(1);
		assertType(simpleForEach, ClassMethod.class);
		final var simpleForEachMethod = (ClassMethod) simpleForEach;
		final var forEach = simpleForEachMethod.getStatements().get(0);
		assertType(forEach, ForEachLoop.class);
		parseAndReprint(filename);
	}

	@Test
	public void testFullQualifiedNameReferences() throws Exception {
		final var typename = "FullQualifiedNameReferences";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertResolveAllProxies(clazz);

		assertEquals(1, clazz.getMembers().size());
		final var firstMember = clazz.getMembers().get(0);
		assertType(firstMember, Method.class);
		final var method = (Method) firstMember;

		final var statement = (ExpressionStatement) method.getStatements().get(0);
		var ref = (IdentifierReference) statement.getExpression();
		assertType(ref.getTarget(), org.emftext.language.java.containers.Package.class);
		var p1 = (org.emftext.language.java.containers.Package) ref
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
		final var typename = "GenericConstructorCalls";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 5);

		parseAndReprint(filename);
	}

	@Test
	public void testGenericConstructors() throws Exception {
		final var typename = "GenericConstructors";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "GenericMethods";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "GenericSuperConstructors";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		registerInClassPath("GenericConstructors" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testIExtendsMultiple() throws Exception {
		testEmptyInterface();
		testIOneMethod();

		final var typename = "IExtendsMultiple";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);

		assertEquals(2, interfaze.getExtends().size(), "IExtendsMultiple extends two interfaces.");

		parseAndReprint(filename);
	}

	@Test
	public void testIGenericMembers() throws Exception {
		final var typename = "IGenericMembers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);
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
		final var typename = "IMembers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);

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
		final var typename = "Import1";
		final var filename = typename + JAVA_FILE_EXTENSION;

		registerInClassPath("Import2" + JAVA_FILE_EXTENSION);

		final var model = (CompilationUnit) parseResource(filename);
		assertNumberOfClassifiers(model, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testImport2() throws Exception {
		final var typename = "Import2";
		final var filename = typename + JAVA_FILE_EXTENSION;

		final var model = (CompilationUnit) parseResource(filename);
		assertNumberOfClassifiers(model, 1);

		registerInClassPath("Import1" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testInstanceOfArrayType() throws Exception {
		final var typename = "InstanceOfArrayType";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename);
	}

	@Test
	public void testInterfaces() throws Exception {
		final var filename1 = "Interface1" + JAVA_FILE_EXTENSION;
		final var filename2 = "Interface2" + JAVA_FILE_EXTENSION;
		final var filename3 = "Interface3" + JAVA_FILE_EXTENSION;

		parseAndReprint(filename1);
		parseAndReprint(filename2);
		parseAndReprint(filename3);

		final var typename = "InterfaceUse";
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);
		final var s = ((Block) clazz.getMembers().get(0)).getStatements().get(1);
		final var target = ((MethodCall) ((IdentifierReference) ((ExpressionStatement) s)
				.getExpression()).getNext()).getTarget().getContainingConcreteClassifier();
		// should point at interface2 with the most concrete type as return type of
		// getX()
		assertEquals("SyntheticContainerClass", target.getName());
		parseAndReprint(typename + JAVA_FILE_EXTENSION);
	}

	@Test
	public void testIOneMethod() throws Exception {
		final var typename = "IOneMethod";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 1);

		parseAndReprint(filename);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testISemicolonOnly() throws Exception {
		final var typename = "ISemicolonOnly";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testITwoPublicVoidMethods() throws Exception {
		final var typename = "ITwoPublicVoidMethods";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 2);

		final List<Member> members = interfaze.getMembers();
		final var member1 = members.get(0);
		final var member2 = members.get(1);
		final var method1 = (Method) member1;
		final var method2 = (Method) member2;
		assertModifierCount(method1, 1);
		assertModifierCount(method2, 1);
		assertIsPublic(method1);
		assertIsPublic(method2);

		parseAndReprint(filename);
	}

	@Test
	public void testIWithComments() throws Exception {
		final var typename = "IWithComments";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var interfaze = assertParsesToInterface(typename);
		assertMemberCount(interfaze, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testJavadoc1() throws Exception {
		final var filename1 = "JavaDocCommentBlock" + JAVA_FILE_EXTENSION;
		parseAndReprint(filename1);
	}

	@Test
	public void testJavadoc2() throws Exception {
		final var filename2 = "JavaDocCommentInClass" + JAVA_FILE_EXTENSION;
		parseAndReprint(filename2);
	}

	@Test
	public void testJavadoc3() throws Exception {
		final var filename3 = "JavaDocCommentInField" + JAVA_FILE_EXTENSION;
		parseAndReprint(filename3);
	}

	@Test
	public void testLegalIdentifiers() throws Exception {
		final var typename = "LegalIdentifiers";
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 1);

		// parseAndReprint(typename + JAVA_FILE_EXTENSION);
	}

	@Test
	public void testLiterals() throws Exception {
		final var typename = "Literals";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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

		final var maxLongField = findElementByName(members, "maxLong");
		assertNotNull(maxLongField);
		assertIsHexLongField(maxLongField, "0xffffffffffffffff");

		final var i7Field = findElementByName(members, "i7");
		assertNotNull(i7Field);
		assertIsHexIntegerField(i7Field, 0xff);

		final var i8Field = findElementByName(members, "i8");
		assertNotNull(i8Field);
		assertIsDecimalLongField(i8Field, "10");

		parseAndReprint(filename);
	}

	@Test
	public void testLocalVariableDeclarations() throws Exception {
		final var typename = "LocalVariableDeclarations";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testLocation() throws Exception {
		final var filename = "locations/Location.java";
		assertParsableAndReprintable(filename);
	}

	@Test
	public void testMembers() throws Exception {
		final var typename = "Members";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "MethodCallsWithLocalTypeReferences";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testMethodOverloading() throws Exception {
		final var filename = "resolving_new/methodOverloading_2/MethodOverloading" + JAVA_FILE_EXTENSION;
		final var cu = (CompilationUnit) parseResource(filename);
		final var clazz = cu.getClassifiers().get(0);

		final var s = ((ClassMethod) clazz.getMembers().get(2)).getStatements().get(2);
		final var target = (ClassMethod) ((MethodCall) ((ExpressionStatement) s).getExpression()).getTarget();
		assertEquals(clazz.getMembers().get(1), target);
		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testMethodOverride() throws Exception {
		final var typename = "MethodOverride";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		final var s = ((Block) clazz.getMembers().get(0)).getStatements().get(1);
		final var target = ((MethodCall) ((IdentifierReference) ((ExpressionStatement) s)
				.getExpression()).getNext()).getTarget().getContainingConcreteClassifier();
		assertEquals("StringBuffer", target.getName());
		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testModifiers() throws Exception {
		final var typename = "Modifiers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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

		final var typename = "MultipleImplements";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 0);
		final List<TypeReference> implementedInterfaces = clazz.getImplements();
		assertEquals(2, implementedInterfaces.size());

		registerInClassPath("ISemicolonOnly" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testMultiplications() throws Exception {
		final var typename = "Multiplications";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		final List<Member> members = clazz.getMembers();

		final var longField = (Field) members.get(1);
		final var initValue = longField.getInitialValue();

		final var iter = initValue.eAllContents();
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
		final var typename = "NoTypeArgument";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);
		final var b = (Field) clazz.getMembers().get(2);
		final var exp = (RelationExpression) b.getInitialValue();
		assertTrue(exp.getRelationOperators().size() == 1);
		assertTrue(exp.getRelationOperators().get(0) instanceof LessThan,
				exp.getRelationOperators().get(0).eClass().getName());
		assertTrue(exp.getChildren().get(1) instanceof ShiftExpression);

		parseAndReprint(filename);
	}

	@Test
	public void testNumberLiterals() throws Exception {
		final var typename = "NumberLiterals";
		final var file = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass("pkg", typename);
		assertMemberCount(clazz, 46);
		// iterate over all fields, get their value using reflection and
		// compare this value with the one from the Java parser
		final var fields = NumberLiterals.class.getDeclaredFields();
		for (final java.lang.reflect.Field field : fields) {
			final var value = field.get(null);
			var bigValue = value;
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
		final var typename = "ParametersWithModifiers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testPkgEmptyClass() throws Exception {
		final var model = (CompilationUnit) parseResource("pkg/EmptyClass.java");
		assertNumberOfClassifiers(model, 1);
		final Classifier declaration = model.getClassifiers().get(0);
		assertEquals("EmptyClass", declaration.getName(), "The name of the declared class equals 'EmptyClass'");
		assertEquals("pkg", model.getNamespaces().get(0), "pkg.Empty is located in a package 'pkg'");
		parseAndReprint("pkg/EmptyClass.java");
	}

	@Test
	public void testPkgInnerEmptyClass() throws Exception {
		final var model = (CompilationUnit) parseResource("pkg/inner/Inner.java");
		assertNumberOfClassifiers(model, 1);
		final Classifier declaraction = model.getClassifiers().get(0);
		assertEquals("Inner", declaraction.getName(), "The name of the declared class equals 'Inner'");
		assertEquals("inner", model.getNamespaces().get(1), "pkg.inner.Inner is located in a package 'inner'");
		assertEquals("pkg", model.getNamespaces().get(0), "Package 'Inner' is located in a package 'pkg'");
		parseAndReprint("pkg/inner/Inner.java");
	}

	@Test
	public void testPkgPackageAnnotation() throws Exception {
		final var model = (CompilationUnit) parseResource("pkg/PackageAnnotation.java");
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
		final var typename = "PrimitiveTypeArrays";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
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
		final var typename = "RoundedLiterals";
		final var file = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 26);
		parseAndReprint(file);
	}

	@Test
	public void testSemicolonAfterExpressions() throws Exception {
		final var typename = "SemicolonAfterExpressions";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	@Disabled("Contains empty members that are not parsed by JDT.")
	public void testSemicolonAfterMembers() throws Exception {
		final var typename = "SemicolonAfterMembers";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2 + 4);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testSimpleAnnotations() throws Exception {
		final var typename = "SimpleAnnotations";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var annotation = assertParsesToAnnotation(typename);
		assertEquals(2, annotation.getMembers().size(), typename + " should have 2 members.");

		parseAndReprint(filename);
	}

	@Test
	public void testSimpleMethodCalls() throws Exception {
		final var typename = "SimpleMethodCalls";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 4);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testSpecialCharacters() throws Exception {
		final var typename = "SpecialCharacters";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}

	@Test
	public void testSpecialHierarchy() {
		try {
			final var model = (CompilationUnit) parseResource(
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
		final var typename = "StaticImports";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var unit = (CompilationUnit) parseResource(filename, getTestInputFolder());
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
		final var typename = "StringLiteralReferencing";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	public void testSuperKeyword() throws Exception {
		final var typename = "SuperKeyword";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		final var method = clazz.getMembers().get(0);
		assertType(method, Constructor.class);

		parseAndReprint(filename);
	}

	@Test
	public void testSynchronized() throws Exception {
		final var typename = "Synchronized";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename);
	}

	@Test
	public void testTempLiterals() throws Exception {
		final var typename = "TempLiterals";
		final var file = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 9);
		parseAndReprint(file);
	}

	@Test
	public void testTypeParameters() throws Exception {
		testIOneMethod();

		final var typename = "TypeParameters";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 14);

		parseAndReprint(filename);
	}

	@Test
	public void testTypeReferencing() throws Exception {
		final var typename = "TypeReferencing";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 3);

		registerInClassPath("pkg/EmptyClass" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/inner/Inner" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testTypeReferencingExternal() throws Exception {
		final var typename = "TypeReferencingExternal";
		final var filename = typename + JAVA_FILE_EXTENSION;
		assertParsesToClass(typename);

		registerInClassPath("TypeReferencing" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/EmptyClass" + JAVA_FILE_EXTENSION);
		registerInClassPath("pkg/inner/Inner" + JAVA_FILE_EXTENSION);

		parseAndReprint(filename);
	}

	@Test
	public void testUnaryExpressions() throws Exception {
		final var typename = "UnaryExpressions";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 1);

		parseAndReprint(filename);
	}

	@Test
	@Deactivate
	public void testUnicode() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "Unicode.java");
	}

	@Test
	public void testUnicodeIdentifiers() throws Exception {
		assertParsableAndReprintable(UNICODE_FOLDER + "UnicodeIdentifiers.java");
	}

	@Test
	public void testUnicodeSurrogateCharacter() throws Exception {
		final var typename = "UnicodeSurrogateCharacters";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		final var m1 = clazz.getMembers().get(0);
		assertTrue(m1 instanceof Field);
		final var value = ((Field) m1).getInitialValue();
		assertTrue(value instanceof CharacterLiteral);
		final var c = ((CharacterLiteral) value).getValue();
		assertEquals("\\uD800", c);
		parseAndReprint(filename);
	}

	@Test
	public void testUsingAnnotations() throws Exception {
		final var typename = "UsingAnnotations";
		final var filename = "pkg" + File.separator + typename + JAVA_FILE_EXTENSION;
		assertParsesToClass("pkg", typename);

		parseAndReprint(filename);
	}

	@Test
	public void testVariableLengthArgumentList() throws Exception {
		final var typename = "VariableLengthArgumentList";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);

		assertMemberCount(clazz, 4);
		final var firstMember = clazz.getMembers().get(0);
		final var constructor = assertIsConstructor(firstMember);
		assertEquals(1, constructor.getParameters().size(), "Constructor of " + typename + " should habe 1 parameter.");
		assertType(constructor.getParameters().get(0), VariableLengthParameter.class);

		parseAndReprint(filename);
	}

	@Test
	public void testVariableReferencing() throws Exception {
		final var typename = "VariableReferencing";
		final var filename = typename + JAVA_FILE_EXTENSION;
		final var clazz = assertParsesToClass(typename);
		assertMemberCount(clazz, 2);

		parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
	}
}
