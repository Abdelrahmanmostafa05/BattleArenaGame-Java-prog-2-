import Fighter.Fighter;
import Fighter.FighterFactory;
import battle.arena.game.Projectile;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import java.util.*;
import static javafx.application.Application.launch;

public class BattleArenaGame extends Application {

    protected Fighter player1, player2;
    protected List<Projectile> projectiles = new ArrayList<>();
    protected  Canvas canvas = new Canvas(800, 600);
    protected GraphicsContext gc = canvas.getGraphicsContext2D();

    protected long lastShotP1 = 0, lastShotP2 = 0;
    protected Stage mainStage;
    protected AnimationTimer loop;

    protected String p1Type, p2Type;
    protected boolean gameOver = false;

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        stage.setTitle("Battle Arena Game");
        stage.setScene(createSelectionScene(stage));
        stage.show();
    }

   
    private Scene createSelectionScene(Stage stage) {

        ComboBox<String> p1Choice = new ComboBox<>();
        p1Choice.setPromptText("Select Fighter");
        p1Choice.getItems().addAll("Warrior", "Mage", "Archer");

        ComboBox<String> p2Choice = new ComboBox<>();
        p2Choice.setPromptText("Select Fighter");
        p2Choice.getItems().addAll("Warrior", "Mage", "Archer");

        Button startBtn = new Button("Start Game");

        Label instructionsLabel = new Label(
            """
            Controls:
            Player 1: (WASD) to move, (SPACE) to shoot, (Q/E) to switch weapons
            Player 2: (Arrow) keys to move,( ENTER ) to shoot, (,/.) to switch weapons""");
    

        VBox layout = new VBox(15,
                new Label("Player 1:"), p1Choice,
                new Label("Player 2:"), p2Choice,
                startBtn,
                instructionsLabel
        );

        layout.setStyle("-fx-alignment: center; -fx-padding: 50;");
        Scene scene = new Scene(layout, 500, 400);

        startBtn.setOnAction(e -> {
            if (p1Choice.getValue() == null || p2Choice.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a fighter for both players.");
                alert.showAndWait();
                return;
            }

            p1Type = p1Choice.getValue();
            p2Type = p2Choice.getValue();

            startNewGame(stage);
        });

        return scene;
    }

    
    private Scene createGameScene(Stage stage) {

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            if (gameOver) {
                return;
            }

            KeyCode code = event.getCode();
            switch (code) {
              
                case W -> player1.move(0, -10);
                case S -> player1.move(0, 10);
                case A -> player1.move(-10, 0);
                case D -> player1.move(10, 0);

                case SPACE -> {
                    if (System.currentTimeMillis() - lastShotP1 > player1.getWeapon().getCooldown()) {
                        projectiles.add(player1.shoot());
                        lastShotP1 = System.currentTimeMillis();
                    }
                }

               
                case Q -> player1.switchWeaponBackward();
                case E -> player1.switchWeapon();

           
                case UP -> player2.move(0, -10);
                case DOWN -> player2.move(0, 10);
                case LEFT -> player2.move(-10, 0);
                case RIGHT -> player2.move(10, 0);

                case ENTER -> {
                    if (System.currentTimeMillis() - lastShotP2 > player2.getWeapon().getCooldown()) {
                        projectiles.add(player2.shoot());
                        lastShotP2 = System.currentTimeMillis();
                    }
                }

              
                case COMMA -> player2.switchWeaponBackward();
                case PERIOD -> player2.switchWeapon();
            }
        });

        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame(stage);
            }
        };

        loop.start();
        return scene;
    }

 
    private void updateGame(Stage stage) {

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        
        gc.setStroke(Color.LIGHTCORAL);
        gc.setLineDashes(5, 5);
        gc.strokeLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight());
        gc.setLineDashes(null);

        player1.draw(gc);
        player2.draw(gc);

        
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.update();
            p.draw(gc);
            
            if (p.collides(player1)) {
                player1.takeDamage(p.getDamage());
                it.remove();
            } else if (p.collides(player2)) {
                player2.takeDamage(p.getDamage());
                it.remove();
            } else if (p.isOutOfBounds(canvas.getWidth(), canvas.getHeight())) {
                it.remove();
            }
        }

        
        gc.setFill(Color.RED);
        gc.fillRect(50, 20, Math.max(0, player1.getHealth() * 2), 20);
        gc.fillRect(550, 20, Math.max(0, player2.getHealth() * 2), 20);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font(16));
        gc.fillText("P1 HP: " + player1.getHealth(), 50, 55);
        gc.fillText("P2 HP: " + player2.getHealth(), 550, 55);

       
        gc.setFont(new Font(14));
        gc.fillText("Weapon: " + player1.getWeapon().getName(), 50, 75);
        gc.fillText("Weapon: " + player2.getWeapon().getName(), 550, 75);

       
        gc.setFont(new Font(12));
        gc.fillText("DMG: " + player1.getWeapon().getDamage() + 
                    " | CD: " + player1.getWeapon().getCooldown() + "ms", 50, 90);
        gc.fillText("DMG: " + player2.getWeapon().getDamage() + 
                    " | CD: " + player2.getWeapon().getCooldown() + "ms", 550, 90);

        if (!gameOver && (player1.getHealth() <= 0 || player2.getHealth() <= 0)) {
            gameOver = true;
            loop.stop();

            String winner = player1.getHealth() <= 0
                    ? "Player 2 Wins!"
                    : "Player 1 Wins!";

            showGameOverWindow(stage, winner);
        }
    }

    private void showGameOverWindow(Stage stage, String winner) {

        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Game Over");

        Label label = new Label(winner);
        label.setFont(new Font(24));
        label.setStyle("-fx-text-fill: #d4af37; -fx-font-weight: bold;");

        Button playAgain = new Button("Play Again");
        Button mainMenu = new Button("Main Menu");
        Button exit = new Button("Exit");

        playAgain.setOnAction(e -> {
            dialog.close();
            startNewGame(stage);
        });

        mainMenu.setOnAction(e -> {
            dialog.close();
            stage.setScene(createSelectionScene(stage));
        });

        exit.setOnAction(e -> {
            dialog.close();
            stage.close();
        });

        VBox layout = new VBox(20, label, playAgain, mainMenu, exit);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30; -fx-background-color: #2c3e50;");

        dialog.setScene(new Scene(layout, 350, 250));
        dialog.show();
    }

    private void startNewGame(Stage stage) {

        projectiles.clear();
        lastShotP1 = 0;
        lastShotP2 = 0;
        gameOver = false;

        player1 = FighterFactory.createFighter(p1Type, 100, 100, true);
        player2 = FighterFactory.createFighter(p2Type, 600, 100, false);

        stage.setScene(createGameScene(stage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}