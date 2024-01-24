package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import java.util.Collections;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import tools.mdsd.jamopp.model.java.commons.NamedElement;
import tools.mdsd.jamopp.model.java.commons.NamespaceAwareElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class UtilNamedElementImpl implements UtilNamedElement {

	@Override
	public void addNameToNameSpaceAndElement(final Name name, final NamespaceAwareElement namespaceElement,
			final NamedElement namedElement) {
		if (name.isSimpleName()) {
			namedElement.setName(((SimpleName) name).getIdentifier());
		} else if (name.isQualifiedName()) {
			final QualifiedName qualifiedName = (QualifiedName) name;
			namedElement.setName(qualifiedName.getName().getIdentifier());
			addNameToNameSpace(qualifiedName.getQualifier(), namespaceElement);
		}
	}

	@Override
	public void addNameToNameSpace(final Name name, final NamespaceAwareElement namespaceElement) {
		if (name.isSimpleName()) {
			final SimpleName simpleName = (SimpleName) name;
			namespaceElement.getNamespaces().add(0, simpleName.getIdentifier());
		} else if (name.isQualifiedName()) {
			final QualifiedName qualifiedName = (QualifiedName) name;
			namespaceElement.getNamespaces().add(0, qualifiedName.getName().getIdentifier());
			addNameToNameSpace(qualifiedName.getQualifier(), namespaceElement);
		}
	}

	@Override
	public void setNameOfElement(final Name name, final NamedElement namedElement) {
		if (name.isSimpleName()) {
			final SimpleName simpleName = (SimpleName) name;
			namedElement.setName(simpleName.getIdentifier());
		} else {
			final QualifiedName qualifiedName = (QualifiedName) name;
			namedElement.setName(qualifiedName.getName().getIdentifier());
		}
	}

	@Override
	public void convertToNameAndSet(final ITypeBinding binding, final NamedElement element) {
		String name = binding.getName();
		if (binding.isParameterizedType()) {
			name = name.substring(0, name.indexOf('<'));
		} else if (binding.isArray()) {
			name = name.substring(0, name.indexOf('['));
		}
		element.setName(name);
	}

	@Override
	public void convertToNamespacesAndSet(final String namespaces, final NamespaceAwareElement ele) {
		ele.getNamespaces().clear();
		final String[] singleNamespaces = namespaces.split("\\.");
		Collections.addAll(ele.getNamespaces(), singleNamespaces);
	}

}
