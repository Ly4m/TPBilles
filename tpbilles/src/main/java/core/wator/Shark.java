package core.wator;

import java.util.ArrayList;
import java.util.List;

import core.AgentPhysique;
import core.Directions;
import core.Environnement;
import core.SMA;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.MainWater;
import tools.Randomizer;

/**
 * Created by leemans on 20/01/16.
 */
public class Shark extends WaterAgent {
	private int LIMITE_SANS_MANGER = 3;
	private int TEMPS_AVANT_REPRODUCTION = 9;
	private int dernierRepas;
	private int derniereReproduction;

	public Shark(Environnement env, SMA sma, int posX, int posY, Directions direction, int sansManger,
			int tempsReproduction) {
		super(env, sma, posX, posY, direction);
		this.Circle = new Circle(2.5, Color.INDIANRED);
		this.Circle.relocate(posX * 5, posY * 5);
		estMangeable = false;
		LIMITE_SANS_MANGER = sansManger;
		TEMPS_AVANT_REPRODUCTION = tempsReproduction;
	}

	@Override
	public void decide() {
		age++;
		find(environnement.getLocations());

		if (this.birthCount++ > TEMPS_AVANT_REPRODUCTION) {
			faireUnBaybay();
			this.birthCount = 0;
		}

		if (this.starvingCount++ > LIMITE_SANS_MANGER) {
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
				System.out.println(this.age + " THIS " + locations[this.getPosY()][this.getPosX()]);
			}

			prey.meurt();

			environnement.removeAgent(this);

			this.posX = newX;
			this.posY = newY;

			environnement.addAgent(this);

			this.Circle.relocate(newX * 5, newY * 5);
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

		final Directions direction = findRandomEmptyCase();

		if (direction == null) {
			return false;
		}

		int nextPosX = (posX + direction.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
		int nextPosY = (posY + direction.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

		WaterAgent agentPresent = (WaterAgent) locations[nextPosY][nextPosX];

		environnement.removeAgent(this);
		posX = nextPosX;
		posY = nextPosY;
		environnement.addAgent(this);

		this.Circle.relocate(posX * 5, posY * 5);
		return true;
	}

	private void faireUnBaybay() {

		Directions dir = findRandomEmptyCase();

		if (dir == null) {
			return;
		}

		int nextPosX = (this.posX + dir.getDirX() + environnement.getLargeur()) % environnement.getLargeur();
		int nextPosY = (this.posY + dir.getDirY() + environnement.getHauteur()) % environnement.getHauteur();

		Shark bebe = new Shark(environnement, sma, nextPosX, nextPosY, direction, LIMITE_SANS_MANGER,
				TEMPS_AVANT_REPRODUCTION);
		try {

			this.environnement.addAgent(bebe);
			sma.addAgentApres(bebe);
			MainWater.canvas.getChildren().addAll(bebe.getCircle());
			MainWater.sharkCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
