/*******************************************************************************
 * Copyright (c) 2006-2014
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.extensions.generics;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.expressions.CastExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.NestedExpression;
import tools.mdsd.jamopp.model.java.generics.ExtendsTypeArgument;
import tools.mdsd.jamopp.model.java.generics.QualifiedTypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.model.java.instantiations.NewConstructorCall;
import tools.mdsd.jamopp.model.java.literals.Super;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.references.ReflectiveClassReference;
import tools.mdsd.jamopp.model.java.references.SelfReference;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypedElement;
import tools.mdsd.jamopp.model.java.util.TemporalCompositeClassifier;
import tools.mdsd.jamopp.model.java.util.TemporalTypeArgumentHolder;

public class TypeParameterExtension {

	/**
	 * @return all type restrictions
	 */
	public static EList<ConcreteClassifier> getAllSuperClassifiers(TypeParameter me) {
		EList<ConcreteClassifier> result = new UniqueEList<>();
		for (TypeReference typeRef : me.getExtendTypes()) {
			Type type = typeRef.getTarget();
			if (type instanceof ConcreteClassifier concreteClassifier) {
				result.add(concreteClassifier);
			}
			if (type instanceof Classifier classifier) {
				result.addAll(classifier.getAllSuperClassifiers());
			}
		}

		return result;
	}

	/**
	 * Returns all members of the given classifier including inner classes and all
	 * members of super types (extended classes and implemented interfaces).
	 *
	 * @param context to check protected visibility
	 * @return member list
	 */
	public static EList<Member> getAllMembers(TypeParameter me, Commentable context) {
		EList<Member> memberList = new UniqueEList<>();

		UniqueEList<Type> possiblyVisibleSuperClassifier = new UniqueEList<>();
		for (TypeReference typeReference : me.getExtendTypes()) {
			Type target = typeReference.getTarget();
			possiblyVisibleSuperClassifier.add(target);
		}

		for (ConcreteClassifier superClassifier : me.getAllSuperClassifiers()) {
			for (Member member : superClassifier.getMembers()) {
				if (member instanceof AnnotableAndModifiable modifiable) {
					if (!modifiable.isHidden(context) || possiblyVisibleSuperClassifier.contains(superClassifier)) {
						memberList.add(member);
					}
				} else {
					memberList.add(member);
				}
			}
			memberList.addAll(superClassifier.getDefaultMembers());
		}
		return memberList;
	}

	/**
	 * Returns the type bound to the given parameter in the context of the given
	 * reference.
	 *
	 * @param typeReference
	 * @param reference
	 * @return bound type or parameter if not bound
	 */
	public static Type getBoundType(TypeParameter me, EObject typeReference, Reference reference) {

		EList<Type> resultList = new BasicEList<>();

		TypeParametrizable typeParameterDeclarator = (TypeParametrizable) me.eContainer();
		Reference parentReference = null;
		EList<Type> prevTypeList = new UniqueEList<>();

		if (reference != null && reference.getPrevious() instanceof NestedExpression) {
			parentReference = getParentReferenceAndFillPrevTypeList(reference, parentReference, prevTypeList);
		} else if (reference != null && reference.getPrevious() != null) {
			parentReference = getParentReferenceAndFillPrevTypeList(reference, prevTypeList);
		} else if (reference != null) {
			fillPrevTypeList(reference, prevTypeList);
		}

		for (Type prevType : prevTypeList) {
			processType(me, reference, resultList, typeParameterDeclarator, parentReference, prevType);
		}

		if (typeParameterDeclarator instanceof Method method && reference instanceof MethodCall methodCall) {
			handleTypeAndReferenceAreMethods(me, typeReference, resultList, parentReference, method, methodCall);
		}

		removeNulls(resultList);

		if (resultList.isEmpty() || resultList.size() == 1 && resultList.get(0).equals(me)) {
			return me;
		}
		TemporalCompositeClassifier temp = new TemporalCompositeClassifier(me);
		for (Type aResult : resultList) {
			processResult(temp, aResult);
		}
		temp.getSuperTypes().add(me);
		return temp;
	}

	private static void removeNulls(EList<Type> resultList) {
		for (Iterator<?> it = resultList.iterator(); it.hasNext();) {
			if (it.next() == null) {
				it.remove();
			}
		}
	}

	private static Type processResult(TemporalCompositeClassifier temp, Type aResult) {
		if (aResult instanceof PrimitiveType) {
			aResult = ((PrimitiveType) aResult).wrapPrimitiveType();
		}

		if (aResult instanceof TemporalCompositeClassifier) {
			// flatten
			temp.getSuperTypes().addAll(((TemporalCompositeClassifier) aResult).getSuperTypes());
		} else {
			temp.getSuperTypes().add(aResult);
		}
		return aResult;
	}

	private static void handleTypeAndReferenceAreMethods(TypeParameter me, EObject typeReference,
			EList<Type> resultList, Reference parentReference, Method method, MethodCall methodCall) {
		if (method.getTypeParameters().size() == methodCall.getCallTypeArguments().size()) {
			TypeArgument typeArgument = methodCall.getCallTypeArguments().get(method.getTypeParameters().indexOf(me));
			if (typeArgument instanceof QualifiedTypeArgument) {
				resultList.add(0,
						((QualifiedTypeArgument) typeArgument).getTypeReference().getBoundTarget(parentReference));
			}
		}

		// class type parameter
		int idx = method.getParameters().indexOf(typeReference.eContainer());

		// method type parameter
		if (idx == -1) {
			idx = handleIndexIsMinusOne(me, method, idx);
		}

		if (idx < methodCall.getArguments().size() && idx >= 0) {
			handleIndexInBetween(me, resultList, method, methodCall, idx);
		}

		// return type
		if (method.equals(typeReference.eContainer())) {
			// bound by the type of a method argument?
			EList<Classifier> allSuperTypes = null;
			for (Parameter parameter : method.getParameters()) {
				allSuperTypes = handleMethodParameter(me, method, methodCall, allSuperTypes, parameter);
			}
			// all types given by all bindings
			if (allSuperTypes != null) {
				resultList.addAll(allSuperTypes);
			}
		}
	}

	private static EList<Classifier> handleMethodParameter(TypeParameter me, Method method, MethodCall methodCall,
			EList<Classifier> allSuperTypes, Parameter parameter) {
		int idx;
		if (me.equals(parameter.getTypeReference().getTarget())) {
			idx = method.getParameters().indexOf(parameter);
			Classifier argumentType = (Classifier) methodCall.getArguments().get(idx).getType();
			if (allSuperTypes == null) {
				allSuperTypes = new UniqueEList<>();
				allSuperTypes.add(argumentType);
				allSuperTypes.addAll(argumentType.getAllSuperClassifiers());
			} else {
				allSuperTypes.add(argumentType);
				EList<Classifier> allOtherSuperTypes = new UniqueEList<>();
				allOtherSuperTypes.add(argumentType);
				allOtherSuperTypes.addAll(argumentType.getAllSuperClassifiers());
				EList<Classifier> temp = allSuperTypes;
				allSuperTypes = new UniqueEList<>();
				for (Classifier st : allOtherSuperTypes) {
					if (temp.contains(st)) {
						allSuperTypes.add(st);
					}
				}
			}
		}
		return allSuperTypes;
	}

	private static void handleIndexInBetween(TypeParameter me, EList<Type> resultList, Method method,
			MethodCall methodCall, int idx) {
		Expression argument = methodCall.getArguments().get(idx);
		Parameter parameter = method.getParameters().get(idx);
		ClassifierReference parameterType = parameter.getTypeReference().getPureClassifierReference();
		if (argument instanceof NewConstructorCall) {
			ClassifierReference argumentType = ((NewConstructorCall) argument).getTypeReference()
					.getPureClassifierReference();
			if (argumentType != null
					&& parameterType.getTypeArguments().size() == argumentType.getTypeArguments().size()) {
				for (TypeArgument typeArgument : parameterType.getTypeArguments()) {
					if (typeArgument instanceof QualifiedTypeArgument
							&& ((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget().equals(me)) {
						resultList.add(0,
								((QualifiedTypeArgument) argumentType.getTypeArguments()
										.get(parameterType.getTypeArguments().indexOf(typeArgument))).getTypeReference()
										.getTarget());
					}
				}
			}
			if (argumentType != null && parameterType.getTarget() instanceof TypeParameter) {
				resultList.add(0, argumentType.getTarget());
			}
		} else if (parameterType != null && argument instanceof Reference argReference) {
			while (!(argReference.getNext() instanceof ReflectiveClassReference)) {
				argReference = argReference.getNext();
			}

			if (argReference instanceof ElementReference elementReference) {
				while (elementReference.getNext() instanceof ElementReference) {
					elementReference = (ElementReference) elementReference.getNext();
				}
				if (elementReference.getTarget() instanceof TypedElement) {
					TypeReference typeRef = ((TypedElement) elementReference.getTarget()).getTypeReference();
					if (typeRef != null) {
						ClassifierReference argumentType = typeRef.getPureClassifierReference();
						if (argumentType != null
								&& parameterType.getTypeArguments().size() == argumentType.getTypeArguments().size()) {
							for (TypeArgument typeArgument : parameterType.getTypeArguments()) {
								if (typeArgument instanceof QualifiedTypeArgument
										&& ((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget()
												.equals(me)) {
									int idx2 = parameterType.getTypeArguments().indexOf(typeArgument);
									if (argumentType.getTypeArguments().get(idx2) instanceof QualifiedTypeArgument) {
										resultList.add(0,
												((QualifiedTypeArgument) argumentType.getTypeArguments().get(idx2))
														.getTypeReference().getTarget());
									} else if (argumentType.getTypeArguments()
											.get(idx2) instanceof ExtendsTypeArgument) {
										resultList.add(0,
												((ExtendsTypeArgument) argumentType.getTypeArguments().get(idx2))
														.getExtendType().getTarget());
									}
								}
							}
						}
						if (argumentType != null && parameterType.getTarget() instanceof TypeParameter) {
							resultList.add(0, argumentType.getTarget());
						}
					}
				}
				if (elementReference.getNext() instanceof ReflectiveClassReference
						&& parameterType.getTypeArguments().size() == 1) {
					for (TypeArgument typeArgument : parameterType.getTypeArguments()) {
						if (typeArgument instanceof QualifiedTypeArgument
								&& ((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget().equals(me)) {
							resultList.add(0, elementReference.getReferencedType());
						}
					}
				}
			} else if (parameterType.getTarget() instanceof TypeParameter) {
				while (argReference.getNext() instanceof Reference) {
					argReference = argReference.getNext();
				}
				resultList.add(0, argReference.getReferencedType());
			}
		}
	}

	private static int handleIndexIsMinusOne(TypeParameter me, Method method, int idx) {
		for (Parameter parameter : method.getParameters()) {
			for (TypeArgument typeArgument : parameter.getTypeArguments()) {
				if (typeArgument instanceof QualifiedTypeArgument
						&& ((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget().equals(me)) {
					idx = method.getParameters().indexOf(parameter);
				}
			}
			ClassifierReference paramTypeReference = parameter.getTypeReference().getPureClassifierReference();
			if (paramTypeReference != null) {
				for (TypeArgument typeArgument : paramTypeReference.getTypeArguments()) {
					if (typeArgument instanceof QualifiedTypeArgument
							&& me.equals(((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget())) {
						idx = method.getParameters().indexOf(parameter);
					}
				}
			}
		}
		return idx;
	}

	private static void processType(TypeParameter me, Reference reference, EList<Type> resultList,
			TypeParametrizable typeParameterDeclarator, Reference parentReference, Type prevType) {
		if (typeParameterDeclarator instanceof ConcreteClassifier) {
			int typeParameterIndex = typeParameterDeclarator.getTypeParameters().indexOf(me);
			if (reference != null) {
				ClassifierReference classifierReference = null;
				if (parentReference instanceof ElementReference) {
					ReferenceableElement prevReferenced = ((ElementReference) parentReference).getTarget();
					if (prevReferenced instanceof TypedElement) {
						TypeReference prevTypeReference = ((TypedElement) prevReferenced).getTypeReference();
						if (prevTypeReference != null) {
							classifierReference = prevTypeReference.getPureClassifierReference();
						}
					}
				}

				if (parentReference instanceof TypedElement) {
					// e.g. New Constructor Call
					TypeReference prevParentReference = ((TypedElement) parentReference).getTypeReference();
					if (prevParentReference != null) {
						classifierReference = prevParentReference.getPureClassifierReference();
					}
				}

				if (prevType instanceof ConcreteClassifier) {
					handleConcreteClassifier(resultList, typeParameterDeclarator, parentReference, prevType,
							typeParameterIndex, classifierReference);
				} else if (prevType instanceof TypeParameter) {
					handleTypeParameter(resultList, prevType);
				}
			}
			if (reference != null && reference.eContainer() instanceof ReflectiveClassReference
					&& reference.eContainer().eContainer() instanceof Reference) {
				// the ".class" instantiation implicitly binds the T parameter of
				// java.lang.Class to the class itself
				resultList.add(0, ((Reference) reference.eContainer().eContainer()).getReferencedType());
			}
		}
	}

	private static void handleTypeParameter(EList<Type> resultList, Type prevType) {
		// the prev. type parameter, although unbound, may contain type restrictions
		// through extends
		resultList.add(prevType);
		for (TypeReference extendedRef : ((TypeParameter) prevType).getExtendTypes()) {
			ConcreteClassifier extended = (ConcreteClassifier) extendedRef.getTarget();
			int idx = ((TypeParametrizable) prevType.eContainer()).getTypeParameters().indexOf(prevType);
			if (extended.getTypeParameters().size() > idx) {
				// also add more precise bindings from extensions
				resultList.add(extended.getTypeParameters().get(idx));
			}
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	private static void handleConcreteClassifier(EList<Type> resultList, TypeParametrizable typeParameterDeclarator,
			Reference parentReference, Type prevType, int typeParameterIndex, ClassifierReference classifierReference) {
		// bound through inheritance?
		int idx = 0;
		for (ClassifierReference superClassifierReference : ((ConcreteClassifier) prevType).getSuperTypeReferences()) {
			// is this an argument for the correct class?
			if (typeParameterIndex < superClassifierReference.getTypeArguments().size()
					&& (typeParameterDeclarator.equals(superClassifierReference.getTarget()) || superClassifierReference
							.getTarget().getAllSuperClassifiers().contains(typeParameterDeclarator))) {
				TypeArgument arg = superClassifierReference.getTypeArguments().get(typeParameterIndex);
				if (arg instanceof QualifiedTypeArgument) {
					resultList.add(idx, ((QualifiedTypeArgument) arg).getTypeReference().getTarget());
					idx++;
				}
			}
		}

		EList<TypeArgument> typeArgumentList;
		TemporalTypeArgumentHolder ttah = null;
		for (Adapter adapter : prevType.eAdapters()) {
			if (adapter instanceof TemporalTypeArgumentHolder) {
				ttah = (TemporalTypeArgumentHolder) adapter;
				prevType.eAdapters().remove(ttah);
				break;
			}
		}
		if (ttah != null) {
			typeArgumentList = ttah.getTypeArguments();
		} else if (classifierReference != null) {
			typeArgumentList = classifierReference.getTypeArguments();
		} else {
			typeArgumentList = ECollections.emptyEList();
		}

		if (typeParameterIndex < typeArgumentList.size()) {
			TypeArgument arg = typeArgumentList.get(typeParameterIndex);
			if (arg instanceof QualifiedTypeArgument) {
				ClassifierReference theTypeRef = ((QualifiedTypeArgument) arg).getTypeReference()
						.getPureClassifierReference();
				if (theTypeRef != null) {
					Type theType = theTypeRef.getBoundTarget(parentReference);
					if (theType != null) {
						if (!theTypeRef.getTypeArguments().isEmpty()) {
							ttah = new TemporalTypeArgumentHolder();
							ttah.getTypeArguments().addAll(theTypeRef.getTypeArguments());
							theType.eAdapters().add(ttah);
						}
						resultList.add(0, theType);
					}
				}
			}
			if (arg instanceof ExtendsTypeArgument) {
				resultList.add(0, ((ExtendsTypeArgument) arg).getExtendType().getBoundTarget(parentReference));
			}
		}
	}

	private static void fillPrevTypeList(Reference reference, EList<Type> prevTypeList) {
		// Prev type is one of the containing classes which can still bind
		// by inheritance
		ConcreteClassifier containingClassifier = reference.getContainingConcreteClassifier();
		while (containingClassifier != null) {
			prevTypeList.add(containingClassifier);
			EObject container = containingClassifier.eContainer();
			if (container instanceof Commentable commentableContainer) {
				containingClassifier = commentableContainer.getContainingConcreteClassifier();
			} else {
				containingClassifier = null;
			}
		}
	}

	private static Reference getParentReferenceAndFillPrevTypeList(Reference reference, EList<Type> prevTypeList) {
		Reference parentReference;
		parentReference = reference.getPrevious();
		while (parentReference instanceof SelfReference) {
			if (!(((SelfReference) parentReference).getSelf() instanceof Super)) {
				break;
			}
			if (parentReference.eContainer() instanceof Reference) {
				parentReference = (Reference) parentReference.eContainer();
			} else {
				ConcreteClassifier containingClassifier = reference.getContainingConcreteClassifier();
				if (containingClassifier != null) {
					prevTypeList.add(containingClassifier);
				}
				parentReference = null;
			}
		}

		if (parentReference != null) {
			Type prevType = parentReference.getReferencedType();
			if (prevType instanceof TemporalCompositeClassifier temporalCompositeClassifier) {
				for (EObject aType : temporalCompositeClassifier.getSuperTypes()) {
					prevTypeList.add((Type) aType);
				}
			} else {
				prevTypeList.add(prevType);
			}
		}
		return parentReference;
	}

	private static Reference getParentReferenceAndFillPrevTypeList(Reference reference, Reference parentReference,
			EList<Type> prevTypeList) {
		NestedExpression nestedExpression = (NestedExpression) reference.getPrevious();
		Expression expression = null;
		Expression nestedExpressionExpression = nestedExpression.getExpression();
		if (nestedExpressionExpression instanceof Reference) {
			expression = nestedExpressionExpression;
		} else if (nestedExpressionExpression instanceof ConditionalExpression conditionalExpression) {
			expression = conditionalExpression.getExpressionIf();
		}

		if (expression instanceof Reference expressionReference) {
			// Navigate down references
			while (expressionReference.getNext() != null) {
				expressionReference = expressionReference.getNext();
			}

			parentReference = expressionReference;
			Type prevType = nestedExpressionExpression.getType();
			if (prevType instanceof TemporalCompositeClassifier temporalCompositeClassifier) {
				for (EObject nextSuperType : temporalCompositeClassifier.getSuperTypes()) {
					prevTypeList.add((Type) nextSuperType);
				}
			} else {
				prevTypeList.add(prevType);
			}
		} else if (nestedExpressionExpression instanceof CastExpression castExpression) {
			prevTypeList.add(castExpression.getTypeReference().getTarget());
		}
		return parentReference;
	}
}
