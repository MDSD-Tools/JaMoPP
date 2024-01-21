package tools.mdsd.jamopp.standalone;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.containers.Package;
import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;
import tools.mdsd.jamopp.parser.jdt.JaMoPPParserAPI;
import tools.mdsd.jamopp.resource.JavaResource2Factory;

/**
 * Class for the stand alone usage of JaMoPP Palladio
 *
 * How to use: - Input any URI (absolute or relative file path/ Directory/
 * Archive) via INPUT - If you want to output any xmi library files define
 * ENABLE_OUTPUT_OF_LIBRARY_FILES as true - The xmi output will be generated and
 * saved in ./standalone_output including its package hierarchy
 *
 * If you have Problems opening the .xmi file with the Ecore Model Editor make
 * sure you installed the Standalone version as an Ecplise Plugin
 */
public final class JaMoPPStandalone {

	private static final PrintStream OUTPUT = System.out;
	private static final String INPUT = "";
	private static final boolean ENABLE_OUTPUT_OF_LIBRARY_FILES = false;

	private JaMoPPStandalone() {
		// Should not be initiated
	}

	public static void main(String[] agrs) {

		ContainersFactory.eINSTANCE.createEmptyModel();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java", new JavaResource2Factory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		JaMoPPParserAPI parser = new JaMoPPJDTParser();
		ResourceSet resourceSet = parser.parseUri(URI.createURI(INPUT));
		EcoreUtil.resolveAll(resourceSet);
		for (Resource javaResource : new ArrayList<>(resourceSet.getResources())) {

			if (javaResource.getContents().isEmpty()) {
				OUTPUT.println("WARNING: Emtpy Resource: " + javaResource.getURI());
				continue;
			}

			if (!"file".equals(javaResource.getURI().scheme()) && !ENABLE_OUTPUT_OF_LIBRARY_FILES) {
				continue;
			}

			File outputFile = createFile(javaResource);
			outputFile.getParentFile().mkdirs();

			URI xmiFileURI = URI.createFileURI(outputFile.getAbsolutePath()).appendFileExtension("xmi");
			Resource xmiResource = resourceSet.createResource(xmiFileURI);
			xmiResource.getContents().addAll(javaResource.getContents());
		}

		for (Resource xmiResource : resourceSet.getResources()) {
			if (xmiResource instanceof XMIResource) {
				try {
					xmiResource.save(resourceSet.getLoadOptions());
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	private static File createFile(Resource javaResource) {
		return new File("." + File.separator + "./standalone_output" + File.separator + checkScheme(javaResource));
	}

	private static String checkScheme(Resource javaResource) {
		String outputFileName = "";
		JavaRoot root = (JavaRoot) javaResource.getContents().get(0);

		if (root instanceof CompilationUnit) {
			outputFileName = root.getNamespacesAsString().replace(".", File.separator) + File.separator;
			CompilationUnit compilationUnit = (CompilationUnit) root;
			if (compilationUnit.getClassifiers().isEmpty()) {
				outputFileName += 0;
			} else {
				outputFileName += compilationUnit.getClassifiers().get(0).getName();
			}

		} else if (root instanceof Package) {
			outputFileName = root.getNamespacesAsString().replace(".", File.separator) + File.separator
					+ "package-info";
			if (outputFileName.startsWith(File.separator)) {
				outputFileName = outputFileName.substring(1);
			}
		} else if (root instanceof tools.mdsd.jamopp.model.java.containers.Module) {
			outputFileName = root.getNamespacesAsString().replace(".", File.separator) + File.separator + "module-info";
		}
		return outputFileName;
	}
}
