package core.wator;

import java.awt.Graphics;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.AgentBille;
import core.billes.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by leemans on 20/01/16.
 */
public class Shark extends WaterAgent{
	
	public Shark(Environnement env, SMA sma, int posX, int posY, Directions direction) {
		super(env, sma, posX, posY, direction);
		this.circle = new Circle(2.5, Color.INDIANRED);
        this.circle.relocate(posX / 5, posY / 5);
	}

	@Override
	public void dessine(Graphics g) {
		
	}

	@Override
	public void decide() {
		return;
	}

	@Override
	public Directions estRencontrePar(WaterAgent autre) {
		// TODO Auto-generated method stub
		return null;
	}
}
