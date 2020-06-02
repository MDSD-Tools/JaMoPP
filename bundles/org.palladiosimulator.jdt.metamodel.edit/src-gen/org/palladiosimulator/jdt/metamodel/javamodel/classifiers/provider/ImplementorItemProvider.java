/**
 */
package org.palladiosimulator.jdt.metamodel.javamodel.classifiers.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.palladiosimulator.jdt.metamodel.javamodel.annotations.provider.JavaModelEditPlugin;

import org.palladiosimulator.jdt.metamodel.javamodel.classifiers.ClassifiersPackage;
import org.palladiosimulator.jdt.metamodel.javamodel.classifiers.Implementor;

import org.palladiosimulator.jdt.metamodel.javamodel.commons.provider.CommentableItemProvider;

import org.palladiosimulator.jdt.metamodel.javamodel.types.TypesFactory;

/**
 * This is the item provider adapter for a {@link org.palladiosimulator.jdt.metamodel.javamodel.classifiers.Implementor} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ImplementorItemProvider extends CommentableItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImplementorItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

        }
        return itemPropertyDescriptors;
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS);
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean shouldComposeCreationImage() {
        return true;
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        return getString("_UI_Implementor_type");
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(Implementor.class)) {
        case ClassifiersPackage.IMPLEMENTOR__IMPLEMENTS:
            fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
            return;
        default:
            super.notifyChanged(notification);
            return;
        }
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createClassifierReference()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createNamespaceClassifierReference()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createBoolean()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createByte()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createChar()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createDouble()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createFloat()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createInt()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createLong()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createShort()));

        newChildDescriptors.add(createChildParameter(ClassifiersPackage.Literals.IMPLEMENTOR__IMPLEMENTS,
                TypesFactory.eINSTANCE.createVoid()));
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return JavaModelEditPlugin.INSTANCE;
    }

}