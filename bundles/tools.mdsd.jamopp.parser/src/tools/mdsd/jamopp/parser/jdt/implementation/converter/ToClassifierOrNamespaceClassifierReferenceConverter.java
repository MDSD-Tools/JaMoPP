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

package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToClassifierOrNamespaceClassifierReferenceConverter implements Converter<Name, TypeReference> {

	private final TypesFactory typesFactory;
	private final UtilNamedElement utilNamedElement;
	private final Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter;

	@Inject
	public ToClassifierOrNamespaceClassifierReferenceConverter(UtilNamedElement utilNamedElement,
			Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter, TypesFactory typesFactory) {
		this.typesFactory = typesFactory;
		this.utilNamedElement = utilNamedElement;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
	}

	@Override
	public TypeReference convert(Name name) {
		TypeReference result;
		if (name.isSimpleName()) {
			result = toClassifierReferenceConverter.convert((SimpleName) name);
		} else {
			QualifiedName qualifiedName = (QualifiedName) name;
			NamespaceClassifierReference ref = typesFactory.createNamespaceClassifierReference();
			if (name.resolveBinding() == null) {
				handleResolveBindingsNull(qualifiedName, ref);
			} else {
				handleResolveBindingsNotNull(qualifiedName, ref);
			}
			result = ref;
		}
		return result;
	}

	private void handleResolveBindingsNotNull(QualifiedName qualifiedName, NamespaceClassifierReference ref) {
		Optional<Name> qualifier = Optional.of(qualifiedName.getQualifier());
		Optional<SimpleName> simpleName = Optional.of(qualifiedName.getName());
		while (simpleName.isPresent() && simpleName.get().resolveBinding() instanceof ITypeBinding) {
			ref.getClassifierReferences().add(0, toClassifierReferenceConverter.convert(simpleName.get()));
			if (qualifier.isEmpty()) {
				simpleName = Optional.empty();
			} else if (qualifier.get().isSimpleName()) {
				simpleName = Optional.of((SimpleName) qualifier.get());
				qualifier = Optional.empty();
			} else {
				simpleName = Optional.of(((QualifiedName) qualifier.get()).getName());
				qualifier = Optional.of(((QualifiedName) qualifier.get()).getQualifier());
			}
		}
		if (simpleName.isPresent() && !(simpleName.get().resolveBinding() instanceof ITypeBinding)) {
			utilNamedElement.addNameToNameSpace(simpleName.get(), ref);
		}
		if (qualifier.isPresent()) {
			utilNamedElement.addNameToNameSpace(qualifier.get(), ref);
		}
	}

	private void handleResolveBindingsNull(QualifiedName qualifiedName, NamespaceClassifierReference ref) {
		ref.getClassifierReferences().add(toClassifierReferenceConverter.convert(qualifiedName.getName()));
		utilNamedElement.addNameToNameSpace(qualifiedName.getQualifier(), ref);
	}

}
