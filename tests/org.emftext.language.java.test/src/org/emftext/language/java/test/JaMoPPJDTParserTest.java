/*******************************************************************************
 * Copyright 2022 Yves Kirschner
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Yves Kirschner - initial implementation
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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    public void testJdtParser() {
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/acmeair-1.2.0").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/bigbluebutton-2.4.7").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/clnr-demo-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/commons-lang-rel-commons-lang-3.12.0").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/esda-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/eventuate-tram-examples-customers-and-orders-redis-be4a3da5502aa11af441b70b7ab6b5f1430b17d4").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/flowing-retail-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/h2database-version-2.1.210").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/meet-eat-data-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/meet-eat-server-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/microservice-kafka-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/microservice-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/Palladio-Addons-PlantUML-main").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/Palladio-Build-DependencyTool-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/piggymetrics-spring.version.2.0.3").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/RUBiS-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/sagan-1995913fb2d90693c97c251fd142b429724cdf44").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/smart-home-websockets-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/SPECjbb2005-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/SPECjvm2008-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/spring-cloud-event-sourcing-example-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/spring-petclinic-microservices-2.3.6").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/spring-rabbitmq-messaging-microservices-019cadd4c1310a4651f3529626ac2acd4853a987").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/teammates-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/TeaStore-1.4.0").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/TimeSheetGenerator-master").toAbsolutePath().normalize()));
    	assertDoesNotThrow(() -> jdtParser(Path.of("../target/resources/trojan-source-main").toAbsolutePath().normalize()));
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
}
