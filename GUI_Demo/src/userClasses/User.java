package userClasses;

public abstract class User {

	String id;
	
	public User(String id)
	{
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

}