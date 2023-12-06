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

package jamopp.parser.jdt.converter.interfaces.converter;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.emftext.language.java.classifiers.ConcreteClassifier;

public interface ToConcreteClassifierConverter extends ToConverter<AbstractTypeDeclaration, ConcreteClassifier> {

	public ConcreteClassifier convert(AbstractTypeDeclaration typeDecl);

}
