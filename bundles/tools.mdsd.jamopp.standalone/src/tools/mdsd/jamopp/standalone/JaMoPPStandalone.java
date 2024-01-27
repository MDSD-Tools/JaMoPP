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
import tools.mdsd.jamopp.parser.JaMoPPJDTParser;
import tools.mdsd.jamopp.parser.JaMoPPParserAPI;
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

	public static void main(final String[] agrs) {

		ContainersFactory.eINSTANCE.createEmptyModel();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("java", new JavaResource2Factory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		final JaMoPPParserAPI parser = new JaMoPPJDTParser();
		final ResourceSet resourceSet = parser.parseUri(URI.createURI(INPUT));
		EcoreUtil.resolveAll(resourceSet);
		for (final Resource javaResource : new ArrayList<>(resourceSet.getResources())) {

			if (javaResource.getContents().isEmpty()) {
				OUTPUT.println("WARNING: Emtpy Resource: " + javaResource.getURI());
				continue;
			}

			if (!"file".equals(javaResource.getURI().scheme()) && !ENABLE_OUTPUT_OF_LIBRARY_FILES) {
				continue;
			}

			final File outputFile = createFile(javaResource);
			outputFile.getParentFile().mkdirs();

			final URI xmiFileURI = URI.createFileURI(outputFile.getAbsolutePath()).appendFileExtension("xmi");
			final Resource xmiResource = resourceSet.createResource(xmiFileURI);
			xmiResource.getContents().addAll(javaResource.getContents());
		}

		for (final Resource xmiResource : resourceSet.getResources()) {
			if (xmiResource instanceof XMIResource) {
				try {
					xmiResource.save(resourceSet.getLoadOptions());
				} catch (final IOException e) {
					// Ignore
				}
			}
		}
	}

	private static File createFile(final Resource javaResource) {
		return new File("." + File.separator + "./standalone_output" + File.separator + checkScheme(javaResource));
	}

	private static String checkScheme(final Resource javaResource) {
		StringBuilder outputFileName = null;
		final JavaRoot root = (JavaRoot) javaResource.getContents().get(0);
		final String nameSpace = root.getNamespacesAsString().replace(".", File.separator);

		if (root instanceof CompilationUnit) {
			outputFileName = new StringBuilder(nameSpace + File.separator);
			final CompilationUnit compilationUnit = (CompilationUnit) root;
			if (compilationUnit.getClassifiers().isEmpty()) {
				outputFileName.append(0);
			} else {
				outputFileName.append(compilationUnit.getClassifiers().get(0).getName());
			}
		} else if (root instanceof Package) {
			outputFileName = new StringBuilder(nameSpace).append(File.separator).append("package-info");
			if (outputFileName.toString().startsWith(File.separator)) {
				outputFileName = new StringBuilder(outputFileName.substring(1));
			}
		} else if (root instanceof tools.mdsd.jamopp.model.java.containers.Module) {
			outputFileName = new StringBuilder(nameSpace).append(File.separator).append("module-info");
		}

		if (outputFileName == null) {
			return "";
		}

		return outputFileName.toString();
	}
}
