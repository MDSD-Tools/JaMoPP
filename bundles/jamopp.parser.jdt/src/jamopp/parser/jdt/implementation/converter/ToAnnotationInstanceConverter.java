package jamopp.parser.jdt.implementation.converter;

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

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.IUtilTypeInstructionSeparation;

@Singleton
public class ToAnnotationInstanceConverter implements Converter<Annotation, AnnotationInstance> {

	private final AnnotationsFactory annotationsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilJdtResolver jdtResolverUtility;
	private final IUtilNamedElement utilNamedElement;
	private IUtilTypeInstructionSeparation typeInstructionSeparationUtility;

	@Inject
	ToAnnotationInstanceConverter(IUtilNamedElement utilNamedElement, IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility, AnnotationsFactory annotationsFactory) {
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

	@Inject
	public void setTypeInstructionSeparationUtility(IUtilTypeInstructionSeparation typeInstructionSeparationUtility) {
		this.typeInstructionSeparationUtility = typeInstructionSeparationUtility;
	}

}
