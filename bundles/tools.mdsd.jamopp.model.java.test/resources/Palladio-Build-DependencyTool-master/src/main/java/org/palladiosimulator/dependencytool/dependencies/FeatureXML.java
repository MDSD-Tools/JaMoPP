package org.palladiosimulator.dependencytool.dependencies;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents a feature.xml file.
 */
public class FeatureXML {

    private static final String FEATURE_TAG = "feature";
    private static final String PLUGIN_NODE_NAME = "plugin";
    private static final String ID_TIME = "id";
    
    private final Set<String> featureSet = new HashSet<>();
    private final Set<String> bundleSet = new HashSet<>();
    
    /**
     * Create a new feature.xml object from a feature.xml file.
     * 
     * @param doc The content of the feature.xml file.
     * @param includeImports Additionally parse imports if true.
     */
    public FeatureXML(Document doc, boolean includeImports) {
        NodeList nList = doc.getElementsByTagName(FEATURE_TAG).item(0).getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Boolean isNode = nNode.getNodeType() == Node.ELEMENT_NODE;
            // if includeImports is true: check for plugin and feature imports
            if (includeImports && isNode && nNode.getNodeName().equals("requires")) {
                parseImports(nNode);
            }
            // check for sub feature or plugin definitions
            if (isNode && nNode.getNodeName().equals(FEATURE_TAG)) {
                featureSet.add(nNode.getAttributes().getNamedItem(ID_TIME).getTextContent());
            } else if (isNode && nNode.getNodeName().equals(PLUGIN_NODE_NAME)) {
                bundleSet.add(nNode.getAttributes().getNamedItem(ID_TIME).getTextContent());
            }
            // check for additionally includes features
            if (isNode && nNode.getNodeName().equals("includes")) {
                parseIncludes(nNode);
            }
        }
    }
    
    /**
     * Returns the features that are required by this feature.xml.
     *
     * @return     The required features.
     */
    public Set<String> getRequiredFeatures() {
        return featureSet;
    }
    
    /**
     * Returns the bundles that are required by this feature.xml.
     *
     * @return     The required bundles.
     */
    public Set<String> getRequiredBundles() {
        return bundleSet;
    }
    
    // Parse feature and plugin imports and add them to respective set.
    private void parseImports(Node nNode) {
        for (int j = 0; j < nNode.getChildNodes().getLength(); j++) {
            Node childNode = nNode.getChildNodes().item(j);
            if (childNode.getNodeName().equals("import")) {
                for (int k = 0; k < childNode.getAttributes().getLength(); k++) {
                    Node attr = childNode.getAttributes().item(k);
                    if (attr.getNodeName().equals(FEATURE_TAG)) {
                        featureSet.add(attr.getTextContent());
                    } else if (attr.getNodeName().equals(PLUGIN_NODE_NAME)) {
                        bundleSet.add(attr.getTextContent());
                    }
                }
            }
        }
    }
    
    // Parse includes and add them to featureSet.
    private void parseIncludes(Node nNode) {
        for (int i = 0; i < nNode.getAttributes().getLength(); i++) {
            Node attr = nNode.getAttributes().item(i);
            if (attr.getNodeName().equals(ID_TIME)) {
                featureSet.add(attr.getTextContent());
            }
        }
    }
}
