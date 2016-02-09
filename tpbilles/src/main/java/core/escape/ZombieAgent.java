package core.escape;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.MainEscape;
import tools.Position;

public class ZombieAgent extends AgentPhysique {

    private Rectangle rectangle;

    public ZombieAgent(Environnement environnement, SMA sma, int posX, int posY) {
        super(environnement, sma, posX, posY);

        rectangle = new Rectangle(5, 5, Color.DARKRED);
        MainEscape.canvas.getChildren().add(rectangle);
        rectangle.relocate(posX * 5, posY * 5);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void decide() {

        Position goTo = dijkstraMove();

        environnement.removeAgent(this);
        this.posX = goTo.getX();
        this.posY = goTo.getY();
        environnement.addAgent(this);
        rectangle.relocate(posX *5, posY*5);


    }

    public Position dijkstraMove(){

        int min = Integer.MAX_VALUE;
        Position pos = new Position(this.posX, this.posY);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j < 1; j++) {
                int value = MainEscape.dijkstra.grille[this.posX + i][this.posY + j];
                if(value > 0 && value < min){
                    min = value;
                    pos = new Position(this.posX + i, this.posY);
                }
            }
        }

        return pos;

    }

}
