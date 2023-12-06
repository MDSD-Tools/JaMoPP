package jamopp.parser.jdt.converter.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.ConcreteClassifier;

public interface IUtilBindingInfoToConcreteClassifierConverter {

	public ConcreteClassifier convertToConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation);

}
