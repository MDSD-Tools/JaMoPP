package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.classifiers.Annotation;
import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilNamedElement;

public class BindingToAnnotationInstanceConverter implements ToConverter<IAnnotationBinding, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final UtilNamedElement utilNamedElement;
	private final Provider<UtilJdtResolver> jdtTResolverUtility;
	private final Provider<ToConverter<IMemberValuePairBinding, AnnotationAttributeSetting>> bindingToAnnotationAttributeSettingConverter;

	@Inject
	BindingToAnnotationInstanceConverter(UtilNamedElement utilNamedElement,
			Provider<UtilJdtResolver> jdtTResolverUtility,
			Provider<ToConverter<IMemberValuePairBinding, AnnotationAttributeSetting>> bindingToAnnotationAttributeSettingConverter,
			AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.utilNamedElement = utilNamedElement;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationAttributeSettingConverter = bindingToAnnotationAttributeSettingConverter;
	}

	public AnnotationInstance convert(IAnnotationBinding binding) {
		AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		Annotation resultClass = jdtTResolverUtility.get().getAnnotation(binding.getAnnotationType());
		utilNamedElement.convertToNameAndSet(binding.getAnnotationType(), resultClass);
		result.setAnnotation(resultClass);
		if (binding.getDeclaredMemberValuePairs().length > 0) {
			org.emftext.language.java.annotations.AnnotationParameterList params = annotationsFactory
					.createAnnotationParameterList();
			for (IMemberValuePairBinding memBind : binding.getDeclaredMemberValuePairs()) {
				params.getSettings().add(bindingToAnnotationAttributeSettingConverter.get().convert(memBind));
			}
			result.setParameter(params);
		}
		return result;
	}

}
