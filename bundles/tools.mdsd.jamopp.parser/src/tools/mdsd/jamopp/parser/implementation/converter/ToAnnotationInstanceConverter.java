package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import com.google.inject.Singleton;

import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationParameterList;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.model.java.annotations.SingleAnnotationParameter;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

@Singleton
public class ToAnnotationInstanceConverter implements Converter<Annotation, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final Provider<UtilTypeInstructionSeparation> typeInstructionSeparationUtility;

	@Inject
	public ToAnnotationInstanceConverter(final UtilNamedElement utilNamedElement,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final AnnotationsFactory annotationsFactory,
			final Provider<UtilTypeInstructionSeparation> typeInstructionSeparationUtility) {
		this.annotationsFactory = annotationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.typeInstructionSeparationUtility = typeInstructionSeparationUtility;
	}

	@Override
	public AnnotationInstance convert(final Annotation annot) {
		final AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		utilNamedElement.addNameToNameSpace(annot.getTypeName(), result);
		tools.mdsd.jamopp.model.java.classifiers.Annotation proxyClass;
		final IAnnotationBinding binding = annot.resolveAnnotationBinding();
		if (binding == null) {
			proxyClass = jdtResolverUtility.getAnnotation(annot.getTypeName().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getAnnotation(binding.getAnnotationType());
		}
		result.setAnnotation(proxyClass);
		if (annot.isSingleMemberAnnotation()) {
			handleSingleMemberAnnotation(annot, result);
		} else if (annot.isNormalAnnotation()) {
			handleNormalAnnotation(annot, result, proxyClass);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, annot);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void handleNormalAnnotation(final Annotation annot, final AnnotationInstance result,
			final tools.mdsd.jamopp.model.java.classifiers.Annotation proxyClass) {
		final AnnotationParameterList param = annotationsFactory.createAnnotationParameterList();
		result.setParameter(param);
		final NormalAnnotation normalAnnot = (NormalAnnotation) annot;
		normalAnnot.values().forEach(obj -> {
			final MemberValuePair memVal = (MemberValuePair) obj;
			final AnnotationAttributeSetting attrSet = annotationsFactory.createAnnotationAttributeSetting();
			InterfaceMethod methodProxy;
			if (memVal.resolveMemberValuePairBinding() != null) {
				methodProxy = jdtResolverUtility
						.getInterfaceMethod(memVal.resolveMemberValuePairBinding().getMethodBinding());
			} else {
				methodProxy = jdtResolverUtility.getInterfaceMethod(memVal.getName().getIdentifier());
				if (!proxyClass.getMembers().contains(methodProxy)) {
					proxyClass.getMembers().add(methodProxy);
				}
			}
			utilNamedElement.setNameOfElement(memVal.getName(), methodProxy);
			attrSet.setAttribute(methodProxy);
			typeInstructionSeparationUtility.get().addAnnotationAttributeSetting(memVal.getValue(), attrSet);
			layoutInformationConverter.convertToMinimalLayoutInformation(attrSet, memVal);
			param.getSettings().add(attrSet);
		});
	}

	private void handleSingleMemberAnnotation(final Annotation annot, final AnnotationInstance result) {
		final SingleAnnotationParameter param = annotationsFactory.createSingleAnnotationParameter();
		result.setParameter(param);
		final SingleMemberAnnotation singleAnnot = (SingleMemberAnnotation) annot;
		typeInstructionSeparationUtility.get().addSingleAnnotationParameter(singleAnnot.getValue(), param);
	}

}
