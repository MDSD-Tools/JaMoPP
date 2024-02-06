package tools.mdsd.jamopp.parser.implementation.converter;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;

import com.google.inject.Inject;
import com.google.inject.Provider;
import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class BindingToAnnotationInstanceConverter implements Converter<IAnnotationBinding, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final UtilNamedElement utilNamedElement;
	private final Provider<JdtResolver> jdtTResolverUtility;
	private final Provider<Converter<IMemberValuePairBinding, AnnotationAttributeSetting>> bindingToAnnotationAttributeSettingConverter;

	@Inject
	public BindingToAnnotationInstanceConverter(final UtilNamedElement utilNamedElement,
			final AnnotationsFactory annotationsFactory, final Provider<JdtResolver> jdtTResolverUtility,
			final Provider<Converter<IMemberValuePairBinding, AnnotationAttributeSetting>> bindingToAnnotationAttributeSettingConverter) {
		this.annotationsFactory = annotationsFactory;
		this.utilNamedElement = utilNamedElement;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationAttributeSettingConverter = bindingToAnnotationAttributeSettingConverter;
	}

	@Override
	public AnnotationInstance convert(final IAnnotationBinding binding) {
		final AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		final Annotation resultClass = jdtTResolverUtility.get().getAnnotation(binding.getAnnotationType());
		utilNamedElement.convertToNameAndSet(binding.getAnnotationType(), resultClass);
		result.setAnnotation(resultClass);
		if (binding.getDeclaredMemberValuePairs().length > 0) {
			final tools.mdsd.jamopp.model.java.annotations.AnnotationParameterList params = annotationsFactory
					.createAnnotationParameterList();
			for (final IMemberValuePairBinding memBind : binding.getDeclaredMemberValuePairs()) {
				params.getSettings().add(bindingToAnnotationAttributeSettingConverter.get().convert(memBind));
			}
			result.setParameter(params);
		}
		return result;
	}

}
