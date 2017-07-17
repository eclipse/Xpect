package org.xpect.setup;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface ISetupInitializer<T> {
	void initialize(T object);

	public static class Null<T> implements ISetupInitializer<T> {
		public void initialize(T object) {
		}
	}
}
