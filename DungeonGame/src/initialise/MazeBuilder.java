package initialise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Document;

import twoVector.TwoVector;
import xmlReader.XMLReader;

public class MazeBuilder {

	Boolean[][] maze;
	List<List<TwoVector>> rooms;
	private int xCells;
	private int yCells;
	private int cellSize;
	private int wallSize;
	private double roomProportion;
	private int idealRoomArea;
	private int minRoomArea;
	private int idealNumberOfCorners;
	private double cornerWeighting;
	private int idealNumberOfDoors;
	private double doorWeighting;
	private double surfaceAreaWeighting;

	public MazeBuilder(Random rand) {
		readParameters();
		makeBlankMaze();
		addCorridors(rand);
		rooms = getRooms(rand);
	}

	public List<TwoVector> getWalls() {
		List<TwoVector> walls = new ArrayList<TwoVector>();
		for (int j = 0; j < maze.length; j++) {
			for (int i = 0; i < maze[j].length; j++) {
				if(!maze[j][i]) {
					walls.add(new TwoVector(i, j));
				}
			}
		}
		return getAllCoords(walls);
	}

	public List<TwoVector> getRoomTiles() {
		List<TwoVector> roomTiles = new ArrayList<TwoVector>();
		List<TwoVector> tiles = allFloorTiles();
		for (int i = 0; i < tiles.size(); i++) {
			TwoVector tile = tiles.get(i);
			for (int j = 0; j < rooms.size(); j++) {
				if (rooms.get(j).contains(tile)) {
					roomTiles.add(tile);
				}
			}
		}
		return getAllCoords(roomTiles);
	}

	public List<TwoVector> getCorridorTiles() {
		List<TwoVector> corridorTiles = allFloorTiles();
		corridorTiles.removeAll(getRoomTiles());
		return getAllCoords(corridorTiles);
	}

	private List<TwoVector> allFloorTiles() {
		List<TwoVector> tiles = new ArrayList<TwoVector>();
		for (int j = 0; j < maze.length; j++) {
			for (int i = 0; i < maze[j].length; j++) {
				if (maze[j][i]) {
					tiles.add(new TwoVector(i, j));
				}
			}
		}
		return getAllCoords(tiles);
	}

	public List<TwoVector> getDoors() {
		List<TwoVector> walls = new ArrayList<TwoVector>();
		for (int j = 0; j < maze.length; j++) {
			for (int i = 0; i < maze[j].length; j++) {
				if(!maze[j][i]) {
					walls.add(new TwoVector(i, j));
				}
			}
		}
		return getAllCoords(walls);
	}

	private List<TwoVector> getAllCoords(List<TwoVector> coords) {
		List<TwoVector> allCoords = new ArrayList<TwoVector>();
		for (int c = 0; c < coords.size(); c++) {
			TwoVector v = coords.get(c);
			List<Integer> xCoords = getInts(v.getX());
			List<Integer> yCoords = getInts(v.getY());
			for (int i = 0; i < xCoords.size(); i++) {
				for (int j = 0; j < yCoords.size(); j++) {
					allCoords.add(new TwoVector(i, j));
				}
			}
		}
		return allCoords;
	}

	private List<Integer> getInts(int i) {
		int size = wallSize + cellSize;
		List<Integer> ints = new ArrayList<Integer>();
		if ((i % 2) == 0) {
			i /= 2;
			i *= size;
			for (int j = 0; j < wallSize; i++) {
				ints.add(i + j);
			}
		}
		else {
			i -= 1;
			i /= 2;
			i = size;
			i += wallSize;
			for (int j = 0; j < wallSize; i++) {
				ints.add(i + j);
			}
		}
		return ints;
	}

	private void readParameters() {
		XMLReader fr = new XMLReader();
		Document doc = fr.getDocument("MazeProperties");
		xCells = fr.getInt(doc, "xCells");
		yCells = fr.getInt(doc, "yCells");
		roomProportion = fr.getDouble(doc, "roomProportion");
		idealRoomArea = fr.getInt(doc, "idealRoomArea");
		minRoomArea = fr.getInt(doc, "minRoomArea");
		idealNumberOfCorners = fr.getInt(doc, "idealNumberOfCorners");
		cornerWeighting = fr.getDouble(doc, "cornerWeighting");
		idealNumberOfDoors = fr.getInt(doc, "idealNumberOfDoors");
		doorWeighting = fr.getDouble(doc, "doorWeighting");
		surfaceAreaWeighting = fr.getDouble(doc, "surfaceAreaWeighting");
	}

	private void makeBlankMaze() {
		this.maze = new Boolean[1 + 2 * yCells][1 + 2 * xCells];
		for (int i = 0; i <= xCells; i++) {
			for (int j = 0; j <= yCells; j++) {
				TwoVector cellCorner = new TwoVector(2 * i, 2 * j);
				setBool(cellCorner, false);
				if (i < xCells) {
					setBool(cellCorner.plusX(), false);
				}
				if (j < yCells) {
					setBool(cellCorner.plusY(), false);
				}
				if ((i < xCells) && (j < yCells)) {
					setBool(cellCorner.plusX().plusY(), true);
				}
			}
		}
	}

	private void addCorridors(Random rand) {
		List<TwoVector> walls = getInternalConnectingWalls();
		while (getCells().size() > getCells(getConnectedObjects(new TwoVector(1, 1))).size()) {
			TwoVector randWall = walls.get(rand.nextInt(walls.size()));
			List<TwoVector> cells = getNeighbouringCells(randWall);
			if ((cells.size() == 2) && !getConnectedObjects(cells.get(0)).contains(cells.get(1))) {
				setBool(randWall, true);
				walls.remove(randWall);
			}
		}
	}

	private List<List<TwoVector>> getRooms(Random rand) {
		List<List<TwoVector>> rooms = new ArrayList<List<TwoVector>>();
		List<TwoVector> mazeCells = getCells();
		List<Integer> roomSizes = getRoomSizes(rand);
		for (int i = 0; i < roomSizes.size(); i++) {
			List<List<TwoVector>> allRooms = getAllRooms(roomSizes.get(i), mazeCells, rand);
			List<TwoVector> lowestEnergyRoom = getLowestEnergyRoom(allRooms, rand);
			mazeCells.removeAll(getCells(lowestEnergyRoom));
			rooms.add(lowestEnergyRoom);
		}
		return rooms;
	}

	private List<List<TwoVector>> getAllRooms(int roomSize, List<TwoVector> cells, Random rand) {
		List<List<TwoVector>> allRooms = new ArrayList<List<TwoVector>>();
		for (int i = 0; i < cells.size(); i++) {
			List<TwoVector> room = addCellToRoom(new ArrayList<TwoVector>(), cells.get(i));
			allRooms.add(room);
		}
		for (int i = 1; i < roomSize; i++) {
			List<List<TwoVector>> newRooms = new ArrayList<List<TwoVector>>();
			for (int j = 0; j < allRooms.size(); j++) {
				List<TwoVector> room = allRooms.get(j);
				List<TwoVector> adjacentCells = getCellsAdjacentToRoom(room, cells);
				for (int k = 0; k < adjacentCells.size(); k++) {
					List<TwoVector> newRoom = addCellToRoom(room, adjacentCells.get(k));
					if (!roomListContainsRoom(newRooms, newRoom)) { 
						newRooms.add(newRoom);
					}
				}
			}
			allRooms = newRooms;
		}
		return allRooms;
	}

	private List<TwoVector> addCellToRoom(List<TwoVector> room, TwoVector cellToAdd) {
		List<TwoVector> newRoom = new ArrayList<TwoVector>();
		newRoom.addAll(room);
		newRoom.add(cellToAdd);
		List<TwoVector> neighbours = cellToAdd.adjacentCoordinates();
		for (int i = 0; i < neighbours.size(); i++) {
			TwoVector v = neighbours.get(i);
			if (!newRoom.contains(v)) {
				newRoom.add(v);
			}
		}
		return newRoom;
	}

	private Boolean roomListContainsRoom(List<List<TwoVector>> roomList, List<TwoVector> room) {
		for (int i = 0; i < roomList.size(); i++) {
			if (room.containsAll(roomList.get(i))) {
				return true;
			}
		}
		return false;
	}

	private List<TwoVector> getLowestEnergyRoom(List<List<TwoVector>> allRooms, Random rand) {
		List<List<TwoVector>> lowestEnergyRooms = new ArrayList<List<TwoVector>>();
		lowestEnergyRooms.add(allRooms.get(0));
		double lowestEnergy = getRoomEnergy(lowestEnergyRooms.get(0));
		for (int i = 1; i < allRooms.size(); i++) {
			List<TwoVector> roomToCheck = allRooms.get(i);
			double energyToCheck = getRoomEnergy(roomToCheck);
			if (energyToCheck < lowestEnergy) {
				lowestEnergyRooms = new ArrayList<List<TwoVector>>();				
				lowestEnergyRooms.add(roomToCheck);
				lowestEnergy = energyToCheck;
			}
			else if (energyToCheck == lowestEnergy) {
				getRoomEnergy(roomToCheck);
				lowestEnergyRooms.add(roomToCheck);
			}
		}
		return lowestEnergyRooms.get(rand.nextInt(lowestEnergyRooms.size()));
	}

	private double getRoomEnergy(List<TwoVector> room) {
		double energy = Math.abs(idealNumberOfCorners - getCorners(room)) * cornerWeighting;
		energy += Math.abs(idealNumberOfDoors - getSortedDoors(room).size()) * doorWeighting;
		energy += getSurfaceArea(room) * surfaceAreaWeighting;		//in this case, surface area should always be minimised.
		return energy;
	}

	private int getSurfaceArea(List<TwoVector> room) {
		List<TwoVector> cells = getCells(room);
		int surfaceArea = cells.size() * 4;
		for (int i = 0; i < (cells.size() - 1); i++) {
			for (int j = (i + 1); j < cells.size(); j++) {
				if (cellsAreAdjacent(cells.get(i), cells.get(j))) {
					surfaceArea -= 2;
				}
			}
		}
		return surfaceArea;
	}

	private List<TwoVector> getAllDoors(List<TwoVector> room) {
		List<TwoVector> doors = new ArrayList<TwoVector>();
		for (int i = 0; i < room.size(); i++) {
			TwoVector v = room.get(i);
			if (getBool(v) && (!room.containsAll(getNeighbours(v)))) {
				doors.add(v);
			}
		}
		return doors;
	}

	private List<List<TwoVector>> getSortedDoors(List<TwoVector> room) {
		List<List<TwoVector>> sortedDoors = new ArrayList<List<TwoVector>>();
		List<TwoVector> doors = getAllDoors(room);
		List<TwoVector> roomCells = getCells(room);
		for (int j = 0; j < roomCells.size(); j++) {
			setBool(roomCells.get(j), false);
		}
		for (int i = 0; i < doors.size(); i++) {
			TwoVector door = doors.get(i);
			Boolean makeNewEntry = true;
			for (int j = 0; j < sortedDoors.size(); j++) {
				if (doorsAreConnected(door, sortedDoors.get(j).get(0))) {
					sortedDoors.get(j).add(door);
					makeNewEntry = false;
					break;
				}
			}
			if (makeNewEntry) {
				List<TwoVector> newDoor = new ArrayList<TwoVector>();
				newDoor.add(door);
				sortedDoors.add(newDoor);
			}
		}
		for (int i = 0; i < roomCells.size(); i++) {
			setBool(roomCells.get(i), true);
		}
		return sortedDoors;
	}

	private Boolean doorsAreConnected(TwoVector doorA, TwoVector doorB) {
		List<TwoVector> cells = getNeighbouringCells(doorA);
		if (cells.size() != 2) {
			return false;
		}
		if (getConnectedObjects(cells.get(0)).contains(doorB)) {
			return true;
		}
		if (getConnectedObjects(cells.get(1)).contains(doorB)) {
			return true;
		}
		return false;
	}

	private int getCorners(List<TwoVector> room) {
		int corners = 0;
		for (int r = 0; r < room.size(); r++) {
			TwoVector position = room.get(r);
			for (int i = -1; i < 2; i +=2) {
				for (int j = -1; j < 2; j +=2) {
					TwoVector X = position.add(new TwoVector(i, 0));
					TwoVector Y = position.add(new TwoVector(0, j));
					TwoVector XY = position.add(new TwoVector(i, j));
					if (!room.contains(X) && !room.contains(Y) && !room.contains(XY)) {
						corners += 1;
					}	// Finds 'external' corners; if there are no objects in any three neighbouring adjacent positions, 
						// there must be a corner.
					if (room.contains(X) && room.contains(Y) && !room.contains(XY)) {
						corners += 1;
					}	// Finds 'internal' corners; if there is no object in a diagonally neighbouring position, 
						// but there are objects in both the horizontally and vertically neighbouring positions, there must be a corner.
				}
			}
		}
		return corners;
	}

	private List<TwoVector> getCellsAdjacentToRoom(List<TwoVector> room, List<TwoVector> cells) {
		List<TwoVector> adjacentCells = new ArrayList<TwoVector>();
		for (int i = 0; i < cells.size(); i++) {
			TwoVector cell = new TwoVector(cells.get(i));
			List<TwoVector> roomCells = getCells(room);
			for (int j = 0; j < roomCells.size(); j++) {
				if (cellsAreAdjacent(cell, roomCells.get(j)) && !room.contains(cell) && !adjacentCells.contains(cell)) {
					adjacentCells.add(cell);
					break;
				}
			}
		}
		return adjacentCells;
	}
	
	private Boolean cellsAreAdjacent(TwoVector cellA, TwoVector cellB) {
		TwoVector distance = cellA.minus(cellB);
		if (distance.magnitude() == 2) {
			return true;
		}
		return false;
	}

	private void setBool(TwoVector position, Boolean bool) {
		maze[maze.length - position.getY() - 1][position.getX()] = bool;
	}

	private Boolean getBool(TwoVector position) {
		return maze[maze.length - position.getY() - 1][position.getX()];
	}

	private Boolean isOutsideMaze(TwoVector v) {
		if ((v.getX() <= 0) || (v.getY() <= 0) || (v.getY() >= maze.length - 1) || (v.getX() >= maze[0].length - 1)) {
			return true;
		}
		return false;
	}
	
	private Boolean isCellCentre(TwoVector v) {
		if (isHorizontalWall(v) || isHorizontalWall(v)) {
			return true;
		}
		return false;
	}

	private Boolean isHorizontalWall(TwoVector v) {
		if ((((v.getX() - 1) % 2) == 0) && (((v.getY() - 1) % 2) == 0)) {
			return true;
		}
		return false;
	}

	private Boolean isCellCorner(TwoVector v) {
		if (((v.getX() % 2) == 0) && ((v.getY() % 2) == 0)) {
			return true;
		}
		return false;
	}

	private Boolean isCellEdge(TwoVector v) {
		if (!isOutsideMaze(v) && !isCellCentre(v) && !isCellCorner(v)) {
			return true;
		}
		return false;
	}

	private List<TwoVector> getAllPositions() {
		List<TwoVector> positions = new ArrayList<TwoVector>();
		for (int j = 0; j < maze.length; j++) {
			for (int i = 0; i < maze[0].length; i++) {
				positions.add(new TwoVector(i, j));
			}
		}
		return positions;
	}

	private List<TwoVector> getInternalConnectingWalls() {
		List<TwoVector> walls = new ArrayList<TwoVector>();
		List<TwoVector> positions = getAllPositions();
		for (int i = 0; i < positions.size(); i++) {
			TwoVector v = positions.get(i);
			if (isCellEdge(v) && !getBool(v)) {
				walls.add(v);
			}
		}
		return walls;
	}

	private List<TwoVector> getCells() {
		List<TwoVector> cells = new ArrayList<TwoVector>();
		List<TwoVector> positions = getAllPositions();
		for (int i = 0; i < positions.size(); i++) {
			TwoVector v = positions.get(i);
			if (isCellCentre(v)) {
				cells.add(v);
			}
		}
		return cells;
	}

	private List<TwoVector> getCells(List<TwoVector> room) {
		List<TwoVector> cells = new ArrayList<TwoVector>();
		List<TwoVector> allCells = getCells();
		for (int i = 0; i < allCells.size(); i++) {
			TwoVector cell = allCells.get(i);
			if (room.contains(cell)) {
				cells.add(cell);
			}
		}
		return cells;
	}

	private List<TwoVector> getNeighbours(TwoVector v) {
		List<TwoVector> neighbours = new ArrayList<TwoVector>();
		if (v.getY() < (maze.length - 1)) {
			neighbours.add(v.plusY());
		}
		if (v.getX() < (maze[0].length - 1)) {
			neighbours.add(v.plusX());
		}
		if (v.getY() > 0) {
			neighbours.add(v.minusY());
		}
		if (v.getX() > 0) {
			neighbours.add(v.minusX());
		}
		return neighbours;
	}

	private List<TwoVector> getNeighbouringCells(TwoVector v) {
		List<TwoVector> neighbours = getNeighbours(v);
		List<TwoVector> neighbouringCells = new ArrayList<TwoVector>();
		for (int i = 0; i < neighbours.size(); i++) {
			TwoVector position = neighbours.get(i);
			if (isCellCentre(position)) {
				neighbouringCells.add(position);
			}
		}
		return neighbouringCells;
	}

	private List<TwoVector> getConnectedObjects(TwoVector v) {
		if (!isCellCentre(v)) {
			return null;
		}
		List<TwoVector> connectedObjects = new ArrayList<TwoVector>();
		List<TwoVector> objectsToCheck = new ArrayList<TwoVector>();
		objectsToCheck.add(v);
		while (objectsToCheck.size() > 0) {
			TwoVector vec = objectsToCheck.get(0);
			if (!connectedObjects.contains(vec)) {
				connectedObjects.add(vec);
				if (getBool(vec)) {
					objectsToCheck.addAll(getNeighbours(vec));
				}
			}
			objectsToCheck.remove(vec);
		}
		return connectedObjects;
	}

	private List<Integer> getRoomSizes(Random rand) {
		List<Integer> roomSizes = new ArrayList<Integer>();
		int cellsInRooms = 0;
		while (cellsInRooms < (getCells().size() * roomProportion)) {
			int roomArea = (int) (minRoomArea + (2 * rand.nextDouble() * (idealRoomArea - minRoomArea)));
			cellsInRooms += roomArea;
			roomSizes.add(roomArea);
		}
		return roomSizes;
	}
	/**
	 * 
	 * @param pos
	 * @return
	private char getWallChar(TwoVector pos) {
		Boolean horizontalWall = false;
		Boolean verticalWall = false;
		if (((pos.getX() > 0) && (!getBool(pos.minusX()).equals(true))) || ((pos.getX() < (maze[0].length - 1)) && (!getBool(pos.plusX())))) {
			horizontalWall = true;
		}
		if (((pos.getY() > 0) && (!getBool(pos.minusY()))) || ((pos.getY() < (maze.length - 1)) && (!getBool(pos.plusY())))) {
			verticalWall = true;
		}
		if (horizontalWall && verticalWall) {
			return '+';
		}
		if (horizontalWall) {
			return '-';
		}
		if (verticalWall) {
			return '|';
		}
		return ' ';
	}

	public void print() {
		System.out.println(" ");
		for (int j = (maze.length - 1); j >= 0; j--) {
			String line = "";
			for (int i = 0; i < maze[j].length; i++) {
				TwoVector v = new TwoVector(i, j);
				if (getBool(v)) {
					line += ".";
				}
				else {
					line += getWallChar(v);
				}
			
			}
			System.out.println(line);
		}
	}
	
	private void print(List<TwoVector> room) {
		System.out.println(" ");
		for (int j = (maze.length - 1); j >= 0; j--) {
			String line = "";
			for (int i = 0; i < maze[j].length; i++) {
				TwoVector v = new TwoVector(i, j);
				if (!room.contains(v)) {
					line += " ";
				}
				else if (getBool(v)) {
					line += ".";
				}
				else {
					line += getWallChar(v);
				}
			
			}
			System.out.println(line);
		}
	}
	*/
}