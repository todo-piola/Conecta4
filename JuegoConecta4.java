package application;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JuegoConecta4 extends Application {

    private static final int FILAS = 6;
    private static final int COLUMNAS = 7;
    private static final int TAM_FICHA = 100;

    private Circle[][] tablero = new Circle[FILAS][COLUMNAS];
    private boolean turnoRojo = true;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: blue; -fx-padding: 20; -fx-hgap: 20; -fx-vgap: 20;");

        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                Circle ficha = new Circle(TAM_FICHA / 2);
                ficha.setFill(Color.WHITE);
                ficha.setStroke(Color.BLACK);
                tablero[fila][columna] = ficha;

                if (fila == 0) {
                    final int columnaFinal = columna;
                    ficha.setOnMouseClicked(e -> manejarClick(e, columnaFinal));
                }

                gridPane.add(ficha, columna, fila);
            }
        }

        Scene scene = new Scene(gridPane);
        primaryStage.setTitle("Conecta 4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void manejarClick(MouseEvent e, int columna) {
        for (int fila = FILAS - 1; fila >= 0; fila--) {
            if (tablero[fila][columna].getFill().equals(Color.WHITE)) {
                Color colorActual = turnoRojo ? Color.RED : Color.YELLOW;
                tablero[fila][columna].setFill(colorActual);

                // Animación de caída
                TranslateTransition transicion = new TranslateTransition(Duration.seconds(0.7), tablero[fila][columna]);
                transicion.setFromY(-TAM_FICHA * fila);
                transicion.setToY(0);
                transicion.play();

                // Comprobación de victoria
                if (hayGanador(fila, columna, colorActual)) {
                    mostrarGanador(colorActual.equals(Color.RED) ? "ROJO" : "AMARILLO");
                    reiniciarTablero();
                }

                turnoRojo = !turnoRojo;
                break;
            }
        }
    }

    private boolean hayGanador(int fila, int columna, Color color) {
        return (contar(fila, columna, 0, 1, color) + contar(fila, columna, 0, -1, color) >= 3) || // Horizontal
               (contar(fila, columna, 1, 0, color) + contar(fila, columna, -1, 0, color) >= 3) || // Vertical
               (contar(fila, columna, 1, 1, color) + contar(fila, columna, -1, -1, color) >= 3) || // Diagonal ↘
               (contar(fila, columna, -1, 1, color) + contar(fila, columna, 1, -1, color) >= 3);   // Diagonal ↗
    }

    private int contar(int fila, int columna, int dFila, int dCol, Color color) {
        int contador = 0;
        int f = fila + dFila;
        int c = columna + dCol;

        while (f >= 0 && f < FILAS && c >= 0 && c < COLUMNAS &&
               tablero[f][c].getFill().equals(color)) {
            contador++;
            f += dFila;
            c += dCol;
        }
        return contador;
    }

    private void mostrarGanador(String ganador) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("¡Fin del juego!");
        alerta.setHeaderText(null);
        alerta.setContentText("Ha ganado EQUIPO " + ganador + "!");
        alerta.showAndWait();
    }

    private void reiniciarTablero() {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                tablero[fila][columna].setFill(Color.WHITE);
            }
        }
        turnoRojo = true; // Comienza siempre el rojo
    }

    public static void main(String[] args) {
        launch(args);
    }
}