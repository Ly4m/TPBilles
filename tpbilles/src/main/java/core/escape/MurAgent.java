package core.escape;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.MainEscape;

import java.awt.*;

/**
 * Created by leemans on 27/01/16.
 */
public class MurAgent extends AgentPhysique{

    private Rectangle rectangle;

    public MurAgent(Environnement env, SMA sma, int posX, int posY) {
        super(env, sma, posX, posY);

        rectangle = new Rectangle(5,5, Color.CORNFLOWERBLUE);
        MainEscape.canvas.getChildren().addAll(rectangle);
       rectangle.relocate(posX*5, posY*5);

    }

    @Override
    public void decide() {
        // je suis un mur.
    }
}
