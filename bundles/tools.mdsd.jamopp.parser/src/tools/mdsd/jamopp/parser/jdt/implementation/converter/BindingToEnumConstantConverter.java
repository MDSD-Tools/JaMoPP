package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToEnumConstantConverter implements Converter<IVariableBinding, EnumConstant> {

	private final JdtResolver iUtilJdtResolver;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	public BindingToEnumConstantConverter(JdtResolver iUtilJdtResolver,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public EnumConstant convert(IVariableBinding binding) {
		EnumConstant result = iUtilJdtResolver.getEnumConstant(binding);
		if (result.eContainer() == null) {
			try {
				for (IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
			result.setName(binding.getName());

		}
		return result;
	}

}
