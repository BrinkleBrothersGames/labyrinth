package object;

public class Object {

	private String type;

	private Position pos;
	private Inventory inv;

	public Object(String type) {
		this.type = type;
	}

	public Object(String type, Position pos) {
		this.type = type;
		this.setPosition(pos);
	}

	public Object(Object obj) {
		type = obj.type;
		pos = obj.pos;
	}

	public Position getPosition() {
		return pos;
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	public String getType() {
		return type;
	}

	public Boolean isNextTo(Object other) {
		return pos.IsNextTo(other.pos);
	}
}
