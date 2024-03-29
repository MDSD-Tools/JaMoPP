package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.IMemberValuePairBinding;

import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class BindingToAnnotationAttributeSettingConverter
		implements Converter<IMemberValuePairBinding, AnnotationAttributeSetting> {

	private final AnnotationsFactory annotationsFactory;
	private final JdtResolver jdtTResolverUtility;
	private final Converter<Object, AnnotationValue> objectToAnnotationValueConverter;

	@Inject
	public BindingToAnnotationAttributeSettingConverter(
			final Converter<Object, AnnotationValue> objectToAnnotationValueConverter,
			final JdtResolver jdtTResolverUtility, final AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.objectToAnnotationValueConverter = objectToAnnotationValueConverter;
	}

	@Override
	public AnnotationAttributeSetting convert(final IMemberValuePairBinding binding) {
		final AnnotationAttributeSetting result = annotationsFactory.createAnnotationAttributeSetting();
		result.setAttribute(jdtTResolverUtility.getInterfaceMethod(binding.getMethodBinding()));
		result.setValue(objectToAnnotationValueConverter.convert(binding.getValue()));
		return result;
	}

}
