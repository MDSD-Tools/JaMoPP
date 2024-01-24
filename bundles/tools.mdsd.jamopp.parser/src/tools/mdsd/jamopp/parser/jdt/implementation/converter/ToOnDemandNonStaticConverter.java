package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ImportDeclaration;

import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.model.java.imports.PackageImport;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToOnDemandNonStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;

	@Inject
	public ToOnDemandNonStaticConverter(final UtilNamedElement utilNamedElement,
			final UtilLayout layoutInformationConverter, final ImportsFactory importsFactory) {
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
	}

	@Override
	public Import convert(final ImportDeclaration importDecl) {
		final PackageImport convertedImport = importsFactory.createPackageImport();
		utilNamedElement.addNameToNameSpace(importDecl.getName(), convertedImport);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

}
