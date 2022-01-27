/*******************************************************************************
 * Copyright 2021 Marvin Meller
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Marvin Meller
 *      - initial implementation
 *  Yves Kirschner
 *      - parameterize implementation
 ******************************************************************************/
package org.emftext.language.java.test.bulk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.test.AbstractJaMoPPTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Parameterized test for resolving use of JDT-based conversion of Java source
 * code to EMF-based models. Apart from JUint, no dependencies to other tools
 * were newly introduced. For this purpose, the existing code was taken almost
 * identically and copied into this test.
 *
 * @author Marvin Meller
 * @author Yves Kirschner
 *
 * @version 1.2
 */
public class ReprintTest extends AbstractJaMoPPTests {
	protected static Path directoryOf(String input) {
		return Path.of("../target/resourcess", input).normalize().toAbsolutePath();
	}

	private String input;

	@ParameterizedTest
	@ValueSource(strings = { "acmeair-master", "commons-lang-master", "flowing-retail-master",
			"bigbluebutton-develop", "SPECjvm2008-master", "SPECjbb2005-master", "TeaStore-master",
			"eventuate-tram-examples-customers-and-orders-redis-master", "microservice-master", "microservice-kafka-master",
			"h2database-master", "spring-cloud-event-sourcing-example-master", "esda-master",
			"TimeSheetGenerator-master", "meet-eat-data-master", "meet-eat-server-master",
			"trojan-source-main", "smart-home-websockets-master", "RUBiS-master", "clnr-demo-master",
			"sagan-main", "spring-petclinic-microservices-master", "piggymetrics-master", "teammates-master" })
	public void testResprint(String input) {
		this.input = input;
		final Path directory = directoryOf(input);
		final ResourceSet resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory),  "Parse sirectory for " + input + "throws.");
		assertDoesNotThrow(() -> testReprint(resourceSet), "Reprint for " + input + "throws.");
	}

	@Override
	protected boolean isExcludedFromReprintTest(String filename) {
		return false;
	}

	@Override
	protected String getTestInputFolder() {
		return directoryOf(input).toString();
	}
}
