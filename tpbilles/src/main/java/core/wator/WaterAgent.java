package core.wator;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.AgentBille;
import core.billes.Directions;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tools.Randomizer;

public abstract class WaterAgent extends AgentPhysique {

	protected Directions direction;
	
	protected  Circle circle;
	
	protected boolean estMangeable;

	
	
	public WaterAgent(Environnement env, SMA sma, int posX, int posY, Directions direction) {
		super(env, sma, posX, posY);
		this.direction = direction;
		
//        int red = Randomizer.randomGenerator.nextInt(255);
//        int green = Randomizer.randomGenerator.nextInt(255);
//        int blue = Randomizer.randomGenerator.nextInt(255);
//        this.color = new Color((float)red/100, (float)green/100, (float)blue/100, 1.0);

//        this.circle = new Circle(2.5);
//        this.circle.relocate(posX / 5, posY / 5);
	}
	
	public Circle getCircle(){
		return circle;
	}
	
	public Directions getDirection(){
		return direction;
	}
	
	public abstract Directions estRencontrePar(WaterAgent autre);
}
