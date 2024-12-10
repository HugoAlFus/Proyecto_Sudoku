package es.cheste.Objetos;

import java.io.Serializable;
import java.util.*;

public class Sudoku implements Serializable {

    private static final int TAMANYO = 9;
    private final int[][] tableroSolucion;
    private final int[][] tablero;
    private final String uuid;


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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sudoku:\n");
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
                sb.append(String.format("%4s", tablero[i][j] == 0 ? "." : tablero[i][j]));
                sb.append(String.format("%4s", " "));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void generar(int celdasVaciar) {
        rellenarTablero();
        copiarTableroSolucion();
        vaciarCeldas(celdasVaciar);
    }

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
                                return Boolean.TRUE;
                            }
                            tableroSolucion[fila][col] = 0;
                        }
                    }
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

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
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    private void copiarTableroSolucion() {
        for (int i = 0; i < TAMANYO; i++) {
            System.arraycopy(tableroSolucion[i], 0, tablero[i], 0, TAMANYO);
        }
    }

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
}