/*******************************************************************************
 * Copyright 2021 Marvin Meller
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Marvin Meller - initial implementation Yves Kirschner - parameterize implementation
 ******************************************************************************/
package org.emftext.language.java.test.bulk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;

import org.emftext.language.java.test.AbstractJaMoPPTests;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Parameterized test for resolving use of JDT-based conversion of Java source code to EMF-based
 * models. Apart from JUint, no dependencies to other tools were newly introduced. For this purpose,
 * the existing code was taken almost identically and copied into this test.
 *
 * @author Marvin Meller
 * @author Yves Kirschner
 *
 * @version 1.3
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ReprintTest extends AbstractJaMoPPTests {
    protected static Path directoryOf(String input) {
        return Path.of("./resourcess", input)
            .toAbsolutePath()
            .normalize();
    }

    private String input;

    @Override
    protected String getTestInputFolder() {
        return directoryOf(input).toString();
    }

    @Override
    protected boolean isExcludedFromReprintTest(String filename) {
        return false;
    }

    @Test
    public void test1ParameterizedResprint() {
        assertDoesNotThrow(() -> reprint("acmeair-1.2.0"));
        assertDoesNotThrow(() -> reprint("bigbluebutton-2.4.7"));
        assertDoesNotThrow(() -> reprint("clnr-demo-master"));
        assertDoesNotThrow(() -> reprint("commons-lang-rel-commons-lang-3.12.0"));
        assertDoesNotThrow(() -> reprint("esda-master"));
        assertDoesNotThrow(() -> reprint("eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
        assertDoesNotThrow(() -> reprint("flowing-retail-master"));
        assertDoesNotThrow(() -> reprint("h2database-version-2.1.210"));
        assertDoesNotThrow(() -> reprint("meet-eat-data-master"));
        assertDoesNotThrow(() -> reprint("meet-eat-server-master"));
        assertDoesNotThrow(() -> reprint("microservice-kafka-master"));
        assertDoesNotThrow(() -> reprint("microservice-master"));
        assertDoesNotThrow(() -> reprint("Palladio-Addons-PlantUML-main"));
        assertDoesNotThrow(() -> reprint("Palladio-Build-DependencyTool-master"));
        assertDoesNotThrow(() -> reprint("piggymetrics-spring.version.2.0.3"));
        assertDoesNotThrow(() -> reprint("RUBiS-master"));
        assertDoesNotThrow(() -> reprint("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
        assertDoesNotThrow(() -> reprint("smart-home-websockets-master"));
        assertDoesNotThrow(() -> reprint("SPECjbb2005-master"));
        assertDoesNotThrow(() -> reprint("SPECjvm2008-master"));
        assertDoesNotThrow(() -> reprint("spring-cloud-event-sourcing-example-master"));
        assertDoesNotThrow(() -> reprint("spring-petclinic-microservices-2.3.6"));
        assertDoesNotThrow(() -> reprint("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
        assertDoesNotThrow(() -> reprint("teammates-master"));
        assertDoesNotThrow(() -> reprint("TeaStore-1.4.0"));
        assertDoesNotThrow(() -> reprint("TimeSheetGenerator-main"));
        assertDoesNotThrow(() -> reprint("trojan-source-main"));
    }

    private void reprint(String input) {
        this.input = input;
        final var directory = directoryOf(input);
        final var resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory),
                "Parse directory for " + input + "throws.");
        assertDoesNotThrow(() -> testReprint(resourceSet), "Reprint for " + input + "throws.");
    }

    @Test
    public void test2ParameterizedResprintWithLatestParser() {
        assertDoesNotThrow(() -> resprintWithLatestParser("acmeair-1.2.0"));
        assertDoesNotThrow(() -> resprintWithLatestParser("bigbluebutton-2.4.7"));
        assertDoesNotThrow(() -> resprintWithLatestParser("clnr-demo-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("commons-lang-rel-commons-lang-3.12.0"));
        assertDoesNotThrow(() -> resprintWithLatestParser("esda-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
        assertDoesNotThrow(() -> resprintWithLatestParser("flowing-retail-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("h2database-version-2.1.210"));
        assertDoesNotThrow(() -> resprintWithLatestParser("meet-eat-data-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("meet-eat-server-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("microservice-kafka-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("microservice-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("Palladio-Addons-PlantUML-main"));
        assertDoesNotThrow(() -> resprintWithLatestParser("Palladio-Build-DependencyTool-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("piggymetrics-spring.version.2.0.3"));
        assertDoesNotThrow(() -> resprintWithLatestParser("RUBiS-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
        assertDoesNotThrow(() -> resprintWithLatestParser("smart-home-websockets-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("SPECjbb2005-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("SPECjvm2008-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("spring-cloud-event-sourcing-example-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("spring-petclinic-microservices-2.3.6"));
        assertDoesNotThrow(() -> resprintWithLatestParser("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
        assertDoesNotThrow(() -> resprintWithLatestParser("teammates-master"));
        assertDoesNotThrow(() -> resprintWithLatestParser("TeaStore-1.4.0"));
        assertDoesNotThrow(() -> resprintWithLatestParser("TimeSheetGenerator-main"));
        assertDoesNotThrow(() -> resprintWithLatestParser("trojan-source-main"));

    }

    private void resprintWithLatestParser(String input) {
        this.input = input;
        final var directory = directoryOf(input);
        final var resourceSet = assertDoesNotThrow(
                () -> new JaMoPPJDTParser().parseDirectory(JaMoPPJDTParser.getJavaParser(null), directory),
                "Parse directory for " + input + "throws.");
        assertDoesNotThrow(() -> testReprint(resourceSet), "Reprint for " + input + "throws.");
    }
}
