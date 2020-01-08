package graphics;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logicalBlocks.Block;

public class AndBlock extends OperatorBlock {
	
	
	protected AndBlock() {
		super(30,50);
		HBox line = new HBox();
		op1 = new VBox();
		op1.setMinWidth(70);
		op1.setMinHeight(30);
		op1.setStyle("-fx-background-color: D96969");
		op2 = new VBox();
		op2.setMinWidth(70);
		op2.setMinHeight(30);
		op2.setStyle("-fx-background-color: D96969");
		line.getChildren().addAll(op1,new Label(" and "),op2);
		this.getChildren().add(line);
		
		this.setStyle("-fx-background-color: D90000");
		this.setPadding(new Insets(10));
	}

	/**
	 * @apiNote O(1)
	 * @author Arjun Khanna
	 */
	@Override
	public Block getLogicalBlock() {
		logicalBlocks.AndBlock ret = new logicalBlocks.AndBlock();
		if(leftOperand == null)
			ret.setLeftOperand(null);
		else
			ret.setLeftOperand(leftOperand.getLogicalBlock());
		if(rightOperand == null)
			ret.setRightOperand(null);
		else
			ret.setRightOperand(rightOperand.getLogicalBlock());
		return ret;
	}

	@Override
	public GraphicalBlock cloneBlock() {
		// TODO Auto-generated method stub
		return new AndBlock();
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public String toJSON() {
		return null;
	}



	

}
