package graphics;

import logicalBlocks.Block;

public class OrBlock extends OperatorBlock {
	
	/**
	 * @apiNote O(1)
	 * @author Arjun Khanna
	 */
	@Override
	public Block getLogicalBlock() {
		logicalBlocks.OrBlock ret = new logicalBlocks.OrBlock();
		ret.setLeftOperand(getLeftOperand().getLogicalBlock());
		ret.setRightOperand(getRightOperand().getLogicalBlock());
		return ret;
	}

	@Override
	public GraphicalBlock cloneBlock() {
		// TODO Auto-generated method stub
		return null;
	}

}
