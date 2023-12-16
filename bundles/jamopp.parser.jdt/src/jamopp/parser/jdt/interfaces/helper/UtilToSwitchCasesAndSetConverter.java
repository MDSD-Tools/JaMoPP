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

package jamopp.parser.jdt.interfaces.helper;

import java.util.List;

public interface UtilToSwitchCasesAndSetConverter {

	@SuppressWarnings("rawtypes")
	public void convert(org.emftext.language.java.statements.Switch switchExprSt, List switchStatementList);

}
