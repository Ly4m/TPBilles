package tools;

import core.Environnement;
import core.escape.MurAgent;
import javafx.scene.shape.Rectangle;
import main.MainEscape;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by willl on 02/02/2016.
 */
public class DijkstraCalculator {

    public int[][] grille;
    Environnement env;
    Stack<Position> stack;
    List<Rectangle> rekt;


    int agentX = MainEscape.playerAgent.getPosX();
    int agentY = MainEscape.playerAgent.getPosY();

    public DijkstraCalculator(Environnement environnement) {
        grille = new int[environnement.getLocations()[0].length][environnement.getLocations().length];
        this.env = environnement;
        stack = new Stack<>();
        this.rekt = new ArrayList<>();
        dijkstraInit();
    }

    public void compute() {

        dijkstraInit();

        grille[MainEscape.playerAgent.getPosY()][MainEscape.playerAgent.getPosX()] = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (grille[agentY + j][agentX + i] == -1) {
                    grille[agentY + j][agentX + i] = 1;
                    stack.push(new Position(agentX + i, agentY + j));
                }
            }
        }

        while (!stack.isEmpty()) {
            Position current = stack.pop();

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (grille[current.getY() + j][current.getX() + i] == -1) {
                        grille[current.getY() + j][current.getX() + i] = grille[current.getY()][current.getX()] + 1;
                        stack.push(new Position(current.getX() + i, current.getY() + j));
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

        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[0].length; x++) {
//                float opacity = 0.0f;
//
//                if (grille[y][x] > 0) {
//                    opacity = 1 / grille[y][x];
//                }
//
//                Color color = new Color(0.8, 0, 0.8, opacity);
//                Rectangle rectangle = new Rectangle(x * 5, y * 5, 5, 5);
//                rectangle.setFill(color);
//                rekt.add(rectangle);
//                MainEscape.canvas.getChildren().addAll(rectangle);

                System.out.print("\t,"  + grille[y][x] +  " , ");



            }
            System.out.println("");
        }
        System.out.println("==================================================");
    }

    private void dijkstraInit() {
        //init du tableau de valeur
        for (int i = 0; i < grille[0].length; i++) {
            for (int j = 0; j < grille.length; j++) {
                if (env.getLocations()[i][j] == null) {
                    grille[i][j] = -1;
                } else if (env.getLocations()[i][j] instanceof MurAgent) {
                    grille[i][j] = -2;
                }
            }
        }
    }
}
