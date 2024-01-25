/*******************************************************************************
 * Copyright 2021 Marvin Meller
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Marvin Meller - initial implementation Yves Kirschner - parameterize implementation
 ******************************************************************************/
package tools.mdsd.jamopp.test.resolving;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.resource.JavaResource2Factory;
import tools.mdsd.jamopp.parser.JaMoPPJDTParser;

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
public class ResolvingTest {
	protected static Path directoryOf(String input) {
		return Path.of("./resources", input).toAbsolutePath().normalize();
	}

	@BeforeEach
	public final void initResourceFactory() {
		ContainersFactory.eINSTANCE.createEmptyModel();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java", new JavaResource2Factory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		JavaClasspath.get().clear();
	}

	private void javaDirectoryExists(String input) {
		final var directory = directoryOf(input);
		assertTrue(Files.exists(directory), directory.toString() + " does not exist.");
		assertTrue(assertDoesNotThrow(() -> Files.walk(directory).anyMatch(path -> path.toString().endsWith(".java"))));
	}

	private void resolveAll(String input) {
		final var directory = directoryOf(input);
		final var resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory),
				"Parse directory for " + input + "throws.");
		assertNotNull(resourceSet, "ResourceSet for " + input + "was null.");
		assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet), "Resolve all for " + input + "throws.");
	}

	@Test
	public void testAcmeair() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("acmeair-1.2.0"));
		assertDoesNotThrow(() -> this.resolveAll("acmeair-1.2.0"));
	}

	@Test
	public void testBigbluebutton() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("bigbluebutton-2.4.7"));
		assertDoesNotThrow(() -> this.resolveAll("bigbluebutton-2.4.7"));
	}

	@Test
	public void testClnr() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("clnr-demo-master"));
		assertDoesNotThrow(() -> this.resolveAll("clnr-demo-master"));
	}

	@Test
	public void testCommons() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("commons-lang-rel-commons-lang-3.12.0"));
		assertDoesNotThrow(() -> this.resolveAll("commons-lang-rel-commons-lang-3.12.0"));
	}

	@Test
	public void testData() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("meet-eat-data-master"));
		assertDoesNotThrow(() -> this.resolveAll("meet-eat-data-master"));
	}

	@Test
	public void testDependencyTool() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("Palladio-Build-DependencyTool-master"));
		assertDoesNotThrow(() -> this.resolveAll("Palladio-Build-DependencyTool-master"));
	}

	@Test
	public void testEsda() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("esda-master"));
		assertDoesNotThrow(() -> this.resolveAll("esda-master"));
	}

	@Test
	public void testEventuate() {
		assertDoesNotThrow(() -> this.javaDirectoryExists(
				"eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
		assertDoesNotThrow(() -> this.resolveAll(
				"eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
	}

	@Test
	public void testFlowing() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("flowing-retail-master"));
		assertDoesNotThrow(() -> this.resolveAll("flowing-retail-master"));
	}

	@Test
	public void testH2database() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("h2database-version-2.1.210"));
		assertDoesNotThrow(() -> this.resolveAll("h2database-version-2.1.210"));
	}

	@Test
	public void testKafka() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("microservice-kafka-master"));
		assertDoesNotThrow(() -> this.resolveAll("microservice-kafka-master"));
	}

	@Test
	public void testLatestAcmeair() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("acmeair-1.2.0"));
		assertDoesNotThrow(() -> this.resolveAll("acmeair-1.2.0"));
	}

	@Test
	public void testLatestBigbluebutton() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("bigbluebutton-2.4.7"));
		assertDoesNotThrow(() -> this.resolveAll("bigbluebutton-2.4.7"));
	}

	@Test
	public void testLatestClnr() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("clnr-demo-master"));
		assertDoesNotThrow(() -> this.resolveAll("clnr-demo-master"));
	}

	@Test
	public void testLatestCommons() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("commons-lang-rel-commons-lang-3.12.0"));
		assertDoesNotThrow(() -> this.resolveAll("commons-lang-rel-commons-lang-3.12.0"));
	}

	@Test
	public void testLatestData() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("meet-eat-data-master"));
		assertDoesNotThrow(() -> this.resolveAll("meet-eat-data-master"));
	}

	@Test
	public void testLatestDependencyTool() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("Palladio-Build-DependencyTool-master"));
		assertDoesNotThrow(() -> this.resolveAll("Palladio-Build-DependencyTool-master"));
	}

	@Test
	public void testLatestEsda() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("esda-master"));
		assertDoesNotThrow(() -> this.resolveAll("esda-master"));
	}

	@Test
	public void testLatestEventuate() {
		assertDoesNotThrow(() -> this.javaDirectoryExists(
				"eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
		assertDoesNotThrow(() -> this.resolveAll(
				"eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
	}

	@Test
	public void testLatestFlowing() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("flowing-retail-master"));
		assertDoesNotThrow(() -> this.resolveAll("flowing-retail-master"));
	}

	@Test
	public void testLatestH2database() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("h2database-version-2.1.210"));
		assertDoesNotThrow(() -> this.resolveAll("h2database-version-2.1.210"));
	}

	@Test
	public void testLatestKafka() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("microservice-kafka-master"));
		assertDoesNotThrow(() -> this.resolveAll("microservice-kafka-master"));
	}

	@Test
	public void testLatestMicroservice() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("microservice-master"));
		assertDoesNotThrow(() -> this.resolveAll("microservice-master"));
	}

	@Test
	public void testLatestPetclinic() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("spring-petclinic-microservices-2.3.6"));
		assertDoesNotThrow(() -> this.resolveAll("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testLatestPiggymetrics() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("piggymetrics-spring.version.2.0.3"));
		assertDoesNotThrow(() -> this.resolveAll("piggymetrics-spring.version.2.0.3"));
	}

	@Test
	public void testLatestPlantUML() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("Palladio-Addons-PlantUML-main"));
		assertDoesNotThrow(() -> this.resolveAll("Palladio-Addons-PlantUML-main"));
	}

	@Test
	public void testLatestRabbitmq() {
		assertDoesNotThrow(() -> this.javaDirectoryExists(
				"spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
		assertDoesNotThrow(() -> this
				.resolveAll("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
	}

	@Test
	public void testLatestRUBiS() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("RUBiS-master"));
		assertDoesNotThrow(() -> this.resolveAll("RUBiS-master"));
	}

	@Test
	public void testLatestSagan() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
		assertDoesNotThrow(() -> this.resolveAll("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
	}

	@Test
	public void testLatestServer() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("meet-eat-server-master"));
		assertDoesNotThrow(() -> this.resolveAll("meet-eat-server-master"));
	}

	@Test
	public void testLatestSmart() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("smart-home-websockets-master"));
		assertDoesNotThrow(() -> this.resolveAll("smart-home-websockets-master"));
	}

	@Test
	public void testLatestSourcing() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("spring-cloud-event-sourcing-example-master"));
		assertDoesNotThrow(() -> this.resolveAll("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testLatestSPECjbb2005() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("SPECjbb2005-master"));
		assertDoesNotThrow(() -> this.resolveAll("SPECjbb2005-master"));
	}

	@Test
	public void testLatestSPECjvm2008() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("SPECjvm2008-master"));
		assertDoesNotThrow(() -> this.resolveAll("SPECjvm2008-master"));
	}

	@Test
	public void testLatestTeammates() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("teammates-master"));
		assertDoesNotThrow(() -> this.resolveAll("teammates-master"));
	}

	@Test
	public void testLatestTeaStore() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("TeaStore-1.4.0"));
		assertDoesNotThrow(() -> this.resolveAll("TeaStore-1.4.0"));
	}

	@Test
	public void testLatestTimeSheetGenerator() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("TimeSheetGenerator-main"));
		assertDoesNotThrow(() -> this.resolveAll("TimeSheetGenerator-main"));
	}

	@Test
	public void testLatestTrojan() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("trojan-source-main"));
		assertDoesNotThrow(() -> this.resolveAll("trojan-source-main"));
	}

	@Test
	public void testMicroservice() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("microservice-master"));
		assertDoesNotThrow(() -> this.resolveAll("microservice-master"));
	}

	@Test
	public void testPetclinic() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("spring-petclinic-microservices-2.3.6"));
		assertDoesNotThrow(() -> this.resolveAll("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testPiggymetrics() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("piggymetrics-spring.version.2.0.3"));
		assertDoesNotThrow(() -> this.resolveAll("piggymetrics-spring.version.2.0.3"));
	}

	@Test
	public void testPlantUML() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("Palladio-Addons-PlantUML-main"));
		assertDoesNotThrow(() -> this.resolveAll("Palladio-Addons-PlantUML-main"));
	}

	@Test
	public void testRabbitmq() {
		assertDoesNotThrow(() -> this.javaDirectoryExists(
				"spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
		assertDoesNotThrow(() -> this
				.resolveAll("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
	}

	@Test
	public void testRUBiS() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("RUBiS-master"));
		assertDoesNotThrow(() -> this.resolveAll("RUBiS-master"));
	}

	@Test
	public void testSagan() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
		assertDoesNotThrow(() -> this.resolveAll("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
	}

	@Test
	public void testServer() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("meet-eat-server-master"));
		assertDoesNotThrow(() -> this.resolveAll("meet-eat-server-master"));
	}

	@Test
	public void testSmart() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("smart-home-websockets-master"));
		assertDoesNotThrow(() -> this.resolveAll("smart-home-websockets-master"));
	}

	@Test
	public void testSourcing() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("spring-cloud-event-sourcing-example-master"));
		assertDoesNotThrow(() -> this.resolveAll("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testSPECjbb2005() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("SPECjbb2005-master"));
		assertDoesNotThrow(() -> this.resolveAll("SPECjbb2005-master"));
	}

	@Test
	public void testSPECjvm2008() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("SPECjvm2008-master"));
		assertDoesNotThrow(() -> this.resolveAll("SPECjvm2008-master"));
	}

	@Test
	public void testTeammates() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("teammates-master"));
		assertDoesNotThrow(() -> this.resolveAll("teammates-master"));
	}

	@Test
	public void testTeaStore() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("TeaStore-1.4.0"));
		assertDoesNotThrow(() -> this.resolveAll("TeaStore-1.4.0"));
	}

	@Test
	public void testTimeSheetGenerator() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("TimeSheetGenerator-main"));
		assertDoesNotThrow(() -> this.resolveAll("TimeSheetGenerator-main"));
	}

	@Test
	public void testTrojan() {
		assertDoesNotThrow(() -> this.javaDirectoryExists("trojan-source-main"));
		assertDoesNotThrow(() -> this.resolveAll("trojan-source-main"));
	}
}
