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

package jamopp.parser.jdt.converter.implementation.helper;

import org.emftext.language.java.references.Reference;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.helper.IUtilReferenceWalker;

public class UtilReferenceWalker implements IUtilReferenceWalker {

	@Inject
	UtilReferenceWalker() {

	}

	@Override
	public Reference walkUp(Reference ref) {
		Reference result = ref;
		Reference parent = result.getPrevious();
		while (parent != null) {
			result = parent;
			parent = result.getPrevious();
		}
		return result;
	}

}
