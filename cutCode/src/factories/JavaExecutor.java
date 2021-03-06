package factories;

import cutcode.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class JavaExecutor extends Executor {
	private String filename;
	public static final String PREFIX = "public class Program {" + System.lineSeparator();
	public static final String SUFFIX = "}";
	private String compileCommand;
	private String runKeyword;
	public JavaExecutor(String filename, String compileCommand, String runKeyword) {
		this.filename = filename;
		this.compileCommand = compileCommand;
		this.runKeyword = runKeyword;
	}


	@Override
	public String execute(HashMap<Integer, GraphicalBlock> lineLocations) throws BlockCodeCompilerErrorException {
		Process p;
		Process p2;
		try {
			p = Runtime.getRuntime().exec(compileCommand + "  Program.java"); // this will compile the program
			p.waitFor();
			p2 = Runtime.getRuntime().exec(runKeyword + " Program"); // this will run the program

			BufferedReader errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			// this is where the process outputs error messages

			String errorLine = errorStream.readLine(); // takes the error statement. null if no errors
			String errorOutput = "";
			while(errorLine != null) {
				errorOutput += errorLine + System.lineSeparator();
				errorLine = errorStream.readLine();
			}
			if(!errorOutput.equals("")) { //Error in code
				GraphicalBlock errorBlock = lineLocations.get(extractError(errorOutput));
				if(errorBlock != null)
					errorBlock.tagErrorOnBlock();
				throw new BlockCodeCompilerErrorException();
			}

			errorStream.close();
			BufferedReader outputStream = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String line = outputStream.readLine();
			String output = "";
			while(line != null) { //No errors, get program output
				output += line + System.lineSeparator();
				line = outputStream.readLine();
			}
			outputStream.close();
			return output;

		} catch (InterruptedException | IOException e) {
			return "An unexpected error occured"; // No way to forsee this
		}
	}

	@Override
	public void export(String code, String filename) throws IOException { //writes code to given file name
		FileManager manager = new FileManager();
		manager.delete(filename);
		manager.setOutput(filename);
		manager.openWriter();
		manager.write(PREFIX);

		manager.write(code);
		manager.write(SUFFIX);
		manager.closeWriter();
	}

	@Override
	public void export(List<LogicalBlock> logicalBlocks, String filename) throws IOException {
		FileManager manager = new FileManager();
		manager.setOutput(filename);
		manager.openWriter();
		manager.write(PREFIX);
		for (LogicalBlock block : logicalBlocks) //translates block to string
			manager.write(block.toString());
		manager.write(SUFFIX);
		manager.closeWriter();
	}

	@Override
	public int extractError(String error) {
		return Integer.parseInt(error.split(":")[1]);
		//converts error message to line number and checks hashmap to check which graphical block that is
	}

}
