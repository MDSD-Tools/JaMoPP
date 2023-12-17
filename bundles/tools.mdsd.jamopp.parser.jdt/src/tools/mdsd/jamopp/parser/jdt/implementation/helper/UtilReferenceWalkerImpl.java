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

package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import tools.mdsd.jamopp.model.java.references.Reference;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilReferenceWalker;

public class UtilReferenceWalkerImpl implements UtilReferenceWalker {

	@Inject
	UtilReferenceWalkerImpl() {

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
