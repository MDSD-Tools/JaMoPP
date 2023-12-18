package tools.mdsd.jamopp.model.java.test;

public class JavaSevenAndUpTestWithoutReprinting extends JavaSevenAndUpTest {
    @Override
    public boolean isExcludedFromReprintTest(String filename) {
        return true;
    }
}
