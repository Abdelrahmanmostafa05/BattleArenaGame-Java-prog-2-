package Fighter;

import battle.arena.game.Weapon;
import javafx.scene.paint.Color;

public class Mage extends Fighter {


    public Mage(double x, double y, boolean fromLeft) {
        super("Mage", x, y, fromLeft);
        this.weapon = new Weapon("Mage", 20, 4, 700);
    }

@Override
public Color getColor() {
    return Color.BLUE;
}}