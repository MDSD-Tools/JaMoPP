package jamopp.parser.jdt.util;

import java.util.Collections;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.commons.NamespaceAwareElement;

public class UtilNamedElement {

	
	public void addNameToNameSpaceAndElement(Name name, NamespaceAwareElement namespaceElement, NamedElement namedElement) {
		if (name.isSimpleName()) {
			namedElement.setName(((SimpleName) name).getIdentifier());
		} else if (name.isQualifiedName()) {
			QualifiedName qualifiedName = (QualifiedName) name;
			namedElement.setName(qualifiedName.getName().getIdentifier());
			addNameToNameSpace(qualifiedName.getQualifier(), namespaceElement);
		}
	}

	public void addNameToNameSpace(Name name, NamespaceAwareElement namespaceElement) {
		if (name.isSimpleName()) {
			SimpleName simpleName = (SimpleName) name;
			namespaceElement.getNamespaces().add(0, simpleName.getIdentifier());
		} else if (name.isQualifiedName()) {
			QualifiedName qualifiedName = (QualifiedName) name;
			namespaceElement.getNamespaces().add(0, qualifiedName.getName().getIdentifier());
			addNameToNameSpace(qualifiedName.getQualifier(), namespaceElement);
		}
	}

	public void setNameOfElement(Name name, NamedElement namedElement) {
		if (name.isSimpleName()) {
			SimpleName simpleName = (SimpleName) name;
			namedElement.setName(simpleName.getIdentifier());
		} else {
			QualifiedName qualifiedName = (QualifiedName) name;
			namedElement.setName(qualifiedName.getName().getIdentifier());
		}
	}
	
	public void convertToNameAndSet(ITypeBinding binding, NamedElement element) {
		String name = binding.getName();
		if (binding.isParameterizedType()) {
			name = name.substring(0, name.indexOf("<"));
		} else if (binding.isArray()) {
			name = name.substring(0, name.indexOf("["));
		}
		element.setName(name);
	}
	
	public void convertToNamespacesAndSet(String namespaces, NamespaceAwareElement ele) {
		ele.getNamespaces().clear();
		String[] singleNamespaces = namespaces.split("\\.");
		Collections.addAll(ele.getNamespaces(), singleNamespaces);
	}

}
