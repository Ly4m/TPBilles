package ia.billes;

import ia.AgentPhysique;
import ia.Environnement;
import ia.SMA;

/**
 * Created by William on 19/01/2016.
 */
public abstract class AgentBille extends AgentPhysique {

    protected Directions direction;


    public AgentBille(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY);
        this.direction = this.direction;

    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public abstract Directions estRencontrePar(AgentBille autre);

}
