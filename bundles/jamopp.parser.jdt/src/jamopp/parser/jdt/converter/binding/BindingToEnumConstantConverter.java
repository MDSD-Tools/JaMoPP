package jamopp.parser.jdt.converter.binding;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.members.EnumConstant;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.UtilJdtResolver;

public class BindingToEnumConstantConverter {

	private final UtilJdtResolver utilJdtResolver;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;

	@Inject
	BindingToEnumConstantConverter(UtilJdtResolver utilJdtResolver,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
		this.utilJdtResolver = utilJdtResolver;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	EnumConstant convertToEnumConstant(IVariableBinding binding) {
		EnumConstant result = utilJdtResolver.getEnumConstant(binding);
		if (result.eContainer() != null) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations()
						.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		return result;
	}

}
