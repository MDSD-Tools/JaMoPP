package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.model.java.imports.StaticClassifierImport;
import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToOnDemandStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ModifiersFactory modifiersFactory;
	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;

	@Inject
	public ToOnDemandStaticConverter(UtilNamedElement utilNamedElement, ModifiersFactory modifiersFactory,
			UtilLayout layoutInformationConverter, JdtResolver jdtResolverUtility, ImportsFactory importsFactory) {
		this.modifiersFactory = modifiersFactory;
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public Import convert(ImportDeclaration importDecl) {
		StaticClassifierImport convertedImport = importsFactory.createStaticClassifierImport();
		convertedImport.setStatic(modifiersFactory.createStatic());
		IBinding binding = importDecl.getName().resolveBinding();
		Classifier proxyClass;
		if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
			proxyClass = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getClassifier((ITypeBinding) binding);
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		utilNamedElement.addNameToNameSpaceAndElement(importDecl.getName(), convertedImport, proxyClass);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

}
