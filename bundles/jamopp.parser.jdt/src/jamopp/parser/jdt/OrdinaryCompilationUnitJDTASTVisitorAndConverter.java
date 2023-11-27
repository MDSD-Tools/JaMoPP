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

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;

class OrdinaryCompilationUnitJDTASTVisitorAndConverter extends ModuleJDTASTVisitorAndConverter {



	OrdinaryCompilationUnitJDTASTVisitorAndConverter(UtilLayout layoutInformationConverter,
			UtilJDTResolver jdtResolverUtility, UtilBaseConverter utilBaseConverter, ModifiersFactory modifiersFactory,
			ImportsFactory importsFactory, UtilNamedElement utilNamedElement,
			ToAnnotationInstanceConverter annotationInstanceConverter,
			UtilClassifierConverter classifierConverterUtility) {
		super(layoutInformationConverter, jdtResolverUtility, utilBaseConverter, modifiersFactory, importsFactory,
				utilNamedElement, annotationInstanceConverter, classifierConverterUtility);
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
		org.emftext.language.java.containers.CompilationUnit result = org.emftext.language.java.containers.ContainersFactory.eINSTANCE
				.createCompilationUnit();
		result.setName("");
		layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, getSource());
		cu.types().forEach(obj -> result.getClassifiers()
				.add(ClassifierConverterUtility.convertToConcreteClassifier((AbstractTypeDeclaration) obj)));
		return result;
	}
}
