package org.palladiosimulator.dependencytool.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import java.util.Set;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.palladiosimulator.dependencytool.github.RepositoryObject;

/**
 * Holds a graph object constructed out of dependencies given in repository Objects.
 */
public class GraphicalRepresentation {

    private static final Logger LOGGER = Logger.getLogger(GraphicalRepresentation.class.getName());
    private SimpleDirectedGraph<RepositoryObject, CustomEdge> graph;
    private List<Set<RepositoryObject>> topologyHierarchy;
        
    
    /**
     * Creates a new GraphicalRepresentation object. Creates a new simple directed graph adding all repository names as vertices and all dependencies as edges.
     * 
     * @param dependencies A set containing RepositoryObjects.
     */
    public GraphicalRepresentation(Map<RepositoryObject, Set<RepositoryObject>> dependencies) {
        graph = new SimpleDirectedGraph<>(CustomEdge.class);
        topologyHierarchy = new ArrayList<>();
        
        // Add all repositories as vertices.
        dependencies.keySet().forEach(a -> graph.addVertex(a));
        
        // Add an edge from this repository to all repositories it depends on. 
        for (Map.Entry<RepositoryObject, Set<RepositoryObject>> repoDependencies : dependencies.entrySet()) {
            for (RepositoryObject dependency : repoDependencies.getValue()) {
                graph.addEdge(repoDependencies.getKey(), dependency);
            }
        }
    }
    
    public SimpleDirectedGraph<RepositoryObject, CustomEdge> getGraph() {
        return graph;
    }
    
    public List<Set<RepositoryObject>> getTopologyHierachy() {
        return topologyHierarchy;
    }

    /**
     * Check if the graph has cycles. If showCycles is true then print the detected cycles.
     * 
     * @param showCycles If true print detected cycles.
     * @return true if there are cycles, false otherwise.
     */
    public boolean hasCycles(boolean showCycles) {
        CycleDetector<RepositoryObject, CustomEdge> cycleDetector = new CycleDetector<>(graph);
        boolean hasCycles = cycleDetector.detectCycles();
        
        if (hasCycles) {
            if (showCycles) {
                Set<RepositoryObject> cycleVertices = cycleDetector.findCycles();
                for (RepositoryObject vertex : cycleVertices) {
                    String cycleInfo = vertex + "is part of the following cycle(s): " + cycleDetector.findCyclesContainingVertex(vertex);
                    LOGGER.info(cycleInfo);
                }
            }
            LOGGER.warning("There is no topological order since there are cyclic dependencies in the graph. Please resolve them and try again.");
        }
        return hasCycles;
    }

    /**
     * Creates topology layers of elements which are interchangeable in the topology of this graph. 
     */
    public void createTopologyHierarchy() {
        DirectedAcyclicGraph<RepositoryObject, DefaultEdge> dag = simpleToDirected(graph);
        boolean isEmpty = false;
        int size = dag.vertexSet().size();
        for (int i = 0; i < size; i++) {
            isEmpty = dag.vertexSet().isEmpty();
            if (isEmpty) {
                break;
            }
            topologyHierarchy.add(removeTopologyLayer(dag));
        }
        
    }
    
    // assumes A -> B means A depends on B
    private Set<RepositoryObject> removeTopologyLayer(DirectedAcyclicGraph<RepositoryObject, DefaultEdge> dag) {
        Set<RepositoryObject> layer = new HashSet<>();
        for (RepositoryObject vertex : dag.vertexSet()) {
            if (dag.outgoingEdgesOf(vertex).isEmpty()) {
                layer.add(vertex);
            }
        }
        layer.forEach(dag::removeVertex);
        return layer;
    }
    
    // converts a simple directed graph to a directed acyclic graph
    private DirectedAcyclicGraph<RepositoryObject, DefaultEdge> simpleToDirected(SimpleDirectedGraph<RepositoryObject, CustomEdge> simple) {
        DirectedAcyclicGraph<RepositoryObject, DefaultEdge> dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
        simple.vertexSet().forEach(dag::addVertex);
        simple.edgeSet().forEach(edge -> dag.addEdge((RepositoryObject) edge.getSource(), (RepositoryObject) edge.getTarget()));
        return dag;
    }
}
