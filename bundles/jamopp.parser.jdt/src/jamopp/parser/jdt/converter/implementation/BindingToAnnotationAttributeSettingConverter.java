package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.annotations.AnnotationsFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class BindingToAnnotationAttributeSettingConverter
		implements ToConverter<IMemberValuePairBinding, AnnotationAttributeSetting> {

	private final AnnotationsFactory annotationsFactory;
	private final UtilJdtResolver jdtTResolverUtility;
	private final ToConverter<Object, AnnotationValue> objectToAnnotationValueConverter;

	@Inject
	BindingToAnnotationAttributeSettingConverter(ToConverter<Object, AnnotationValue> objectToAnnotationValueConverter,
			UtilJdtResolver jdtTResolverUtility, AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.objectToAnnotationValueConverter = objectToAnnotationValueConverter;
	}

	@Override
	public AnnotationAttributeSetting convert(IMemberValuePairBinding binding) {
		AnnotationAttributeSetting result = annotationsFactory.createAnnotationAttributeSetting();
		result.setAttribute(jdtTResolverUtility.getInterfaceMethod(binding.getMethodBinding()));
		result.setValue(objectToAnnotationValueConverter.convert(binding.getValue()));
		return result;
	}

}
