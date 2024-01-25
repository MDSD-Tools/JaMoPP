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

public final class TypeParameterExtension {

	private TypeParameterExtension() {
		// Should not be initiated.
	}

	/**
	 * @return all type restrictions
	 */
	public static EList<ConcreteClassifier> getAllSuperClassifiers(final TypeParameter typeParameter) {
		final EList<ConcreteClassifier> result = new UniqueEList<>();
		for (final TypeReference typeRef : typeParameter.getExtendTypes()) {
			final Type type = typeRef.getTarget();
			if (type instanceof final ConcreteClassifier concreteClassifier) {
				result.add(concreteClassifier);
			}
			if (type instanceof final Classifier classifier) {
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
	public static EList<Member> getAllMembers(final TypeParameter typeParameter, final Commentable context) {
		final EList<Member> memberList = new UniqueEList<>();

		final UniqueEList<Type> possiblyVisibleSuperClassifier = new UniqueEList<>();
		for (final TypeReference typeReference : typeParameter.getExtendTypes()) {
			final Type target = typeReference.getTarget();
			possiblyVisibleSuperClassifier.add(target);
		}

		for (final ConcreteClassifier superClassifier : typeParameter.getAllSuperClassifiers()) {
			for (final Member member : superClassifier.getMembers()) {
				if (member instanceof final AnnotableAndModifiable modifiable) {
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
	public static Type getBoundType(final TypeParameter typeParameter, final EObject typeReference,
			final Reference reference) {

		final EList<Type> resultList = new BasicEList<>();

		final TypeParametrizable typeParameterDeclarator = (TypeParametrizable) typeParameter.eContainer();
		Reference parentReference = null;
		final EList<Type> prevTypeList = new UniqueEList<>();

		if (reference != null && reference.getPrevious() instanceof NestedExpression) {
			parentReference = getParentReferenceAndFillPrevTypeListWithNested(reference, prevTypeList);
		} else if (reference != null && reference.getPrevious() != null) {
			parentReference = getParentReferenceAndFillPrevTypeList(reference, prevTypeList);
		} else if (reference != null) {
			fillPrevTypeList(reference, prevTypeList);
		}

		for (final Type prevType : prevTypeList) {
			processType(typeParameter, reference, resultList, typeParameterDeclarator, parentReference, prevType);
		}

		if (typeParameterDeclarator instanceof final Method method
				&& reference instanceof final MethodCall methodCall) {
			handleTypeAndReferenceAreMethods(typeParameter, typeReference, resultList, parentReference, method,
					methodCall);
		}

		removeNulls(resultList);

		if (resultList.isEmpty() || resultList.size() == 1 && resultList.get(0).equals(typeParameter)) {
			return typeParameter;
		}
		final TemporalCompositeClassifier temp = new TemporalCompositeClassifier(typeParameter);
		for (final Type aResult : resultList) {
			processResult(temp, aResult);
		}
		temp.getSuperTypes().add(typeParameter);
		return temp;
	}

	private static void removeNulls(final EList<Type> resultList) {
		for (final Iterator<?> it = resultList.iterator(); it.hasNext();) {
			if (it.next() == null) {
				it.remove();
			}
		}
	}

	private static Type processResult(final TemporalCompositeClassifier temp, final Type aResult) {
		Type newResult = aResult;
		if (newResult instanceof PrimitiveType) {
			newResult = ((PrimitiveType) newResult).wrapPrimitiveType();
		}

		if (newResult instanceof TemporalCompositeClassifier) {
			// flatten
			temp.getSuperTypes().addAll(((TemporalCompositeClassifier) newResult).getSuperTypes());
		} else {
			temp.getSuperTypes().add(newResult);
		}
		return newResult;
	}

	private static void handleTypeAndReferenceAreMethods(final TypeParameter typeParameter, final EObject typeReference,
			final EList<Type> resultList, final Reference parentReference, final Method method,
			final MethodCall methodCall) {
		if (method.getTypeParameters().size() == methodCall.getCallTypeArguments().size()) {
			final TypeArgument typeArgument = methodCall.getCallTypeArguments()
					.get(method.getTypeParameters().indexOf(typeParameter));
			if (typeArgument instanceof QualifiedTypeArgument) {
				resultList.add(0,
						((QualifiedTypeArgument) typeArgument).getTypeReference().getBoundTarget(parentReference));
			}
		}

		// class type parameter
		int idx = method.getParameters().indexOf(typeReference.eContainer());

		// method type parameter
		if (idx == -1) {
			idx = handleIndexIsMinusOne(typeParameter, method, idx);
		}

		if (idx < methodCall.getArguments().size() && idx >= 0) {
			handleIndexInBetween(typeParameter, resultList, method, methodCall, idx);
		}

		// return type
		if (method.equals(typeReference.eContainer())) {
			// bound by the type of a method argument?
			EList<Classifier> allSuperTypes = null;
			for (final Parameter parameter : method.getParameters()) {
				allSuperTypes = handleMethodParameter(typeParameter, method, methodCall, allSuperTypes, parameter);
			}
			// all types given by all bindings
			if (allSuperTypes != null) {
				resultList.addAll(allSuperTypes);
			}
		}
	}

	private static EList<Classifier> handleMethodParameter(final TypeParameter typeParameter, final Method method,
			final MethodCall methodCall, final EList<Classifier> allSuperTypes, final Parameter parameter) {
		int idx;
		EList<Classifier> newAllSuperTypes = allSuperTypes;
		if (typeParameter.equals(parameter.getTypeReference().getTarget())) {
			idx = method.getParameters().indexOf(parameter);
			final Classifier argumentType = (Classifier) methodCall.getArguments().get(idx).getType();
			if (newAllSuperTypes == null) {
				newAllSuperTypes = new UniqueEList<>();
				newAllSuperTypes.add(argumentType);
				newAllSuperTypes.addAll(argumentType.getAllSuperClassifiers());
			} else {
				newAllSuperTypes.add(argumentType);
				final EList<Classifier> allOtherSuperTypes = new UniqueEList<>();
				allOtherSuperTypes.add(argumentType);
				allOtherSuperTypes.addAll(argumentType.getAllSuperClassifiers());
				final EList<Classifier> temp = newAllSuperTypes;
				newAllSuperTypes = new UniqueEList<>();
				for (final Classifier st : allOtherSuperTypes) {
					if (temp.contains(st)) {
						newAllSuperTypes.add(st);
					}
				}
			}
		}
		return newAllSuperTypes;
	}

	private static void handleIndexInBetween(final TypeParameter typeParameter, final EList<Type> resultList,
			final Method method, final MethodCall methodCall, final int idx) {
		final Expression argument = methodCall.getArguments().get(idx);
		final Parameter parameter = method.getParameters().get(idx);
		final ClassifierReference parameterType = parameter.getTypeReference().getPureClassifierReference();
		if (argument instanceof NewConstructorCall) {
			final ClassifierReference argumentType = ((NewConstructorCall) argument).getTypeReference()
					.getPureClassifierReference();
			if (argumentType != null
					&& parameterType.getTypeArguments().size() == argumentType.getTypeArguments().size()) {
				for (final TypeArgument typeArgument : parameterType.getTypeArguments()) {
					if (typeArgument instanceof QualifiedTypeArgument && ((QualifiedTypeArgument) typeArgument)
							.getTypeReference().getTarget().equals(typeParameter)) {
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
					final TypeReference typeRef = ((TypedElement) elementReference.getTarget()).getTypeReference();
					if (typeRef != null) {
						final ClassifierReference argumentType = typeRef.getPureClassifierReference();
						if (argumentType != null
								&& parameterType.getTypeArguments().size() == argumentType.getTypeArguments().size()) {
							for (final TypeArgument typeArgument : parameterType.getTypeArguments()) {
								if (typeArgument instanceof QualifiedTypeArgument
										&& ((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget()
												.equals(typeParameter)) {
									final int idx2 = parameterType.getTypeArguments().indexOf(typeArgument);
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
					for (final TypeArgument typeArgument : parameterType.getTypeArguments()) {
						if (typeArgument instanceof QualifiedTypeArgument && ((QualifiedTypeArgument) typeArgument)
								.getTypeReference().getTarget().equals(typeParameter)) {
							resultList.add(0, elementReference.getReferencedType());
						}
					}
				}
			} else if (parameterType.getTarget() instanceof TypeParameter) {
				while (argReference.getNext() != null) {
					argReference = argReference.getNext();
				}
				resultList.add(0, argReference.getReferencedType());
			}
		}
	}

	private static int handleIndexIsMinusOne(final TypeParameter typeParameter, final Method method, final int idx) {
		int newIdx = idx;
		for (final Parameter parameter : method.getParameters()) {
			for (final TypeArgument typeArgument : parameter.getTypeArguments()) {
				if (typeArgument instanceof QualifiedTypeArgument && ((QualifiedTypeArgument) typeArgument)
						.getTypeReference().getTarget().equals(typeParameter)) {
					newIdx = method.getParameters().indexOf(parameter);
				}
			}
			final ClassifierReference paramTypeReference = parameter.getTypeReference().getPureClassifierReference();
			if (paramTypeReference != null) {
				for (final TypeArgument typeArgument : paramTypeReference.getTypeArguments()) {
					if (typeArgument instanceof QualifiedTypeArgument && typeParameter
							.equals(((QualifiedTypeArgument) typeArgument).getTypeReference().getTarget())) {
						newIdx = method.getParameters().indexOf(parameter);
					}
				}
			}
		}
		return newIdx;
	}

	private static void processType(final TypeParameter typeParameter, final Reference reference,
			final EList<Type> resultList, final TypeParametrizable typeParameterDeclarator,
			final Reference parentReference, final Type prevType) {
		if (typeParameterDeclarator instanceof ConcreteClassifier) {
			final int typeParameterIndex = typeParameterDeclarator.getTypeParameters().indexOf(typeParameter);
			if (reference != null) {
				ClassifierReference classifierReference = null;
				if (parentReference instanceof ElementReference) {
					final ReferenceableElement prevReferenced = ((ElementReference) parentReference).getTarget();
					if (prevReferenced instanceof TypedElement) {
						final TypeReference prevTypeReference = ((TypedElement) prevReferenced).getTypeReference();
						if (prevTypeReference != null) {
							classifierReference = prevTypeReference.getPureClassifierReference();
						}
					}
				}

				if (parentReference instanceof TypedElement) {
					// e.g. New Constructor Call
					final TypeReference prevParentReference = ((TypedElement) parentReference).getTypeReference();
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

	private static void handleTypeParameter(final EList<Type> resultList, final Type prevType) {
		// the prev. type parameter, although unbound, may contain type restrictions
		// through extends
		resultList.add(prevType);
		for (final TypeReference extendedRef : ((TypeParameter) prevType).getExtendTypes()) {
			final ConcreteClassifier extended = (ConcreteClassifier) extendedRef.getTarget();
			final int idx = ((TypeParametrizable) prevType.eContainer()).getTypeParameters().indexOf(prevType);
			if (extended.getTypeParameters().size() > idx) {
				// also add more precise bindings from extensions
				resultList.add(extended.getTypeParameters().get(idx));
			}
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	private static void handleConcreteClassifier(final EList<Type> resultList,
			final TypeParametrizable typeParameterDeclarator, final Reference parentReference, final Type prevType,
			final int typeParameterIndex, final ClassifierReference classifierReference) {
		// bound through inheritance?
		int idx = 0;
		for (final ClassifierReference superClassifierReference : ((ConcreteClassifier) prevType)
				.getSuperTypeReferences()) {
			// is this an argument for the correct class?
			if (typeParameterIndex < superClassifierReference.getTypeArguments().size()
					&& (typeParameterDeclarator.equals(superClassifierReference.getTarget()) || superClassifierReference
							.getTarget().getAllSuperClassifiers().contains(typeParameterDeclarator))) {
				final TypeArgument arg = superClassifierReference.getTypeArguments().get(typeParameterIndex);
				if (arg instanceof QualifiedTypeArgument) {
					resultList.add(idx, ((QualifiedTypeArgument) arg).getTypeReference().getTarget());
					idx++;
				}
			}
		}

		EList<TypeArgument> typeArgumentList;
		TemporalTypeArgumentHolder ttah = null;
		for (final Adapter adapter : prevType.eAdapters()) {
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
			final TypeArgument arg = typeArgumentList.get(typeParameterIndex);
			if (arg instanceof QualifiedTypeArgument) {
				final ClassifierReference theTypeRef = ((QualifiedTypeArgument) arg).getTypeReference()
						.getPureClassifierReference();
				if (theTypeRef != null) {
					final Type theType = theTypeRef.getBoundTarget(parentReference);
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

	private static void fillPrevTypeList(final Reference reference, final EList<Type> prevTypeList) {
		// Prev type is one of the containing classes which can still bind
		// by inheritance
		ConcreteClassifier containingClassifier = reference.getContainingConcreteClassifier();
		while (containingClassifier != null) {
			prevTypeList.add(containingClassifier);
			final EObject container = containingClassifier.eContainer();
			if (container instanceof final Commentable commentableContainer) {
				containingClassifier = commentableContainer.getContainingConcreteClassifier();
			} else {
				containingClassifier = null;
			}
		}
	}

	private static Reference getParentReferenceAndFillPrevTypeList(final Reference reference,
			final EList<Type> prevTypeList) {
		Reference parentReference;
		parentReference = reference.getPrevious();
		while (parentReference instanceof SelfReference) {
			if (!(((SelfReference) parentReference).getSelf() instanceof Super)) {
				break;
			}
			if (parentReference.eContainer() instanceof Reference) {
				parentReference = (Reference) parentReference.eContainer();
			} else {
				final ConcreteClassifier containingClassifier = reference.getContainingConcreteClassifier();
				if (containingClassifier != null) {
					prevTypeList.add(containingClassifier);
				}
				parentReference = null;
			}
		}

		if (parentReference != null) {
			final Type prevType = parentReference.getReferencedType();
			if (prevType instanceof final TemporalCompositeClassifier temporalCompositeClassifier) {
				for (final EObject aType : temporalCompositeClassifier.getSuperTypes()) {
					prevTypeList.add((Type) aType);
				}
			} else {
				prevTypeList.add(prevType);
			}
		}
		return parentReference;
	}

	private static Reference getParentReferenceAndFillPrevTypeListWithNested(final Reference reference,
			final EList<Type> prevTypeList) {
		Reference newParentReference = null;
		final NestedExpression nestedExpression = (NestedExpression) reference.getPrevious();
		Expression expression = null;
		final Expression nestedExpressionExpression = nestedExpression.getExpression();
		if (nestedExpressionExpression instanceof Reference) {
			expression = nestedExpressionExpression;
		} else if (nestedExpressionExpression instanceof final ConditionalExpression conditionalExpression) {
			expression = conditionalExpression.getExpressionIf();
		}

		if (expression instanceof Reference expressionReference) {
			// Navigate down references
			while (expressionReference.getNext() != null) {
				expressionReference = expressionReference.getNext();
			}

			newParentReference = expressionReference;
			final Type prevType = nestedExpressionExpression.getType();
			if (prevType instanceof final TemporalCompositeClassifier temporalCompositeClassifier) {
				for (final EObject nextSuperType : temporalCompositeClassifier.getSuperTypes()) {
					prevTypeList.add((Type) nextSuperType);
				}
			} else {
				prevTypeList.add(prevType);
			}
		} else if (nestedExpressionExpression instanceof final CastExpression castExpression) {
			prevTypeList.add(castExpression.getTypeReference().getTarget());
		}
		return newParentReference;
	}
}
