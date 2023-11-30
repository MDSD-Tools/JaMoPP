package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class InjectorGuice extends AbstractModule {

	@Provides
	ImportsFactory provideImportsFactory() {
		return ImportsFactory.eINSTANCE;
	}

	@Provides
	ModifiersFactory provideModifiersFactory() {
		return ModifiersFactory.eINSTANCE;
	}

	@Provides
	ExpressionsFactory provideExpressionsFactory() {
		return ExpressionsFactory.eINSTANCE;
	}
	
	@Provides
	LiteralsFactory provideLiteralsFactory() {
		return LiteralsFactory.eINSTANCE;
	}
	
	@Provides
	OperatorsFactory provideOperatorsFactory() {
		return OperatorsFactory.eINSTANCE;
	}
	

}
