package Java;

import cutcode.LogicalBlock;

/**
 * @deprecated
 */
public class LogicalBreakBlock extends LogicalBlock {

	@Override
	public String toString() {
		String indents = "";
		for(int i = 0; i < getIndentFactor(); i++)
			indents += "	";
		return indents + "break;" + System.lineSeparator();
	}
}
