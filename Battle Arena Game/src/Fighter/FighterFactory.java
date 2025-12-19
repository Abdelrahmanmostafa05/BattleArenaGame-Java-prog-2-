package Fighter;



public class FighterFactory {
    public static Fighter createFighter(String type, double x, double y, boolean fromLeft) {
        return switch (type) {
            case "Warrior" -> new Warrior(x, y, fromLeft);
            case "Mage" -> new Mage(x, y, fromLeft);
            case "Archer" -> new Archer(x, y, fromLeft);
            default -> new Warrior(x, y, fromLeft);
        };
    }
}