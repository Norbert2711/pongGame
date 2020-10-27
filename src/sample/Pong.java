package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Pong extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_RADIUS = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private double playerOneYPosition = 300;
    private double playerTwoYPosition = 300;
    private double ballXposition = 400;
    private double ballYposition = 300;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private boolean gameStarted;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("P.O.N.G");

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10),
                e -> run(graphicsContext)));
        timeline.setCycleCount(Timeline.INDEFINITE);


        //sterowanie mysza
        canvas.setOnMouseMoved(e -> playerOneYPosition = e.getY());
        canvas.setOnMouseClicked(e -> gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        timeline.play();

    }

    private void run(GraphicsContext graphicsContext) {

        graphicsContext.setFill(Color.PAPAYAWHIP);
        graphicsContext.fillRect(0, 0, WIDTH, HEIGHT);

        graphicsContext.setFont(Font.font(50));
        graphicsContext.setFill(Color.BLACK);

        if (gameStarted) {
            ballXposition = ballXposition + ballXSpeed;
            ballYposition = ballYposition + ballYSpeed;
//pilka porusza sie z przedkoscia 1 w osi x i y. do jej polozenia (x,y) dodawany jest 1
            if (ballXposition < WIDTH - WIDTH / 4) {
                playerTwoYPosition = ballYposition - PLAYER_HEIGHT / 2;
                //jesli pozycja ilki na osi X jest wieksza od 600 to gracz 2 porusza sie za pilka na osi Y
            } else {

                playerTwoYPosition = ballYposition > playerTwoYPosition + PLAYER_HEIGHT / 2
                        ? playerTwoYPosition = playerTwoYPosition + 1 : playerTwoYPosition - 1;
                //??ruch player 2 + 1 i -1 na osi Y -- do testu.
            }

            //rysuje pilke 'oval'
            graphicsContext.fillOval(ballXposition, ballYposition, BALL_RADIUS, BALL_RADIUS);
        } else {

            graphicsContext.setStroke(Color.DARKRED);
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.strokeText("START", WIDTH / 2, HEIGHT / 2);
            //reset pozycji pilki
            ballXposition = WIDTH / 2;
            ballYposition = HEIGHT / 2;

            //reset speeda i kierunku ruchu
            ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            //jesli ruch pilki po osi X w randomie jest rowny 0 to dodaj mu 1 jesli nie to odejmij 1
            ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
        }

        //pilka nie wychodzi poza plansze
        if (ballYposition > HEIGHT || ballYposition < 0) ballYSpeed *= -1;

        //jesli ominiesz pilke pkt zdobywa PC
        int playerOneXPosition = 0;
        if (ballXposition < playerOneXPosition - PLAYER_WIDTH) {
            scorePlayer2++;
            gameStarted = false;
        }
        //pkt dla gracza
        //785
        int playerTwoXPosition = WIDTH - PLAYER_WIDTH;
        if (ballXposition > playerTwoXPosition + PLAYER_WIDTH) {
            scorePlayer1++;
            gameStarted = false;
        }

        //szybkosc pilki
        if (((ballXposition + BALL_RADIUS > playerTwoXPosition) && ballYposition >= playerTwoYPosition &&
                ballYposition <= playerTwoYPosition + PLAYER_HEIGHT) ||
                ((ballXposition < playerOneXPosition + PLAYER_WIDTH) && ballYposition >= playerOneYPosition
                        && ballYposition <= playerOneYPosition + PLAYER_HEIGHT)) {

            ballYSpeed = (int) (ballYSpeed + (1 * Math.signum(ballYSpeed)));
            ballXSpeed = (int) (ballXSpeed + (1 * Math.signum(ballXSpeed)));
            ballXSpeed = ballXSpeed * -1;
            ballYSpeed = ballYSpeed * -1;

        }
//score
        graphicsContext.setFont(new Font("Verdana",26));
        graphicsContext.setStroke(Color.DARKGREEN);
        graphicsContext.strokeText("POINTS", 400, 50);
        graphicsContext.setStroke(Color.DARKGREEN);
        graphicsContext.strokeText("One: " + scorePlayer1 + "\t\t" + "Two: " + scorePlayer2, 400, 100);

//pionki graczy
        graphicsContext.fillRect(playerTwoXPosition, playerTwoYPosition, PLAYER_WIDTH, PLAYER_HEIGHT);
        graphicsContext.fillRect(playerOneXPosition, playerOneYPosition, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
