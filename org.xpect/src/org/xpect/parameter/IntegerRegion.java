package org.xpect.parameter;

public class IntegerRegion extends DerivedRegion {
	public IntegerRegion(IStatementRelatedRegion origin) {
		super(origin, -1, -1);
	}

	public IntegerRegion(IStatementRelatedRegion origin, int offset, int length) {
		super(origin, offset, length);
	}

	public int getIntegerValue() {
		String text = getRegionText();
		if (text != null)
			return Integer.valueOf(text);
		return 0;
	}
}
