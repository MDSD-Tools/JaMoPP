package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class BindingToAnnotationInstanceConverter implements Converter<IAnnotationBinding, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private Converter<IMemberValuePairBinding, AnnotationAttributeSetting> bindingToAnnotationAttributeSettingConverter;
	private UtilJdtResolver jdtTResolverUtility;
	private final UtilNamedElement utilNamedElement;

	@Inject
	BindingToAnnotationInstanceConverter(UtilNamedElement utilNamedElement, AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.utilNamedElement = utilNamedElement;
	}

	@Override
	public AnnotationInstance convert(IAnnotationBinding binding) {
		var result = this.annotationsFactory.createAnnotationInstance();
		var resultClass = this.jdtTResolverUtility.getAnnotation(binding.getAnnotationType());
		this.utilNamedElement.convertToNameAndSet(binding.getAnnotationType(), resultClass);
		result.setAnnotation(resultClass);
		if (binding.getDeclaredMemberValuePairs().length > 0) {
			var params = this.annotationsFactory
					.createAnnotationParameterList();
			for (IMemberValuePairBinding memBind : binding.getDeclaredMemberValuePairs()) {
				params.getSettings().add(this.bindingToAnnotationAttributeSettingConverter.convert(memBind));
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
	public void setJdtTResolverUtility(UtilJdtResolver jdtTResolverUtility) {
		this.jdtTResolverUtility = jdtTResolverUtility;
	}

}
