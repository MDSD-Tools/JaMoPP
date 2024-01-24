package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToReferenceConverterFromName implements Converter<Name, IdentifierReference> {

	private final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName;

	@Inject
	public ToReferenceConverterFromName(
			final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName) {
		this.toReferenceConverterFromSimpleName = toReferenceConverterFromSimpleName;
	}

	@Override
	public IdentifierReference convert(final Name name) {
		IdentifierReference result;
		if (name.isSimpleName()) {
			result = toReferenceConverterFromSimpleName.convert((SimpleName) name);
		} else {
			final QualifiedName qualifiedName = (QualifiedName) name;
			final IdentifierReference parent = convert(qualifiedName.getQualifier());
			final IdentifierReference child = toReferenceConverterFromSimpleName.convert(qualifiedName.getName());
			parent.setNext(child);
			result = child;
		}
		return result;
	}

}
