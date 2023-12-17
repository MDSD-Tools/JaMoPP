package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.imports.ClassifierImport;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToNonOnDemandNonStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver jdtResolverUtility;

	@Inject
	public ToNonOnDemandNonStaticConverter(UtilNamedElement utilNamedElement, UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility, ImportsFactory importsFactory) {
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public Import convert(ImportDeclaration importDecl) {
		ClassifierImport convertedImport = importsFactory.createClassifierImport();
		Classifier proxy = null;
		IBinding b = importDecl.getName().resolveBinding();
		if (b instanceof IPackageBinding) {
			proxy = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			ITypeBinding binding = (ITypeBinding) b;
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
