/*******************************************************************************
 * Copyright 2021 Marvin Meller
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Marvin Meller - initial implementation Yves Kirschner - parameterize implementation
 ******************************************************************************/
package org.emftext.language.java.test.resolving;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.ContainersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jamopp.parser.jdt.JaMoPPJDTParser;
import jamopp.resource.JavaResource2Factory;

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
public class ResolvingTest {
    protected static Path directoryOf(String input) {
        return Path.of("./resources", input)
            .toAbsolutePath()
            .normalize();
    }

    @BeforeEach
    public final void initResourceFactory() {
        ContainersFactory.eINSTANCE.createEmptyModel();
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
            .put("java", new JavaResource2Factory());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
            .put("xmi", new XMIResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
            .put("xml", new XMLResourceFactoryImpl());
        JavaClasspath.get()
            .clear();
    }

    @Test
    public void test1ParameterizedJavaDirectoryExists() {
    	assertDoesNotThrow(() -> javaDirectoryExists("acmeair-1.2.0"));
    	assertDoesNotThrow(() -> javaDirectoryExists("bigbluebutton-2.4.7"));
    	assertDoesNotThrow(() -> javaDirectoryExists("clnr-demo-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("commons-lang-rel-commons-lang-3.12.0"));
    	assertDoesNotThrow(() -> javaDirectoryExists("esda-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
    	assertDoesNotThrow(() -> javaDirectoryExists("flowing-retail-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("h2database-version-2.1.210"));
    	assertDoesNotThrow(() -> javaDirectoryExists("meet-eat-data-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("meet-eat-server-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("microservice-kafka-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("microservice-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("Palladio-Addons-PlantUML-main"));
    	assertDoesNotThrow(() -> javaDirectoryExists("Palladio-Build-DependencyTool-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("piggymetrics-spring.version.2.0.3"));
    	assertDoesNotThrow(() -> javaDirectoryExists("RUBiS-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
    	assertDoesNotThrow(() -> javaDirectoryExists("smart-home-websockets-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("SPECjbb2005-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("SPECjvm2008-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("spring-cloud-event-sourcing-example-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("spring-petclinic-microservices-2.3.6"));
    	assertDoesNotThrow(() -> javaDirectoryExists("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
    	assertDoesNotThrow(() -> javaDirectoryExists("teammates-master"));
    	assertDoesNotThrow(() -> javaDirectoryExists("TeaStore-1.4.0"));
    	assertDoesNotThrow(() -> javaDirectoryExists("TimeSheetGenerator-main"));
    	assertDoesNotThrow(() -> javaDirectoryExists("trojan-source-main"));
    }
    
    private void javaDirectoryExists(String input) {
        final var directory = directoryOf(input);
        assertTrue(Files.exists(directory), directory.toString() + " does not exist.");
        assertTrue(assertDoesNotThrow(() -> Files.walk(directory)
            .anyMatch(path -> path.toString()
                .endsWith(".java"))));
    }
    
    @Test
    public void test2ParameterizedResolveAll() {
    	assertDoesNotThrow(() -> resolveAll("acmeair-1.2.0"));
    	assertDoesNotThrow(() -> resolveAll("bigbluebutton-2.4.7"));
    	assertDoesNotThrow(() -> resolveAll("clnr-demo-master"));
    	assertDoesNotThrow(() -> resolveAll("commons-lang-rel-commons-lang-3.12.0"));
    	assertDoesNotThrow(() -> resolveAll("esda-master"));
    	assertDoesNotThrow(() -> resolveAll("eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
    	assertDoesNotThrow(() -> resolveAll("flowing-retail-master"));
    	assertDoesNotThrow(() -> resolveAll("h2database-version-2.1.210"));
    	assertDoesNotThrow(() -> resolveAll("meet-eat-data-master"));
    	assertDoesNotThrow(() -> resolveAll("meet-eat-server-master"));
    	assertDoesNotThrow(() -> resolveAll("microservice-kafka-master"));
    	assertDoesNotThrow(() -> resolveAll("microservice-master"));
    	assertDoesNotThrow(() -> resolveAll("Palladio-Addons-PlantUML-main"));
    	assertDoesNotThrow(() -> resolveAll("Palladio-Build-DependencyTool-master"));
    	assertDoesNotThrow(() -> resolveAll("piggymetrics-spring.version.2.0.3"));
    	assertDoesNotThrow(() -> resolveAll("RUBiS-master"));
    	assertDoesNotThrow(() -> resolveAll("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
    	assertDoesNotThrow(() -> resolveAll("smart-home-websockets-master"));
    	assertDoesNotThrow(() -> resolveAll("SPECjbb2005-master"));
    	assertDoesNotThrow(() -> resolveAll("SPECjvm2008-master"));
    	assertDoesNotThrow(() -> resolveAll("spring-cloud-event-sourcing-example-master"));
    	assertDoesNotThrow(() -> resolveAll("spring-petclinic-microservices-2.3.6"));
    	assertDoesNotThrow(() -> resolveAll("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
    	assertDoesNotThrow(() -> resolveAll("teammates-master"));
    	assertDoesNotThrow(() -> resolveAll("TeaStore-1.4.0"));
    	assertDoesNotThrow(() -> resolveAll("TimeSheetGenerator-main"));
    	assertDoesNotThrow(() -> resolveAll("trojan-source-main"));

    }
    
    private void resolveAll(String input) {
        final var directory = directoryOf(input);
        final var resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory),
                "Parse directory for " + input + "throws.");
        assertNotNull(resourceSet, "ResourceSet for " + input + "was null.");
        assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet), "Resolve all for " + input + "throws.");
    }

    @Test
    public void test3ParameterizedResolveAllWithLatestParser() {
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("acmeair-1.2.0"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("bigbluebutton-2.4.7"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("clnr-demo-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("commons-lang-rel-commons-lang-3.12.0"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("esda-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("flowing-retail-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("h2database-version-2.1.210"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("meet-eat-data-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("meet-eat-server-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("microservice-kafka-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("microservice-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("Palladio-Addons-PlantUML-main"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("Palladio-Build-DependencyTool-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("piggymetrics-spring.version.2.0.3"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("RUBiS-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("sagan-1995913fb2d90693c97c251fd142b429724cdf44"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("smart-home-websockets-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("SPECjbb2005-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("SPECjvm2008-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("spring-cloud-event-sourcing-example-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("spring-petclinic-microservices-2.3.6"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("teammates-master"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("TeaStore-1.4.0"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("TimeSheetGenerator-main"));
    	assertDoesNotThrow(() -> resolveAllWithLatestParser("trojan-source-main"));
    }
    
    private void resolveAllWithLatestParser(String input) {
        final var directory = directoryOf(input);
        final var resourceSet = assertDoesNotThrow(
                () -> new JaMoPPJDTParser().parseDirectory(JaMoPPJDTParser.getJavaParser(null), directory),
                "Parse directory for " + input + "throws.");
        assertNotNull(resourceSet, "ResourceSet for " + input + "was null.");
        assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet), "Resolve all for " + input + "throws.");
    }
}
