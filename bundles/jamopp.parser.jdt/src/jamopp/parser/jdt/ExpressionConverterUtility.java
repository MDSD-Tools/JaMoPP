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

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.OperatorsFactory;

class ExpressionConverterUtility {

	private ToExpressionConverter toExpressionConverter;

	ExpressionConverterUtility() {
	}

	org.emftext.language.java.expressions.Expression convertToExpression(Expression expr) {
		return toExpressionConverter.convertToExpression(expr);
	}

	void setToExpressionConverter(ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
	}
}
