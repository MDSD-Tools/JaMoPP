package tools.mdsd.jamopp.parser.interfaces.converter;

import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;

public interface ToConcreteClassifierConverterWithExtraInfo {

	ConcreteClassifier convert(ITypeBinding binding, boolean extractAdditionalInformation);

}
