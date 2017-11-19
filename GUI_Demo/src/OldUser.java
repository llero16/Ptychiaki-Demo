public class OldUser extends User {

	public OldUser(String id) {
		super(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = String.format("%04d", id);
	}
}
