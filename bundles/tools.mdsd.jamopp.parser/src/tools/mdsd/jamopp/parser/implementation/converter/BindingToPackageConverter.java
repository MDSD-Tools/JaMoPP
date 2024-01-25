package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToPackageConverter
		implements Converter<IPackageBinding, tools.mdsd.jamopp.model.java.containers.Package> {

	private final JdtResolver jdtTResolverUtility;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	public BindingToPackageConverter(final JdtResolver jdtTResolverUtility,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.containers.Package convert(final IPackageBinding binding) {
		final tools.mdsd.jamopp.model.java.containers.Package pack = jdtTResolverUtility.getPackage(binding);
		pack.setModule(jdtTResolverUtility.getModule(binding.getModule()));
		if (pack.getAnnotations().isEmpty()) {
			pack.getNamespaces().clear();
			Collections.addAll(pack.getNamespaces(), binding.getNameComponents());
			pack.setName("");
			try {
				for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
					pack.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
		}
		return pack;
	}

}
