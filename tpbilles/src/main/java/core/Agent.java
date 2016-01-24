package core;

/**
 * Created by William on 19/01/2016.
 */
public abstract class Agent  {

    protected Environnement environnement;
    protected SMA sma;

    public Agent(Environnement environnement, SMA sma) {
        super();
        this.sma = sma;
        this.environnement = environnement;
    }

    public Environnement getEnvironnement() {return environnement;}

    public SMA getSma(){return sma;}

    public abstract void decide();

    public abstract boolean isPhysique();

}
