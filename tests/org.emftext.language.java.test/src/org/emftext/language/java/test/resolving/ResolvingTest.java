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
package org.emftext.language.java.test.resolving;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.ContainersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jamopp.parser.jdt.JaMoPPJDTParser;
import jamopp.resource.JavaResource2Factory;

/**
 * Parameterized test for resolving use of JDT-based conversion of Java source
 * code to EMF-based models. Apart from JUint, no dependencies to other tools
 * were newly introduced. For this purpose, the existing code was taken almost
 * identically and copied into this test.
 *
 * @author Marvin Meller
 * @author Yves Kirschner
 *
 * @version 1.3
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ResolvingTest {
	protected static Path directoryOf(String input) {
		return Path.of("../target/resources", input).normalize().toAbsolutePath();
	}

	@BeforeEach
	public final void initResourceFactory() {
		ContainersFactory.eINSTANCE.createEmptyModel();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java", new JavaResource2Factory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		JavaClasspath.get().clear();
	}

	@ParameterizedTest
	@ValueSource(strings = { "acmeair-master", "commons-lang-master", "flowing-retail-master", "bigbluebutton-develop",
			"SPECjvm2008-master", "SPECjbb2005-master", "TeaStore-master",
			"eventuate-tram-examples-customers-and-orders-redis-master", "microservice-master",
			"microservice-kafka-master", "h2database-master", "spring-cloud-event-sourcing-example-master",
			"esda-master", "TimeSheetGenerator-master", "meet-eat-data-master", "meet-eat-server-master",
			"trojan-source-main", "smart-home-websockets-master", "RUBiS-master", "clnr-demo-master", "sagan-main",
			"spring-petclinic-microservices-master", "piggymetrics-master", "teammates-master",
			"Palladio-Addons-PlantUML-main", "Palladio-Build-DependencyTool-master",
			"spring-rabbitmq-messaging-microservices-master" })
	public void test1JavaDirectoryExists(String input) {
		final Path directory = directoryOf(input);
		assertTrue(Files.exists(directory), directory.toString() + " does not exist.");
		assertTrue(assertDoesNotThrow(() -> Files.walk(directory).anyMatch(path -> path.toString().endsWith(".java"))));
	}

	@ParameterizedTest
	@ValueSource(strings = { "acmeair-master", "commons-lang-master", "flowing-retail-master", "bigbluebutton-develop",
			"SPECjvm2008-master", "SPECjbb2005-master", "TeaStore-master",
			"eventuate-tram-examples-customers-and-orders-redis-master", "microservice-master",
			"microservice-kafka-master", "h2database-master", "spring-cloud-event-sourcing-example-master",
			"esda-master", "TimeSheetGenerator-master", "meet-eat-data-master", "meet-eat-server-master",
			"trojan-source-main", "smart-home-websockets-master", "RUBiS-master", "clnr-demo-master", "sagan-main",
			"spring-petclinic-microservices-master", "piggymetrics-master", "teammates-master",
			"Palladio-Addons-PlantUML-main", "Palladio-Build-DependencyTool-master",
			"spring-rabbitmq-messaging-microservices-master" })
	public void test2ResolveAll(String input) {
		final Path directory = directoryOf(input);
		final ResourceSet resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory));
		assertNotNull(resourceSet, "ResourceSet for " + input + "was null.");
		assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet), "Resolve all for " + input + "throws.");
	}

	@ParameterizedTest
	@ValueSource(strings = { "acmeair-master", "commons-lang-master", "flowing-retail-master", "bigbluebutton-develop",
			"SPECjvm2008-master", "SPECjbb2005-master", "TeaStore-master",
			"eventuate-tram-examples-customers-and-orders-redis-master", "microservice-master",
			"microservice-kafka-master", "h2database-master", "spring-cloud-event-sourcing-example-master",
			"esda-master", "TimeSheetGenerator-master", "meet-eat-data-master", "meet-eat-server-master",
			"trojan-source-main", "smart-home-websockets-master", "RUBiS-master", "clnr-demo-master", "sagan-main",
			"spring-petclinic-microservices-master", "piggymetrics-master", "teammates-master",
			"Palladio-Addons-PlantUML-main", "Palladio-Build-DependencyTool-master",
			"spring-rabbitmq-messaging-microservices-master" })
	public void test3ResolveAllWithLatestParser(String input) {
		final Path directory = directoryOf(input);
		final ResourceSet resourceSet = assertDoesNotThrow(
				() -> new JaMoPPJDTParser().parseDirectory(JaMoPPJDTParser.getJavaParser(null), directory));
		assertNotNull(resourceSet, "ResourceSet for " + input + "was null.");
		assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet), "Resolve all for " + input + "throws.");
	}
}
