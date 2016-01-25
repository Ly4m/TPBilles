package core.wator;

import java.awt.Graphics;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by leemans & Piorun on 20/01/16.
 */
public class Nemo extends WaterAgent {

	public Nemo(Environnement env, SMA sma, int posX, int posY, Directions direction) {
		super(env, sma, posX, posY, direction);
		this.circle = new Circle(2.5, Color.CORNFLOWERBLUE);
		this.circle.relocate(posX / 5, posY / 5);
		estMangeable = true;
	}

	@Override
	public void dessine(Graphics g) {

	}

	@Override
	public void decide() {
		int nextPosX = posX + direction.getDirX();
		int nextPosY = posY + direction.getDirY();
		AgentPhysique[][] locations = environnement.getLocations();
		WaterAgent agentPresent = (WaterAgent) locations[nextPosY][nextPosX];
		if (agentPresent != null) {
			this.direction = agentPresent.estRencontrePar(this);
			return;
		}

		locations[posY][posX] = null;
		posX = nextPosX;
		posY = nextPosY;
		locations[posY][posX] = this;
		this.circle.relocate(posX, posY);

	}

	@Override
	public Directions estRencontrePar(WaterAgent other) {
		Directions oldDirection = this.getDirection();

		this.direction = other.getDirection();
		return oldDirection.getOpposeX().getOpposeY();
	}
}
