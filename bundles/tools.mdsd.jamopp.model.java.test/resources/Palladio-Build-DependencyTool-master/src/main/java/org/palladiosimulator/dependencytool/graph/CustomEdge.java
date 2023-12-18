package org.palladiosimulator.dependencytool.graph;

import org.jgrapht.graph.DefaultEdge;

/**
 * Extension of DefaultEdge to get public access to getters.
 */
public class CustomEdge extends DefaultEdge {

    private static final long serialVersionUID = -1519449485682906990L;

    @Override
    public Object getSource() {
        return super.getSource();
    }
    
    @Override
    public Object getTarget() {
        return super.getTarget();
    }
}
