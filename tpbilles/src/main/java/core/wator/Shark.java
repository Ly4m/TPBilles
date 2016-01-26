package core.wator;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.MainWater;
import tools.Randomizer;

/**
 * Created by leemans on 20/01/16.
 */
public class Shark extends WaterAgent {
    final private int LIMITE_SANS_MANGER = 3;
    final private int TEMPS_AVANT_REPRODUCTION = 9;
    private int dernierRepas;
    private int derniereReproduction;

    public Shark(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY, direction);
        this.circle = new Circle(2.5, Color.INDIANRED);
        this.circle.relocate(posX * 5, posY * 5);
        estMangeable = false;
    }

    @Override
    public void dessine(Graphics g) {

    }

    @Override
    public void decide() {

        this.age++;
        this.starvingCount++;

        int oldY = posY;
        int oldX = posX;

        find(environnement.getLocations());

        if (this.birthCount > TEMPS_AVANT_REPRODUCTION) {

            faireUnBaybay(environnement.getLocations(), oldX, oldY);
            this.birthCount = 0;
        } else {
            this.birthCount++;

        }


        if (this.starvingCount > LIMITE_SANS_MANGER) {
            this.meurt();
        }
    }

    private boolean find(AgentPhysique[][] locations) {
        List<WaterAgent> listPrey = new ArrayList<WaterAgent>();
        int newX = 0;
        int newY = 0;

        // check autour
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {

                newY = (posY + y + environnement.getHauteur()) % environnement.getHauteur();
                newX = (posX + x + environnement.getLargeur()) % environnement.getLargeur();

                AgentPhysique truc = locations[newY][newX];


                if (truc != null && ((WaterAgent) truc).estMangeable) {
                    listPrey.add(((WaterAgent) truc));
                }
            }
        }


        if (!listPrey.isEmpty()) {
            WaterAgent prey = listPrey.get(Randomizer.randomGenerator.nextInt(listPrey.size()));

            newY = prey.getPosY();
            newX = prey.getPosX();

            if (newX == this.posX && newY == this.posY) {
                System.out.println("test");
            }

            prey.meurt();

            environnement.removeAgent(this);

            this.posX = newX;
            this.posY = newY;

            environnement.addAgent(this);

            this.circle.relocate(newX * 5, newY * 5);
            this.starvingCount = 0;
            return true;
        }

        return randomMove(locations);

    }

    @Override
    public Directions estRencontrePar(WaterAgent other) {
        Directions oldDirection = this.getDirection();

        this.direction = other.getDirection();
        return oldDirection.getOpposeX().getOpposeY();
    }

    public boolean randomMove(AgentPhysique[][] locations) {

        final Directions direction = Directions.values()[Randomizer.randomGenerator.nextInt(Directions.values().length)];

        int nextPosX = (posX + direction.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
        int nextPosY = (posY + direction.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

        WaterAgent agentPresent = (WaterAgent) locations[nextPosY][nextPosX];

        if (agentPresent == null) {

            locations[posY][posX] = null;

            posX = nextPosX;
            posY = nextPosY;

            locations[posY][posX] = this;
            this.circle.relocate(posX * 5, posY * 5);
            return true;
        }
        return false;
    }

    private void faireUnBaybay(AgentPhysique[][] locations, int posX, int posY) {
        Directions dir = findRandomEmptyCase();

        int nextPosX = (posX + dir.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
        int nextPosY = (posY + dir.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

        Shark bebe = new Shark(environnement, sma, nextPosX, nextPosY, direction);
        try {
            if (locations[posY][posX] != null)
                System.out.println(locations[posY][posX] + "  gnn " + this);
            this.environnement.addAgent(bebe);
            sma.addAgentApres(bebe);
            MainWater.canvas.getChildren().addAll(bebe.getCircle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
