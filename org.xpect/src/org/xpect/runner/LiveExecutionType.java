package org.xpect.runner;

import org.eclipse.xtext.validation.CheckType;

// org.eclipse.xtext.validation.CheckType + DISABLED 
public enum LiveExecutionType {
	DISABLED {
		@Override
		public CheckType toCheckType() {
			return null;
		}
	},
	FAST {
		@Override
		public CheckType toCheckType() {
			return CheckType.FAST;
		}
	};

	public abstract CheckType toCheckType();
}
