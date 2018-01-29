package twoVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoVector {
	// position
	private int x;
	private int y;
	
	public TwoVector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
     * 
     * @param maxX
     * @param maxY
     * @param rand
     * @return		Random coordinates with values between 0 and specified numbers.
     */
    public TwoVector(int maxX, int maxY, Random rand) {
    	this.x = rand.nextInt(maxX);
    	this.y = rand.nextInt(maxY);
    }
    
    
    public TwoVector(TwoVector v) {
    	this.x = new Integer(v.x);
    	this.y = new Integer(v.y);
    }
	
    
	public TwoVector() {
		this.x = 0;
		this.y = 0;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	public TwoVector add(TwoVector v) {
		return new TwoVector(x + v.x, y + v.y);
	}
	
	
	public TwoVector minus(TwoVector v) {
		return new TwoVector(x - v.x, y - v.y);
	}
	
	
	/**
	 * 
	 * @return	Coordinates with x increased by one.
	 */
	public TwoVector plusX() {
		return new TwoVector(x+1, y);
	}
	
	/**
	 * 
	 * @return	Coordinates with y increased by one.
	 */
	public TwoVector plusY() {
		return new TwoVector(x, y+1);
	}
	
	/**
	 * 
	 * @return	Coordinates with x decreased by one.
	 */
	public TwoVector minusX() {
		return new TwoVector(x-1, y);
	}
	
	/**
	 * 
	 * @return  Coordinates with y decreased by one.
	 */
	public TwoVector minusY() {
		return new TwoVector(x, y-1);
	}
	
	
	/**
	 * 
	 * @return  All coordinates adjacent to given coords, starting with positive y direction, and moving clockwise around.
	 */
	public List<TwoVector> adjacentCoordinates() {
		List<TwoVector> adj = new ArrayList<TwoVector>();
		adj.add(this.plusY());
		adj.add(this.plusY().plusX());
		adj.add(this.plusX());
		adj.add(this.plusX().minusY());
		adj.add(this.minusY());
		adj.add(this.minusY().minusX());
		adj.add(this.minusX());
		adj.add(this.minusX().plusY());
		return adj;
	}
	
	
	public double magnitude() {
		return (Math.sqrt((x * x) + (y * y)));
	}
	
	public TwoVector multiply(int factor) {
		return new TwoVector(x * factor, y * factor);
	}

	public Boolean isNextTo(TwoVector v) {
		if (adjacentCoordinates().contains(v)) {
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
		result = prime * result + x;
		result = prime * result + y;
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
		TwoVector other = (TwoVector) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
