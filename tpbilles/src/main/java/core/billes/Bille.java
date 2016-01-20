package core.billes;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tools.Randomizer;

import java.awt.*;

/**
 * Created by William on 19/01/2016.
 */
public class Bille extends AgentBille {

    public Circle getCircle() {
        return circle;
    }

    private Circle circle;
    private Color couleur;

    public Bille(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY, direction);
        int red = Randomizer.randomGenerator.nextInt(255);
        int green = Randomizer.randomGenerator.nextInt(255);
        int blue = Randomizer.randomGenerator.nextInt(255);
        this.couleur = new Color((float)red/100, 0.4, (float)green/100, 1.0);
        this.circle = new Circle(5, couleur);
        this.circle.relocate(posX, posY);
    }

    @Override
    public Directions estRencontrePar(AgentBille autre) {
        Directions maDir = this.direction;
        this.direction = autre.getDirection();
        return maDir;
    }

    @Override
    public void dessine(Graphics g) {
    }

    public javafx.scene.paint.Color getCouleur() {
        return this.couleur;
    }

    @Override
    public void decide() {
        int nextPosX = posX + direction.getDirX();
        int nextPosY = posY + direction.getDirY();
        AgentPhysique[][] locations = environnement.getLocations();
        AgentBille agentPresent = (AgentBille) locations[nextPosY][nextPosX];
        if (agentPresent != null) {

            this.direction = agentPresent.estRencontrePar(this);
            System.out.println("change");
            return;
        }

        locations[posY][posX] = null;
        posX = nextPosX;
        posY = nextPosY;
        locations[posY][posX] = this;
        this.circle.relocate(posX, posY);

    }
}
