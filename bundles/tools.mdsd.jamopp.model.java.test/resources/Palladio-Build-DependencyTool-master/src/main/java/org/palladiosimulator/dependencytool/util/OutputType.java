package org.palladiosimulator.dependencytool.util;

/**
 * Defindes the type of query.
 */
public enum OutputType {
    /** Output a representation of all repositories that were included in the dependency caluculation. */
    REPOSITORIES,
    /** Output a representation of the topology that is defined by the dependencies. */
    TOPOLOGY,
    /** Output a representation of the dependencies between repositories. */
    DEPENDENCIES,
    /** Output the dependency graph as a Neo4J database. */
    NEO4J
}
