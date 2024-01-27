package tools.mdsd.jamopp.parser.injection;

import com.google.inject.AbstractModule;

import tools.mdsd.jamopp.parser.implementation.visitor.VisitorAndConverterAbstractAndEmptyModelJDTAST;
import tools.mdsd.jamopp.parser.interfaces.visitor.AbstractVisitor;

public class VisitorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AbstractVisitor.class).to(VisitorAndConverterAbstractAndEmptyModelJDTAST.class);
	}

}
