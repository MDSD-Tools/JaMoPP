package org.palladiosimulator.dependencytool.github;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.github.GHRepository;
import org.palladiosimulator.dependencytool.dependencies.FeatureXMLHandler;
import org.palladiosimulator.dependencytool.dependencies.ManifestMFDependencyHandler;
import org.palladiosimulator.dependencytool.dependencies.P2RepositoryReader;
import org.palladiosimulator.dependencytool.dependencies.UpdateSiteTypes;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a GitHub repository with it's provided and required bundles and features.
 */
@JsonPropertyOrder({"name", "githubUrl", "updatesiteUrl"})
public class RepositoryObject implements Comparable<RepositoryObject> {

    private static final Logger LOGGER = Logger.getLogger(RepositoryObject.class.getName());

    private final GHRepository repository;

    private final Set<String> requiredBundles;
    private final Set<String> requiredFeatures;
    private final Set<String> providedBundles;
    private final Set<String> providedFeatures;

    private String updateSite;

    /**
     * Constructs a new instance.
     *
     * @param      repository      The repository to be analyzed
     * @param      updateSite      The update site url that is used to find the corresponing update site for a repo.
     *                             Provided bundles and features can only be computed for repositories with update sites
     * @param      updateSiteType  The type of update site to use (release or nightly)
     * @param      includeImports  Set to true to consider feature.xml includes while calculating dependencies
     * 
     * @throws IOException if a repository or file of a repository could not be read.
     * @throws ParserConfigurationException indicates an issue with parsing of feature.xml files.
     * @throws SAXException indicates an issue with parsing of feature.xml files.
     */
    public RepositoryObject(GHRepository repository,
                            String updateSite,
                            UpdateSiteTypes updateSiteType,
                            boolean includeImports) throws IOException, ParserConfigurationException, SAXException {
        this.repository = repository;

        this.requiredBundles = new TreeSet<>();
        this.requiredFeatures = new TreeSet<>();
        this.providedBundles = new TreeSet<>();
        this.providedFeatures = new TreeSet<>();

        calculateRequired(includeImports);
        calculateProvided(updateSite, updateSiteType);
    }
    
    /**
     * The full GitHub repository name (including user or organization).
     *
     * @return     The repository name.
     */
    @JsonGetter("name")
    public String getName() {
        return repository.getFullName();
    }

    /**
     * Returns the GitHub repository URL.
     *
     * @return     The the GitHub repository URL.
     */
    @JsonGetter("githubUrl")
    public String getGithubURL() {
        return repository.getHtmlUrl().toString();
    }

    /**
     * Returns the update site URL if one was found using heuristics.
     *
     * @return     The update site or null if none was found.
     */
    @JsonGetter("updatesiteUrl")
    public String getUpdateSite() {
        return updateSite;
    }
    
    @JsonGetter("requiredBundles")
    public Set<String> getRequiredBundles() {
        return requiredBundles;
    }

    @JsonGetter("requiredFeatures")
    public Set<String> getRequiredFeatures() {
        return requiredFeatures;
    }

    @JsonGetter("providedBundles")
    public Set<String> getProvidedBundles() {
        return providedBundles;
    }

    @JsonGetter("providedFeatures")
    public Set<String> getProvidedFeatures() {
        return providedFeatures;
    }
    
    @Override 
    public String toString() {
        return getName();
    }

    private void calculateRequired(boolean includeImports) throws IOException, ParserConfigurationException, SAXException {
        // get required bundles from all bundle Manifest.MF
        ManifestMFDependencyHandler manifestMfHandler = new ManifestMFDependencyHandler(repository);
        requiredBundles.addAll(manifestMfHandler.getRequiredBundles());

        FeatureXMLHandler featureXMLHandler = new FeatureXMLHandler(repository, includeImports);
        requiredBundles.addAll(featureXMLHandler.getRequiredBundles());
        requiredFeatures.addAll(featureXMLHandler.getRequiredFeatures());
    }

    private void calculateProvided(String updateSite, UpdateSiteTypes updateSiteType) throws IOException {
        try (P2RepositoryReader repoReader = new P2RepositoryReader()) {
            String maybeUpdateSiteUrl = updateSite + repository.getName().toLowerCase() + "/" + updateSiteType.toString() + "/";
            providedBundles.addAll(repoReader.readProvidedBundles(maybeUpdateSiteUrl));
            providedFeatures.addAll(repoReader.readProvidedFeatures(maybeUpdateSiteUrl));
            
            if (providedBundles.isEmpty() && providedFeatures.isEmpty()) {
                LOGGER.warning("No update site or provided bundles and features found for "
                               + getName()
                               + " provided bundles and features cannot be determined");
            } else {
                this.updateSite = maybeUpdateSiteUrl;
            }
        }
    }

    @Override
    public int compareTo(RepositoryObject o) {
        return getName().compareTo(o.getName());
    }
}
