package core.wator;

import java.awt.Graphics;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tools.Randomizer;

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
        final Directions direction = Directions.values()[Randomizer.randomGenerator.nextInt(Directions.values().length - 1) + 1];

        int nextPosX = (posX + direction.getDirX()+ environnement.getLargeur())  % environnement.getLargeur() ;
		int nextPosY = (posY + direction.getDirY()+ environnement.getHauteur()) % environnement.getHauteur();

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
//		this.meurt(environnement.getLocations());
		return oldDirection.getOpposeX().getOpposeY();
	}

	private void meurt(AgentPhysique[][] locations) {
		locations[this.posY][this.posX] = null;
		this.circle.setOpacity(0);
		sma.removeAgentApres(this);
	}
	private void faireUnBaybay(AgentPhysique[][] locations, int posX, int posY) {
		Nemo bebe = new Nemo(environnement, sma, posX, posY, Directions.GAUCHE);
		try {
			this.environnement.addAgent(bebe);
			sma.addAgentApres(bebe);
			locations[posY][posX] = bebe;
			bebe.getCircle().setFill(new Color(0,1,0,1));
			bebe.getCircle().setRadius(200);
			this.round = 0;
		} catch (Exception e) {
		}
	}
}
