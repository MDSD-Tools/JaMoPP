package org.palladiosimulator.dependencytool.dependencies;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Represent a manifest.mf file.
 */
public class ManifestMF {

    private final Manifest manifest;
    
    /**
     * Create a new {@link ManifestMF} object from a manifest.mf file.
     *
     * @param      stream  The input stream that points to a manifest.mf file.
     */
    public ManifestMF(InputStream stream) throws IOException {
        manifest = new Manifest(stream);
    }
    
    /**
     * Parses the manifest.mf file for names of bundles specified in require-bundle.
     * 
     * @return Names of bundles required in this manifest.mf file.
     */
    public Set<String> getRequiredBundles() {
        Set<String> required = new HashSet<>();
        Attributes attr = manifest.getMainAttributes();
        if (attr.containsKey(new Attributes.Name("Require-Bundle"))) {
            String[] requireBundle = attr.getValue("Require-Bundle").split(",");
            for (String bundle : requireBundle) {
                // workaround to deal with version range
                if (!bundle.contains(")") && !bundle.contains("]")) {
                    if (bundle.contains(";")) {
                        required.add(bundle.substring(0, bundle.indexOf(';')));
                    } else {
                        required.add(bundle);
                    }
                }
            }
        }
        return required;
    }
}
