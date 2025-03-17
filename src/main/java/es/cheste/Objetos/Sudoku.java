package es.cheste.Objetos;

import es.cheste.Utilidad.Configuracion;

import java.io.Serializable;
import java.util.*;

/**
 * @author Hugo Almodóvar Fuster
 * @version 1.0
 *
 * La clase Sudoku representa un juego de Sudoku y proporciona métodos para generar y comprobar tableros de Sudoku.
 * Implementa la interfaz Serializable para permitir la serialización de objetos Sudoku.
 */
public class Sudoku implements Serializable {

    private static final int TAMANYO = Integer.parseInt(Configuracion.getConfiguracion("sudoku.tamanyo"));
    private final int[][] tableroSolucion;
    private final int[][] tablero;
    private final String uuid;

    /**
     * Constructor de la clase Sudoku.
     * Inicializa los tableros de solución y juego, y genera un UUID único para el Sudoku.
     */
    public Sudoku() {
        this.tableroSolucion = new int[TAMANYO][TAMANYO];
        this.tablero = new int[TAMANYO][TAMANYO];
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tableroSolucion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sudoku sudoku = (Sudoku) o;
        return Objects.deepEquals(tableroSolucion, sudoku.tableroSolucion);
    }

    /**
     * Obtiene el UUID del Sudoku.
     * @return el UUID del Sudoku.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Obtiene el tablero de solución del Sudoku.
     * @return el tablero de solución.
     */
    public int[][] getTableroSolucion() {
        return tableroSolucion;
    }

    /**
     * Obtiene el tablero del Sudoku.
     * @return el tablero del Sudoku.
     */
    public int[][] getTablero() {
        return tablero;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sudoku{");
        sb.append("tableroSolucion=").append(Arrays.toString(tableroSolucion));
        sb.append(", tablero=").append(Arrays.toString(tablero));
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Muestra el tablero de Sudoku en formato de cadena.
     * @param tableroMostrar el tablero a mostrar.
     * @return una representación en cadena del tablero.
     */
    public static String mostrarTablero(int[][] tableroMostrar) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANYO; i++) {
            if (i % 3 == 0 && i != 0) {
                sb.append(String.format("%4s", "-").repeat(6)).append(String.format("%4s", "+"))
                        .append(String.format("%4s", "-").repeat(6)).append(String.format("%4s", "+"))
                        .append(String.format("%4s", "-").repeat(5)).append("\n");
            }
            for (int j = 0; j < TAMANYO; j++) {
                if (j % 3 == 0 && j != 0) {
                    sb.append(String.format("%4s", "|"));
                }
                sb.append(String.format("%4s", tableroMostrar[i][j] == 0 ? "." : tableroMostrar[i][j]));
                sb.append(String.format("%4s", " "));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Genera un tablero de Sudoku con un número específico de celdas vacías.
     * @param celdasVaciar el número de celdas a vaciar.
     */
    public void generar(int celdasVaciar) {
        rellenarTablero();
        copiarTableroSolucion();
        vaciarCeldas(celdasVaciar);
    }

    /**
     * Rellena el tablero de solución con números válidos de Sudoku.
     * @return true si el tablero se ha rellenado correctamente, false en caso contrario.
     */
    private boolean rellenarTablero() {
        for (int fila = 0; fila < TAMANYO; fila++) {
            for (int col = 0; col < TAMANYO; col++) {
                if (tableroSolucion[fila][col] == 0) {
                    List<Integer> numeros = new ArrayList<>();
                    for (int num = 1; num <= TAMANYO; num++) {
                        numeros.add(num);
                    }
                    Collections.shuffle(numeros);
                    for (int num : numeros) {
                        if (esValido(fila, col, num)) {
                            tableroSolucion[fila][col] = num;
                            if (rellenarTablero()) {
                                return true;
                            }
                            tableroSolucion[fila][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifica si un número es válido en una posición específica del tablero de solución.
     * @param fila la fila de la posición.
     * @param col la columna de la posición.
     * @param num el número a verificar.
     * @return true si el número es válido, false en caso contrario.
     */
    private boolean esValido(int fila, int col, int num) {
        for (int i = 0; i < TAMANYO; i++) {
            if (tableroSolucion[fila][i] == num || tableroSolucion[i][col] == num) {
                return false;
            }
        }

        int boxFila = fila - fila % 3;
        int boxCol = col - col % 3;
        for (int i = boxFila; i < boxFila + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (tableroSolucion[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Copia el tablero de solución al tablero de juego.
     */
    private void copiarTableroSolucion() {
        for (int i = 0; i < TAMANYO; i++) {
            System.arraycopy(tableroSolucion[i], 0, tablero[i], 0, TAMANYO);
        }
    }

    /**
     * Vacía un número específico de celdas en el tablero de juego.
     * @param numCeldas el número de celdas a vaciar.
     */
    private void vaciarCeldas(int numCeldas) {
        int vaciadas = 0;
        while (vaciadas < numCeldas) {
            int fila = (int) (Math.random() * TAMANYO);
            int col = (int) (Math.random() * TAMANYO);
            if (tablero[fila][col] != 0) {
                tablero[fila][col] = 0;
                vaciadas++;
            }
        }
    }

    /**
     * Comprueba el resultado del tablero del jugador comparándolo con el tablero de solución.
     * @param tableroJugador el tablero del jugador.
     * @return un tablero que muestra las celdas correctas e incorrectas.
     */
    public int[][] comprobarResultado(int[][] tableroJugador) {
        int[][] tableroComprobacion = new int[TAMANYO][TAMANYO];

        for (int i = 0; i < TAMANYO; i++) {
            for (int j = 0; j < TAMANYO; j++) {
                if (tableroJugador[i][j] != tableroSolucion[i][j]) {
                    tableroComprobacion[i][j] = 0;
                } else tableroComprobacion[i][j] = tableroSolucion[i][j];
            }
        }

        return tableroComprobacion;
    }
}