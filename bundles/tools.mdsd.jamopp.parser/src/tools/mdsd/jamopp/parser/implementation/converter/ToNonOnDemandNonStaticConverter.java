package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.imports.ClassifierImport;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToNonOnDemandNonStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToNonOnDemandNonStaticConverter(final UtilNamedElement utilNamedElement,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final ImportsFactory importsFactory) {
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public Import convert(final ImportDeclaration importDecl) {
		final ClassifierImport convertedImport = importsFactory.createClassifierImport();
		Classifier proxy;
		final IBinding iBinding = importDecl.getName().resolveBinding();
		if (iBinding instanceof IPackageBinding) {
			proxy = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			final ITypeBinding binding = (ITypeBinding) iBinding;
			if (binding == null || binding.isRecovered()) {
				proxy = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
			} else {
				proxy = jdtResolverUtility.getClassifier((ITypeBinding) importDecl.getName().resolveBinding());
			}
		}
		convertedImport.setClassifier((ConcreteClassifier) proxy);
		utilNamedElement.addNameToNameSpaceAndElement(importDecl.getName(), convertedImport, proxy);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

}
