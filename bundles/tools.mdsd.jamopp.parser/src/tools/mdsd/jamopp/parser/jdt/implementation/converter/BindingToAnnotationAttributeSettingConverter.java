package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMemberValuePairBinding;

import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class BindingToAnnotationAttributeSettingConverter
		implements Converter<IMemberValuePairBinding, AnnotationAttributeSetting> {

	private final AnnotationsFactory annotationsFactory;
	private final JdtResolver jdtTResolverUtility;
	private final Converter<Object, AnnotationValue> objectToAnnotationValueConverter;

	@Inject
	public BindingToAnnotationAttributeSettingConverter(
			Converter<Object, AnnotationValue> objectToAnnotationValueConverter, JdtResolver jdtTResolverUtility,
			AnnotationsFactory annotationsFactory) {
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
