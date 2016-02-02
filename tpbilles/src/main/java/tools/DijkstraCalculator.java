package tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import core.Environnement;
import core.escape.MurAgent;
import javafx.scene.shape.Rectangle;
import main.MainEscape;

/**
 * Created by willl on 02/02/2016.
 */
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

		System.out.println(agentX);
		System.out.println(agentY);
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

		// while (!file.isEmpty()) {
		// Position current = file.pop();
		//
		// for (int i = -1; i <= 1; i++) {
		// for (int j = -1; j <= 1; j++) {
		// if (grille[current.getY() + j][current.getX() + i] == -1) {
		// grille[current.getY() + j][current.getX() + i] =
		// grille[current.getY()][current.getX()] + 1;
		// file.push(new Position(current.getX() + i, current.getY() + j));
		// }
		// }
		// }
		// }

		dessineGrid();

	}

	private void dessineGrid() {

		for (Rectangle rec : rekt) {
			MainEscape.canvas.getChildren().removeAll(rec);
			rekt.remove(rec);
		}

		for (int y = 0; y < grille.length; y++) {
			for (int x = 0; x < grille[0].length; x++) {
				// float opacity = 0.0f;
				//
				// if (grille[y][x] > 0) {
				// opacity = 1 / grille[y][x];
				// }
				//
				// Color color = new Color(0.8, 0, 0.8, opacity);
				// Rectangle rectangle = new Rectangle(x * 5, y * 5, 5, 5);
				// rectangle.setFill(color);
				// rekt.add(rectangle);
				// MainEscape.canvas.getChildren().addAll(rectangle);

				System.out.print("\t," + grille[y][x] + " , ");

			}
			System.out.println("");
		}
		System.out.println("==================================================");
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
