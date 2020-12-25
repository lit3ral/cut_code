package python;

import cutcode.BlockCodeCompilerErrorException;
import cutcode.LogicalBlock;
import cutcode.GraphicalBlock;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import cutcode.InvalidNestException;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphicalIfBlock extends GraphicalBlock {
	private GraphicalBooleanBinaryOperatorBlock condition; // TODO make this OperatorBlock
	private VBox[] nestBoxes;
	private HashMap<VBox, double[]> nestDimensions;

	public GraphicalIfBlock() { //sets up visuals of the block
		super(200, 80);

		nestDimensions = new HashMap<>();

		this.setPadding(new Insets(10));
		this.setBackground(new Background(new BackgroundFill(Color.web("#907FDE"), CornerRadii.EMPTY, Insets.EMPTY)));

		HBox topLine = new HBox();
		topLine.setSpacing(8);
		topLine.getChildren().addAll(new Label("if"));
		VBox bottomLine = new VBox();
		bottomLine.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		bottomLine.setMinWidth(140);
		bottomLine.setMinHeight(32);
		double[] bottomLineDimensions = {140.0, 32.0};
		nestDimensions.put(bottomLine, bottomLineDimensions);
		VBox conditionSpace = new VBox();
		conditionSpace.setMinHeight(30);
		conditionSpace.setMinWidth(140);
		double[] conditionSpaceDimensions = {140.0, 30.0};
		nestDimensions.put(conditionSpace, conditionSpaceDimensions);
		conditionSpace.setBackground(
				new Background(new BackgroundFill(Color.web("#E6E6E6"), CornerRadii.EMPTY, Insets.EMPTY)));

		topLine.getChildren().add(conditionSpace);

		this.getChildren().addAll(topLine, bottomLine);

		nestBoxes = new VBox[2];
		nestBoxes[0] = conditionSpace;
		nestBoxes[1] = bottomLine;
	}

	public GraphicalIfBlock(double width, double height) {
		super(width, height);
	}

	@Override
	public LogicalBlock getLogicalBlock() throws BlockCodeCompilerErrorException {
		if(nestBoxes[0].getChildren().size() != 1) {
			tagErrorOnBlock();
			throw new BlockCodeCompilerErrorException();
		}
		ArrayList<LogicalBlock> executeBlocks = new ArrayList<>();
		for(Node n : nestBoxes[1].getChildren()) { //gets all the blocks to be executed if the if statement evaluates to true
			((GraphicalBlock) n).setIndentFactor(getIndentFactor() + 1);
			executeBlocks.add(((GraphicalBlock) n).getLogicalBlock());
		}
		return logicalFactory.createIf(getIndentFactor(), ((GraphicalBlock) nestBoxes[0].getChildren().get(0)).getLogicalBlock(), executeBlocks);
	}

	@Override
	public GraphicalBlock cloneBlock() {
		return new GraphicalIfBlock();
	}

	/**
	 * @return an array of 2 points, the top left of the slots of the operand spaces, regardless of whether something
	 * is already nested there or not.
	 */
	@Override
	public Point2D[] getNestables() {
		Point2D[] ret = new Point2D[nestBoxes.length];
		ret[0] = nestBoxes[0].localToScene(nestBoxes[0].getLayoutBounds().getMinX(), nestBoxes[0].getLayoutBounds().getMinY());
		double secondaryIncrementY = 0;
		for (Node n : nestBoxes[1].getChildren())
			secondaryIncrementY += ((GraphicalBlock) n).getHeight();
		ret[1] = nestBoxes[1].localToScene(nestBoxes[1].getLayoutBounds().getMinX(), nestBoxes[0].getLayoutBounds().getMinY() + secondaryIncrementY);
		return ret;
	}


	@Override
	public void nest(int index, GraphicalBlock nest) throws InvalidNestException {

		if (index == 0) {
			VBox box = nestBoxes[0];
			if (nestBoxes[index].getChildren().size() != 0)
				throw new InvalidNestException();
			increment(box, nest);
			box.getChildren().add(nest);

		} else if (index == 1) {
			VBox box = nestBoxes[1];
			double incrementWidth = nest.getWidth() - box.getWidth();
			double incrementHeight = nest.getHeight() - box.getHeight();
			if (box.getChildren().size() != 0) {
				for (Node n : box.getChildren()) {
					incrementWidth += ((GraphicalBlock) n).getWidth();
					incrementHeight += ((GraphicalBlock) n).getHeight();
				}
			}
			increment(box, nest);
			box.getChildren().add(nest);
		} else
			throw new InvalidNestException();
		nest.setNestedIn(this);
	}


	@Override
	public void unnest(VBox box, GraphicalBlock rem) throws InvalidNestException {
		//resets sizes if necessary and removes rem
		box.getChildren().remove(rem);
		double[] dimensions = nestDimensions.get(box);
		if (dimensions != null && dimensions.length == 2) {
			box.setMinWidth(dimensions[0]);
			box.setMinHeight(dimensions[1]);
			box.setMaxWidth(dimensions[0]);
			box.setMaxHeight(dimensions[1]);
		}
		if (nestBoxes[0].getChildren().size() == 0 && nestBoxes[1].getChildren().size() == 0) { //full size reset for block
			this.setMinWidth(200);
			this.setMinHeight(80);
			this.setMaxWidth(200);
			this.setMaxHeight(80);
		}
	}

	@Override
	public int putInHashMap(HashMap<Integer, GraphicalBlock> lineLocations) {
		lineLocations.put(getLineNumber(), this);
		int ret = getLineNumber() + 1;
		for(Node n : nestBoxes[1].getChildren()) {
			if(n instanceof GraphicalBlock) {
				((GraphicalBlock) n).setLineNumber(ret);
				ret = ((GraphicalBlock) n).putInHashMap(lineLocations);
			}
		}
		return ret + logicalFactory.getEndingBrace(); //Block class reused for java so might have ending brace
	}


	@Override
	public ArrayList<GraphicalBlock> getChildBlocks() {
		ArrayList<GraphicalBlock> ret = new ArrayList<>();
		for (VBox box : nestBoxes) {
			for (Node b : box.getChildren()) {
				if (b instanceof GraphicalBlock) {
					ret.add((GraphicalBlock) b);
				}
			}
		}
		return ret;
	}


}
