package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.classifiers.Annotation;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;

public class BindingToAnnotationInstanceConverter implements Converter<IAnnotationBinding, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final IUtilNamedElement utilNamedElement;
	private IUtilJdtResolver jdtTResolverUtility;
	private Converter<IMemberValuePairBinding, AnnotationAttributeSetting> bindingToAnnotationAttributeSettingConverter;

	@Inject
	BindingToAnnotationInstanceConverter(IUtilNamedElement utilNamedElement, AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.utilNamedElement = utilNamedElement;
	}

	public AnnotationInstance convert(IAnnotationBinding binding) {
		AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		Annotation resultClass = jdtTResolverUtility.getAnnotation(binding.getAnnotationType());
		utilNamedElement.convertToNameAndSet(binding.getAnnotationType(), resultClass);
		result.setAnnotation(resultClass);
		if (binding.getDeclaredMemberValuePairs().length > 0) {
			org.emftext.language.java.annotations.AnnotationParameterList params = annotationsFactory
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
	public void setJdtTResolverUtility(IUtilJdtResolver jdtTResolverUtility) {
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

}
