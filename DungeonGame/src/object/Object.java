package object;

import position.Position;

public class Object {

	private String type;

	private Position position;

	public Object(String type) {
		this.type = type;
	}
	
	
	public Object(String type, Position pos) {
		this.type = type;
		this.setPosition(pos);
	}
	
	
	public Object(Object obj) {
		type = obj.type;
		position = obj.position;
	}
	
	
	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}
	
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(Position pos) {
		this.position = pos;
	}
	
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	
	public Boolean isNextTo(Object other) {
		return position.IsNextTo(other.position);
	}
}
