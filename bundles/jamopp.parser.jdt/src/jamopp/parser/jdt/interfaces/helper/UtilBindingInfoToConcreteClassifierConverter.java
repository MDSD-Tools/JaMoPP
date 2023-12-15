package jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.ConcreteClassifier;

public interface UtilBindingInfoToConcreteClassifierConverter {

	public ConcreteClassifier convertToConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation);

}