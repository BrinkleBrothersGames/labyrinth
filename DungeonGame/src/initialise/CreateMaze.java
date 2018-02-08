package initialise;

import java.util.List;
import java.util.Random;

import object.Maze;
import twoVector.TwoVector;

public class CreateMaze {

	Maze maze = new Maze();
	
	public CreateMaze(Random rand) {
		MazeBuilder mg = new MazeBuilder(rand);
		MazePopulator populator = new MazePopulator();
		addObjects(mg.getCorridorTiles(), "floor");
		addObjects(mg.getRoomTiles(), "floor");
		addObjects(mg.getDoors(), "door");
		addObjects(mg.getWalls(), "wall");
	}
	
	private void addObjects(List<TwoVector> objects, String type) {
		for (int i = 0; i < objects.size(); i++) {
			maze.createObject(type, objects.get(i));
		}
	}
	
	private void populateRooms(List<Object> objects) {
		
	}
}
