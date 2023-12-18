package org.palladiosimulator.dependencytool.dependencies;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Calculate required features and bundles for a GitHub repository that are defined by a feature.xml file.
 */
public class FeatureXMLHandler {
    private static final Logger LOGGER = Logger.getLogger(FeatureXMLHandler.class.getName());
    
    private final GHRepository repository;
    private final boolean includeImports;
    private final Set<String> requiredBundles = new HashSet<>();
    private final Set<String> requiredFeatures = new HashSet<>();
    
    /**
     * Constructs a new instance.
     *
     * @param      repository      The repository to be analyzed
     * @param      includeImports  Indicates if the imports in the feature.xml should be included in the calculation
     */
    public FeatureXMLHandler(GHRepository repository, boolean includeImports) throws IOException, ParserConfigurationException, SAXException {
        this.repository = repository;
        this.includeImports = includeImports;

        calculateDependencies();
    }

    /**
     * Returns the bundles that are required by this feature.xml.
     *
     * @return     The required bundles.
     */
    public Set<String> getRequiredBundles() {
        return requiredBundles;
    }

    /**
     * Returns the features that are required by this feature.xml.
     *
     * @return     The required features.
     */
    public Set<String> getRequiredFeatures() {
        return requiredFeatures;
    }

    private void calculateDependencies() throws IOException, ParserConfigurationException, SAXException {
        // get required bundles and features from all Feature.xml
        Set<String> featureXMLs = new HashSet<>();
        for (String feature : getFeatures()) {
            featureXMLs.add("/features/" + feature + "/feature.xml");
        }
        for (String featureXML : featureXMLs) {
            Optional<GHContent> featureContent = getFileContent(featureXML);
            if (featureContent.isPresent()) {
                Document featureDoc = getDocumentFromStream(featureContent.get().read());
                FeatureXML feature = new FeatureXML(featureDoc, includeImports);
                requiredBundles.addAll(feature.getRequiredBundles());
                requiredFeatures.addAll(feature.getRequiredFeatures());
            }
        }
    }

    // Returns a set of strings, containing all names of features present for the given repository name.
    private Set<String> getFeatures() {
        Set<String> features = new HashSet<>();
        try {
            for (GHContent feature : repository.getDirectoryContent("features")) {
                if (feature.isDirectory()) {
                    features.add(feature.getName());
                }
            }
        } catch (IOException e) {
            LOGGER.warning("No features directory found in " + repository.getFullName() + ".");
        }
        return features;
    }

    private Document getDocumentFromStream(InputStream content) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(content);
        document.getDocumentElement().normalize();
        return document;
    }

    // Fetches file content from a given file in a given repository.
    private Optional<GHContent> getFileContent(String filePath) {
        Optional<GHContent> content = Optional.empty();
        try {
            content = Optional.of(repository.getFileContent(filePath));
        } catch (IOException e) {
            LOGGER.warning("File " + filePath + " not found in " + repository.getFullName() + ".");
        }
        return content;
    }
}
