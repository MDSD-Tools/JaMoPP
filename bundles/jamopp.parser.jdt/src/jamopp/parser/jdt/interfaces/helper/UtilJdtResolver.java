package jamopp.parser.jdt.interfaces.helper;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.parameters.OrdinaryParameter;
import org.emftext.language.java.parameters.VariableLengthParameter;
import org.emftext.language.java.references.ReferenceableElement;
import org.emftext.language.java.variables.AdditionalLocalVariable;
import org.emftext.language.java.variables.LocalVariable;

public interface UtilJdtResolver {

	void completeResolution();

	AdditionalField getAdditionalField(IVariableBinding binding);

	AdditionalField getAdditionalField(String name);

	AdditionalLocalVariable getAdditionalLocalVariable(IVariableBinding binding);

	AdditionalLocalVariable getAdditionalLocalVariable(String varName);

	Annotation getAnnotation(ITypeBinding binding);

	Annotation getAnnotation(String annotName);

	AnonymousClass getAnonymousClass(ITypeBinding binding);

	AnonymousClass getAnonymousClass(String typeName);

	CatchParameter getCatchParameter(IVariableBinding binding);

	CatchParameter getCatchParameter(String paramName);

	org.emftext.language.java.classifiers.Class getClass(ITypeBinding binding);

	org.emftext.language.java.classifiers.Class getClass(String typeName);

	Classifier getClassifier(ITypeBinding binding);

	ClassMethod getClassMethod(IMethodBinding binding);

	ClassMethod getClassMethod(String methodName);

	Constructor getConstructor(IMethodBinding binding);

	Constructor getConstructor(String methName);

	EnumConstant getEnumConstant(IVariableBinding binding);

	EnumConstant getEnumConstant(String enumCN);

	Enumeration getEnumeration(ITypeBinding binding);

	Field getField(IVariableBinding binding);

	Field getField(String name);

	Interface getInterface(ITypeBinding binding);

	InterfaceMethod getInterfaceMethod(IMethodBinding binding);

	InterfaceMethod getInterfaceMethod(String methodName);

	LocalVariable getLocalVariable(IVariableBinding binding);

	LocalVariable getLocalVariable(String varName);

	Method getMethod(IMethodBinding binding);

	org.emftext.language.java.containers.Module getModule(IModuleBinding binding);

	org.emftext.language.java.containers.Module getModule(String modName);

	OrdinaryParameter getOrdinaryParameter(IVariableBinding binding);

	OrdinaryParameter getOrdinaryParameter(String paramName);

	org.emftext.language.java.containers.Package getPackage(IPackageBinding binding);

	ReferenceableElement getReferencableElement(IVariableBinding binding);

	ReferenceableElement getReferenceableElementByNameMatching(String name);

	TypeParameter getTypeParameter(ITypeBinding binding);

	VariableLengthParameter getVariableLengthParameter(IVariableBinding binding);

	void prepareNextUid();

	void setResourceSet(ResourceSet set);

}
