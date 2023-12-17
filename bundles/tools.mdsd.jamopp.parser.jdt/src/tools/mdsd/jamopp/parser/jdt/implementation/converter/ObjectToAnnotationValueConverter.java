package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.arrays.ArraysFactory;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.references.ReflectiveClassReference;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;

public class ObjectToAnnotationValueConverter implements Converter<Object, AnnotationValue> {

	private final ArraysFactory arraysFactory;
	private final ReferencesFactory referencesFactory;
	private final UtilJdtResolver jdtTResolverUtility;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<ITypeBinding, Reference> bindingToInternalReferenceConverter;
	private final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter;

	@Inject
	public ObjectToAnnotationValueConverter(ReferencesFactory referencesFactory,
			Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter,
			UtilJdtResolver jdtTResolverUtility,
			Converter<ITypeBinding, Reference> bindingToInternalReferenceConverter,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter,
			ArraysFactory arraysFactory) {
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
			tools.mdsd.jamopp.model.java.arrays.ArrayInitializer initializer = arraysFactory.createArrayInitializer();
			for (Object value2 : values) {
				initializer.getInitialValues()
						.add((tools.mdsd.jamopp.model.java.arrays.ArrayInitializationValue) convert(value2));
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
