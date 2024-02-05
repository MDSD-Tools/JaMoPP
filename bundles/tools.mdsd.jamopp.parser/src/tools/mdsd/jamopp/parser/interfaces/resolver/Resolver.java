package tools.mdsd.jamopp.parser.interfaces.resolver;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;

public interface Resolver {

	Classifier getClassifier(final ITypeBinding binding);

}
