package tools.mdsd.jamopp.test;

public class JavaSevenAndUpTestWithoutReprinting extends JavaSevenAndUpTest {
    @Override
    public boolean isExcludedFromReprintTest(String filename) {
        return true;
    }
}
