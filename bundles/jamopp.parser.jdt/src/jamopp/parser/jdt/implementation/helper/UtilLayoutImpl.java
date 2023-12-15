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

import org.eclipse.jdt.core.dom.ASTNode;
import org.emftext.commons.layout.LayoutFactory;
import org.emftext.commons.layout.MinimalLayoutInformation;
import org.emftext.language.java.commons.Commentable;
import org.emftext.language.java.containers.JavaRoot;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class UtilLayoutImpl implements UtilLayout {

	private static MinimalLayoutInformation currentRootLayout;
	private final LayoutFactory layoutFactory;

	@Inject
	public UtilLayoutImpl(LayoutFactory layoutFactory) {
		this.layoutFactory = layoutFactory;
	}

	@Override
	public void convertJavaRootLayoutInformation(JavaRoot root, ASTNode rootSource, String sourceCode) {
		currentRootLayout = null;
		if (sourceCode != null) {
			currentRootLayout = this.layoutFactory.createMinimalLayoutInformation();
			currentRootLayout.setVisibleTokenText(sourceCode);
			currentRootLayout.setStartOffset(rootSource.getStartPosition());
			currentRootLayout.setLength(rootSource.getLength());
			currentRootLayout.setObject(root);
			currentRootLayout.setRootLayout(currentRootLayout);
			root.getLayoutInformations().add(currentRootLayout);
		}
	}

	@Override
	public void convertToMinimalLayoutInformation(Commentable target, ASTNode source) {
		if (currentRootLayout != null) {
			var li = this.layoutFactory.createMinimalLayoutInformation();
			li.setStartOffset(source.getStartPosition());
			li.setLength(source.getLength());
			li.setObject(target);
			li.setRootLayout(currentRootLayout);
			target.getLayoutInformations().add(li);
		}
	}

}