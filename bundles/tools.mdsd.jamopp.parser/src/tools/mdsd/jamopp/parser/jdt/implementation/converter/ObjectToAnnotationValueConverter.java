package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

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
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ObjectToAnnotationValueConverter implements Converter<Object, AnnotationValue> {

	private final ArraysFactory arraysFactory;
	private final ReferencesFactory referencesFactory;
	private final JdtResolver jdtTResolverUtility;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;
	private final Converter<ITypeBinding, Reference> bindingToInternalReferenceConverter;
	private final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter;

	@Inject
	public ObjectToAnnotationValueConverter(final ReferencesFactory referencesFactory,
			final Converter<Object, PrimaryExpression> objectToPrimaryExpressionConverter,
			final JdtResolver jdtTResolverUtility,
			final Converter<ITypeBinding, Reference> bindingToInternalReferenceConverter,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter,
			final ArraysFactory arraysFactory) {
		this.arraysFactory = arraysFactory;
		this.referencesFactory = referencesFactory;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
		this.bindingToInternalReferenceConverter = bindingToInternalReferenceConverter;
		this.objectToPrimaryExpressionConverter = objectToPrimaryExpressionConverter;
	}

	@Override
	public AnnotationValue convert(final Object value) {
		AnnotationValue result;
		if (value instanceof final IVariableBinding varBind) {
			final Reference parentRef = bindingToInternalReferenceConverter.convert(varBind.getDeclaringClass());
			final IdentifierReference varRef = referencesFactory.createIdentifierReference();
			varRef.setTarget(jdtTResolverUtility.getEnumConstant(varBind));
			parentRef.setNext(varRef);
			result = getTopReference(varRef);
		} else if (value instanceof IAnnotationBinding) {
			result = bindingToAnnotationInstanceConverter.convert((IAnnotationBinding) value);
		} else if (value instanceof final Object[] values) {
			final tools.mdsd.jamopp.model.java.arrays.ArrayInitializer initializer = arraysFactory
					.createArrayInitializer();
			for (final Object value2 : values) {
				initializer.getInitialValues()
						.add((tools.mdsd.jamopp.model.java.arrays.ArrayInitializationValue) convert(value2));
			}
			result = initializer;
		} else if (value instanceof ITypeBinding) {
			final Reference parentRef = bindingToInternalReferenceConverter.convert((ITypeBinding) value);
			final ReflectiveClassReference classRef = referencesFactory.createReflectiveClassReference();
			parentRef.setNext(classRef);
			result = getTopReference(classRef);
		} else {
			result = objectToPrimaryExpressionConverter.convert(value);
		}
		return result;
	}

	private Reference getTopReference(final Reference ref) {
		Reference currentRef = ref;
		Reference parentRef = ref.getPrevious();
		while (parentRef != null) {
			currentRef = parentRef;
			parentRef = currentRef.getPrevious();
		}
		return currentRef;
	}

}
