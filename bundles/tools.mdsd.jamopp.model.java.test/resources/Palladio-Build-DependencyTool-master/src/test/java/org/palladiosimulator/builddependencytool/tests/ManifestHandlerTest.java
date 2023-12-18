package org.palladiosimulator.builddependencytool.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.kohsuke.github.GitHub;
import org.palladiosimulator.dependencytool.dependencies.ManifestMFDependencyHandler;

public class ManifestHandlerTest {
    
    @Test
    public void test() throws IOException {
        ManifestMFDependencyHandler handler = new ManifestMFDependencyHandler(GitHub.connectAnonymously().getRepository("PalladioSimulator/Palladio-Core-PCM"));
        Set<String> dependencies = handler.getRequiredBundles();
        assertTrue(dependencies.contains("org.eclipse.core.runtime"));
        assertTrue(dependencies.contains("org.eclipse.emf.ecore"));
        assertTrue(dependencies.contains("org.eclipse.ocl"));
        assertTrue(dependencies.contains("org.eclipse.ocl.ecore"));
        assertTrue(dependencies.contains("org.eclipse.emf.cdo"));
        assertTrue(dependencies.contains("org.eclipse.emf.ecore.xmi"));
        assertTrue(dependencies.contains("de.uka.ipd.sdq.identifier"));
        assertTrue(dependencies.contains("de.uka.ipd.sdq.probfunction"));
        assertTrue(dependencies.contains("de.uka.ipd.sdq.stoex"));
        assertTrue(dependencies.contains("de.uka.ipd.sdq.units"));
        assertTrue(dependencies.contains("de.uka.ipd.sdq.errorhandling"));
        assertTrue(dependencies.contains("org.eclipse.ui"));
        assertTrue(dependencies.contains("org.eclipse.ui.console"));
        assertTrue(dependencies.contains("org.eclipse.debug.ui"));
        assertTrue(dependencies.contains("org.eclipse.emf.edit"));
        assertTrue(dependencies.contains("org.eclipse.emf"));
        assertTrue(dependencies.contains("org.palladiosimulator.pcm"));
        assertTrue(dependencies.contains("org.eclipse.emf.edit.ui"));
        assertTrue(dependencies.contains("org.eclipse.ui.forms"));
        assertTrue(dependencies.contains("org.eclipse.ui.ide"));
        assertTrue(dependencies.contains("org.eclipse.sirius.ui"));
        assertTrue(dependencies.contains("org.eclipse.ui.workbench"));
        assertTrue(dependencies.contains("org.palladiosimulator.pcm.workflow"));
    }

}
