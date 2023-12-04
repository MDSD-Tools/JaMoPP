package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class BindingToAnnotationAttributeSettingConverter {

	private final AnnotationsFactory annotationsFactory;
	private final UtilJdtResolver jdtTResolverUtility;
	private final ObjectToAnnotationValueConverter objectToAnnotationValueConverter;

	@Inject
	BindingToAnnotationAttributeSettingConverter(ObjectToAnnotationValueConverter objectToAnnotationValueConverter,
			UtilJdtResolver jdtTResolverUtility, AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.objectToAnnotationValueConverter = objectToAnnotationValueConverter;
	}

	org.emftext.language.java.annotations.AnnotationAttributeSetting convertToAnnotationAttributeSetting(
			IMemberValuePairBinding binding) {
		AnnotationAttributeSetting result = annotationsFactory.createAnnotationAttributeSetting();
		result.setAttribute(jdtTResolverUtility.getInterfaceMethod(binding.getMethodBinding()));
		result.setValue(objectToAnnotationValueConverter.convertToAnnotationValue(binding.getValue()));
		return result;
	}

}
