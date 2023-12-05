package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.members.EnumConstant;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class BindingToEnumConstantConverter implements ToConverter<IVariableBinding, EnumConstant> {

	private final UtilJdtResolver utilJdtResolver;
	private final ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	BindingToEnumConstantConverter(UtilJdtResolver utilJdtResolver,
			ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public EnumConstant convert(IVariableBinding binding) {
		EnumConstant result = utilJdtResolver.getEnumConstant(binding);
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
