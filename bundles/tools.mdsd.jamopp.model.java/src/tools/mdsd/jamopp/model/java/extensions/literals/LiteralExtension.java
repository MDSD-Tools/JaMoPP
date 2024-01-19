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
package tools.mdsd.jamopp.model.java.extensions.literals;

import tools.mdsd.jamopp.model.java.literals.BooleanLiteral;
import tools.mdsd.jamopp.model.java.literals.CharacterLiteral;
import tools.mdsd.jamopp.model.java.literals.DoubleLiteral;
import tools.mdsd.jamopp.model.java.literals.FloatLiteral;
import tools.mdsd.jamopp.model.java.literals.IntegerLiteral;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.literals.LongLiteral;
import tools.mdsd.jamopp.model.java.literals.NullLiteral;

public final class LiteralExtension {

	private LiteralExtension() {
		// Should not be initiated
	}

	/**
	 * @return type of the literal
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class getOneType(Literal literal) {
		// Overrides implementation in Expression
		tools.mdsd.jamopp.model.java.classifiers.Class javaClass = null;

		if (literal instanceof NullLiteral) {
			javaClass = literal.getLibClass("Void");
		} else if (literal instanceof BooleanLiteral) {
			javaClass = literal.getLibClass("Boolean");
		} else if (literal instanceof DoubleLiteral) {
			javaClass = literal.getLibClass("Double");
		} else if (literal instanceof FloatLiteral) {
			javaClass = literal.getLibClass("Float");
		} else if (literal instanceof IntegerLiteral) {
			javaClass = literal.getLibClass("Integer");
		} else if (literal instanceof LongLiteral) {
			javaClass = literal.getLibClass("Long");
		} else if (literal instanceof CharacterLiteral) {
			javaClass = literal.getLibClass("Character");
		}

		return javaClass;
	}
}
