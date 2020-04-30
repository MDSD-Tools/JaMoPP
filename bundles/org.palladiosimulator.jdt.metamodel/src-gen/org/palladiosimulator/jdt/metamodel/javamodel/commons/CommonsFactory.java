/**
 */
package org.palladiosimulator.jdt.metamodel.javamodel.commons;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.palladiosimulator.jdt.metamodel.javamodel.commons.CommonsPackage
 * @generated
 */
public interface CommonsFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CommonsFactory eINSTANCE = org.palladiosimulator.jdt.metamodel.javamodel.commons.impl.CommonsFactoryImpl.init();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    CommonsPackage getCommonsPackage();

} //CommonsFactory
