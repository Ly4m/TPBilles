package core.wator;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.Directions;
import javafx.scene.shape.Circle;
import main.MainWater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class WaterAgent extends AgentPhysique {

    protected Directions direction;


    protected boolean agonie;
    protected Circle circle;
    protected int age, birthCount, starvingCount;

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    protected boolean estMangeable;

    public WaterAgent(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY);
        this.direction = direction;
    }

    public Circle getCircle() {
        return circle;
    }

    public Directions getDirection() {
        return direction;
    }

    public abstract Directions estRencontrePar(WaterAgent autre);

    public void meurt() {
        MainWater.canvas.getChildren().remove(this.circle);
        environnement.removeAgent(this);
        sma.removeAgentApres(this);
        agonie = true;
    }

    public Directions findRandomEmptyCase() {

        List<Directions> listEmpty = new ArrayList<Directions>();

        Directions[] directionses = Directions.values();

        for (int i = 1; i < directionses.length; i++) {

            int newX = (posX + directionses[i].getDirX() + environnement.getLargeur()) % environnement.getLargeur();
            int newY = (posY + directionses[i].getDirY() + environnement.getHauteur()) % environnement.getHauteur();

            if (environnement.getLocations()[newY][newX] == null) {
                listEmpty.add(directionses[i]);
            }
        }

        if (!listEmpty.isEmpty()) {
            Collections.shuffle(listEmpty);
            return listEmpty.get(0);
        }

        return null;
    }
}
