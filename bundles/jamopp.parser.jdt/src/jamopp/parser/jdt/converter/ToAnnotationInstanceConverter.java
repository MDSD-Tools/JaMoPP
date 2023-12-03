package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationParameterList;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.annotations.SingleAnnotationParameter;
import org.emftext.language.java.members.InterfaceMethod;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jamopp.parser.jdt.other.UtilJdtResolver;
import jamopp.parser.jdt.other.UtilLayout;
import jamopp.parser.jdt.other.UtilNamedElement;
import jamopp.parser.jdt.other.UtilTypeInstructionSeparation;

@Singleton
public class ToAnnotationInstanceConverter {

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
	public
	AnnotationInstance convertToAnnotationInstance(Annotation annot) {
		AnnotationInstance result = annotationsFactory.createAnnotationInstance();
		utilNamedElement.addNameToNameSpace(annot.getTypeName(), result);
		org.emftext.language.java.classifiers.Annotation proxyClass;
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

	public void setTypeInstructionSeparationUtility(UtilTypeInstructionSeparation typeInstructionSeparationUtility) {
		this.typeInstructionSeparationUtility = typeInstructionSeparationUtility;
	}

}