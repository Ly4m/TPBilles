package tools;

import core.Environnement;
import core.escape.MurAgent;
import javafx.scene.shape.Rectangle;
import main.MainEscape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DijkstraCalculator {

	public int[][] grille;
	Environnement env;
	List<Rectangle> rekt;

	public DijkstraCalculator(Environnement environnement) {
		grille = new int[environnement.getLocations()[0].length][environnement.getLocations().length];
		this.env = environnement;
		this.rekt = new ArrayList<>();
		dijkstraInit();
	}

	public void dijkstra() {
		int x, y;
		LinkedList<Position> list = new LinkedList<>();

		// reset dijkstra
		dijkstraInit();
		int agentX = MainEscape.playerAgent.getPosX();
		int agentY = MainEscape.playerAgent.getPosY();

		grille[MainEscape.playerAgent.getPosY()][MainEscape.playerAgent.getPosX()] = 0;

		// init list
		list.add(new Position(agentX, agentY));

		while (list.size() > 0) {
			Position nextCase = list.removeFirst();
			x = nextCase.getX();
			y = nextCase.getY();
			int voisin = grille[y][x] + 1;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (grille[y + j][x + i] == -1 || grille[y + j][x + i] > voisin) {
						grille[y + j][x + i] = voisin;
						list.addLast(new Position(x + i, y + j));
						// file.push(new Position(agentX + i, agentY + j));
					}
				}
			}
		}

		dessineGrid();

	}

	private void dessineGrid() {

		for (Rectangle rec : rekt) {
			MainEscape.canvas.getChildren().removeAll(rec);
			rekt.remove(rec);
		}

	}

	private void dijkstraInit() {
		// init du tableau de valeur
		for (int i = 0; i < grille[0].length; i++) {
			for (int j = 0; j < grille.length; j++) {
				grille[i][j] = -1;
				if (env.getLocations()[i][j] instanceof MurAgent) {
					grille[i][j] = -2;
				}
			}
		}
	}
}
