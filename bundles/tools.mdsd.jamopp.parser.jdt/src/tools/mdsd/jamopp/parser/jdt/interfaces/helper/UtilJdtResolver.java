package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public interface UtilJdtResolver {

	void setResourceSet(ResourceSet set);

	tools.mdsd.jamopp.model.java.containers.Module getModule(IModuleBinding binding);

	tools.mdsd.jamopp.model.java.containers.Module getModule(String modName);

	tools.mdsd.jamopp.model.java.containers.Package getPackage(IPackageBinding binding);

	tools.mdsd.jamopp.model.java.containers.Package getPackage(String packageName);

	tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(String annotName);

	tools.mdsd.jamopp.model.java.classifiers.Enumeration getEnumeration(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.classifiers.Class getClass(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.classifiers.Interface getInterface(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.generics.TypeParameter getTypeParameter(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.classifiers.Classifier getClassifier(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(String methodName);

	tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(IMethodBinding binding);

	tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(String methodName);

	tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(IMethodBinding binding);

	tools.mdsd.jamopp.model.java.members.Constructor getConstructor(IMethodBinding binding);

	tools.mdsd.jamopp.model.java.members.Constructor getConstructor(String methName);

	tools.mdsd.jamopp.model.java.members.Method getMethod(IMethodBinding binding);

	tools.mdsd.jamopp.model.java.classifiers.Class getClass(String typeName);

	tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(String typeName);

	tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(ITypeBinding binding);

	tools.mdsd.jamopp.model.java.members.Field getField(String name);

	tools.mdsd.jamopp.model.java.members.Field getField(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(String enumCN);

	tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(String name);

	tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(String varName);

	tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(String varName);

	tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(String paramName);

	tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter getVariableLengthParameter(
			IVariableBinding binding);

	tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(String paramName);

	void prepareNextUid();

	tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferencableElement(IVariableBinding binding);

	tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferenceableElementByNameMatching(String name);

	void completeResolution();

}
