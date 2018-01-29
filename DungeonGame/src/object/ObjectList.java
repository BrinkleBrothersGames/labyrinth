package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import fileReader.FileReader;
import graphics.Graphics;
import position.Position;
import twoVector.TwoVector;

public class ObjectList {
	
	private List<Object> objects;
	private HashMap<String, Document> properties;
	
	public ObjectList(List<Object> objects) {
		this.objects = objects;
	}
	
	public ObjectList(ObjectList objectList) {
		for (int i = 0; i < objectList.objects.size(); i++) {
			objects.add(new Object(objectList.objects.get(i)));
		}
	}
	
	public List<Object> getObjects() {
		return objects;
	}
	
	
	
	public Boolean AddObject(Object obj) {
		return objects.add(obj);
	}
	
	
	public Boolean createObject(String type) {
		Object obj = new Object(type);
		return objects.add(obj);
	}
	
	
	public Boolean createObject(String type, TwoVector location, TwoVector size) {
		Object obj = new Object(type);
		objects.add(obj);
		if (!addPosition(obj, location, size)) {
			objects.remove(obj);
			return false;
		}
		return true;
	}
	
	
	public Boolean addPosition(Object obj, TwoVector location, TwoVector size) {
		FileReader fr = new FileReader();
		Boolean blocksMovement = fr.getBoolean(properties.get(obj.getType()), "blocksMovement");
		char representation = fr.getChar(properties.get(obj.getType()), "representation");
		if ((blocksMovement == null) || (representation == ' ')) {
			return false;
		}
		Position pos = new Position(location, size, blocksMovement, representation);
		if (!pos.canBePlaced(GetPositions())) {
			return false;
		}
		if (objects.remove(obj)) {
			obj.setPosition(pos);
			return objects.add(obj);
		}
		return false;
	}
	
	public Boolean removeObject(Object randWall) {
		return objects.remove(randWall);
	}
	
	public Boolean AddObjects(List<Object> objects) {
		return objects.addAll(objects);
	}
	
	public Boolean RemoveObjects(List<Object> objects) {
		return objects.removeAll(objects);
	}
	
		
	public List<Position> GetPositions() {
		List<Position> positions = new ArrayList<Position>();
		for (int i = 0; i < objects.size(); i++) {
			Position objPosition = objects.get(i).getPosition();
			if (objPosition != null) {
				positions.add(objPosition);
			}
		}
		return positions;
	}
	
		
	public List<Object> GetNeighbouringObjects(Object obj) {
		List<Object> neighbours = new ArrayList<Object>();
		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.get(i);
			if (obj.getPosition().IsNextTo(object.getPosition())) {
				neighbours.add(object);
			}
		}
		return neighbours;
	}
	
	
	public void Print() {
		List<Position> positions = GetPositions();
		HashMap<TwoVector, Character> display = new HashMap<TwoVector, Character>();
		for (int i = 0; i < positions.size(); i++) {
			List<TwoVector> internalCoords = positions.get(i).GetInternalCoordinates();
			for (int j = 0; j < internalCoords.size(); j++) {
				display.put(internalCoords.get(i), positions.get(i).GetRep());
			}
		}
		Graphics g = new Graphics(display);
		g.Print();
	}
	
	
	public Boolean contains(Object obj) {
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
		ObjectList other = (ObjectList) obj;
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
