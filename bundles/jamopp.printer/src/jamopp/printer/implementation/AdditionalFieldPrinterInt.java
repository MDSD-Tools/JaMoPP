package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.members.AdditionalField;

import com.google.inject.ImplementedBy;

import jamopp.printer.interfaces.Printer;

@ImplementedBy(AdditionalFieldPrinter.class)
interface AdditionalFieldPrinterInt extends Printer<AdditionalField> {

}