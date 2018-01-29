package graphics;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import twoVector.TwoVector;


public class Graphics {
	
	private char[][] tileMaze;
	
	
	public Graphics(HashMap<TwoVector, Character> maze) {
		Iterator<TwoVector> it = maze.keySet().iterator();
		TwoVector min = it.next();
		TwoVector max = new TwoVector(min);
		while (it.hasNext()) {
			TwoVector next = it.next();
			if (next.getX() < min.getX()) {
				min.setX(next.getX());
			}
			if (next.getY() < min.getY()) {
				min.setY(next.getY());
			}
			if (next.getX() > max.getX()) {
				max.setX(next.getX());
			}
			if (next.getY() > max.getY()) {
				max.setY(next.getY());
			}
		}
		tileMaze = BlankMaze(max.minus(min));
		Iterator<Entry<TwoVector, Character>> entryIt = maze.entrySet().iterator();
		while (entryIt.hasNext()) {
			Entry<TwoVector, Character> entry = entryIt.next();
			AddCharacter(entry.getKey().minus(min), entry.getValue());
		}
	}
	
	
	private char[][] BlankMaze(TwoVector size) {
		char[][] blankMaze = new char[size.getY()][size.getX()];
		for (int j = 0; j < size.getY(); j++) {
			for (int i = 0; i < size.getX(); i++) {
				blankMaze[j][i] = ' ';
			}
		}
		return blankMaze;
	}
	
	private void AddCharacter(TwoVector location, char character) {
		tileMaze[tileMaze.length - (location.getY() + 1)][location.getX()] = character;
	}
	
	
	public void Print() {
		//PrintBlankLines(tileMaze.length);
		System.out.println(" ");
		for (int j = (tileMaze.length - 1); j >= 0; j--) {
			String line = "";
			for (int i = 0; i < tileMaze[j].length; i++) {
				
				line += tileMaze[j][i];
			}
			System.out.println(line);
		}
	}
	
	
	public void Save(String fileName) {
		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			for (int j = (tileMaze.length - 1); j >= 0; j--) {
				String line = "";
				for (int i = 0; i < tileMaze[j].length; i++) {
					line += tileMaze[j][i];
				}
				writer.print(line);
			}
			writer.close();
		} 
		catch (Exception e) {
			
		}
	}
}