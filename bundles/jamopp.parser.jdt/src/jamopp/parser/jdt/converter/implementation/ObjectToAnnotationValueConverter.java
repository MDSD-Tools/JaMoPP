package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.references.ReflectiveClassReference;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ObjectToAnnotationValueConverter implements ToConverter<Object, AnnotationValue> {

	private final ArraysFactory arraysFactory;
	private final ReferencesFactory referencesFactory;
	private final UtilJdtResolver jdtTResolverUtility;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;
	private final BindingToInternalReferenceConverter bindingToInternalReferenceConverter;
	private final ObjectToPrimaryExpressionConverter objectToPrimaryExpressionConverter;

	@Inject
	public ObjectToAnnotationValueConverter(ReferencesFactory referencesFactory,
			ObjectToPrimaryExpressionConverter objectToPrimaryExpressionConverter, UtilJdtResolver jdtTResolverUtility,
			BindingToInternalReferenceConverter bindingToInternalReferenceConverter,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter, ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.referencesFactory = referencesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.bindingToInternalReferenceConverter = bindingToInternalReferenceConverter;
		this.objectToPrimaryExpressionConverter = objectToPrimaryExpressionConverter;
	}

	@Override
	public AnnotationValue convert(Object value) {
		if (value instanceof IVariableBinding varBind) {
			Reference parentRef = bindingToInternalReferenceConverter.convert(varBind.getDeclaringClass());
			IdentifierReference varRef = referencesFactory.createIdentifierReference();
			varRef.setTarget(jdtTResolverUtility.getEnumConstant(varBind));
			parentRef.setNext(varRef);
			return getTopReference(varRef);
		}
		if (value instanceof IAnnotationBinding) {
			return bindingToAnnotationInstanceConverter.convert((IAnnotationBinding) value);
		}
		if (value instanceof Object[] values) {
			org.emftext.language.java.arrays.ArrayInitializer initializer = arraysFactory.createArrayInitializer();
			for (Object value2 : values) {
				initializer.getInitialValues()
						.add((org.emftext.language.java.arrays.ArrayInitializationValue) convert(value2));
			}
			return initializer;
		}
		if (value instanceof ITypeBinding) {
			Reference parentRef = bindingToInternalReferenceConverter.convert((ITypeBinding) value);
			ReflectiveClassReference classRef = referencesFactory.createReflectiveClassReference();
			parentRef.setNext(classRef);
			return getTopReference(classRef);
		}
		return objectToPrimaryExpressionConverter.convert(value);
	}

	private Reference getTopReference(Reference ref) {
		Reference currentRef = ref;
		Reference parentRef = ref.getPrevious();
		while (parentRef != null) {
			currentRef = parentRef;
			parentRef = currentRef.getPrevious();
		}
		return currentRef;
	}

}
