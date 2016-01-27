package core.wator;

import core.Environnement;
import core.SMA;
import core.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.MainWater;

/**
 * Created by leemans & Piorun on 20/01/16.
 */
public class Nemo extends WaterAgent {
    final private int TEMPS_AVANT_REPRODUCTION = 3;

    public Nemo(Environnement env, SMA sma, int posX, int posY, Directions direction) {
        super(env, sma, posX, posY, direction);
        this.Circle = new Circle(2.5, Color.CORNFLOWERBLUE);
        this.Circle.relocate(posX * 5, posY * 5);
        estMangeable = true;
    }


    @Override
    public void decide() {
        if (this.agonie) {
            return;
        }
        this.age++;
//        if (this != environnement.getLocations()[this.getPosY()][this.getPosX()])
//            System.out.println("ERROR Poisson" );

        final Directions direction = findRandomEmptyCase();

        if (direction == null) return;

        int nextPosX = (posX + direction.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
        int nextPosY = (posY + direction.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

        environnement.removeAgent(this);
        posX = nextPosX;
        posY = nextPosY;
        environnement.addAgent(this);
        this.Circle.relocate(posX * 5, posY * 5);

        if (this.birthCount++ > TEMPS_AVANT_REPRODUCTION) {
            faireUnBaybay();
            this.birthCount = 0;
        }
    }

    @Override
    public Directions estRencontrePar(WaterAgent other) {
        Directions oldDirection = this.getDirection();

        this.direction = other.getDirection();
//		this.meurt(environnement.getLocations());
        return oldDirection.getOpposeX().getOpposeY();
    }


    private void faireUnBaybay() {

        Directions dir = findRandomEmptyCase();

        int nextPosX = (this.posX + dir.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
        int nextPosY = (this.posY + dir.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

        Nemo bebe = new Nemo(environnement, sma, nextPosX, nextPosY, direction);

        try {
            this.environnement.addAgent(bebe);
            sma.addAgentApres(bebe);
            MainWater.canvas.getChildren().addAll(bebe.getCircle());
            MainWater.nemoCount++;
        } catch (Exception e) {
//			e.printStackTrace();
        }
    }
}
