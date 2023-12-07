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

package jamopp.parser.jdt.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.emftext.language.java.annotations.AnnotationInstance;
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
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.references.ReferenceableElement;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public abstract class VisitorAndConverterAbstractAndEmptyModelJDTAST extends ASTVisitor {

	protected final ContainersFactory containersFactory;
	protected final ModulesFactory modulesFactory;
	protected final ModifiersFactory modifiersFactory;
	protected final ImportsFactory importsFactory;
	protected final IUtilLayout layoutInformationConverter;
	protected final IUtilNamedElement utilNamedElement;
	protected final IUtilJdtResolver jdtResolverUtility;
	protected final Converter<Name, TypeReference> utilBaseConverter;
	protected final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	protected final Converter<AbstractTypeDeclaration, ConcreteClassifier> ClassifierConverterUtility;

	@Inject
	protected VisitorAndConverterAbstractAndEmptyModelJDTAST(IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility, Converter<Name, TypeReference> utilBaseConverter,
			ModifiersFactory modifiersFactory, ImportsFactory importsFactory, IUtilNamedElement utilNamedElement,
			Converter<Annotation, AnnotationInstance> annotationInstanceConverter,
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility,
			ContainersFactory containersFactory, ModulesFactory modulesFactory) {
		this.containersFactory = containersFactory;
		this.modulesFactory = modulesFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.modifiersFactory = modifiersFactory;
		this.importsFactory = importsFactory;
		this.utilNamedElement = utilNamedElement;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.ClassifierConverterUtility = classifierConverterUtility;
	}

	private JavaRoot convertedRootElement;
	private String originalSource;

	public void setSource(String src) {
		originalSource = src;
	}

	protected String getSource() {
		return originalSource;
	}

	protected void setConvertedElement(JavaRoot root) {
		convertedRootElement = root;
	}

	public JavaRoot getConvertedElement() {
		return convertedRootElement;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		if (convertedRootElement == null) {
			convertedRootElement = containersFactory.createEmptyModel();
			convertedRootElement.setName("");
		}
		for (Object obj : node.imports()) {
			convertedRootElement.getImports().add(convertToImport((ImportDeclaration) obj));
		}

		return false;
	}

	private Import convertToImport(ImportDeclaration declaration) {
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

	private Import convertToOnDemandStatic(ImportDeclaration importDecl) {
		StaticClassifierImport convertedImport = importsFactory.createStaticClassifierImport();
		convertedImport.setStatic(modifiersFactory.createStatic());
		IBinding binding = importDecl.getName().resolveBinding();
		Classifier proxyClass = null;
		if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
			proxyClass = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getClassifier((ITypeBinding) binding);
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		utilNamedElement.addNameToNameSpaceAndElement(importDecl.getName(), convertedImport, proxyClass);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private Import convertToOnDemandNonStatic(ImportDeclaration importDecl) {
		PackageImport convertedImport = importsFactory.createPackageImport();
		utilNamedElement.addNameToNameSpace(importDecl.getName(), convertedImport);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private Import convertToNonOnDemandStatic(ImportDeclaration importDecl) {
		StaticMemberImport convertedImport = importsFactory.createStaticMemberImport();
		convertedImport.setStatic(modifiersFactory.createStatic());
		QualifiedName qualifiedName = (QualifiedName) importDecl.getName();
		IBinding b = qualifiedName.resolveBinding();
		ReferenceableElement proxyMember = null;
		Classifier proxyClass = null;
		if (b == null || b.isRecovered()) {
			proxyMember = jdtResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		} else if (b instanceof IMethodBinding) {
			proxyMember = jdtResolverUtility.getMethod((IMethodBinding) b);
		} else if (b instanceof IVariableBinding) {
			proxyMember = jdtResolverUtility.getReferencableElement((IVariableBinding) b);
		} else if (b instanceof ITypeBinding typeBinding) {
			if (!typeBinding.isNested()) {
				proxyClass = jdtResolverUtility.getClassifier(typeBinding);
				ConcreteClassifier conCl = (ConcreteClassifier) proxyClass;
				for (Member m : conCl.getMembers()) {
					if (!(m instanceof Constructor) && m.getName().equals(qualifiedName.getName().getIdentifier())) {
						proxyMember = (ReferenceableElement) m;
						break;
					}
				}
				if (proxyMember == null) {
					proxyMember = jdtResolverUtility.getClassMethod(qualifiedName.getFullyQualifiedName());
					proxyMember.setName(qualifiedName.getName().getIdentifier());
					conCl.getMembers().add((Member) proxyMember);
				}
			} else {
				proxyMember = jdtResolverUtility.getClassifier(typeBinding);
				proxyClass = jdtResolverUtility.getClassifier(typeBinding.getDeclaringClass());
			}
		} else {
			proxyMember = jdtResolverUtility.getField(qualifiedName.getFullyQualifiedName());
		}
		proxyMember.setName(qualifiedName.getName().getIdentifier());
		convertedImport.getStaticMembers().add(proxyMember);
		if (proxyClass == null) {
			IBinding binding = qualifiedName.getQualifier().resolveBinding();
			if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
				proxyClass = jdtResolverUtility.getClass(qualifiedName.getQualifier().getFullyQualifiedName());
			} else {
				proxyClass = jdtResolverUtility.getClassifier((ITypeBinding) binding);
			}
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		utilNamedElement.addNameToNameSpaceAndElement(qualifiedName.getQualifier(), convertedImport, proxyClass);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

	private Import convertToNonOnDemandNonStatic(ImportDeclaration importDecl) {
		ClassifierImport convertedImport = importsFactory.createClassifierImport();
		Classifier proxy = null;
		IBinding b = importDecl.getName().resolveBinding();
		if (b instanceof IPackageBinding) {
			proxy = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			ITypeBinding binding = (ITypeBinding) b;
			if (binding == null || binding.isRecovered()) {
				proxy = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
			} else {
				proxy = jdtResolverUtility.getClassifier((ITypeBinding) importDecl.getName().resolveBinding());
			}
		}
		convertedImport.setClassifier((ConcreteClassifier) proxy);
		utilNamedElement.addNameToNameSpaceAndElement(importDecl.getName(), convertedImport, proxy);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}
}
