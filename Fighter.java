package Fighter;
import battle.arena.game.Projectile;
import battle.arena.game.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.ArrayList;

public abstract class Fighter {
    protected String name;
    protected int health;
    protected double x, y;
    protected Weapon weapon;
    protected boolean fromLeft;
    protected List<Weapon> weapons;
    protected int currentWeaponIndex = 0;
    protected double rotation = 0; 

    public Fighter(String name, double x, double y, boolean fromLeft) {
        this.name = name;
        this.health = 100;
        this.x = x;
        this.y = y;
        this.fromLeft = fromLeft;
        this.weapons = new ArrayList<>();
        initializeWeapons(); 
        if (!weapons.isEmpty()) {
            this.weapon = weapons.get(0); 
        }
    }

    protected final void initializeWeapons() {
        weapons.add(new Weapon("Warrior", 10, 5, 400));
        weapons.add(new Weapon("Mage", 20, 7, 700));
        weapons.add(new Weapon("Archer", 30, 3, 1500));
    }

    public abstract Color getColor();

   
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
        
      
        
        if (x < 0) x = 0;
        if (x > 760) x = 760; 
        if (y < 0) y = 0;
        if (y > 560) y = 560; 
        
        
        if (dx != 0 || dy != 0) {
            rotation = Math.toDegrees(Math.atan2(dy, dx));
        }
    }

    public Projectile shoot() {
      
        return new Projectile(x + 20, y + 20, weapon.getSpeed(), weapon.getDamage(), rotation, this);
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        
       
        gc.translate(x + 20, y + 20);
        gc.rotate(rotation);
       
        gc.setFill(getColor());
        gc.fillOval(-20, -20, 40, 40);
        
      
        gc.setFill(Color.WHITE);
        gc.fillPolygon(
            new double[]{15, 10, 10},
            new double[]{0, -5, 5},
            3
        );
        
        gc.restore();
    }

   
    public void switchWeapon() {
        if (weapons.isEmpty()) return;
        currentWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
        weapon = weapons.get(currentWeaponIndex);
    }

  
    public void switchWeaponBackward() {
        if (weapons.isEmpty()) return;
        currentWeaponIndex = (currentWeaponIndex - 1 + weapons.size()) % weapons.size();
        weapon = weapons.get(currentWeaponIndex);
    }

   
    public int getHealth() { return health; }
    public Weapon getWeapon() { return weapon; }
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isFromLeft() { return fromLeft; }
    public double getRotation() { return rotation; }
    public String getName() { return name; }
}