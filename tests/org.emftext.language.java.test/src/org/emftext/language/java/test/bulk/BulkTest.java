/*******************************************************************************
 * Copyright (c) 2006-2015
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Dresden, Amtsgericht Dresden, HRB 34001
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Dresden, Germany
 *      - initial API and implementation
 ******************************************************************************/
package org.emftext.language.java.test.bulk;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.examples.Expander;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.java.test.AbstractJaMoPPTests;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jamopp.parser.jdt.JaMoPPJDTParser;

public class BulkTest extends AbstractJaMoPPTests {
	private final static String BASE_ZIP = "JaMoPP-BulkTest" + File.separator + "Tests" + File.separator
		+ "org.emftext.language.java.test.bulk" + File.separator + "input" + File.separator;
	private final static String END_ZIP = File.separator + "src.zip";
	private final static String END_SRC = File.separator + "src";
	private String inputFolder;
	private String generalInputFolder;
	
	@BeforeAll
	public static void setUpBeforeTests() {
		try {
			Files.walk(Paths.get(BASE_ZIP)).filter(Files::isRegularFile)
				.filter(path -> path.endsWith("bin.jar") || path.endsWith("rt.jar")
						|| path.endsWith("jsse.jar")).forEach(path -> {
					try {
						Files.delete(path);
					} catch (IOException e) {
					}
				});
		} catch (IOException e) {
		}
	}
	
	@Test
	public void testAndroMDA_3_3() {
		inputFolder = "andromda-3.3";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testApacheAnt_1_8_1() {
		inputFolder = "apache-ant-1.8.1";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testApacheCommonsMath_1_2() {
		inputFolder = "apache-commons-math-1.2";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testApacheTomcat_6_0_18() {
		inputFolder = "apache-tomcat-6.0.18";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testEclipse_3_4_1() {
		inputFolder = "eclipse-3.4.1";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testGWT_1_5_3() {
		inputFolder = "gwt-1.5.3";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testJacks_1_6_0_07() {
		inputFolder = "jacks_javac_1.6.0_07_passed";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testJBoss_5_0_0() {
		inputFolder = "jboss-5.0.0.GA";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testJDownloader_0_9579() {
		inputFolder = "jdownloader_0.9579";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testJDTTestFiles() {
		inputFolder = "jdt_test_files";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testMantissa_7_2() {
		inputFolder = "mantissa-7.2";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testNetbeans_6_5_1() {
		inputFolder = "netbeans-6.5.1";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testSpingFramework_3_0_0_M1() {
		inputFolder = "spring-framework-3.0.0.M1";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testStruts_2_1_6() {
		inputFolder = "struts-2.1.6";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testSunJDK_1_5_0_16() {
		inputFolder = "sun_jdk_1.5.0_16";
		generalInputFolder = null;
		testProject();
	}
	
	@Test
	public void testXerces_2_9_1() {
		inputFolder = "Xerces-J-2.9.1";
		generalInputFolder = null;
		testProject();
	}
	
	@Override
	protected boolean isExcludedFromReprintTest(String filename) {
		return false;
	}

	@Override
	protected String getTestInputFolder() {
		return generalInputFolder == null ? BASE_ZIP + inputFolder : generalInputFolder;
	}
	
	private String getSrcZip() {
		return generalInputFolder == null ? BASE_ZIP + inputFolder + END_ZIP : null;
	}
	
	private void testProject() {
		JaMoPPJDTParser parser = new JaMoPPJDTParser();
		Path target = null;
		if (generalInputFolder == null) {
			decompressZipFile();
		}
		target = Paths.get(getTestInputFolder());
		ResourceSet set = parser.parseDirectory(target);
		try {
			this.testReprint(set);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	private void decompressZipFile() {
		Path file = Paths.get(getSrcZip());
		if (Files.exists(file)) {
			Path target = Paths.get(getTestInputFolder(), END_SRC);
			if (Files.exists(target)) {
				return;
			}
			try {
				new Expander().expand(ArchiveStreamFactory.ZIP, file.toFile(), target.toFile());
			} catch (IOException | ArchiveException e) {
				fail(e.getMessage());
			}
		} else {
			fail("File " + file + " is not a zip file.");
		}
	}
}
