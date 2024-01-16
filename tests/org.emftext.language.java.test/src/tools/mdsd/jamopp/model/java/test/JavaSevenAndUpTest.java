/*******************************************************************************
 * Copyright (c) 2019-2020, Martin Armbruster
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Martin Armbruster - Initial implementation
 ******************************************************************************/

package tools.mdsd.jamopp.model.java.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.containers.Module;
import tools.mdsd.jamopp.model.java.expressions.ArrayConstructorReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.ClassTypeConstructorReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.LambdaExpression;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpressionReferenceExpression;
import tools.mdsd.jamopp.model.java.instantiations.NewConstructorCall;
import tools.mdsd.jamopp.model.java.literals.BinaryIntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.BinaryLongLiteral;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.modifiers.Default;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.modifiers.Static;
import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.DefaultSwitchRule;
import tools.mdsd.jamopp.model.java.statements.EmptyStatement;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.model.java.statements.LocalVariableStatement;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchCase;
import tools.mdsd.jamopp.model.java.statements.NormalSwitchRule;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.model.java.statements.SwitchCase;
import tools.mdsd.jamopp.model.java.statements.TryBlock;
import tools.mdsd.jamopp.model.java.statements.YieldStatement;
import tools.mdsd.jamopp.model.java.types.InferableType;
import tools.mdsd.jamopp.model.java.types.Int;
import tools.mdsd.jamopp.model.java.types.TypeReference;

/**
 * Test class for the features of Java 7+.
 *
 * @author Martin Armbruster
 */
public class JavaSevenAndUpTest extends AbstractJaMoPPTests {
	@Override
	public String getTestInputFolder() {
		return "src-sevenandup";
	}

	@Override
	public boolean isExcludedFromReprintTest(String file) {
		return false;
	}

	@Test
	public void testIntersectionTypeWithTypeArguments() {
		try {
			final String file = "pkgJava14" + File.separator + "IntersectionTypeWithTypeArguments.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit cu = (CompilationUnit) root;
			final Member m = cu.getClassifiers().get(0).getMembers().get(0);
			assertType(m, ClassMethod.class);
			final ClassMethod method = (ClassMethod) m;
			final LocalVariableStatement locStat = (LocalVariableStatement) method.getBlock().getStatements().get(0);
			final TypeReference typeRef = locStat.getVariable().getTypeReference();
			assertType(typeRef, InferableType.class);
			final InferableType inferType = (InferableType) typeRef;
			assertEquals(1, inferType.getActualTargets().size());
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testModuleInfo() {
		try {
			final String file = "module-info.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, Module.class);
			assertEquals(6, ((Module) root).getTarget().size());
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testPackageInfo() {
		try {
			final String file = "simplepackage" + File.separator + "package-info.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, tools.mdsd.jamopp.model.java.containers.Package.class);
			final tools.mdsd.jamopp.model.java.containers.Package pRoot = (tools.mdsd.jamopp.model.java.containers.Package) root;
			assertEquals(1, pRoot.getNamespaces().size());
			assertEquals("simplepackage", pRoot.getNamespaces().get(0));
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithDiamondTypeArguments() {
		try {
			final String file = "simplepackage" + File.separator + "SimpleClassWithDiamondTypeArguments.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertMemberCount(classifier, 2);
			final Method method = (Method) classifier.getMembers().get(0);
			assertEquals(6, method.getBlock().getStatements().size());
			for (final Statement s : method.getBlock().getStatements()) {
				assertTrue(s instanceof LocalVariableStatement || s instanceof ExpressionStatement);
				if (s instanceof LocalVariableStatement castedS) {
					assertType(castedS.getVariable().getInitialValue(), NewConstructorCall.class);
				} else {
					assertType(((ExpressionStatement) s).getExpression(), NewConstructorCall.class);
				}
			}
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithLambdaExpressions() {
		try {
			final String file = "pkg2" + File.separator + "SimpleClassWithLambdaExpressions.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertClassifierName(classifier, "SimpleClassWithLambdaExpressions");
			assertMemberCount(classifier, 2);
			for (final Member m : classifier.getMembers()) {
				if (m instanceof Interface) {
					assertEquals("I", m.getName());
					assertEquals(1, ((Interface) m).getAnnotationInstances().size());
				} else if (m instanceof Method) {
					assertEquals("lambdas", m.getName());
					assertIsPublic((Method) m);
					assertType(((Method) m).getTypeReference(), tools.mdsd.jamopp.model.java.types.Void.class);
					for (final Statement s : ((Method) m).getBlock().getStatements()) {
						assertTrue(s instanceof ExpressionStatement || s instanceof LocalVariableStatement);
						if (s instanceof ExpressionStatement castedS) {
							assertType(castedS.getExpression(), AssignmentExpression.class);
							final AssignmentExpression expr = (AssignmentExpression) castedS.getExpression();
							assertType(expr.getValue(), LambdaExpression.class);
						} else if (s instanceof LocalVariableStatement castedS) {
							assertType(castedS.getVariable().getInitialValue(), LambdaExpression.class);
						}
					}
				}
			}
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithLiterals() {
		try {
			final String file = "pkg2" + File.separator + "SimpleClassWithLiterals.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertEquals(1, classifier.getMembers().size());
			final Method m = (Method) classifier.getMembers().get(0);
			for (final Statement s : m.getBlock().getStatements()) {
				assertTrue(s instanceof LocalVariableStatement || s instanceof ExpressionStatement);
				if (s instanceof LocalVariableStatement castedS) {
					assertTrue(castedS.getVariable().getInitialValue() instanceof BinaryIntegerLiteral
							|| castedS.getVariable().getInitialValue() instanceof BinaryLongLiteral);
				} else if (s instanceof ExpressionStatement) {
					final AssignmentExpression expr = (AssignmentExpression) ((ExpressionStatement) s).getExpression();
					assertTrue(expr.getValue() instanceof BinaryIntegerLiteral
							|| expr.getValue() instanceof BinaryLongLiteral);
				}
			}
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithMethodReferenceExpressions() {
		try {
			final String file = "simplepackage" + File.separator + "SimpleClassWithMethodReferenceExpressions.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertMemberCount(classifier, 2);
			final Method method = (Method) classifier.getMembers().get(0);
			assertEquals(8, method.getBlock().getStatements().size());
			LocalVariableStatement locStat = (LocalVariableStatement) method.getBlock().getStatements().get(0);
			assertType(locStat.getVariable().getInitialValue(), PrimaryExpressionReferenceExpression.class);
			locStat = (LocalVariableStatement) method.getBlock().getStatements().get(1);
			assertType(locStat.getVariable().getInitialValue(), ClassTypeConstructorReferenceExpression.class);
			AssignmentExpression expr = (AssignmentExpression) ((ExpressionStatement) method.getBlock().getStatements()
					.get(2)).getExpression();
			assertType(expr.getValue(), ClassTypeConstructorReferenceExpression.class);
			expr = (AssignmentExpression) ((ExpressionStatement) method.getBlock().getStatements().get(3))
					.getExpression();
			assertType(expr.getValue(), ClassTypeConstructorReferenceExpression.class);
			expr = (AssignmentExpression) ((ExpressionStatement) method.getBlock().getStatements().get(4))
					.getExpression();
			assertType(expr.getValue(), PrimaryExpressionReferenceExpression.class);
			locStat = (LocalVariableStatement) method.getBlock().getStatements().get(5);
			assertType(locStat.getVariable().getInitialValue(), PrimaryExpressionReferenceExpression.class);
			locStat = (LocalVariableStatement) method.getBlock().getStatements().get(6);
			assertType(locStat.getVariable().getInitialValue(), ArrayConstructorReferenceExpression.class);
			expr = (AssignmentExpression) ((ExpressionStatement) method.getBlock().getStatements().get(7))
					.getExpression();
			assertType(expr.getValue(), ArrayConstructorReferenceExpression.class);
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithReceiverParameters() {
		try {
			final String file = "simplepackage" + File.separator + "SimpleClassWithReceiverParameters.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertMemberCount(classifier, 7);
			for (final Member member : classifier.getMembers()) {
				if (member instanceof Constructor) {
					assertTrue(((Constructor) member).getParameters().isEmpty()
							|| 1 == ((Constructor) member).getParameters().size());
				} else if (member instanceof Method method) {
					assertType(method.getParameters().get(0), ReceiverParameter.class);
					for (int i = 1; i < method.getParameters().size(); i++) {
						assertFalse(method.getParameters().get(i) instanceof ReceiverParameter);
					}
				} else if (member instanceof tools.mdsd.jamopp.model.java.classifiers.Class innerClass) {
					assertMemberCount(innerClass, 3);
					for (final Member innerMember : innerClass.getMembers()) {
						if (innerMember instanceof Constructor) {
							assertType(((Constructor) innerMember).getParameters().get(0), ReceiverParameter.class);
						} else if (innerMember instanceof Method) {
							assertType(((Method) innerMember).getParameters().get(0), ReceiverParameter.class);
						} else {
							fail("There should be no other member.");
						}
					}
				} else {
					fail("There should be no other member.");
				}
			}
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithRestrictedKeywords() {
		try {
			final String file = "simplepackage" + File.separator + "SimpleClassWithRestrictedKeywords.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Disabled("JDT puts break statements into Blocks on the right side of a SwitchRule.")
	@Test
	public void testSimpleClassWithSwitch() {
		try {
			final String file = "pkgJava14" + File.separator + "SimpleClassWithSwitch.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit cu = (CompilationUnit) root;
			final Member m = cu.getClassifiers().get(0).getMembers().get(1);
			assertType(m, ClassMethod.class);
			final ClassMethod method = (ClassMethod) m;
			assertEquals(7, method.getBlock().getStatements().size());
			final LocalVariableStatement locStat = (LocalVariableStatement) method.getBlock().getStatements().get(5);
			final Expression e = locStat.getVariable().getInitialValue();
			assertType(e, Switch.class);
			Switch swith = (Switch) e;
			final SwitchCase ca = swith.getCases().get(0);
			assertType(ca, NormalSwitchCase.class);
			final NormalSwitchCase nca = (NormalSwitchCase) ca;
			assertNotNull(nca.getCondition());
			assertEquals(2, nca.getAdditionalConditions().size());
			assertType(nca.getStatements().get(0), YieldStatement.class);
			swith = (Switch) method.getBlock().getStatements().get(1);
			assertType(swith.getCases().get(0), NormalSwitchRule.class);
			assertType(swith.getCases().get(2), DefaultSwitchRule.class);
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithTryCatch() {
		try {
			final String file = "pkg2" + File.separator + "SimpleClassWithTryCatch.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertMemberCount(classifier, 3);
			final Method method = (Method) classifier.getMembers().get(1);
			assertIsPublic(method);
			assertEquals("tryCatch", method.getName());
			assertEquals(6, method.getBlock().getStatements().size());
			TryBlock tryBlock = (TryBlock) method.getBlock().getStatements().get(0);
			assertEquals(1, tryBlock.getBlock().getStatements().size());
			assertEquals(1, tryBlock.getResources().size());
			assertEquals(1, tryBlock.getCatchBlocks().size());
			assertTrue(tryBlock.getFinallyBlock() != null);
			tryBlock = (TryBlock) method.getBlock().getStatements().get(1);
			assertEquals(1, tryBlock.getBlock().getStatements().size());
			assertEquals(1, tryBlock.getResources().size());
			assertEquals(1, tryBlock.getCatchBlocks().size());
			assertTrue(tryBlock.getFinallyBlock() == null);
			tryBlock = (TryBlock) method.getBlock().getStatements().get(2);
			assertEquals(1, tryBlock.getBlock().getStatements().size());
			assertEquals(2, tryBlock.getResources().size());
			assertEquals(1, tryBlock.getCatchBlocks().size());
			assertTrue(tryBlock.getFinallyBlock() == null);
			tryBlock = (TryBlock) method.getBlock().getStatements().get(3);
			assertEquals(1, tryBlock.getBlock().getStatements().size());
			assertEquals(0, tryBlock.getResources().size());
			assertEquals(1, tryBlock.getCatchBlocks().size());
			assertType(tryBlock.getCatchBlocks().get(0).getParameter(), CatchParameter.class);
			final CatchParameter catchParam = (CatchParameter) tryBlock.getCatchBlocks().get(0).getParameter();
			assertEquals(3, catchParam.getTypeReferences().size());
			assertTrue(tryBlock.getFinallyBlock() == null);
			tryBlock = (TryBlock) method.getBlock().getStatements().get(4);
			assertEquals(0, tryBlock.getBlock().getStatements().size());
			assertEquals(1, tryBlock.getResources().size());
			assertEquals(0, tryBlock.getCatchBlocks().size());
			assertTrue(tryBlock.getFinallyBlock() == null);
			tryBlock = (TryBlock) method.getBlock().getStatements().get(5);
			assertEquals(0, tryBlock.getBlock().getStatements().size());
			assertEquals(1, tryBlock.getResources().size());
			assertEquals(0, tryBlock.getCatchBlocks().size());
			assertTrue(tryBlock.getFinallyBlock() != null);
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleClassWithVar() {
		try {
			final String file = "simplepackage" + File.separator + "SimpleClassWithVar.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final tools.mdsd.jamopp.model.java.classifiers.Class classifier = unit.getContainedClass();
			assertMemberCount(classifier, 6);
			for (final Member member : classifier.getMembers()) {
				if (member instanceof Method method) {
					assertEquals(8, method.getBlock().getStatements().size());
					final LocalVariableStatement locStat = (LocalVariableStatement) method.getBlock().getStatements()
							.get(0);
					assertType(locStat.getVariable().getTypeReference(), InferableType.class);
					final TypeReference reference = ((InferableType) locStat.getVariable().getTypeReference())
							.getActualTargets().get(0);
					assertType(reference, Int.class);
				}
			}
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSimpleInterfaceWithDefaultMethods() {
		try {
			final String file = "pkg2" + File.separator + "SimpleInterfaceWithDefaultMethods.java";
			final JavaRoot root = this.parseResource(file);
			assertType(root, CompilationUnit.class);
			final CompilationUnit unit = (CompilationUnit) root;
			assertNumberOfClassifiers(unit, 1);
			final Interface classifier = unit.getContainedInterface();
			assertMemberCount(classifier, 5);
			int numberOfDefaultOrStaticMethods = 0;
			for (final Member member : classifier.getMembers()) {
				if (member instanceof Method method) {
					boolean hasStaticOrDefaultModifier = false;
					for (final Modifier modifier : method.getModifiers()) {
						if (modifier instanceof Static || modifier instanceof Default) {
							hasStaticOrDefaultModifier = true;
							numberOfDefaultOrStaticMethods++;
						}
					}
					if (hasStaticOrDefaultModifier) {
						assertType(method.getStatement(), Block.class);
					} else {
						assertType(method.getStatement(), EmptyStatement.class);
					}
				}
			}
			assertEquals(3, numberOfDefaultOrStaticMethods);
			AbstractJaMoPPTests.assertResolveAllProxies(root);
			this.parseAndReprint(file);
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}
}
