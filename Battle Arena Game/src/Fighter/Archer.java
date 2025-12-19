package Fighter;

import battle.arena.game.Weapon;
import javafx.scene.paint.Color;

public class Archer extends Fighter {
    public Archer(double x, double y, boolean fromLeft) {
        super("Archer", x, y, fromLeft);
        this.weapon = new Weapon("Archer", 12, 8, 300);
    }
@Override
public Color getColor() {
    return Color.GREEN;
}

}