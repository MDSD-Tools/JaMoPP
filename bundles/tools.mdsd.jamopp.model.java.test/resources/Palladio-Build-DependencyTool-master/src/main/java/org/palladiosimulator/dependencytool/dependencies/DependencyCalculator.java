package org.palladiosimulator.dependencytool.dependencies;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.github.GHRepository;
import org.palladiosimulator.dependencytool.github.RepositoryObject;
import org.xml.sax.SAXException;

/**
 *  Computes dependencies between GitHub repositories by using heuristics to guess the provided and required
 *  features and bundles of a repository.
 *  
 *  @see RepositoryObject
 */
public class DependencyCalculator {

    private static final Logger LOGGER = Logger.getLogger(DependencyCalculator.class.getName());
    
    private final UpdateSiteTypes updateSiteType;
    private final String updateSiteUrl;
    private final boolean includeImports;
    private final Set<String> reposToIgnore;
    private final boolean includeArchived;
    private final boolean includeNoUpdateSite;
    
    private final Set<RepositoryObject> repositories;

    /**
     * Constructs a new instance.
     *
     * @param      updateSiteUrl        The update site url that is used to find the corresponing update site for a repo.
     *                                  Provided bundles and features can only be computed for repositories with update sites
     * @param      type                 The type of update site to use (release or nightly)
     * @param      includeImports       Set to true to consider feature.xml includes while calculating dependencies
     * @param      reposToIgnore        A set of repository names that should be ignored
     * @param      includeArchived      Set to true to include repositories that were archived
     * @param      includeNoUpdateSite  Set to true to include repositories for which no update site could be determined
     * 
     * @throws IOException if a repository or a file of a repository could not be read.
     */
    public DependencyCalculator(final String updateSiteUrl,
                                final UpdateSiteTypes type,
                                final boolean includeImports,
                                final Set<String> reposToIgnore,
                                final boolean includeArchived,
                                final boolean includeNoUpdateSite) throws IOException {
        this.repositories = new HashSet<>();
        this.updateSiteUrl = updateSiteUrl;
        this.updateSiteType = type;
        this.includeImports = includeImports;
        this.reposToIgnore = reposToIgnore;
        this.includeArchived = includeArchived;
        this.includeNoUpdateSite = includeNoUpdateSite;
    }

    /**
     * Add a GitHub repository to the dependency calculation.
     *
     * @param      repository  The GitHub repository to add
     */
    public void add(GHRepository repository) {
        addAll(List.of(repository));
    }

    /**
     * Add all GitHub repositories to the dependency calculation.
     *
     * @param      repositories  A collection of GitHub repositories
     */
    public void addAll(Collection<GHRepository> repositories) {
        ExecutorService ex = Executors.newFixedThreadPool(128);

        this.repositories.addAll(repositories
            .parallelStream()
            .filter(e -> !reposToIgnore.contains(e.getName()) && !reposToIgnore.contains(e.getFullName()))
            .filter(e -> includeArchived || !e.isArchived())
            .sequential()
            .map(e -> ex.submit(() -> {
                try {
                    return new RepositoryObject(e, updateSiteUrl, updateSiteType, includeImports);
                } catch (IOException | ParserConfigurationException | SAXException exception) {
                    throw new RuntimeException(exception);
                }
            }))
            .parallel()
            .map(e -> {
                try {
                    return e.get();
                } catch (InterruptedException | ExecutionException e1) {
                    LOGGER.warning("Exception while waiting for future to complete");
                    return null;
                }
            })
            .filter(e -> e != null)
            .filter(e -> {
                boolean keep = includeNoUpdateSite || e.getUpdateSite() != null;
                if (!keep)
                    LOGGER.warning("No updatesite found for " + e.getName() + ". skipping...");
                return keep;
            })
            .sorted()
            .collect(Collectors.toSet()));

        ex.shutdown();
        try {
            ex.awaitTermination(15, TimeUnit.MINUTES);
        } catch (InterruptedException e1) {
            LOGGER.warning("Interrupted while waiting for executor termination");
        }
    }
    
    /**
     * Returns the repository dependencies as map.
     * 
     * The repository in the key depends on every repository in its value.
     *
     * @return     A map representing the dependencies between repositories.
     */
    public Map<RepositoryObject, Set<RepositoryObject>> getDependencies() {
        final Map<String, RepositoryObject> providedBundleToRepo = reverseProvidedMap(RepositoryObject::getProvidedBundles);
        final Map<String, RepositoryObject> providedFeatureToRepo = reverseProvidedMap(RepositoryObject::getProvidedFeatures);

        final Map<RepositoryObject, Set<RepositoryObject>> dependencies = new HashMap<>();
        for (RepositoryObject repo : repositories) {
            final Set<RepositoryObject> repoDependencies = new HashSet<>();
            repoDependencies.addAll(resolveDependencies(repo.getRequiredBundles(), providedBundleToRepo));
            repoDependencies.addAll(resolveDependencies(repo.getRequiredFeatures(), providedFeatureToRepo));

            // loops not allowed by jgrapht
            repoDependencies.remove(repo);
            dependencies.put(repo, repoDependencies);
        }

        return dependencies;
    }

    private Set<RepositoryObject> resolveDependencies(Set<String> required, Map<String, RepositoryObject> providedToRepo) {
        final Set<RepositoryObject> repoDependencies = new HashSet<>();

        for (String dependency : required) {
            if (providedToRepo.containsKey(dependency)) {
                repoDependencies.add(providedToRepo.get(dependency));
            } else {
                LOGGER.warning(dependency + "is not provided by any repository.");
            }
        }

        return repoDependencies;
    }

    private Map<String, RepositoryObject> reverseProvidedMap(Function<RepositoryObject, Set<String>> map) {
        final Map<String, Set<RepositoryObject>> reverseMapWithDuplicates = new HashMap<>();
        final Map<String, RepositoryObject> reverseMap = new HashMap<>();

        for (RepositoryObject repo : repositories) {
            Set<String> reverseKeys = map.apply(repo);
            for (String reverseKey : reverseKeys) {

                Set<RepositoryObject> duplicates = reverseMapWithDuplicates.getOrDefault(reverseKey, new TreeSet<>());
                duplicates.add(repo);
                reverseMapWithDuplicates.put(reverseKey, duplicates);
            }
        }

        // TODO: Do not use the first in Alphabet but use the one that mimimizes the topology three depth
        for (var entry : reverseMapWithDuplicates.entrySet()) {
            reverseMap.put(entry.getKey(), entry.getValue().iterator().next());
        }


        // Make sure every bundle/feature/.. is provided by exactly one repository.
        for (Map.Entry<String, Set<RepositoryObject>> entry : reverseMapWithDuplicates.entrySet()) {
            if (entry.getValue().size() > 1) {
                List<String> repoNames = entry.getValue().stream().map(RepositoryObject::getName).collect(Collectors.toList());
                LOGGER.warning(entry.getKey()  + " is provided by multiple repositories: " + repoNames + " using " + reverseMap.get(entry.getKey()) + ".");
            }
        }

        return reverseMap;
    }
}

