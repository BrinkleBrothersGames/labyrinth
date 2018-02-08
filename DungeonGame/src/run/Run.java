package run;

import java.util.Random;

import initialise.MazeBuilder;

public class Run {

	public static void main(String[] args) {
		Random rand = new Random(4000);
		MazeBuilder mg = new MazeBuilder(rand);
	}
}