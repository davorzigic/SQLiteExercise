package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Main extends Application {

	Connection conn;
	PreparedStatement pst = null;
	ResultSet rs = null;
	TextField id, fn, ln, age, un;
	PasswordField pass;
	DatePicker date;

	@Override
	public void start(Stage primaryStage) {
		try {
			CheckConnection();
				
			
			
						
			// create transparent stage

			//primaryStage.initStyle(StageStyle.TRANSPARENT);
			
			primaryStage.setTitle("Java Tutorial");
			BorderPane layout = new BorderPane();
			Scene newScene = new Scene(layout, 1000, 500, Color.rgb(0, 0, 0));
			
			
			Group root = new Group();
			Scene scene = new Scene(root, 320,250, Color.rgb(0,0,0,0));

			Color foreground = Color.rgb(255,255,255,0.9);

			// Rectangle background

			Rectangle background = new Rectangle(320,250);
			background.setX(0);
			background.setY(0);
			background.setArcHeight(15);
			background.setArcWidth(15);
			background.setFill(Color.rgb(0,0,0,0.55));
			background.setStroke(foreground);
			background.setStrokeWidth(1.5);
		
			VBox vbox = new VBox(5);
			vbox.setPadding(new Insets(10,0,0,10));

			Label label = new Label("Label");
			label.setTextFill(Color.WHITESMOKE);
			label.setFont(new Font("SanSerif",20));
			
			TextField username = new TextField();
			username.setFont(Font.font("SanSerif",20));
			username.setPromptText("Username");

			PasswordField password = new PasswordField();
			password.setFont(Font.font("SanSerif",20));
			password.setPromptText("Password");
			
			
			// Login button
			Button btn = new Button("Login");
			btn.setFont(Font.font("SanSerif",15));
			btn.setOnAction(e -> {
				try {
					String query = "select * from employee where username=? and password= ?";
					pst = conn.prepareStatement(query);
					pst.setString(1, username.getText());
					pst.setString(2, password.getText());
					rs = pst.executeQuery();
					
					if(rs.next()) {
						label.setText("Login Successful");
						primaryStage.setScene(newScene);
						primaryStage.show();
					} else {
						label.setText("Login Failed");
					}
					pst.close();
					rs.close();
				} catch (Exception e1) {
					label.setText("SQL Error");
					System.err.println(e1);
				}
			});

			vbox.getChildren().addAll(label, username, password, btn);
			root.getChildren().addAll(background, vbox);

			VBox fields = new VBox(5);
			
			// Screen for entering the data and table
			Label label1 = new Label("Create New User");
			label1.setFont(new Font("SanSerif",20));

			id = new TextField();
			id.setFont(Font.font("SanSerif",20));
			id.setPromptText("ID");
			id.setMaxWidth(300);

			fn = new TextField();
			fn.setFont(Font.font("SanSerif",20));
			fn.setPromptText("First name");
			fn.setMaxWidth(300);

			ln = new TextField();
			ln.setFont(Font.font("SanSerif",20));
			ln.setPromptText("Last name");
			ln.setMaxWidth(300);

			age = new TextField();
			age.setFont(Font.font("SanSerif",20));
			age.setPromptText("Age");
			age.setMaxWidth(300);

			un = new TextField();
			un.setFont(Font.font("SanSerif",20));
			un.setPromptText("Username");
			un.setMaxWidth(300);

			pass = new PasswordField();
			pass.setFont(Font.font("SanSerif",20));
			pass.setPromptText("Password");
			pass.setMaxWidth(300);
			
			date = new DatePicker();
			date.setPromptText("Date of birth");
			date.setMaxWidth(300);
			date.setStyle("-fx-font-size:20");
			
			
			// Save button
			Button button = new Button("Save");
			button.setFont(Font.font("SanSerif",15));
			button.setOnAction(e -> {
				try {
					String query = "INSERT INTO employee (id, firstName, lastName, age, username, password, DOB) VALUES(?,?,?,?,?,?,?)";
					pst = conn.prepareStatement(query);
					pst.setString(1, id.getText());
					pst.setString(2, fn.getText());
					pst.setString(3, ln.getText());
					pst.setString(4, age.getText());
					pst.setString(5, un.getText());
					pst.setString(6, pass.getText());
					pst.setString(7, ((TextField)date.getEditor()).getText());
					pst.execute();
					pst.close();
					
					clearFields();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information dialog");
					alert.setHeaderText(null);
					alert.setContentText("User has been created!");
					alert.showAndWait();
					
					
					
				} catch (Exception e1) {
					label.setText("SQL Error");
					System.err.println(e1);
				}
			});

			fields.getChildren().addAll(label1, id, fn, ln, age, un, pass, date, button);
			layout.setCenter(fields);
			
			BorderPane.setMargin(fields, new Insets(0,0,0,20));
			
			
			// Table for viewing data inside database
			TableView<User> table = new TableView<>();
			final ObservableList<User> data = FXCollections.observableArrayList();
			 
			
			// Getting the ID from the database and inserting it into the first column
			TableColumn<User, String> column1 = new TableColumn<User,String>("ID");
			column1.setMinWidth(20);
			column1.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
			column1.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // u.getValue() returns the Person instance for a particular TableView row
			    	
			        return u.getValue().getId();
			     }
			  });
		
			// Getting the first name from the database and inserting it into the second column			
			TableColumn<User, String> column2 = new TableColumn<User,String>("First name");
			column2.setMinWidth(80);
			column2.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
			column2.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // u.getValue() returns the Person instance for a particular TableView row
			    	
			        return u.getValue().getFirstName();
			     }
			  });
			
			// Getting the last name from the database and inserting it into the third column
			TableColumn<User, String> column3 = new TableColumn<User,String>("Last Name");
			column3.setMinWidth(80);
			column3.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
			column3.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // u.getValue() returns the Person instance for a particular TableView row
			    	
			        return u.getValue().getLastName();
			     }
			  });
			
			// Getting the age from the database and inserting it into the fourth column
			TableColumn<User, String> column4 = new TableColumn<User,String>("Age");
			column4.setMinWidth(80);
			column4.setCellValueFactory(new PropertyValueFactory<User, String>("age"));
			column4.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // u.getValue() returns the Person instance for a particular TableView row
			    	
			        return u.getValue().getAge();
			     }
			  });
			
			// Getting the username from the database and inserting it into the fifth column
			TableColumn<User, String> column5 = new TableColumn<User,String>("Username");
			column5.setMinWidth(80);
			column5.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
			column5.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // p.getValue() returns the Person instance for a particular TableView row
			    	
			        return u.getValue().getUsername();
			     }
			  });
			
			// Getting the password from the database and inserting it into the sixth column
			TableColumn<User, String> column6 = new TableColumn<User,String>("Password");
			column6.setMinWidth(80);
			column6.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
			column6.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // u.getValue() returns the Person instance for a particular TableView row
			    	
			        return u.getValue().getPassword();
			     }
			  });
			
			
			// Getting the DOB from the database and inserting it into the seventh column
			TableColumn<User, String> column7 = new TableColumn<User,String>("DOB");
			column7.setMinWidth(80);
			column7.setCellValueFactory(new PropertyValueFactory<User, String>("DOB"));
			column7.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
			    public ObservableValue<String> call(CellDataFeatures<User, String> u) {
			        // u.getValue() returns the User instance for a particular TableView row
			    	
			        return u.getValue().getDOB();
			     }
			  });
			
				
//			TableColumn column2 = new TableColumn("First Name");
//			column2.setMinWidth(200);
//			column2.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//				
//			TableColumn column3 = new TableColumn("Last Name");
//			column3.setMinWidth(200);
//			column3.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//				
//			TableColumn column4 = new TableColumn("Age");
//			column4.setMinWidth(200);
//			column4.setCellValueFactory(new PropertyValueFactory<>("age"));
//			
//			TableColumn column5 = new TableColumn("Username");
//			column5.setMinWidth(200);
//			column5.setCellValueFactory(new PropertyValueFactory<>("username"));
//			
//			TableColumn column6 = new TableColumn("Password");
//			column6.setMinWidth(200);
//			column6.setCellValueFactory(new PropertyValueFactory<>("password"));
//
//			TableColumn column7 = new TableColumn("DOB");
//			column7.setMinWidth(200);
//			column7.setCellValueFactory(new PropertyValueFactory<>("DOB"));
			
				
			table.getColumns().addAll(column1,column2,column3,column4,column5,column6,column7);
			
			layout.setRight(table);
			BorderPane.setMargin(table, new Insets(0,10,10,0));
			
			Button load = new Button("Load");
			load.setFont(Font.font("SanSerif",15));
			load.setOnAction(e -> {
				try {
					String query = "select * from employee";
					
					pst = conn.prepareStatement(query);
					rs = pst.executeQuery();
					
					while(rs.next()) {
						data.add(new User(
								rs.getString("id"),
								rs.getString("firstName"),
								rs.getString("lastName"),
								rs.getString("age"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("DOB")
							));
						table.setItems(data);
					}
					pst.close();
					rs.close();
				} catch (Exception e2) {
					System.err.println(e2);
					
				}
			});
			
			HBox hBox = new HBox(5);
			hBox.getChildren().add(load);
			layout.setBottom(hBox);
			BorderPane.setMargin(hBox, new Insets(10,0,10,10));
			
			
			primaryStage.setScene(scene);	
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/***
	 * This method checks do we have the connection with the database
	 */
	public void CheckConnection() {
		conn = SqlConnection.DbConnector();
		if (conn == null) {
			System.out.println("Connection Not Successful");
			System.exit(1);
		} else {
			System.out.println("Connection Successful");
		}
	}

	/***
	 * This method clears the fields after pressing the Save button
	 */
	public void clearFields() {
		id.clear();
		fn.clear();
		ln.clear();
		age.clear();
		un.clear();
		pass.clear();
		date.setValue(null);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
