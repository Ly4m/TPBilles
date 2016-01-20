package ia.billes;

import ia.AgentPhysique;
import ia.Environnement;
import ia.SMA;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import tools.Randomizer;

import java.awt.*;

/**
 * Created by William on 19/01/2016.
 */
public class Bille extends AgentBille {

    private Color couleur;

    public Bille(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY, direction);
        int red = Randomizer.randomGenerator.nextInt(255);
        int green = Randomizer.randomGenerator.nextInt(255);
        int blue = Randomizer.randomGenerator.nextInt(255);
        this.couleur = new Color(0, 0.4, 1, 1.0);
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
            return;
        }


        locations[posY][posX] = null;
        posX = nextPosX;
        posY = nextPosY;
        locations[posY][posX] = this;
    }
}
