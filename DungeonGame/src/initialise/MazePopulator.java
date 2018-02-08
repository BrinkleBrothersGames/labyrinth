package initialise;

import java.util.List;

import object.Maze;
import twoVector.TwoVector;
import xmlReader.XMLReader;

public class MazePopulator {

	private List<String> roomObjects;
	private List<String> coridoorObjects;

	public MazePopulator(Maze maze) {
		XMLReader x = new XMLReader();
		roomObjects = x.getDocument(RoomObjects);
	}

	public List<Object> getCorridorObjects(List<TwoVector> corridors) {
		
	}
}
