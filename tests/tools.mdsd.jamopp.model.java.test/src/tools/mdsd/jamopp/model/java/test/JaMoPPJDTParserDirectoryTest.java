package tools.mdsd.jamopp.model.java.test;

import java.nio.file.Paths;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Class for testing the parseDirectory method of the JaMoPPJDTParser.
 */
public class JaMoPPJDTParserDirectoryTest extends AbstractJaMoPPTests {
    private JaMoPPJDTParser parser;

    @Override
    protected String getTestInputFolder() {
        return "";
    }

    @Override
    protected boolean isExcludedFromReprintTest(String filename) {
        return false;
    }

    @BeforeEach
    public void setUp() {
        super.initResourceFactory();
        parser = new JaMoPPJDTParser();
    }

    @Test
    public void testSrcInputDirectory() throws Exception {
        final ResourceSet set = parser.parseDirectory(Paths.get("src-input"));
        AbstractJaMoPPTests.assertModelValid(set);
        this.testReprint(set);
    }

    @Test
    @Disabled("JDT puts break statements into Blocks on the right side of a SwitchRule.")
    public void testSrcSevenAndUp() throws Exception {
        final ResourceSet set = parser.parseDirectory(Paths.get("src-sevenandup"));
        AbstractJaMoPPTests.assertModelValid(set);
        this.testReprint(set);
    }
}
