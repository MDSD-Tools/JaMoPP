package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.commons.NamespaceAwareElement;

public interface UtilNamedElement {

	void addNameToNameSpaceAndElement(Name name, NamespaceAwareElement namespaceElement, NamedElement namedElement);

	void addNameToNameSpace(Name name, NamespaceAwareElement namespaceElement);

	void setNameOfElement(Name name, NamedElement namedElement);

	void convertToNameAndSet(ITypeBinding binding, NamedElement element);

	void convertToNamespacesAndSet(String namespaces, NamespaceAwareElement ele);

}