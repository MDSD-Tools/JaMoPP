/*******************************************************************************
 * Copyright 2022 Yves Kirschner
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Yves Kirschner
 *      - initial implementation
 ******************************************************************************/
package org.emftext.language.java.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.jdt.core.dom.ASTParser;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jamopp.parser.jdt.JaMoPPJDTParser;
import jamopp.resource.JavaResource2Factory;

/**
 * Parameterized test for testing the JaMoPPJDTParser-API.
 *
 * @author Yves Kirschner
 *
 * @version 1.0
 */
@DisplayName("Test the individual steps with the JDT parser")
public class JaMoPPJDTParserTest {

	private static Stream<Path> caseStudiesProvider() {
		return Stream
				.of("acmeair-master", "commons-lang-master", "flowing-retail-master", "bigbluebutton-develop",
						"SPECjvm2008-master", "SPECjbb2005-master", "TeaStore-master",
						"eventuate-tram-examples-customers-and-orders-redis-master", "microservice-master",
						"microservice-kafka-master", "h2database-master", "spring-cloud-event-sourcing-example-master",
						"esda-master", "TimeSheetGenerator-master", "meet-eat-data-master", "meet-eat-server-master",
						"trojan-source-main", "smart-home-websockets-master", "RUBiS-master", "clnr-demo-master",
						"sagan-main", "spring-petclinic-microservices-master", "piggymetrics-master",
						"teammates-master", "Palladio-Addons-PlantUML-main", "Palladio-Build-DependencyTool-master",
						"spring-rabbitmq-messaging-microservices-master")
				.map(c -> Path.of("../target/resources", c).toAbsolutePath().normalize());

	}

	@BeforeEach
	public final void initResourceFactory() {
		ContainersFactory.eINSTANCE.createEmptyModel();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java", new JavaResource2Factory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		JavaClasspath.get().clear();
	}

	@DisplayName("Test with different case studies")
	@ParameterizedTest(name = "{index}. case study: {0}")
	@MethodSource("caseStudiesProvider")
	public void testJdtParser(Path caseStudy) {
		final ASTParser parser = assertDoesNotThrow(() -> JaMoPPJDTParser.getJavaParser(null));
		assertNotNull(parser);
		final JaMoPPJDTParser api = new JaMoPPJDTParser();
		assertNotNull(api);

		final String[] classpathEntries = assertDoesNotThrow(() -> JaMoPPJDTParser.getClasspathEntries(caseStudy));
		assertNotNull(classpathEntries);

		final String[] sourcepathEntries = assertDoesNotThrow(() -> api.getSourcepathEntries(caseStudy));
		assertNotNull(sourcepathEntries);
		assertTrue(sourcepathEntries.length > 0);

		final String[] encodings = new String[sourcepathEntries.length];
		Arrays.fill(encodings, JaMoPPJDTParser.DEFAULT_ENCODING);
		assertNotNull(sourcepathEntries);
		assertTrue(sourcepathEntries.length == encodings.length);

		final var units = assertDoesNotThrow(
				() -> JaMoPPJDTParser.getCompilationUnits(parser, classpathEntries, sourcepathEntries, encodings));
		assertNotNull(units);
		assertTrue(units.size() == sourcepathEntries.length);

		final List<JavaRoot> javaRoots = assertDoesNotThrow(() -> api.convertCompilationUnits(units));
		assertNotNull(javaRoots);
		assertTrue(javaRoots.size() == sourcepathEntries.length);

		assertNotNull(api.getResourceSet());
		assertDoesNotThrow(() -> EcoreUtil.resolveAll(api.getResourceSet()));
		final Set<CompilationUnit> compilationUnits = assertDoesNotThrow(() -> api.get(CompilationUnit.class));
		assertNotNull(compilationUnits);
		assertFalse(compilationUnits.isEmpty());
	}
}
