package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.ImportDeclaration;

import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToImportConverter implements Converter<ImportDeclaration, Import> {

	private final Converter<ImportDeclaration, Import> toOnDemandStaticConverter;
	private final Converter<ImportDeclaration, Import> toOnDemandNonStaticConverter;
	private final Converter<ImportDeclaration, Import> toNonOnDemandStaticConverter;
	private final Converter<ImportDeclaration, Import> toNonOnDemandNonStaticConverter;

	@Inject
	public ToImportConverter(
			@Named("ToOnDemandStaticConverter") Converter<ImportDeclaration, Import> toOnDemandStaticConverter,
			@Named("ToOnDemandNonStaticConverter") Converter<ImportDeclaration, Import> toOnDemandNonStaticConverter,
			@Named("ToNonOnDemandStaticConverter") Converter<ImportDeclaration, Import> toNonOnDemandStaticConverter,
			@Named("ToNonOnDemandNonStaticConverter") Converter<ImportDeclaration, Import> toNonOnDemandNonStaticConverter) {
		this.toOnDemandStaticConverter = toOnDemandStaticConverter;
		this.toOnDemandNonStaticConverter = toOnDemandNonStaticConverter;
		this.toNonOnDemandStaticConverter = toNonOnDemandStaticConverter;
		this.toNonOnDemandNonStaticConverter = toNonOnDemandNonStaticConverter;
	}

	@Override
	public Import convert(ImportDeclaration declaration) {
		Import result;
		if (declaration.isOnDemand()) {
			if (declaration.isStatic()) {
				result = toOnDemandStaticConverter.convert(declaration);
			} else {
				result = toOnDemandNonStaticConverter.convert(declaration);
			}
		} else if (declaration.isStatic()) {
			result = toNonOnDemandStaticConverter.convert(declaration);
		} else {
			result = toNonOnDemandNonStaticConverter.convert(declaration);
		}
		return result;
	}
}
