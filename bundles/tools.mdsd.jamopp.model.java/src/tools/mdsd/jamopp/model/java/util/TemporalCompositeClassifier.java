/*******************************************************************************
 * Copyright (c) 2006-2012
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.impl.ClassifierImpl;

/**
 * A temporal create classifier that combines all type restrictions of one type
 * parameter.
 */
public class TemporalCompositeClassifier extends ClassifierImpl {

	private final EObject creator;

	private final EList<EObject> superTypes = new UniqueEList<>();

	public TemporalCompositeClassifier(final EObject creator) {
		this.creator = creator;
	}

	@Override
	public Resource eResource() {
		return creator.eResource();
	}

	public EList<EObject> getSuperTypes() {
		return superTypes;
	}

	@Override
	public EList<ConcreteClassifier> getAllSuperClassifiers() {
		final EList<ConcreteClassifier> result = new UniqueEList<>();
		for (final EObject superType : getSuperTypes()) {
			result.addAll(((Classifier) superType).getAllSuperClassifiers());
		}
		return result;
	}

}
