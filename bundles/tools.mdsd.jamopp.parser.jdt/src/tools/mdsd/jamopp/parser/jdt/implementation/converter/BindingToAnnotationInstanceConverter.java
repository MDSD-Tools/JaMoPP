package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class BindingToAnnotationInstanceConverter implements Converter<IAnnotationBinding, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final UtilNamedElement utilNamedElement;
	private JdtResolver jdtTResolverUtility;
	private Converter<IMemberValuePairBinding, AnnotationAttributeSetting> bindingToAnnotationAttributeSettingConverter;

	@Inject
	BindingToAnnotationInstanceConverter(UtilNamedElement utilNamedElement, AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.utilNamedElement = utilNamedElement;
	}

	public AnnotationInstance convert(IAnnotationBinding binding) {
		AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		Annotation resultClass = jdtTResolverUtility.getAnnotation(binding.getAnnotationType());
		utilNamedElement.convertToNameAndSet(binding.getAnnotationType(), resultClass);
		result.setAnnotation(resultClass);
		if (binding.getDeclaredMemberValuePairs().length > 0) {
			tools.mdsd.jamopp.model.java.annotations.AnnotationParameterList params = annotationsFactory
					.createAnnotationParameterList();
			for (IMemberValuePairBinding memBind : binding.getDeclaredMemberValuePairs()) {
				params.getSettings().add(bindingToAnnotationAttributeSettingConverter.convert(memBind));
			}
			result.setParameter(params);
		}
		return result;
	}

	@Inject
	public void setBindingToAnnotationAttributeSettingConverter(
			Converter<IMemberValuePairBinding, AnnotationAttributeSetting> bindingToAnnotationAttributeSettingConverter) {
		this.bindingToAnnotationAttributeSettingConverter = bindingToAnnotationAttributeSettingConverter;
	}

	@Inject
	public void setJdtTResolverUtility(JdtResolver jdtTResolverUtility) {
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

}
