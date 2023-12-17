package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;

public interface UtilBindingInfoToConcreteClassifierConverter {

	public ConcreteClassifier convertToConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation);

}
