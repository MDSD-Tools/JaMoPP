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

package tools.mdsd.jamopp.parser.implementation.helper;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;

import tools.mdsd.jamopp.commons.layout.LayoutFactory;
import tools.mdsd.jamopp.commons.layout.MinimalLayoutInformation;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class UtilLayoutImpl implements UtilLayout {

	private final LayoutFactory layoutFactory;
	private MinimalLayoutInformation currentRootLayout;
	private boolean layoutSet;

	@Inject
	public UtilLayoutImpl(final LayoutFactory layoutFactory) {
		this.layoutFactory = layoutFactory;
		layoutSet = false;
	}

	@Override
	public void convertJavaRootLayoutInformation(final JavaRoot root, final ASTNode rootSource,
			final String sourceCode) {
		layoutSet = false;
		if (sourceCode != null) {
			currentRootLayout = layoutFactory.createMinimalLayoutInformation();
			currentRootLayout.setVisibleTokenText(sourceCode);
			currentRootLayout.setStartOffset(rootSource.getStartPosition());
			currentRootLayout.setLength(rootSource.getLength());
			currentRootLayout.setObject(root);
			currentRootLayout.setRootLayout(currentRootLayout);
			root.getLayoutInformations().add(currentRootLayout);
			layoutSet = true;
		}
	}

	@Override
	public void convertToMinimalLayoutInformation(final Commentable target, final ASTNode source) {
		if (layoutSet) {
			final MinimalLayoutInformation information = layoutFactory.createMinimalLayoutInformation();
			information.setStartOffset(source.getStartPosition());
			information.setLength(source.getLength());
			information.setObject(target);
			information.setRootLayout(currentRootLayout);
			target.getLayoutInformations().add(information);
		}
	}

}