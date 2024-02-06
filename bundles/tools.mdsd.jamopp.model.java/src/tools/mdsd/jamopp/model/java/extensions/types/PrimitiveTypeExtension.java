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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.types.Char;
import tools.mdsd.jamopp.model.java.types.Int;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;

public final class PrimitiveTypeExtension {

	private final static Map<Class<?>, String> MAPPINGS;

	static {
		MAPPINGS = new HashMap<>();
		MAPPINGS.put(Boolean.class, "Boolean");
		MAPPINGS.put(Byte.class, "Byte");
		MAPPINGS.put(Char.class, "Character");
		MAPPINGS.put(Double.class, "Double");
		MAPPINGS.put(Float.class, "Float");
		MAPPINGS.put(Int.class, "Integer");
		MAPPINGS.put(Long.class, "Long");
		MAPPINGS.put(Short.class, "Short");
		MAPPINGS.put(Void.class, "Void");
	}

	private PrimitiveTypeExtension() {
		// Should not be initiated
	}

	/**
	 * @param context to check protected visibility
	 * @return all members (including super type members)
	 */
	public static EList<Member> getAllMembers(final PrimitiveType primitiveType, final Commentable context) {
		final tools.mdsd.jamopp.model.java.classifiers.Class javaClass = primitiveType.wrapPrimitiveType();
		return javaClass.getAllMembers(context);
	}

	/**
	 * @return primitive type as a class representation
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class wrapPrimitiveType(final PrimitiveType type) {
		tools.mdsd.jamopp.model.java.classifiers.Class javaClass = null;
		for (final Entry<Class<?>, String> entry : MAPPINGS.entrySet()) {
			if (entry.getKey().isInstance(type)) {
				javaClass = type.getLibClass(entry.getValue());
				break;
			}
		}

		return javaClass;
	}
}
