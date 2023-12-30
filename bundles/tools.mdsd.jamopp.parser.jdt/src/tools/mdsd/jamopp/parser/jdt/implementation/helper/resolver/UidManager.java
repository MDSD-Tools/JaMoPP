package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

public class UidManager {

	private int uid;

	public void prepareNextUid() {
		uid++;
	}

	public int getUid() {
		return uid;
	}

}
