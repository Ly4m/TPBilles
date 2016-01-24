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
	final private int LIMITE_SANS_MANGER = 10;
	final private int TEMPS_AVANT_REPRODUCTION = 30;
	private int dernierRepas;
	private int derniereReproduction;
	
	public Shark(Environnement env, SMA sma, int posX, int posY, Directions direction) {
		super(env, sma, posX, posY, direction);
		this.circle = new Circle(2.5, Color.INDIANRED);
        this.circle.relocate(posX / 5, posY / 5);
        dernierRepas=0;
        derniereReproduction=0;
        estMangeable = false;
	}

	@Override
	public void dessine(Graphics g) {
		
	}

	@Override
	public void decide() {
		 int nextPosX = posX + direction.getDirX();
	        int nextPosY = posY + direction.getDirY();
	        AgentPhysique[][] locations = environnement.getLocations();
	        Directions targetDir = nearTarget(locations);
        	WaterAgent target = null;
	        if(targetDir !=null){
	        	target = (WaterAgent) locations[posY + targetDir.getDirY()][posX + direction.getDirX()];
	        }
	        
	        if( target !=null  && target.estMangeable){
	        	direction = targetDir;
		        
	        	sma.getAgents().remove(target);
		        posX = posX + direction.getDirX();
		        posY = posY + targetDir.getDirY();
		        this.circle.relocate(posX, posY);
	        }else{
	        	
	        
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
	}

	private Directions nearTarget(AgentPhysique[][] locations) {
		if(locations[posY+Directions.DROITE.getDirY()][posX+Directions.DROITE.getDirX()] !=null){
			return Directions.DROITE;
		}
		if(locations[posY+Directions.HAUT.getDirY()][posX + Directions.HAUT.getDirX()] !=null){
			return Directions.HAUT;
		}
		if(locations[posY + Directions.HAUT_DROITE.getDirY()][posX + Directions.HAUT_DROITE.getDirX()] !=null){
			return Directions.HAUT_DROITE;
		}
		if(locations[posY + Directions.GAUCHE.getDirY()][posX + Directions.GAUCHE.getDirX()] !=null){
			return Directions.GAUCHE;
		}
		if(locations[posY + Directions.BAS_DROITE.getDirY()][posX + Directions.BAS_DROITE.getDirX()] !=null){
			return Directions.BAS_DROITE;
		}
		if(locations[posY + Directions.BAS_GAUCHE.getDirY()][posX + Directions.BAS_GAUCHE.getDirX()] !=null){
			return Directions.BAS_GAUCHE;
		}
		if(locations[posY + Directions.HAUT_GAUCHE.getDirY()][posX + Directions.HAUT_GAUCHE.getDirX()] !=null){
			return Directions.HAUT_GAUCHE;
		}
		if(locations[posY+ Directions.BAS.getDirY()][posX + Directions.BAS.getDirX()] !=null){
			return Directions.BAS;
		}
		
		return null;
	}

	@Override
	public Directions estRencontrePar(WaterAgent other) {
		Directions oldDirection = this.getDirection();


        this.direction = other.getDirection();
        return oldDirection.getOpposeX().getOpposeY();
	}
}
