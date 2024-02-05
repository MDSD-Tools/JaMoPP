package tools.mdsd.jamopp.parser.implementation.resolver;

import com.google.inject.Singleton;

import tools.mdsd.jamopp.parser.interfaces.resolver.UidManager;

@Singleton
public class UidManagerImpl implements UidManager {

	private int uid;

	@Override
	public void prepareNextUid() {
		uid++;
	}

	@Override
	public int getUid() {
		return uid;
	}

}
