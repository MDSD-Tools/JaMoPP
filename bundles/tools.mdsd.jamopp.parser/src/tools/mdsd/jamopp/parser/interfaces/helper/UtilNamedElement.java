package tools.mdsd.jamopp.parser.interfaces.helper;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import tools.mdsd.jamopp.model.java.commons.NamedElement;
import tools.mdsd.jamopp.model.java.commons.NamespaceAwareElement;

public interface UtilNamedElement {

	void addNameToNameSpaceAndElement(Name name, NamespaceAwareElement namespaceElement, NamedElement namedElement);

	void addNameToNameSpace(Name name, NamespaceAwareElement namespaceElement);

	void setNameOfElement(Name name, NamedElement namedElement);

	void convertToNameAndSet(ITypeBinding binding, NamedElement element);

	void convertToNamespacesAndSet(String namespaces, NamespaceAwareElement ele);

}