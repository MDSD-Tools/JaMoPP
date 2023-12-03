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

package jamopp.parser.jdt.other;

import org.eclipse.jdt.core.dom.Expression;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import jamopp.parser.jdt.converter.ToExpressionConverter;

@Singleton
public class UtilExpressionConverter {

	private ToExpressionConverter toExpressionConverter;

	@Inject
	UtilExpressionConverter() {
	}

	public org.emftext.language.java.expressions.Expression convertToExpression(Expression expr) {
		return toExpressionConverter.convert(expr);
	}

	public void setToExpressionConverter(ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
	}
}
