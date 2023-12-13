package jamopp.printer.interfaces.printer;

import org.emftext.language.java.members.Member;

import com.google.inject.ImplementedBy;

import jamopp.printer.implementation.MemberPrinter;
import jamopp.printer.interfaces.Printer;

@ImplementedBy(MemberPrinter.class)
public interface MemberPrinterInt extends Printer<Member> {

}