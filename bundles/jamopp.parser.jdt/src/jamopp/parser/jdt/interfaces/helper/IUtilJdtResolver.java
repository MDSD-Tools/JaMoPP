package jamopp.parser.jdt.interfaces.helper;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

public interface IUtilJdtResolver {

	public void setResourceSet(ResourceSet set);

	public org.emftext.language.java.containers.Module getModule(IModuleBinding binding);

	public org.emftext.language.java.containers.Module getModule(String modName);

	public org.emftext.language.java.containers.Package getPackage(IPackageBinding binding);

	public org.emftext.language.java.classifiers.Annotation getAnnotation(ITypeBinding binding);

	public org.emftext.language.java.classifiers.Annotation getAnnotation(String annotName);

	public org.emftext.language.java.classifiers.Enumeration getEnumeration(ITypeBinding binding);

	public org.emftext.language.java.classifiers.Class getClass(ITypeBinding binding);

	public org.emftext.language.java.classifiers.Interface getInterface(ITypeBinding binding);

	public org.emftext.language.java.generics.TypeParameter getTypeParameter(ITypeBinding binding);

	public org.emftext.language.java.classifiers.Classifier getClassifier(ITypeBinding binding);

	public org.emftext.language.java.members.InterfaceMethod getInterfaceMethod(String methodName);

	public org.emftext.language.java.members.InterfaceMethod getInterfaceMethod(IMethodBinding binding);

	public org.emftext.language.java.members.ClassMethod getClassMethod(String methodName);

	public org.emftext.language.java.members.ClassMethod getClassMethod(IMethodBinding binding);

	public org.emftext.language.java.members.Constructor getConstructor(IMethodBinding binding);

	public org.emftext.language.java.members.Constructor getConstructor(String methName);

	public org.emftext.language.java.members.Method getMethod(IMethodBinding binding);

	public org.emftext.language.java.classifiers.Class getClass(String typeName);

	public org.emftext.language.java.classifiers.AnonymousClass getAnonymousClass(String typeName);

	public org.emftext.language.java.classifiers.AnonymousClass getAnonymousClass(ITypeBinding binding);

	public org.emftext.language.java.members.Field getField(String name);

	public org.emftext.language.java.members.Field getField(IVariableBinding binding);

	public org.emftext.language.java.members.EnumConstant getEnumConstant(IVariableBinding binding);

	public org.emftext.language.java.members.EnumConstant getEnumConstant(String enumCN);

	public org.emftext.language.java.members.AdditionalField getAdditionalField(String name);

	public org.emftext.language.java.members.AdditionalField getAdditionalField(IVariableBinding binding);

	public org.emftext.language.java.variables.LocalVariable getLocalVariable(IVariableBinding binding);

	public org.emftext.language.java.variables.LocalVariable getLocalVariable(String varName);

	public org.emftext.language.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(
			IVariableBinding binding);

	public org.emftext.language.java.variables.AdditionalLocalVariable getAdditionalLocalVariable(String varName);

	public org.emftext.language.java.parameters.OrdinaryParameter getOrdinaryParameter(IVariableBinding binding);

	public org.emftext.language.java.parameters.OrdinaryParameter getOrdinaryParameter(String paramName);

	public org.emftext.language.java.parameters.VariableLengthParameter getVariableLengthParameter(
			IVariableBinding binding);

	public org.emftext.language.java.parameters.CatchParameter getCatchParameter(IVariableBinding binding);

	public org.emftext.language.java.parameters.CatchParameter getCatchParameter(String paramName);

	public void prepareNextUid();

	public org.emftext.language.java.references.ReferenceableElement getReferencableElement(IVariableBinding binding);

	public org.emftext.language.java.references.ReferenceableElement getReferenceableElementByNameMatching(String name);

	public void completeResolution();

}
