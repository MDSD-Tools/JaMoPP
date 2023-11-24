/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;
import org.emftext.language.java.imports.ClassifierImport;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.imports.PackageImport;
import org.emftext.language.java.imports.StaticClassifierImport;
import org.emftext.language.java.imports.StaticMemberImport;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.references.ReferenceableElement;

class AbstractAndEmptyModelJDTASTVisitorAndConverter extends ASTVisitor {

	private static final LayoutInformationConverter LayoutInformationConverter = new LayoutInformationConverter();
	private static final JDTResolverUtility JDTResolverUtility = new JDTResolverUtility();
	private static final BaseConverterUtility BaseConverterUtility = new BaseConverterUtility();

	private static final ModifiersFactory MODIFIERS_FACTORY = ModifiersFactory.eINSTANCE;
	private static final ImportsFactory IMPORTS_FACTORY = ImportsFactory.eINSTANCE;
	private JavaRoot convertedRootElement;
	private String originalSource;

	void setSource(String src) {
		originalSource = src;
	}

	String getSource() {
		return originalSource;
	}

	void setConvertedElement(JavaRoot root) {
		convertedRootElement = root;
	}

	JavaRoot getConvertedElement() {
		return convertedRootElement;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		if (convertedRootElement == null) {
			convertedRootElement = ContainersFactory.eINSTANCE.createEmptyModel();
			convertedRootElement.setName("");
		}
		for (Object obj : node.imports()) {
			convertedRootElement.getImports().add(convertToImport((ImportDeclaration) obj));
		}

		return false;
	}

	private static Import convertToImport(ImportDeclaration declaration) {
		if (declaration.isOnDemand()) {
			if (declaration.isStatic()) {
				return convertToOnDemandStatic(declaration);
			} else {
				return convertToOnDemandNonStatic(declaration);
			}
		} else {
			if (declaration.isStatic()) {
				return convertToNonOnDemandStatic(declaration);
			} else {
				return convertToNonOnDemandNonStatic(declaration);
			}
		}

	}

	private static Import convertToOnDemandStatic(ImportDeclaration importDecl) {
		StaticClassifierImport convertedImport = IMPORTS_FACTORY.createStaticClassifierImport();
		convertedImport.setStatic(MODIFIERS_FACTORY.createStatic());
		IBinding binding = importDecl.getName().resolveBinding();
		Classifier proxyClass = null;
		if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
			proxyClass = JDTResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			proxyClass = JDTResolverUtility.getClassifier((ITypeBinding) binding);
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		BaseConverterUtility.convertToNamespacesAndSimpleNameAndSet(importDecl.getName(), convertedImport, proxyClass);
		LayoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private static Import convertToOnDemandNonStatic(ImportDeclaration importDecl) {
		PackageImport convertedImport = IMPORTS_FACTORY.createPackageImport();
		BaseConverterUtility.convertToNamespacesAndSet(importDecl.getName(), convertedImport);
		LayoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private static Import convertToNonOnDemandStatic(ImportDeclaration importDecl) {
		StaticMemberImport convertedImport = IMPORTS_FACTORY.createStaticMemberImport();
		convertedImport.setStatic(MODIFIERS_FACTORY.createStatic());
		QualifiedName qualifiedName = (QualifiedName) importDecl.getName();
		IBinding b = qualifiedName.resolveBinding();
		ReferenceableElement proxyMember = null;
		Classifier proxyClass = null;
		if (b == null || b.isRecovered()) {
			proxyMember = JDTResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		} else if (b instanceof IMethodBinding) {
			proxyMember = JDTResolverUtility.getMethod((IMethodBinding) b);
		} else if (b instanceof IVariableBinding) {
			proxyMember = JDTResolverUtility.getReferencableElement((IVariableBinding) b);
		} else if (b instanceof ITypeBinding typeBinding) {
			if (!typeBinding.isNested()) {
				proxyClass = JDTResolverUtility.getClassifier(typeBinding);
				ConcreteClassifier conCl = (ConcreteClassifier) proxyClass;
				for (Member m : conCl.getMembers()) {
					if (!(m instanceof Constructor) && m.getName().equals(qualifiedName.getName().getIdentifier())) {
						proxyMember = (ReferenceableElement) m;
						break;
					}
				}
				if (proxyMember == null) {
					proxyMember = JDTResolverUtility.getClassMethod(qualifiedName.getFullyQualifiedName());
					proxyMember.setName(qualifiedName.getName().getIdentifier());
					conCl.getMembers().add((Member) proxyMember);
				}
			} else {
				proxyMember = JDTResolverUtility.getClassifier(typeBinding);
				proxyClass = JDTResolverUtility.getClassifier(typeBinding.getDeclaringClass());
			}
		} else {
			proxyMember = JDTResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		}
		proxyMember.setName(qualifiedName.getName().getIdentifier());
		convertedImport.getStaticMembers().add(proxyMember);
		if (proxyClass == null) {
			IBinding binding = qualifiedName.getQualifier().resolveBinding();
			if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
				proxyClass = JDTResolverUtility.getClass(qualifiedName.getQualifier().getFullyQualifiedName());
			} else {
				proxyClass = JDTResolverUtility.getClassifier((ITypeBinding) binding);
			}
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		BaseConverterUtility.convertToNamespacesAndSimpleNameAndSet(qualifiedName.getQualifier(), convertedImport,
				proxyClass);
		LayoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private static Import convertToNonOnDemandNonStatic(ImportDeclaration importDecl) {
		ClassifierImport convertedImport = IMPORTS_FACTORY.createClassifierImport();
		Classifier proxy = null;
		IBinding b = importDecl.getName().resolveBinding();
		if (b instanceof IPackageBinding) {
			proxy = JDTResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			ITypeBinding binding = (ITypeBinding) b;
			if (binding == null || binding.isRecovered()) {
				proxy = JDTResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
			} else {
				proxy = JDTResolverUtility.getClassifier((ITypeBinding) importDecl.getName().resolveBinding());
			}
		}
		convertedImport.setClassifier((ConcreteClassifier) proxy);
		BaseConverterUtility.convertToNamespacesAndSimpleNameAndSet(importDecl.getName(), convertedImport, proxy);
		LayoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}
}
