/*******************************************************************************
 * Copyright (c) 2006-2014
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
package tools.mdsd.jamopp.model.java.extensions.references;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.references.Argumentable;
import tools.mdsd.jamopp.model.java.types.Type;

public final class ArgumentableExtension {

	private ArgumentableExtension() {
		// Should not be initiated.
	}

	/**
	 * Returns a list containing the types of the given {@link Argumentable}.
	 *
	 * @return list of types of 'me'
	 */
	public static EList<Type> getArgumentTypes(final Argumentable argumentable) {
		final EList<Type> resultList = new BasicEList<>();

		for (final Expression argument : argumentable.getArguments()) {
			final Type type = argument.getType();
			resultList.add(type);
		}
		return resultList;
	}
}
