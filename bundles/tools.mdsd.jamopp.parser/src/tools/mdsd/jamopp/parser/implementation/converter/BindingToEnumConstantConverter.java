package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToEnumConstantConverter implements Converter<IVariableBinding, EnumConstant> {

	private final JdtResolver iUtilJdtResolver;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	public BindingToEnumConstantConverter(final JdtResolver iUtilJdtResolver,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public EnumConstant convert(final IVariableBinding binding) {
		final EnumConstant result = iUtilJdtResolver.getEnumConstant(binding);
		if (result.eContainer() == null) {
			try {
				for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
			result.setName(binding.getName());

		}
		return result;
	}

}
