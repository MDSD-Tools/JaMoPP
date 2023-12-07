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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.ModuleDirective;
import org.eclipse.jdt.core.dom.ModuleModifier;
import org.eclipse.jdt.core.dom.ModulePackageAccess;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ProvidesDirective;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.RequiresDirective;
import org.eclipse.jdt.core.dom.UsesDirective;
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

public class VisitorAndConverterAbstractAndEmptyModelJDTAST extends MyAbstractVisitor {

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
		super();
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

	@Override
	public void setSource(String src) {
		originalSource = src;
	}

	private String getSource() {
		return originalSource;
	}

	private void setConvertedElement(JavaRoot root) {
		convertedRootElement = root;
	}

	@Override
	public JavaRoot getConvertedElement() {
		return convertedRootElement;
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.setConvertedElement(null);
		if (!node.types().isEmpty()) {
			this.setConvertedElement(this.convertToCompilationUnit(node));
		}
		visit2(node);
		return false;
	}

	private boolean visit4(CompilationUnit node) {
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

	@SuppressWarnings("unchecked")
	private boolean visit3(CompilationUnit node) {
		org.emftext.language.java.containers.JavaRoot root = this.getConvertedElement();
		if (root == null && node.getPackage() != null) {
			root = jdtResolverUtility.getPackage(node.getPackage().resolveBinding());
			root.setName("");
			layoutInformationConverter.convertJavaRootLayoutInformation(root, node, this.getSource());
			this.setConvertedElement(root);
		}
		org.emftext.language.java.containers.JavaRoot finalRoot = root;
		if (node.getPackage() != null) {
			node.getPackage().annotations().forEach(
					obj -> finalRoot.getAnnotations().add(annotationInstanceConverter.convert((Annotation) obj)));
			root.getNamespaces().clear();
			utilNamedElement.addNameToNameSpace(node.getPackage().getName(), root);
		}
		visit4(node);
		return false;
	}

	private boolean visit2(CompilationUnit node) {
		if (node.getModule() != null) {
			org.emftext.language.java.containers.Module module = this.convertToModule(node.getModule());
			this.setConvertedElement(module);
		}
		visit3(node);
		return false;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.containers.Module convertToModule(ModuleDeclaration node) {
		org.emftext.language.java.containers.Module module = jdtResolverUtility.getModule(node.resolveBinding());
		if (node.isOpen()) {
			module.setOpen(modifiersFactory.createOpen());
		}
		layoutInformationConverter.convertJavaRootLayoutInformation(module, node, this.getSource());
		utilNamedElement.addNameToNameSpace(node.getName(), module);
		module.setName("");
		node.annotations()
				.forEach(obj -> module.getAnnotations().add(annotationInstanceConverter.convert((Annotation) obj)));
		node.moduleStatements().forEach(obj -> module.getTarget().add(this.convertToDirective((ModuleDirective) obj)));
		return module;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.modules.ModuleDirective convertToDirective(ModuleDirective directive) {
		if (directive.getNodeType() == ASTNode.REQUIRES_DIRECTIVE) {
			RequiresDirective reqDir = (RequiresDirective) directive;
			org.emftext.language.java.modules.RequiresModuleDirective result = modulesFactory
					.createRequiresModuleDirective();
			reqDir.modifiers().forEach(obj -> {
				ModuleModifier modifier = (ModuleModifier) obj;
				if (modifier.isStatic()) {
					result.setModifier(modifiersFactory.createStatic());
				} else if (modifier.isTransitive()) {
					result.setModifier(modifiersFactory.createTransitive());
				}
			});
			result.setRequiredModule(convertToModuleReference(reqDir.getName()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
			return result;
		}
		if (directive.getNodeType() == ASTNode.EXPORTS_DIRECTIVE
				|| directive.getNodeType() == ASTNode.OPENS_DIRECTIVE) {
			ModulePackageAccess accessDir = (ModulePackageAccess) directive;
			org.emftext.language.java.modules.AccessProvidingModuleDirective convertedDir;
			if (directive.getNodeType() == ASTNode.OPENS_DIRECTIVE) {
				convertedDir = modulesFactory.createOpensModuleDirective();
			} else { // directive.getNodeType() == ASTNode.EXPORTS_DIRECTIVE
				convertedDir = modulesFactory.createExportsModuleDirective();
			}
			IPackageBinding binding = (IPackageBinding) accessDir.getName().resolveBinding();
			convertedDir.setAccessablePackage(jdtResolverUtility.getPackage(binding));
			accessDir.modules().forEach(obj -> convertedDir.getModules().add(convertToModuleReference((Name) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(convertedDir, directive);
			return convertedDir;
		}
		if (directive.getNodeType() == ASTNode.PROVIDES_DIRECTIVE) {
			ProvidesDirective provDir = (ProvidesDirective) directive;
			org.emftext.language.java.modules.ProvidesModuleDirective result = modulesFactory
					.createProvidesModuleDirective();
			result.setTypeReference(utilBaseConverter.convert(provDir.getName()));
			provDir.implementations()
					.forEach(obj -> result.getServiceProviders().add(utilBaseConverter.convert((Name) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
			return result;
		}
		UsesDirective usDir = (UsesDirective) directive;
		org.emftext.language.java.modules.UsesModuleDirective result = modulesFactory.createUsesModuleDirective();
		result.setTypeReference(utilBaseConverter.convert(usDir.getName()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
		return result;
	}

	private org.emftext.language.java.modules.ModuleReference convertToModuleReference(Name name) {
		org.emftext.language.java.modules.ModuleReference ref = modulesFactory.createModuleReference();
		org.emftext.language.java.containers.Module modProxy = jdtResolverUtility
				.getModule((IModuleBinding) name.resolveBinding());
		modProxy.setName("");
		ref.setTarget(modProxy);
		utilNamedElement.addNameToNameSpace(name, modProxy);
		return ref;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.containers.CompilationUnit convertToCompilationUnit(CompilationUnit cu) {
		org.emftext.language.java.containers.CompilationUnit result = containersFactory.createCompilationUnit();
		result.setName("");
		layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, getSource());
		cu.types().forEach(
				obj -> result.getClassifiers().add(ClassifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}

}
