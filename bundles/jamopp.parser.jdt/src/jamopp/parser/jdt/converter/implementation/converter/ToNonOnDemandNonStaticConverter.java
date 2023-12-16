package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.imports.ClassifierImport;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportsFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToNonOnDemandNonStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ImportsFactory importsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtResolverUtility;

	@Inject
	public ToNonOnDemandNonStaticConverter(IUtilNamedElement utilNamedElement, IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility, ImportsFactory importsFactory) {
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
