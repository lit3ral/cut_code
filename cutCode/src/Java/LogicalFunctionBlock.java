package Java;

import java.util.List;

import cutcode.LogicalBlock;

public class LogicalFunctionBlock extends LogicalBlock {
	private String name;
	private String retType;
	private String[] parameters;
	private List<LogicalBlock> executeBlocks;

	@Override
	public String toString() {
		if (retType == null)
			retType = "void";
		String indents = "";
		for (int i = 0; i < getIndentFactor(); i++)
			indents += "	";
		String ret = indents + "public static " + retType + " " + name + "(";
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				if (i == parameters.length - 1) {
					ret = ret + parameters[i];
				} else {
					ret = ret + parameters[i] + ", ";
				}
			}
		}
		ret = ret + ") {" + System.lineSeparator();
		for (LogicalBlock l : executeBlocks)
			ret = ret + l.toString();
		ret = ret + indents + "}" + System.lineSeparator();
		return ret;
	}

	/**
	 * 
	 * @param parameters - an array of strings for the parameters of the function
	 */
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * 
	 * @param executeBlocks - a list of logical blocks that are executed when the function are called
	 */
	public void setExecuteBlocks(List<LogicalBlock> executeBlocks) {
		this.executeBlocks = executeBlocks;
	}

	/**
	 * 
	 * @param name - the name of the function
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param type - the return type of the function
	 */
	public void setRetType(String type) {
		this.retType = type;
	}

}
