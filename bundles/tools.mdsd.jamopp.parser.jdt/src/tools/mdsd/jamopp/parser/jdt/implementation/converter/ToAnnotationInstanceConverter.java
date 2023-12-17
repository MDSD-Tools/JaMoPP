package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.annotations.AnnotationParameterList;
import tools.mdsd.jamopp.model.java.annotations.AnnotationsFactory;
import tools.mdsd.jamopp.model.java.annotations.SingleAnnotationParameter;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

@Singleton
public class ToAnnotationInstanceConverter implements Converter<Annotation, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private UtilTypeInstructionSeparation typeInstructionSeparationUtility;

	@Inject
	ToAnnotationInstanceConverter(UtilNamedElement utilNamedElement, UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility, AnnotationsFactory annotationsFactory) {
		this.annotationsFactory = annotationsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AnnotationInstance convert(Annotation annot) {
		AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		utilNamedElement.addNameToNameSpace(annot.getTypeName(), result);
		tools.mdsd.jamopp.model.java.classifiers.Annotation proxyClass;
		IAnnotationBinding binding = annot.resolveAnnotationBinding();
		if (binding == null) {
			proxyClass = jdtResolverUtility.getAnnotation(annot.getTypeName().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getAnnotation(binding.getAnnotationType());
		}
		result.setAnnotation(proxyClass);
		if (annot.isSingleMemberAnnotation()) {
			SingleAnnotationParameter param = annotationsFactory.createSingleAnnotationParameter();
			result.setParameter(param);
			SingleMemberAnnotation singleAnnot = (SingleMemberAnnotation) annot;
			typeInstructionSeparationUtility.addSingleAnnotationParameter(singleAnnot.getValue(), param);
		} else if (annot.isNormalAnnotation()) {
			AnnotationParameterList param = annotationsFactory.createAnnotationParameterList();
			result.setParameter(param);
			NormalAnnotation normalAnnot = (NormalAnnotation) annot;
			normalAnnot.values().forEach(obj -> {
				MemberValuePair memVal = (MemberValuePair) obj;
				AnnotationAttributeSetting attrSet = annotationsFactory.createAnnotationAttributeSetting();
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
				typeInstructionSeparationUtility.addAnnotationAttributeSetting(memVal.getValue(), attrSet);
				layoutInformationConverter.convertToMinimalLayoutInformation(attrSet, memVal);
				param.getSettings().add(attrSet);
			});
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, annot);
		return result;
	}

	@Inject
	public void setTypeInstructionSeparationUtility(UtilTypeInstructionSeparation typeInstructionSeparationUtility) {
		this.typeInstructionSeparationUtility = typeInstructionSeparationUtility;
	}

}
