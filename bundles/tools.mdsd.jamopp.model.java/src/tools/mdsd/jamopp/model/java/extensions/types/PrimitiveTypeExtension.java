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
package tools.mdsd.jamopp.model.java.extensions.types;

import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;

public final class PrimitiveTypeExtension {

	private PrimitiveTypeExtension() {
		// Should not initiated.
	}

	/**
	 * @param context to check protected visibility
	 * @return all members (including super type members)
	 */
	public static EList<Member> getAllMembers(PrimitiveType primitiveType, Commentable context) {
		tools.mdsd.jamopp.model.java.classifiers.Class javaClass = primitiveType.wrapPrimitiveType();
		return javaClass.getAllMembers(context);
	}

	/**
	 * @return primitive type as a class representation
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class wrapPrimitiveType(PrimitiveType type) {
		tools.mdsd.jamopp.model.java.classifiers.Class javaClass = null;
		if (type instanceof tools.mdsd.jamopp.model.java.types.Boolean) {
			javaClass = type.getLibClass("Boolean");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Byte) {
			javaClass = type.getLibClass("Byte");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Char) {
			javaClass = type.getLibClass("Character");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Double) {
			javaClass = type.getLibClass("Double");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Float) {
			javaClass = type.getLibClass("Float");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Int) {
			javaClass = type.getLibClass("Integer");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Long) {
			javaClass = type.getLibClass("Long");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Short) {
			javaClass = type.getLibClass("Short");
		} else if (type instanceof tools.mdsd.jamopp.model.java.types.Void) {
			javaClass = type.getLibClass("Void");
		}
		return javaClass;
	}
}
