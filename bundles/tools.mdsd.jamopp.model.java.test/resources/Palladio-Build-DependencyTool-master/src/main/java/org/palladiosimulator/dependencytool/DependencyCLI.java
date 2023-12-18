package org.palladiosimulator.dependencytool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.palladiosimulator.dependencytool.dependencies.DependencyCalculator;
import org.palladiosimulator.dependencytool.dependencies.UpdateSiteTypes;
import org.palladiosimulator.dependencytool.github.RepositoryObject;
import org.palladiosimulator.dependencytool.graph.GraphicalRepresentation;
import org.palladiosimulator.dependencytool.neo4j.EmbeddedNeo4j;
import org.palladiosimulator.dependencytool.util.OutputType;
import org.palladiosimulator.dependencytool.util.Views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Command Line Interface for the dependency tool.
 */
public final class DependencyCLI {

    private static final Logger LOGGER = Logger.getLogger(DependencyCLI.class.getName());

    /**
     * Main method for CLI.
     *
     * @param args organization and authentication-token are required arguments.
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        final Options options = createOptions();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            LOGGER.warning(e.getMessage());
            printHelp(options);
            return;
        }

        final String updateSiteUrl = cmd.getOptionValue("us");
        final boolean includeImports = cmd.hasOption("ii");
        final boolean jsonOutput = cmd.hasOption("json");
        final boolean includeArchived = cmd.hasOption("ia");
        final boolean includeNoUpdateSite = cmd.hasOption("inus");
        UpdateSiteTypes updateSiteType = UpdateSiteTypes.NIGHTLY;
        final Set<String> reposToIgnore = new HashSet<>();

        final GitHub github;
        try {
            if (cmd.hasOption("at")) {
                final String githubOAuthToken = cmd.getOptionValue("at");
                github = GitHub.connectUsingOAuth(githubOAuthToken);
            } else {
                github = GitHubBuilder.fromEnvironment().build();
            }
        } catch (IOException e) {
            LOGGER.warning("Could not connect to GitHub! Did you set your login data / token?: " + e.getMessage());
            System.exit(1);
            return;
        }

        if (cmd.hasOption("help")) {
            printHelp(options);
            return;
        }
        if (cmd.hasOption("ur")) {
            updateSiteType = UpdateSiteTypes.RELEASE;
        }
        if (cmd.hasOption("ri")) {
            reposToIgnore.addAll(Arrays.asList(cmd.getOptionValue("ri").split(",")));
        }
        if (cmd.hasOption("rif")) {
            try (BufferedReader in = new BufferedReader(new FileReader(cmd.getOptionValue("rif")))) {
                String line = null;
                while ((line = in.readLine()) != null) {
                    reposToIgnore.add(line);
                }
            } catch (final IOException e) {
                LOGGER.warning("Something went wrong while opening the file, please make sure the path is correct. "
                        + e.getMessage());
            }
        }
        final OutputType outputType = OutputType.valueOf(cmd.getOptionValue("o").toUpperCase());

        try {
            Set<GHRepository> repos = repositoriesFromArgs(cmd.getArgList(), github);

            if (cmd.hasOption("rrf")) {
                String requiredFile = cmd.getOptionValue("rrf");

                repos.removeIf(repo -> {
                    try {
                        GHContent content = repo.getFileContent(requiredFile);
                        boolean remove = !content.isFile();
                        if (remove)
                            LOGGER.warning("File " + requiredFile + " missing in " + repo.getName() + ". skipping...");
                        return remove;
                    } catch (IOException e) {
                        LOGGER.warning("IOException while accessing " + requiredFile + " in " + repo.getName() + ". skipping...");
                        return true;
                    }
                });
            }

            final DependencyCalculator dc = new DependencyCalculator(updateSiteUrl, updateSiteType, includeImports, reposToIgnore, includeArchived, includeNoUpdateSite);
            dc.addAll(repos);

            final Map<RepositoryObject, Set<RepositoryObject>> dependencies = dc.getDependencies();
            createOutput(outputType, jsonOutput, dependencies);
        } catch (IOException e) {
            LOGGER.warning("An error occured during calculating the dependencies: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Options createOptions() {
        final Options options = new Options();
        options.addRequiredOption("us", "update-site", true, "The update site to use")
                .addRequiredOption("o", "output", true, "Decide what to output. One of " + Arrays.toString(OutputType.values()) + ".");

        options.addOption("h", "help", false, "Print this message")
                .addOption("at", "oauth", true, "OAuth authentication token for GitHub API. Can be omiited to use the GITHUB_OAUTH environment variable.")
                .addOption("ur", "use-release", false, "Use release update site instead of nightly.")
                .addOption("ii", "include-imports", false, "Consider feature.xml includes while calculating dependencies.")
                .addOption("j", "json", false, "Format the output as json.")
                .addOption("ri", "repository-ignore", true, "Specify one or more repositories which should be ignored when calculating dependencies. Split by an underscore.")
                .addOption("rif", "repository-ignore-file", true, "Path to file with repositories to ignore. Each repository name must be in a new line.")
                .addOption("ia", "include-archived", false, "Include archived repositories into the dependency calculation.")
                .addOption("inus", "include-no-updatesite", false, "Include repositories even if an update site could not be found.")
                .addOption("rrf", "require-repo-file", true, "Filter repositories that do not have the file specified by `<arg>`");

        return options;
    }

    private static void createOutput(OutputType outputType, boolean jsonOutput,
            Map<RepositoryObject, Set<RepositoryObject>> dependencies) throws JsonProcessingException {

        final ObjectMapper objectMapper = new ObjectMapper();
        final ObjectWriter objectWriter = objectMapper.writer();

        switch (outputType) {
            case NEO4J:
                try (EmbeddedNeo4j neo4j = new EmbeddedNeo4j()) {
                    neo4j.commit(dependencies);
                }
                break;
            case TOPOLOGY:
                final GraphicalRepresentation graphRep = new GraphicalRepresentation(dependencies);
                graphRep.createTopologyHierarchy();
                final List<Set<RepositoryObject>> topology = graphRep.getTopologyHierachy();

                if (jsonOutput) {
                    System.out.println(objectWriter.withView(Views.Topology.class).writeValueAsString(topology));
                } else {
                    System.out.println(topology.toString().replaceAll("],", "],\n"));
                }
                break;
            case REPOSITORIES:
                if (jsonOutput) {
                    System.out.println(objectWriter.withView(Views.Repository.class).writeValueAsString(dependencies.keySet()));
                } else {
                    final StringBuilder reposString = new StringBuilder();
                    for (final RepositoryObject repo : dependencies.keySet()) {
                        reposString.append("Name: ").append(repo.getName()).append("\n");
                        reposString.append("Address: ").append(repo.getGithubURL()).append("\n");
                        reposString.append("UpdateSite: ").append(repo.getUpdateSite()).append("\n");
                        reposString.append("Dependencies: ").append(dependencies.get(repo)).append("\n");
                        reposString.append("ProvidedFeatures: ").append(repo.getProvidedFeatures()).append("\n");
                        reposString.append("ProvidedBundles: ").append(repo.getProvidedBundles()).append("\n");
                        reposString.append("RequiredFeatures: ").append(repo.getRequiredFeatures()).append("\n");
                        reposString.append("RequiredBundles: ").append(repo.getRequiredBundles()).append("\n");
                        reposString.append("\n");
                    }
                    System.out.println(reposString.toString());
                }
                break;
            case DEPENDENCIES:
                Map<String, List<String>> stringDependencies = new HashMap<>();
                for (final Map.Entry<RepositoryObject, Set<RepositoryObject>> entry : dependencies.entrySet()) {
                    final String repoName = entry.getKey().getName();
                    final List<String> repoDependencies = entry.getValue().stream().map(RepositoryObject::getName).collect(Collectors.toList()); 
                    stringDependencies.put(repoName, repoDependencies);
                }

                if (jsonOutput) {
                    System.out.println(objectWriter.withView(Views.Dependency.class).writeValueAsString(stringDependencies));
                } else {
                    final StringBuilder dependencyString = new StringBuilder();
                    for (final Map.Entry<String, List<String>> entry : stringDependencies.entrySet()) {
                        dependencyString.append(entry.getKey()).append(":").append("\n");
                        for (String dependency : entry.getValue()) {
                            dependencyString.append("  ").append(dependency).append("\n");
                        }
                        dependencyString.append("\n");
                    }
                    System.out.println(dependencyString.toString().trim());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown output type: " + outputType);
        }
    }

    private static void printHelp(final Options options) {
        new HelpFormatter().printHelp("java -jar dependencytool.jar [flags] [<org> <user/repo> ...]", options);
    }

    private static Set<GHRepository> repositoriesFromArgs(final List<String> args, final GitHub github) throws IOException {
        final HashSet<GHRepository> githubRepos = new HashSet<>();
        for (String repoOrOrganization : args) {
            final boolean isOrganization = !repoOrOrganization.contains("/");
            if (isOrganization) {
                githubRepos.addAll(github.getOrganization(repoOrOrganization).getRepositories().values());
            } else {
                githubRepos.add(github.getRepository(repoOrOrganization));
            }
        }
        return githubRepos;
    }

    /**
     * Private constructor to avoid object generation.
     */
    private DependencyCLI() {
        throw new IllegalStateException("Utility-class constructor.");
    }

}
