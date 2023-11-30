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

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

class ToClassifierOrNamespaceClassifierReferenceConverter {

	private final UtilNamedElement utilNamedElement;
	private final ToClassifierReferenceConverter toClassifierReferenceConverter;

	@Inject
	ToClassifierOrNamespaceClassifierReferenceConverter(UtilNamedElement utilNamedElement,
			ToClassifierReferenceConverter toClassifierReferenceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
	}

	TypeReference convertToClassifierOrNamespaceClassifierReference(Name name) {
		if (name.isSimpleName()) {
			return toClassifierReferenceConverter.convertToClassifierReference((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		NamespaceClassifierReference ref = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
		if (name.resolveBinding() == null) {
			ref.getClassifierReferences()
					.add(toClassifierReferenceConverter.convertToClassifierReference(qualifiedName.getName()));
			utilNamedElement.addNameToNameSpace(qualifiedName.getQualifier(), ref);
			return ref;
		}
		Name qualifier = qualifiedName.getQualifier();
		SimpleName simpleName = qualifiedName.getName();
		while (simpleName != null && simpleName.resolveBinding() instanceof ITypeBinding) {
			ref.getClassifierReferences().add(0,
					toClassifierReferenceConverter.convertToClassifierReference(simpleName));
			if (qualifier == null) {
				simpleName = null;
			} else if (qualifier.isSimpleName()) {
				simpleName = (SimpleName) qualifier;
				qualifier = null;
			} else {
				simpleName = ((QualifiedName) qualifier).getName();
				qualifier = ((QualifiedName) qualifier).getQualifier();
			}
		}
		if (simpleName != null && !(simpleName.resolveBinding() instanceof ITypeBinding)) {
			utilNamedElement.addNameToNameSpace(simpleName, ref);
		}
		if (qualifier != null) {
			utilNamedElement.addNameToNameSpace(qualifier, ref);
		}
		return ref;
	}

}
