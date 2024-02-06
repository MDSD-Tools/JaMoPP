package tools.mdsd.jamopp.parser.interfaces.resolver;

import org.eclipse.jdt.core.dom.IMethodBinding;

import tools.mdsd.jamopp.model.java.members.Method;

public interface MethodChecker {

	<T extends Method> T checkMethod(Method mem, IMethodBinding binding);

}