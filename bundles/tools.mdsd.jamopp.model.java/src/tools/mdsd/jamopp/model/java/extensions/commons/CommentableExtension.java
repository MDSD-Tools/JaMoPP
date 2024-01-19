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
package tools.mdsd.jamopp.model.java.extensions.commons;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.model.java.statements.StatementListContainer;

public final class CommentableExtension {

	private CommentableExtension() {
		// Should not be initiated.
	}

	/**
	 * Adds the given statement before the statement that contains this element.
	 */
	public static void addBeforeContainingStatement(Commentable commentable, Statement statementToAdd) {
		EObject container = commentable.eContainer();
		EObject statement = commentable;
		while (container != null) {
			if (container instanceof StatementListContainer) {
				break;
			}
			container = container.eContainer();
			statement = statement.eContainer();
		}
		if (container == null) {
			throw new IllegalArgumentException(
					"Element " + commentable + " is not contained in a StatementListContainer.");
		}
		StatementListContainer statementListContainer = (StatementListContainer) container;
		EList<Statement> statements = statementListContainer.getStatements();
		int index = statements.indexOf(statement);

		statements.add(index, statementToAdd);
	}

	/**
	 * Adds the given statement after the statement that contains this element.
	 */
	public static void addAfterContainingStatement(Commentable commentable, Statement statementToAdd) {
		EObject container = commentable.eContainer();
		EObject statement = commentable;
		while (container != null) {
			if (container instanceof StatementListContainer) {
				break;
			}
			container = container.eContainer();
			statement = statement.eContainer();
		}
		if (container == null) {
			throw new IllegalArgumentException(
					"Element " + commentable + " is not contained in a StatementListContainer.");
		}
		StatementListContainer statementListContainer = (StatementListContainer) container;
		EList<Statement> statements = statementListContainer.getStatements();
		int index = statements.indexOf(statement);

		if (index == statements.size() - 1) {
			// statement is the last one
			statements.add(statementToAdd);
		} else {
			statements.add(index + 1, statementToAdd);
		}
	}

	/**
	 * Walks up the containment hierarchy and returns the first parent with the
	 * given type. If no such parent is found, null is returned.
	 */
	public static EObject getParentByEType(EObject eObject, EClassifier type) {
		EObject container = eObject.eContainer();
		while (container != null) {
			if (type.isInstance(container)) {
				return container;
			}
			container = container.eContainer();
		}
		return null;
	}

	/**
	 * Walks up the containment hierarchy and returns the first parent with the
	 * given type. If no such parent is found, null is returned.
	 */
	public static <T> T getParentByType(EObject eObject, Class<T> type) {
		EObject container = eObject.eContainer();
		while (container != null) {
			if (type.isInstance(container)) {
				return type.cast(container);
			}
			container = container.eContainer();
		}
		return null;
	}

	/**
	 * Searches for the first child with the given type. If no such child is found,
	 * <code>null</code> is returned.
	 */
	public static EObject getFirstChildByEType(EObject eObject, EClassifier type) {
		Iterator<EObject> iterator = eObject.eAllContents();
		while (iterator.hasNext()) {
			EObject next = iterator.next();
			if (type.isInstance(next)) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Searches for the first child with the given type. If no such child is found,
	 * <code>null</code> is returned.
	 */
	public static <T> T getFirstChildByType(EObject eObject, Class<T> type) {
		Iterator<EObject> it = eObject.eAllContents();
		while (it.hasNext()) {
			EObject next = it.next();
			if (type.isInstance(next)) {
				return type.cast(next);
			}
		}
		return null;
	}

	/**
	 * Returns all children of the given type.
	 */
	public static EList<EObject> getChildrenByEType(EObject eObject, EClassifier type) {
		EList<EObject> children = new BasicEList<>();
		Iterator<EObject> it = eObject.eAllContents();
		while (it.hasNext()) {
			EObject next = it.next();
			if (type.isInstance(next)) {
				children.add(next);
			}
		}
		return children;
	}

	/**
	 * Returns all children of the given type.
	 */
	public static <T> EList<T> getChildrenByType(EObject eObject, Class<T> type) {
		EList<T> children = new BasicEList<>();
		Iterator<EObject> it = eObject.eAllContents();
		while (it.hasNext()) {
			EObject next = it.next();
			if (type.isInstance(next)) {
				children.add(type.cast(next));
			}
		}
		return children;
	}

	/**
	 * Finds the {@link ConcreteClassifier} representing the class with the given
	 * classified name.
	 *
	 * @param name classified name of the ConcreteClassifier
	 */
	public static ConcreteClassifier getConcreteClassifier(Commentable commentable, String name) {
		return JavaClasspath.get().getFirstConcreteClassifier(name);
	}

	/**
	 * Finds all {@link ConcreteClassifier} representing the classes in the given
	 * package or a single class from that package.
	 *
	 * @param packageName     name of the package
	 * @param classifierQuery * for all classifiers or name of a single classifier
	 */
	public static EList<ConcreteClassifier> getConcreteClassifiers(Commentable me, String packageName,
			String classifierQuery) {
		EList<ConcreteClassifier> result = new UniqueEList<>();
		if ("*".equals(classifierQuery)) {
			tools.mdsd.jamopp.model.java.containers.Package pack = JavaClasspath.get().getPackage(packageName);
			if (pack != null) {
				result.addAll(pack.getClassifiers());
			}
		} else {
			ConcreteClassifier classifier = JavaClasspath.get()
					.getConcreteClassifier(packageName + "." + classifierQuery);
			if (classifier != null) {
				result.add(classifier);
			}
		}
		return result;
	}

	/**
	 * Finds the {@link tools.mdsd.jamopp.model.java.classifiers.Class} representing
	 * the class with the given name located in <code>java.lang</code>.
	 *
	 * @param name name of the Class.
	 * @return the Class.
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class getLibClass(Commentable commentable, String name) {
		ConcreteClassifier result = JavaClasspath.get().getConcreteClassifier("java.lang." + name);
		if (result instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
			return (tools.mdsd.jamopp.model.java.classifiers.Class) result;
		}
		return null;
	}

	/**
	 * Finds the {@link tools.mdsd.jamopp.model.java.classifiers.Interface}
	 * representing the interface with the given name located in
	 * <code>java.lang</code>.
	 *
	 * @param name name of the Interface.
	 * @return the interface.
	 */
	public static Interface getLibInterface(Commentable commentable, String name) {
		ConcreteClassifier interfaceClass = JavaClasspath.get().getConcreteClassifier("java.lang." + name);
		if (interfaceClass instanceof Interface) {
			return (Interface) interfaceClass;
		}
		return null;
	}

	/**
	 * Finds the {@link tools.mdsd.jamopp.model.java.classifiers.Class} representing
	 * <code>java.lang.Class</code>.
	 *
	 * @return the Class.
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class getClassClass(Commentable commentabke) {
		return commentabke.getLibClass("Class");
	}

	/**
	 * Finds the {@link tools.mdsd.jamopp.model.java.classifiers.Class} representing
	 * <code>java.lang.Object</code>.
	 *
	 * @return the Class.
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class getObjectClass(Commentable commentable) {
		return commentable.getLibClass("Object");
	}

	/**
	 * Finds the {@link tools.mdsd.jamopp.model.java.classifiers.Class} representing
	 * <code>java.lang.String</code>.
	 *
	 * @return the Class.
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class getStringClass(Commentable commentable) {
		return commentable.getLibClass("String");
	}

	/**
	 * Finds the {@link tools.mdsd.jamopp.model.java.classifiers.Interface}
	 * representing <code>java.lang.annotation.Annotation</code>.
	 *
	 * @return the Class.
	 */
	public static Interface getAnnotationInterface(Commentable commentable) {
		ConcreteClassifier proxy = JavaClasspath.get().getConcreteClassifier("java.lang.annotation.Annotation");
		if (proxy instanceof Interface) {
			return (Interface) proxy;
		}
		return null;
	}

	// ===== Container look up =====

	/**
	 * Finds the containing classifier for the given element.
	 *
	 * @param value
	 * @return containing classifier
	 */
	public static ConcreteClassifier getContainingConcreteClassifier(Commentable commentable) {
		EObject value = commentable;
		while (!(value instanceof ConcreteClassifier) && value != null) {
			value = value.eContainer();
		}
		return (ConcreteClassifier) value;
	}

	/**
	 * Finds the classifier that is the parent of this element. If this element is
	 * an inner classifier the parent classifier does not necessarily contain this
	 * element, since it can reside in a different compilation unit when stored in
	 * byte code.
	 *
	 * @return containing classifier
	 */
	public static ConcreteClassifier getParentConcreteClassifier(Commentable commentable) {
		return commentable.getContainingConcreteClassifier();
	}

	/**
	 * Finds the containing anonymous class for the given element.
	 *
	 * @return containing anonymous class
	 */
	public static AnonymousClass getContainingAnonymousClass(Commentable commentable) {
		EObject value = commentable;
		while (!(value instanceof AnonymousClass) && !(value instanceof ConcreteClassifier) // Do not jump over other
																							// classifiers
				&& value != null) {
			value = value.eContainer();
		}
		if (!(value instanceof AnonymousClass)) {
			return null;
		}
		return (AnonymousClass) value;
	}

	/**
	 * Finds the containing compilation unit for the given element.
	 *
	 * @return containing compilation unit
	 */
	public static CompilationUnit getContainingCompilationUnit(Commentable commentable) {
		EObject value = commentable;
		while (!(value instanceof CompilationUnit) && value != null) {
			value = value.eContainer();
		}
		return (CompilationUnit) value;
	}

	/**
	 * Finds the containing annotation instance for the given element.
	 *
	 * @return containing annotation instance
	 */
	public static AnnotationInstance getContainingAnnotationInstance(Commentable commentable) {
		EObject value = commentable;
		while (!(value instanceof AnnotationInstance) && value != null) {
			value = value.eContainer();
		}
		return (AnnotationInstance) value;
	}

	public static EList<String> getContainingPackageName(Commentable commentable) {
		CompilationUnit compilationUnit = commentable.getContainingCompilationUnit();
		if (compilationUnit == null) {
			return ECollections.emptyEList();
		}

		int idx = compilationUnit.getNamespaces().size();
		if (compilationUnit.getName() != null) {
			char[] fullName = compilationUnit.getName().toCharArray();
			for (char element : fullName) {
				if (element == '$') {
					idx--;
				}
			}
		}
		List<String> packageNameParts = compilationUnit.getNamespaces().subList(0, idx);
		BasicEList<String> packageNameList = new BasicEList<>(packageNameParts);
		return ECollections.unmodifiableEList(packageNameList);
	}

	public static EList<String> getContainingContainerName(Commentable commentable) {
		CompilationUnit compilationUnit = commentable.getContainingCompilationUnit();
		if (compilationUnit == null) {
			return ECollections.emptyEList();
		}
		return ECollections.unmodifiableEList(compilationUnit.getNamespaces());
	}
}
