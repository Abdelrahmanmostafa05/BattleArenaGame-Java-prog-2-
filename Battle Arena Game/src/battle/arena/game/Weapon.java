package battle.arena.game;
public class Weapon {
    protected String name;
    protected int damage;
    protected int speed;
    protected int cooldown; 

    public Weapon(String name, int damage, int speed, int cooldown) {
        this.name = name;
        this.damage = damage;
        this.speed = speed;
        this.cooldown = cooldown;
    }

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public int getSpeed() { return speed; }
    public int getCooldown() { return cooldown; }
}