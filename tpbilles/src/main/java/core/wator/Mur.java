package core.wator;

import java.awt.Color;
import java.awt.Graphics;

import core.Environnement;
import core.SMA;
import core.Directions;

public class Mur extends WaterAgent {

	private TypeMur type;

	public enum TypeMur {
		VERTICAL {
			public Directions rebond(Directions origine) {
				return origine.getOpposeX();
			}
		},
		HORIZONTAL {
			public Directions rebond(Directions origine) {
				return origine.getOpposeY();
			}
		};

		public Directions rebond(Directions origine) {
			return origine.getOpposeX().getOpposeY();
		}
	}

	public Mur(Environnement env, SMA sma, int posX, int posY, TypeMur type) {
		super(env, sma, posX, posY, Directions.IMMOBILE);
		this.type = type;
		estMangeable = false;
	}

	@Override
	public void decide() {
		// Stand still
	}


	@Override
	public Directions estRencontrePar(WaterAgent autre) {
		return this.type.rebond(autre.getDirection());
	}

}
