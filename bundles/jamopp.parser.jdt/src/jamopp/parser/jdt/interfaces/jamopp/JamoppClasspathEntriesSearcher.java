package jamopp.parser.jdt.interfaces.jamopp;

import java.nio.file.Path;

public interface JamoppClasspathEntriesSearcher {

	String[] getClasspathEntries(Path dir);

}