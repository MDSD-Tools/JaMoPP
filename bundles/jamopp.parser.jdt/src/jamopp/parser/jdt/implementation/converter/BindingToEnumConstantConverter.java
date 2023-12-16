package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.members.EnumConstant;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;

@SuppressWarnings("restriction")
public class BindingToEnumConstantConverter implements Converter<IVariableBinding, EnumConstant> {

	private final IUtilJdtResolver iUtilJdtResolver;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	BindingToEnumConstantConverter(IUtilJdtResolver iUtilJdtResolver,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public EnumConstant convert(IVariableBinding binding) {
		EnumConstant result = iUtilJdtResolver.getEnumConstant(binding);
		if (result.eContainer() != null) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		return result;
	}

}
