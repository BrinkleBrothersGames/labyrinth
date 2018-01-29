package run;

import java.util.Random;

import initialiseGame.MazeGenerator;

public class Run {

	public static void main(String[] args) {
		Random rand = new Random(4000);
		MazeGenerator mg = new MazeGenerator(rand);
		mg.print();
	}
}