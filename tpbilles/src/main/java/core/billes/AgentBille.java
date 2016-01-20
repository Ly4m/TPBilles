package core.billes;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import javafx.scene.shape.Circle;

/**
 * Created by William on 19/01/2016.
 */
public abstract class AgentBille extends AgentPhysique {

    protected Directions direction;

    Circle circle;

    public AgentBille(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY);
        this.direction = direction;

    }

    public Circle getCircle() { return this.circle;}

    public Directions getDirection() {
        return this.direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public abstract Directions estRencontrePar(AgentBille autre);

}
