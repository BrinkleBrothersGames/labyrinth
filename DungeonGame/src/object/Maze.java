package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;

import graphics.Graphics;
import twoVector.TwoVector;
import xmlReader.XMLReader;

public class Maze {

	private List<Object> objects;
	private HashMap<String, Document> properties;

	public Maze() {
		
	}

	public Maze(Maze maze) {
		for (int i = 0; i < maze.objects.size(); i++) {
			objects.add(new Object(maze.objects.get(i)));
		}
	}

	public List<Object> getObjects() {
		return objects;
	}

	public boolean addObject(Object obj) {
		Position objPos = obj.getPosition();
		if ((objPos == null) || !objPos.canBePlaced(getPositions())) {
			return false;
		}
		return addObject(obj);
	}

	public boolean createObject(String type, TwoVector location) {
		Object obj = new Object(type);
		XMLReader fr = new XMLReader();
		Boolean blocksMovement = fr.getBoolean(properties.get(obj.getType()), "blocksMovement");
		char representation = fr.getChar(properties.get(obj.getType()), "representation");
		Position pos = new Position(location, blocksMovement, representation);
		if ((blocksMovement == null) || (representation == ' ') || !pos.canBePlaced(getPositions())) {
			return false;
		}
		if (!objects.add(obj)) {
			return false;
		}
		return true;
	}

	public boolean removeObject(Object obj) {
		return objects.remove(obj);
	}

	public boolean addObjects(List<Object> objects) {
		boolean returnBool = false;
		for (int i = 0; i < objects.size(); i++) {
			if (addObject(objects.get(i))) {
				returnBool = true;
			}
		}
		return returnBool;
	}

	public boolean removeObjects(List<Object> objects) {
		return objects.removeAll(objects);
	}

	public List<Position> getPositions() {
		List<Position> positions = new ArrayList<Position>();
		for (int i = 0; i < objects.size(); i++) {
			Position objPosition = objects.get(i).getPosition();
			if (objPosition != null) {
				positions.add(objPosition);
			}
		}
		return positions;
	}

	public List<Object> getNeighbouringObjects(Object obj) {
		List<Object> neighbours = new ArrayList<Object>();
		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.get(i);
			if (obj.getPosition().IsNextTo(object.getPosition())) {
				neighbours.add(object);
			}
		}
		return neighbours;
	}

	public void print() {
		List<Position> positions = getPositions();
		HashMap<TwoVector, Character> display = new HashMap<TwoVector, Character>();
		for (int i = 0; i < positions.size(); i++) {
			display.put(positions.get(i).GetCoords(), positions.get(i).GetRep());
		}
		Graphics g = new Graphics(display);
		g.Print();
	}

	public boolean contains(Object obj) {
		if (objects.contains(obj)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objects == null) ? 0 : objects.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;
		if (obj.equals(null)) {
			if (this.equals(null)) {
				return false;
			}
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Maze other = (Maze) obj;
		if (objects.equals(null)) {
			if (other.objects != null) {
				return false;
			}
			return true;
		}
		if (objects.size() != other.objects.size()) {
			return false;
		}
		if (!objects.containsAll(other.objects)) {
			return false;
		}
		return true;
	}
}
