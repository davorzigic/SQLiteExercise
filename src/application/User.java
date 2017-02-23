package application;

import javafx.beans.property.SimpleStringProperty;

public class User {
	
	private final SimpleStringProperty id;
	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;
	private final SimpleStringProperty age;
	private final SimpleStringProperty username;
	private final SimpleStringProperty password;
	private final SimpleStringProperty DOB;
	
	
	public User(String id,String fName, String lName, String age, String username, String password, String dob) {
		this.id = new SimpleStringProperty(id);
		this.firstName = new SimpleStringProperty(fName);
		this.lastName = new SimpleStringProperty(lName);
		this.age = new SimpleStringProperty(age);
		this.username = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.DOB = new SimpleStringProperty(dob);
	}


	public SimpleStringProperty getUsername() {
		return username;
	}

	public SimpleStringProperty getId() {
		return id;
	}

	public SimpleStringProperty getPassword() {
		return password;
	}

	public SimpleStringProperty getDOB() {
		return DOB;
	}


	public SimpleStringProperty getFirstName() {
		return firstName;
	}


	public SimpleStringProperty getLastName() {
		return lastName;
	}


	public SimpleStringProperty getAge() {
		return age;
	}

}
