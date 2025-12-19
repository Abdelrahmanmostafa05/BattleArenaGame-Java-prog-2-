
package Fighter;

import battle.arena.game.Weapon;
import javafx.scene.paint.Color;

public class Warrior extends Fighter {
    public Warrior(double x, double y, boolean fromLeft) {
        super("Warrior", x, y, fromLeft);
        this.weapon = new Weapon("Warrior", 15, 6, 500);
    }
    @Override
public Color getColor() {
    return Color.RED;
}
}