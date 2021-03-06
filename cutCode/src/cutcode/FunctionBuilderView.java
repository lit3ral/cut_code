package cutcode;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FunctionBuilderView extends Stage {
	private ComboBox<String> retType;
	private BorderPane root;
	private List<FunctionBuilderRow> rows;
	private Scene scene;
	private String[] types;
	private double width, height;

	/**
	 * 
	 * @param types  - the list of types for return and parameters. null if untyped
	 *               language
	 * @param width  - the width of this window
	 * @param height - the height of this window
	 */
	public FunctionBuilderView(String[] types, double width, double height) {
		// initialize variables
		rows = new ArrayList<FunctionBuilderRow>();
		this.width = width;
		this.height = height;
		this.types = types;
		if (types != null) {
			retType = new ComboBox<String>(FXCollections.observableArrayList(types));
			retType.setValue(types[0]);
		}
		this.root = new BorderPane();
		this.scene = new Scene(root, width, height);
		this.setScene(scene);
		this.setResizable(false);
	}

	/**
	 * 
	 * @return the return type of this function. null if untyped.
	 */
	public String getRetType() {
		if (retType == null) {
			return null;
		}
		return this.retType.getValue();
	}

	/**
	 * 
	 * @return a list of all the parameters for this function
	 */
	public List<FunctionBuilderRow> getRows() {
		return rows;
	}

	/**
	 * Creates and displays the function builder view.
	 * 
	 * @param title - the function's name
	 */
	public void make(String title) {
		root.setPadding(new Insets(width * 0.06));

		// STEP 1: Need to add a title at the top with the name of the function
		Label label = new Label(title);
		label.setFont(new Font("Helvetica", height * 0.05));
		HBox top = new HBox(label);
		top.setAlignment(Pos.CENTER);
		root.setTop(top);

		// STEP 2: Center needs to be a scrollpane of rows
		ScrollPane middle = new ScrollPane();
		Label paramLabel = new Label("Parameters");
		paramLabel.setFont(new Font("Helvetica", height * 0.04));
		VBox rowBox = new VBox();
		if (types != null) { // if the language is typed, need a return type
			Label retLabel = new Label("Return: ");
			retLabel.setFont(paramLabel.getFont());
			HBox retBox = new HBox(retLabel, retType);
			rowBox.getChildren().add(retBox);
		}
		rowBox.getChildren().add(paramLabel);
		for (FunctionBuilderRow row : rows) { // load pre-existing parameters
			rowBox.getChildren().add(row);
		}
		rowBox.setAlignment(Pos.TOP_LEFT);
		middle.setContent(rowBox);
		middle.setMaxSize(width * 0.88, height * 0.6);
		middle.setMinSize(width * 0.88, height * 0.6);
		root.setCenter(middle);

		// STEP 3: Need to set bottom to be buttons for adding and removing parameters
		Button add = new Button("+");
		Button rem = new Button("-");
		add.setMaxSize(width * 0.44, height * 0.2);
		add.setMinSize(width * 0.44, height * 0.2);
		rem.setMaxSize(width * 0.44, height * 0.2);
		rem.setMinSize(width * 0.44, height * 0.2);
		add.setOnMouseClicked(new EventHandler<MouseEvent>() {
			// user adds a parameter
			@Override
			public void handle(MouseEvent event) {
				FunctionBuilderRow addRow = new FunctionBuilderRow(types, width * 0.84, height * 0.1);
				rows.add(addRow);
				rowBox.getChildren().add(addRow);
			}

		});

		rem.setOnMouseClicked(new EventHandler<MouseEvent>() {
			// user deletes a parameter (assumed to be the last one)
			@Override
			public void handle(MouseEvent event) {
				if (rows.size() > 0) {
					FunctionBuilderRow remRow = rows.remove(rows.size() - 1);
					rowBox.getChildren().remove(remRow);
				}
			}
		});

		HBox bottom = new HBox(add, rem);
		root.setBottom(bottom);

		this.show();
	}

}
