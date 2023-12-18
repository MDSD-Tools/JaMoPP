/*******************************************************************************
 * Copyright 2021 Marvin Meller
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Marvin Meller - initial implementation Yves Kirschner - parameterize implementation
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.test.bulk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;

import tools.mdsd.jamopp.model.java.test.AbstractJaMoPPTests;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

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
public class ReprintTest extends AbstractJaMoPPTests {
	protected static Path directoryOf(String input) {
		return Path.of("./resourcess", input).toAbsolutePath().normalize();
	}

	private String input;

	@Override
	protected String getTestInputFolder() {
		return directoryOf(this.input).toString();
	}

	@Override
	protected boolean isExcludedFromReprintTest(String filename) {
		return false;
	}

	private void reprint(String input) {
		this.input = input;
		final var directory = directoryOf(input);
		final var resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory),
				"Parse directory for " + input + "throws.");
		assertDoesNotThrow(() -> this.testReprint(resourceSet), "Reprint for " + input + "throws.");
	}

	private void resprintWithLatestParser(String input) {
		this.input = input;
		final var directory = directoryOf(input);
		final var resourceSet = assertDoesNotThrow(
				() -> new JaMoPPJDTParser().parseDirectory(JaMoPPJDTParser.getJavaParser(null), directory),
				"Parse directory for " + input + "throws.");
		assertDoesNotThrow(() -> this.testReprint(resourceSet), "Reprint for " + input + "throws.");
	}

	@Test
	public void testAcmeair() {
		assertDoesNotThrow(() -> this.reprint("acmeair-1.2.0"));
	}

	@Test
	public void testBigbluebutton() {
		assertDoesNotThrow(() -> this.reprint("bigbluebutton-2.4.7"));
	}

	@Test
	public void testClnr() {
		assertDoesNotThrow(() -> this.reprint("clnr-demo-master"));
	}

	@Test
	public void testCommons() {
		assertDoesNotThrow(() -> this.reprint("commons-lang-rel-commons-lang-3.12.0"));
	}

	@Test
	public void testData() {
		assertDoesNotThrow(() -> this.reprint("meet-eat-data-master"));
	}

	@Test
	public void testDependencyTool() {
		assertDoesNotThrow(() -> this.reprint("Palladio-Build-DependencyTool-master"));
	}

	@Test
	public void testEsda() {
		assertDoesNotThrow(() -> this.reprint("esda-master"));
	}

	@Test
	public void testEventuate() {
		assertDoesNotThrow(() -> this.reprint(
				"eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
	}

	@Test
	public void testFlowing() {
		assertDoesNotThrow(() -> this.reprint("flowing-retail-master"));
	}

	@Test
	public void testH2database() {
		assertDoesNotThrow(() -> this.reprint("h2database-version-2.1.210"));
	}

	@Test
	public void testKafka() {
		assertDoesNotThrow(() -> this.reprint("microservice-kafka-master"));
	}

	@Test
	public void testLatestAcmeair() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("acmeair-1.2.0"));
	}

	@Test
	public void testLatestBigbluebutton() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("bigbluebutton-2.4.7"));
	}

	@Test
	public void testLatestClnr() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("clnr-demo-master"));
	}

	@Test
	public void testLatestCommons() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("commons-lang-rel-commons-lang-3.12.0"));
	}

	@Test
	public void testLatestData() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("meet-eat-data-master"));
	}

	@Test
	public void testLatestDependencyTool() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("Palladio-Build-DependencyTool-master"));
	}

	@Test
	public void testLatestEsda() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("esda-master"));
	}

	@Test
	public void testLatestEventuate() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser(
				"eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
	}

	@Test
	public void testLatestFlowing() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("flowing-retail-master"));
	}

	@Test
	public void testLatestH2database() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("h2database-version-2.1.210"));
	}

	@Test
	public void testLatestKafka() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("microservice-kafka-master"));
	}

	@Test
	public void testLatestMicroservice() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("microservice-master"));
	}

	@Test
	public void testLatestPetclinic() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testLatestPiggymetrics() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("piggymetrics-spring.version.2.0.3"));
	}

	@Test
	public void testLatestPlantUML() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("Palladio-Addons-PlantUML-main"));
	}

	@Test
	public void testLatestRabbitmq() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser(
				"spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
	}

	@Test
	public void testLatestRUBiS() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("RUBiS-master"));
	}

	@Test
	public void testLatestSagan() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
	}

	@Test
	public void testLatestServer() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("meet-eat-server-master"));
	}

	@Test
	public void testLatestSmart() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("smart-home-websockets-master"));
	}

	@Test
	public void testLatestSourcing() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testLatestSPECjbb2005() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("SPECjbb2005-master"));
	}

	@Test
	public void testLatestSPECjvm2008() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("SPECjvm2008-master"));
	}

	@Test
	public void testLatestTeammates() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("teammates-master"));
	}

	@Test
	public void testLatestTeaStore() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("TeaStore-1.4.0"));
	}

	@Test
	public void testLatestTimeSheetGenerator() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("TimeSheetGenerator-main"));
	}

	@Test
	public void testLatestTrojan() {
		assertDoesNotThrow(() -> this.resprintWithLatestParser("trojan-source-main"));
	}

	@Test
	public void testMicroservice() {
		assertDoesNotThrow(() -> this.reprint("microservice-master"));
	}

	@Test
	public void testPetclinic() {
		assertDoesNotThrow(() -> this.reprint("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testPiggymetrics() {
		assertDoesNotThrow(() -> this.reprint("piggymetrics-spring.version.2.0.3"));
	}

	@Test
	public void testPlantUML() {
		assertDoesNotThrow(() -> this.reprint("Palladio-Addons-PlantUML-main"));
	}

	@Test
	public void testRabbitmq() {
		assertDoesNotThrow(
				() -> this.reprint("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
	}

	@Test
	public void testRUBiS() {
		assertDoesNotThrow(() -> this.reprint("RUBiS-master"));
	}

	@Test
	public void testSagan() {
		assertDoesNotThrow(() -> this.reprint("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
	}

	@Test
	public void testServer() {
		assertDoesNotThrow(() -> this.reprint("meet-eat-server-master"));
	}

	@Test
	public void testSmart() {
		assertDoesNotThrow(() -> this.reprint("smart-home-websockets-master"));
	}

	@Test
	public void testSourcing() {
		assertDoesNotThrow(() -> this.reprint("spring-petclinic-microservices-2.3.6"));
	}

	@Test
	public void testSPECjbb2005() {
		assertDoesNotThrow(() -> this.reprint("SPECjbb2005-master"));
	}

	@Test
	public void testSPECjvm2008() {
		assertDoesNotThrow(() -> this.reprint("SPECjvm2008-master"));
	}

	@Test
	public void testTeammates() {
		assertDoesNotThrow(() -> this.reprint("teammates-master"));
	}

	@Test
	public void testTeaStore() {
		assertDoesNotThrow(() -> this.reprint("TeaStore-1.4.0"));
	}

	@Test
	public void testTimeSheetGenerator() {
		assertDoesNotThrow(() -> this.reprint("TimeSheetGenerator-main"));
	}

	@Test
	public void testTrojan() {
		assertDoesNotThrow(() -> this.reprint("trojan-source-main"));
	}
}
