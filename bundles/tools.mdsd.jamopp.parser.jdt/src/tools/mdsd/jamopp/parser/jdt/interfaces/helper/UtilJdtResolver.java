package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public interface UtilJdtResolver {

	public void setResourceSet(ResourceSet set);

	public tools.mdsd.jamopp.model.java.containers.Module getModule(IModuleBinding binding);

	public tools.mdsd.jamopp.model.java.containers.Module getModule(String modName);

	public tools.mdsd.jamopp.model.java.containers.Package getPackage(IPackageBinding binding);

	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.classifiers.Annotation getAnnotation(String annotName);

	public tools.mdsd.jamopp.model.java.classifiers.Enumeration getEnumeration(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.classifiers.Interface getInterface(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.generics.TypeParameter getTypeParameter(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.classifiers.Classifier getClassifier(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(String methodName);

	public tools.mdsd.jamopp.model.java.members.InterfaceMethod getInterfaceMethod(IMethodBinding binding);

	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(String methodName);

	public tools.mdsd.jamopp.model.java.members.ClassMethod getClassMethod(IMethodBinding binding);

	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(IMethodBinding binding);

	public tools.mdsd.jamopp.model.java.members.Constructor getConstructor(String methName);

	public tools.mdsd.jamopp.model.java.members.Method getMethod(IMethodBinding binding);

	public tools.mdsd.jamopp.model.java.classifiers.Class getClass(String typeName);

	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(String typeName);

	public tools.mdsd.jamopp.model.java.classifiers.AnonymousClass getAnonymousClass(ITypeBinding binding);

	public tools.mdsd.jamopp.model.java.members.Field getField(String name);

	public tools.mdsd.jamopp.model.java.members.Field getField(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.members.EnumConstant getEnumConstant(String enumCN);

	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(String name);

	public tools.mdsd.jamopp.model.java.members.AdditionalField getAdditionalField(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.variables.LocalVariable getLocalVariable(String varName);

	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(
			IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(String varName);

	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter getOrdinaryParameter(String paramName);

	public tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter getVariableLengthParameter(
			IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.parameters.CatchParameter getCatchParameter(String paramName);

	public void prepareNextUid();

	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferencableElement(IVariableBinding binding);

	public tools.mdsd.jamopp.model.java.references.ReferenceableElement getReferenceableElementByNameMatching(String name);

	public void completeResolution();

}
