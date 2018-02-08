package object;


import java.util.ArrayList;
import java.util.List;

import twoVector.TwoVector;

public class Position {

	private TwoVector coords;
	private Boolean blocksMovement;
	private char representation;

	public Position(TwoVector coords, Boolean blocksMovement, char representation) {
		this.coords = coords;
		this.blocksMovement = blocksMovement;
		this.representation = representation;
	}

	public Position(Position pos) {
		this.coords = new TwoVector(pos.coords);
		this.blocksMovement = new Boolean(blocksMovement);
		this.representation = new Character(representation);
	}

	public TwoVector GetCoords() {
		return coords;
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
		if (this.coords.isNextTo(other.coords)) {
			return true;
		}
		return false;
	}

	private Boolean OverlapsWith(Position other) {
		if (coords.equals(other.coords)) {
			return true;
		}
		return false;
	}

	public List<TwoVector> GetNeighbouringCoordinates() {
		return coords.adjacentCoordinates();
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
		result = prime * result + ((blocksMovement == null) ? 0 : blocksMovement.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Position pos) {
		if (this == pos)
			return true;
		if (pos == null)
			return false;
		if (getClass() != pos.getClass())
			return false;
		Position other = (Position) pos;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		if (blocksMovement == null) {
			if (other.blocksMovement != null)
				return false;
		} else if (!blocksMovement.equals(other.blocksMovement))
			return false;
		return true;
	}
}