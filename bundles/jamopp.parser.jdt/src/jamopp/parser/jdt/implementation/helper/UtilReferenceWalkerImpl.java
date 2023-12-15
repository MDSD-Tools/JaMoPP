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

package jamopp.parser.jdt.implementation.helper;

import org.emftext.language.java.references.Reference;

import jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;

public class UtilReferenceWalkerImpl implements UtilReferenceWalker {

	@Override
	public Reference walkUp(Reference ref) {
		var result = ref;
		var parent = result.getPrevious();
		while (parent != null) {
			result = parent;
			parent = result.getPrevious();
		}
		return result;
	}

}
