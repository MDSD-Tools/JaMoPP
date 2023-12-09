package jamopp.parser.jdt.injector;

import com.google.inject.AbstractModule;

import jamopp.parser.jdt.visitor.AbstractVisitor;
import jamopp.parser.jdt.visitor.VisitorAndConverterAbstractAndEmptyModelJDTAST;

public class VisitorModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(AbstractVisitor.class).to(VisitorAndConverterAbstractAndEmptyModelJDTAST.class);
	}

}
