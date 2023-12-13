package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.MemberContainer;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MemberContainerPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MemberContainerPrinter.class)
public interface MemberContainerPrinterInt extends Printer<MemberContainer> {

}