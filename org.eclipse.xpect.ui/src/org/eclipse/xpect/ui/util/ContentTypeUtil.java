/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.eclipse.xpect.ui.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.xpect.XpectConstants;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class ContentTypeUtil {

	public enum XpectContentType {
		BINARY, TEXT, XPECT
	}

	private static Logger LOG = Logger.getLogger(ContentTypeUtil.class);

	private static final String XPECT = "XPECT";
	private static final String IGNORE_TAIL = "_IGNORE";

	public static final String XPECT_CONTENT_TYPE_NAME = "text/x-xpect-xt";

	public XpectContentType getContentType(IFile file) {
		if (file == null || !file.exists())
			return XpectContentType.BINARY;
		if (XpectConstants.XT_FILE_EXT.equals(file.getFileExtension()))
			return XpectContentType.XPECT;
		Reader contents = null;
		try {
			contents = new InputStreamReader(file.getContents(), file.getCharset());
			return getContentType(contents);
		} catch (CoreException e) {
			return XpectContentType.BINARY;
		} catch (IOException e) {
			return XpectContentType.BINARY;
		} finally {
			if (contents != null)
				try {
					contents.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}

	}

	public XpectContentType getContentType(Reader contents) throws IOException {
		char[] buf = new char[2048];
		int len = contents.read(buf);
		for (int i = 0; i < len; i++) {
			char c = buf[i];
			if (c < ' ' && c != '\n' && c != '\r' && c != '\t')
				return XpectContentType.BINARY;
		}
		String stringBuf = new String(buf);
		int index = -1;
		while (true) {
			index = stringBuf.indexOf(XPECT, index + 1);
			if (index >= 0) {
				int before = index - 1;
				if (before >= 0 && Character.isAlphabetic(stringBuf.charAt(before))) {
					continue;
				}
				int after = index + XPECT.length();
				if (after < stringBuf.length() && Character.isAlphabetic(stringBuf.charAt(after))) {
					continue;
				}
				if (after + IGNORE_TAIL.length() < stringBuf.length()) {
					String tail = stringBuf.substring(after, after + IGNORE_TAIL.length());
					if (IGNORE_TAIL.equals(tail)) {
						return XpectContentType.TEXT;
					}
				}
				return XpectContentType.XPECT;
			} else {
				return XpectContentType.TEXT;
			}
		}

	}
}
