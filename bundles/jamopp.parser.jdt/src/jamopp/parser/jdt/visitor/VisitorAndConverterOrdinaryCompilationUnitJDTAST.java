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

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToAnnotationInstanceConverter;
import jamopp.parser.jdt.converter.ToClassifierOrNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.converter.ToConcreteClassifierConverter;
import jamopp.parser.jdt.resolver.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class VisitorAndConverterOrdinaryCompilationUnitJDTAST extends VisitorAndConverterModuleJDTAST {

	@Inject
	VisitorAndConverterOrdinaryCompilationUnitJDTAST(UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility, ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter,
			ModifiersFactory modifiersFactory, ImportsFactory importsFactory, UtilNamedElement utilNamedElement,
			ToAnnotationInstanceConverter annotationInstanceConverter,
			ToConcreteClassifierConverter classifierConverterUtility, ContainersFactory containersFactory,
			ModulesFactory modulesFactory) {
		super(layoutInformationConverter, jdtResolverUtility, utilBaseConverter, modifiersFactory, importsFactory,
				utilNamedElement, annotationInstanceConverter, classifierConverterUtility, containersFactory,
				modulesFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.setConvertedElement(null);
		if (!node.types().isEmpty()) {
			this.setConvertedElement(this.convertToCompilationUnit(node));
		}
		super.visit(node);
		return false;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.containers.CompilationUnit convertToCompilationUnit(CompilationUnit cu) {
		org.emftext.language.java.containers.CompilationUnit result = containersFactory.createCompilationUnit();
		result.setName("");
		layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, getSource());
		cu.types().forEach(obj -> result.getClassifiers()
				.add(ClassifierConverterUtility.convertToConcreteClassifier((AbstractTypeDeclaration) obj)));
		return result;
	}
}
