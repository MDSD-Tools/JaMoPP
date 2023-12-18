/*******************************************************************************
 * Copyright 2022 Yves Kirschner
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Yves Kirschner - initial implementation
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.resource.JavaResource2Factory;
import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Parameterized test for testing the JaMoPPJDTParser-API.
 *
 * @author Yves Kirschner
 *
 * @version 1.0
 */
@DisplayName("Test the individual steps with the JDT parser")
public class JaMoPPJDTParserTest {

	@BeforeEach
	public final void initResourceFactory() {
		ContainersFactory.eINSTANCE.createEmptyModel();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java", new JavaResource2Factory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		JavaClasspath.get().clear();
	}

	private void jdtParser(Path caseStudy) {
		final var parser = assertDoesNotThrow(() -> JaMoPPJDTParser.getJavaParser(null));
		assertNotNull(parser);
		final var api = new JaMoPPJDTParser();
		assertNotNull(api);

		final var classpathEntries = assertDoesNotThrow(() -> JaMoPPJDTParser.getClasspathEntries(caseStudy));
		assertNotNull(classpathEntries);

		final var sourcepathEntries = assertDoesNotThrow(() -> api.getSourcepathEntries(caseStudy));
		assertNotNull(sourcepathEntries);
		assertTrue(sourcepathEntries.length > 0);

		final var encodings = new String[sourcepathEntries.length];
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

	@Test
	public void testAcmeair() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of("./resources/acmeair-1.2.0").toAbsolutePath().normalize()));
	}

	@Test
	public void testBigbluebutton() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/bigbluebutton-2.4.7").toAbsolutePath().normalize()));
	}

	@Test
	public void testClnr() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of("./resources/clnr-demo-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testCommons() {
		assertDoesNotThrow(() -> this
				.jdtParser(Path.of("./resources/commons-lang-rel-commons-lang-3.12.0").toAbsolutePath().normalize()));
	}

	@Test
	public void testData() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/meet-eat-data-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testDependencyTool() {
		assertDoesNotThrow(() -> this
				.jdtParser(Path.of("./resources/Palladio-Build-DependencyTool-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testEsda() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of("./resources/esda-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testEventuate() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of(
				"./resources/eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4")
				.toAbsolutePath().normalize()));
	}

	@Test
	public void testFlowing() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/flowing-retail-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testH2database() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/h2database-version-2.1.210").toAbsolutePath().normalize()));
	}

	@Test
	public void testKafka() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/microservice-kafka-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testMicroservice() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/microservice-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testPetclinic() {
		assertDoesNotThrow(() -> this
				.jdtParser(Path.of("./resources/spring-petclinic-microservices-2.3.6").toAbsolutePath().normalize()));
	}

	@Test
	public void testPiggymetrics() {
		assertDoesNotThrow(() -> this
				.jdtParser(Path.of("./resources/piggymetrics-spring.version.2.0.3").toAbsolutePath().normalize()));
	}

	@Test
	public void testPlantUML() {
		assertDoesNotThrow(() -> this
				.jdtParser(Path.of("./resources/Palladio-Addons-PlantUML-main").toAbsolutePath().normalize()));
	}

	@Test
	public void testRabbitmq() {
		assertDoesNotThrow(() -> this.jdtParser(
				Path.of("./resources/spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987")
						.toAbsolutePath().normalize()));
	}

	@Test
	public void testRUBiS() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of("./resources/RUBiS-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testSagan() {
		assertDoesNotThrow(() -> this.jdtParser(
				Path.of("./resources/sagan-1995913fb2d90693c97c251fd142b429724cdf44").toAbsolutePath().normalize()));
	}

	@Test
	public void testServer() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/meet-eat-server-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testSourcing() {
		assertDoesNotThrow(() -> this.jdtParser(
				Path.of("./resources/spring-cloud-event-sourcing-example-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testSPECjbb2005() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/SPECjbb2005-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testSPECjvm2008() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/SPECjvm2008-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testTeammates() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of("./resources/teammates-master").toAbsolutePath().normalize()));
	}

	@Test
	public void testTeaStore() {
		assertDoesNotThrow(() -> this.jdtParser(Path.of("./resources/TeaStore-1.4.0").toAbsolutePath().normalize()));
	}

	@Test
	public void testTimeSheetGenerator() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/TimeSheetGenerator-main").toAbsolutePath().normalize()));
	}

	@Test
	public void testTrojan() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/trojan-source-main").toAbsolutePath().normalize()));
	}

	@Test
	public void testWebsockets() {
		assertDoesNotThrow(
				() -> this.jdtParser(Path.of("./resources/smart-home-websockets-master").toAbsolutePath().normalize()));
	}
}
