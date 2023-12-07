package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.annotations.AnnotationsFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;

public class BindingToAnnotationAttributeSettingConverter
		implements Converter<IMemberValuePairBinding, AnnotationAttributeSetting> {

	private final AnnotationsFactory annotationsFactory;
	private final IUtilJdtResolver jdtTResolverUtility;
	private final Converter<Object, AnnotationValue> objectToAnnotationValueConverter;

	@Inject
	BindingToAnnotationAttributeSettingConverter(Converter<Object, AnnotationValue> objectToAnnotationValueConverter,
			IUtilJdtResolver jdtTResolverUtility, AnnotationsFactory annotationsFactory) {
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
