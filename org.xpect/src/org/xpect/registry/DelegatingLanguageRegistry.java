package org.xpect.registry;

import java.util.Collection;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class DelegatingLanguageRegistry implements ILanguageInfo.Registry {

	private ILanguageInfo.Registry delegate;

	public ILanguageInfo.Registry getDelegate() {
		return delegate;
	}

	public ILanguageInfo getLanguageByFileExtension(String fileExtension) {
		return delegate.getLanguageByFileExtension(fileExtension);
	}

	public ILanguageInfo getLanguageByName(String languageName) {
		return delegate.getLanguageByName(languageName);
	}

	public Collection<ILanguageInfo> getLanguages() {
		return delegate.getLanguages();
	}

	public void setDelegate(ILanguageInfo.Registry delegate) {
		this.delegate = delegate;
	}

}
