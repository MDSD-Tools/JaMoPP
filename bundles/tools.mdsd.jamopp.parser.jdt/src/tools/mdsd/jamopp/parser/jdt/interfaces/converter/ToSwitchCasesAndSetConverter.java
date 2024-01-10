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

package tools.mdsd.jamopp.parser.jdt.interfaces.converter;

import java.util.List;

public interface ToSwitchCasesAndSetConverter {

	@SuppressWarnings("rawtypes")
	void convert(tools.mdsd.jamopp.model.java.statements.Switch switchExprSt, List switchStatementList);

}
