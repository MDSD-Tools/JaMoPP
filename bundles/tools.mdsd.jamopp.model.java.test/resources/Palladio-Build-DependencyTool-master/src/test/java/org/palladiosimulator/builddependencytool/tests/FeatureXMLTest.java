package org.palladiosimulator.builddependencytool.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.palladiosimulator.dependencytool.dependencies.FeatureXML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FeatureXMLTest {

    @Test
    public void testPlugins() throws ParserConfigurationException, SAXException, IOException {
        URL featureURL = getClass().getResource("/pcm_feature.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(featureURL.openStream());
        document.getDocumentElement().normalize();
        FeatureXML testFeatureXML = new FeatureXML(document, false);
        Set<String> testFeatures = testFeatureXML.getRequiredFeatures();
        Set<String> testPlugins = testFeatureXML.getRequiredBundles();
        assertTrue(testFeatures.size() == 0);
        assertTrue(testPlugins.size() == 6);
        assertTrue(testPlugins.contains("org.palladiosimulator.pcm.resources"));
        assertTrue(testPlugins.contains("org.palladiosimulator.pcm"));
        assertTrue(testPlugins.contains("de.uka.ipd.sdq.pcm.stochasticexpressions"));
        assertTrue(testPlugins.contains("de.uka.ipd.sdq.stoex.analyser"));
        assertTrue(testPlugins.contains("org.palladiosimulator.pcm.ui"));
        assertTrue(testPlugins.contains("org.palladiosimulator.pcm.help"));
    }
    
    @Test
    public void testFeatures() throws ParserConfigurationException, SAXException, IOException {
        URL featureURL = getClass().getResource("/core-commons_feature.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(featureURL.openStream());
        document.getDocumentElement().normalize();
        FeatureXML testFeatureXML = new FeatureXML(document, false);
        Set<String> testFeatures = testFeatureXML.getRequiredFeatures();
        Set<String> testPlugins = testFeatureXML.getRequiredBundles();
        assertTrue(testFeatures.size() == 7);
        assertTrue(testPlugins.size() == 1);
        assertTrue(testPlugins.contains("org.palladiosimulator.commons"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.dialogs.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.errorhandling.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.identifier.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.stoex.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.units.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.statistics.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.probfunction.feature"));
    }
    
    @Test
    public void testIncludeTrue() throws ParserConfigurationException, SAXException, IOException {
        URL featureURL = getClass().getResource("/core-commons_feature.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(featureURL.openStream());
        document.getDocumentElement().normalize();
        FeatureXML testFeatureXML = new FeatureXML(document, true);
        Set<String> testFeatures = testFeatureXML.getRequiredFeatures();
        Set<String> testPlugins = testFeatureXML.getRequiredBundles();
        assertTrue(testFeatures.size() == 7);
        assertTrue(testPlugins.size() == 7);
        
        assertTrue(testPlugins.contains("org.eclipse.emf.ecore"));
        assertTrue(testPlugins.contains("org.eclipse.emf.edit"));
        assertTrue(testPlugins.contains("org.eclipse.emf.edit.ui"));
        assertTrue(testPlugins.contains("org.eclipse.core.runtime"));
        assertTrue(testPlugins.contains("org.eclipse.core.resources"));
        assertTrue(testPlugins.contains("org.palladiosimulator.branding"));
        
        assertTrue(testPlugins.contains("org.palladiosimulator.commons"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.dialogs.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.errorhandling.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.identifier.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.stoex.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.units.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.statistics.feature"));
        assertTrue(testFeatures.contains("de.uka.ipd.sdq.probfunction.feature"));
    }

}
