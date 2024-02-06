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

package tools.mdsd.jamopp.model.java.extensions.arrays;

import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;

public final class ArrayTypeableExtension {

	private ArrayTypeableExtension() {
		// Should not be initiated.
	}

	public static long getArrayDimension(final ArrayTypeable arrayTypeable) {
		long size = (long) arrayTypeable.getArrayDimensionsBefore().size()
				+ arrayTypeable.getArrayDimensionsAfter().size();
		if (arrayTypeable instanceof VariableLengthParameter) {
			size++;
		}
		return size;
	}
}
