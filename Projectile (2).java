package battle.arena.game;

import Fighter.Fighter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Projectile {
    protected double x, y;
    protected int speed;
    protected int damage;
    protected double angle; 
    protected double dx, dy;
    protected Fighter owner; 

    public Projectile(double x, double y, int speed, int damage, double angle, Fighter owner) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.angle = angle;
        this.owner = owner;
        
     
        this.dx = Math.cos(Math.toRadians(angle)) * speed;
        this.dy = Math.sin(Math.toRadians(angle)) * speed;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(x - 5, y - 5, 10, 10);
    }

    public boolean collides(Fighter fighter) {
       
        if (fighter == owner) {
            return false;
        }
        
        Rectangle2D projBounds = new Rectangle2D(x - 5, y - 5, 10, 10);
        Rectangle2D fighterBounds = new Rectangle2D(fighter.getX(), fighter.getY(), 40, 40);
        return projBounds.intersects(fighterBounds);
    }

    public boolean isOutOfBounds(double width, double height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public int getDamage() { return damage; }
    public Fighter getOwner() { return owner; }
}