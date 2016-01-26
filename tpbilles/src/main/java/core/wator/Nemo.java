package core.wator;

import java.awt.Graphics;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.MainWater;
import tools.Randomizer;

/**
 * Created by leemans & Piorun on 20/01/16.
 */
public class Nemo extends WaterAgent {
    final private int TEMPS_AVANT_REPRODUCTION = 2;

    public Nemo(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY, direction);
        this.circle = new Circle(2.5, Color.CORNFLOWERBLUE);
        this.circle.relocate(posX * 5, posY * 5);
        estMangeable = true;
    }

    @Override
    public void dessine(Graphics g) {

    }

    @Override
    public void decide() {

        final Directions direction = findRandomEmptyCase();

        int oldX = posX;
        int oldY = posY;

        int nextPosX = (posX + direction.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
        int nextPosY = (posY + direction.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

        AgentPhysique[][] locations = environnement.getLocations();
        WaterAgent agentPresent = (WaterAgent) locations[nextPosY][nextPosX];

        if (agentPresent != null) {
            return;
        }

        locations[posY][posX] = null;
        posX = nextPosX;
        posY = nextPosY;
        locations[posY][posX] = this;
        this.circle.relocate(posX * 5, posY * 5);

        if (this.birthCount > TEMPS_AVANT_REPRODUCTION) {

            faireUnBaybay(environnement.getLocations(), oldX, oldY);
            this.birthCount = 0;
        } else {
            this.birthCount++;

        }

    }

    @Override
    public Directions estRencontrePar(WaterAgent other) {
        Directions oldDirection = this.getDirection();

        this.direction = other.getDirection();
//		this.meurt(environnement.getLocations());
        return oldDirection.getOpposeX().getOpposeY();
    }


    private void faireUnBaybay(AgentPhysique[][] locations, int posX, int posY) {
        Directions dir = findRandomEmptyCase();


        int nextPosX = (posX + dir.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
        int nextPosY = (posY + dir.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

        Nemo bebe = new Nemo(environnement, sma, nextPosX, nextPosY, direction);

        try {
            this.environnement.addAgent(bebe);
            sma.addAgentApres(bebe);
            MainWater.canvas.getChildren().addAll(bebe.getCircle());
        } catch (Exception e) {
//			e.printStackTrace();
        }
    }
}
