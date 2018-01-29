package position;


import java.util.ArrayList;
import java.util.List;

import object.ObjectList;
import twoVector.TwoVector;

public class Position {
	
	private TwoVector coords;
	private TwoVector size;
	private Boolean blocksMovement;
	private char representation;
	
	public Position(TwoVector coords, TwoVector size, Boolean blocksMovement, char representation) {
		this.coords = coords;
		this.size = size;
		this.blocksMovement = blocksMovement;
		this.representation = representation;
	}
	
	
	public Position(Position pos) {
		this.coords = new TwoVector(pos.coords);
		this.size = new TwoVector(size);
		this.blocksMovement = new Boolean(blocksMovement);
		this.representation = new Character(representation);
	}
	
	public TwoVector GetCoords() {
		return coords;
	}
	
	
	public TwoVector GetSize() {
		return size;
	}
	
	
	public Boolean Move(List<Position> allPositions) {
		return this.blocksMovement;
	}
	
	
	public Boolean BlocksMovement() {
		return blocksMovement;
	}
	
	
	public char GetRep() {
		return representation;
	}
	
	public Boolean IsNextTo(Position other) {
		TwoVector relativePos = other.coords.minus(coords);
		if ((relativePos.getX() <= size.getX()) && (relativePos.getX() >= (- other.size.getX())) && (relativePos.getY() <= size.getY()) && (relativePos.getY() >= (- other.size.getY()))) {
			return true;
		}
		return false;
	}
	
	
	private Boolean OverlapsWith(Position other) {
		TwoVector relativePos = other.coords.minus(coords);
		if ((relativePos.getX() < size.getX()) && (relativePos.getX() > (- other.size.getX())) && (relativePos.getY() < size.getY()) && (relativePos.getY() > (- other.size.getY()))) {
			return true;
		}
		return false;
	}
	
	
	public Boolean Contains(TwoVector otherCoords) {
		TwoVector relativeCoordinates = this.coords.minus(otherCoords);
		if ((relativeCoordinates.getX() >= 0) && (relativeCoordinates.getX() < size.getX()) && (relativeCoordinates.getY() >= 0) && (relativeCoordinates.getY() < size.getY())) {
			return true;
		}
		return false;
	}
		
	
	public List<TwoVector> GetInternalCoordinates() {
		List<TwoVector> internalCoords = new ArrayList<TwoVector>();
		for (int i = (coords.getX()); i < (coords.getX() + size.getX()); i++) {
			for (int j = (coords.getY()); j < (coords.getY() + size.getY()); j++) {
				internalCoords.add(new TwoVector(i, j));
			}
		}
		return internalCoords;
	}
	
	
	public List<TwoVector> GetNeighbouringCoordinates() {
		List<TwoVector> neighbours = new ArrayList<TwoVector>();
		for (int i = (coords.getX() - 1); i <= (coords.getX() + size.getX()); i++) {
			for (int j = (coords.getY() - 1); j <= (coords.getY() + size.getY()); j++) {
				neighbours.add(new TwoVector(i, j));
			}
		}
		return neighbours;
	}
	
		
	/**
	 * Checks to see if the current position overlaps with any positions which block movement.
	 * @param positions
	 * @return
	 */
	public Boolean canBePlaced(List<Position> positions) {
		for (int i = 0; i < positions.size(); i++) {
			Position pos = positions.get(i);
			if (this.OverlapsWith(pos) && pos.blocksMovement) {
				return true;
			}
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
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((blocksMovement == null) ? 0 : blocksMovement.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (blocksMovement == null) {
			if (other.blocksMovement != null)
				return false;
		} else if (!blocksMovement.equals(other.blocksMovement))
			return false;
		return true;
	}
}