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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.ModuleDirective;
import org.eclipse.jdt.core.dom.ModuleModifier;
import org.eclipse.jdt.core.dom.ModulePackageAccess;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ProvidesDirective;
import org.eclipse.jdt.core.dom.RequiresDirective;
import org.eclipse.jdt.core.dom.UsesDirective;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;

import com.google.inject.Inject;

class VisitorAndConverterModuleJDTAST extends PackageJDTASTVisitorAndConverter {

	@Inject
	VisitorAndConverterModuleJDTAST(UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter, ModifiersFactory modifiersFactory,
			ImportsFactory importsFactory, UtilNamedElement utilNamedElement,
			ToAnnotationInstanceConverter annotationInstanceConverter,
			ToConcreteClassifierConverter classifierConverterUtility, ContainersFactory containersFactory,
			ModulesFactory modulesFactory) {
		super(layoutInformationConverter, jdtResolverUtility, utilBaseConverter, modifiersFactory, importsFactory,
				utilNamedElement, annotationInstanceConverter, classifierConverterUtility, containersFactory,
				modulesFactory);
	}

	@Override
	public boolean visit(CompilationUnit node) {
		if (node.getModule() != null) {
			org.emftext.language.java.containers.Module module = this.convertToModule(node.getModule());
			this.setConvertedElement(module);
		}
		super.visit(node);
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
		node.annotations().forEach(obj -> module.getAnnotations()
				.add(annotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
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
			result.setTypeReference(
					utilBaseConverter.convertToClassifierOrNamespaceClassifierReference(provDir.getName()));
			provDir.implementations().forEach(obj -> result.getServiceProviders()
					.add(utilBaseConverter.convertToClassifierOrNamespaceClassifierReference((Name) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
			return result;
		}
		UsesDirective usDir = (UsesDirective) directive;
		org.emftext.language.java.modules.UsesModuleDirective result = modulesFactory.createUsesModuleDirective();
		result.setTypeReference(utilBaseConverter.convertToClassifierOrNamespaceClassifierReference(usDir.getName()));
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
}
