package application;

public class DoubleBlock extends VariableBlock<Double> {

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return "double " + super.getName() + " = " + super.getValue() + ";";
	}

}