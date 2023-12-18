package org.palladiosimulator.dependencytool.dependencies;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Calculate required features and bundles for a GitHub repository that are defined by a manifest.mf file.
 */
public class ManifestMFDependencyHandler {

    private static final Logger LOGGER = Logger.getLogger(ManifestMFDependencyHandler.class.getName());
    
    private final GHRepository repository;
    
    /**
     * Constructs a new instance.
     *
     * @param      repository  The repository to be analyzed
     */
    public ManifestMFDependencyHandler(GHRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Parses dependencies for all given bundles.
     * 
     * @return A set of all dependencies from manifest files.
     */
    public Set<String> getRequiredBundles() {
        Set<String> dependencies = new HashSet<>();
        for (String bundle : getBundles()) {
            Optional<ManifestMF> manifest = Optional.empty();
            final String manifestPath = "bundles/" + bundle + "/META-INF/MANIFEST.MF";
            try {
                GHContent ghManifest = repository.getFileContent(manifestPath);
                manifest = Optional.of(new ManifestMF(ghManifest.read()));
            } catch (IOException e) {
                LOGGER.warning("No Manifest.MF found at " + manifestPath + " in " + repository.getFullName());
            }
            if (manifest.isPresent()) {
                dependencies.addAll(manifest.get().getRequiredBundles());
            }
        }
        return dependencies;
    }

    // Returns a set of strings, containing all names of bundles present for the given repository name.
    private Set<String> getBundles() {
        Set<String> bundles = new HashSet<>();
        try {
            for (GHContent bundle : repository.getDirectoryContent("bundles")) {
                if (bundle.isDirectory()) {
                    bundles.add(bundle.getName());
                }
            }
        } catch (IOException e) {
            LOGGER.warning("No bundles page found for " + repository.getFullName() + ".");
        }
        return bundles;
    }
}
