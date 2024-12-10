package es.cheste.Utilidad;

import es.cheste.Objetos.Sudoku;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GestorSudokus {

    private static final String DIRECTORIO_PARTIDAS = Configuracion.getConfiguracion("directorio.partidas");
    private static final String DIRECTORIO_DATOS = Configuracion.getConfiguracion("directorio.datos");
    private static final Logger LOGGER = LogManager.getLogger(GestorSudokus.class);

    public void comenzarSudoku() {

        Sudoku sudoku = new Sudoku();
        sudoku.generar(2);

        /*guardarSudoku(sudoku);
        guardarDatosSudoku(sudoku);*/
        Sudoku sudoku1 = cargarDatosSudoku("d006d1ed-7783-4d82-b318-e2179483fc5f");
        int[][] tablero = cargarSudoku("d006d1ed-7783-4d82-b318-e2179483fc5f");

        System.out.println(sudoku1.comprobarResultado(tablero));
    }

    private int[][] cargarSudoku(String uuidSudoku) {
        final int tamanyo = Integer.parseInt(Configuracion.getConfiguracion("sudoku.tamanyo"));
        Path pathPartida = Paths.get(DIRECTORIO_PARTIDAS + uuidSudoku + ".txt");
        List<String> lineas;
        int[][] tablero = new int[tamanyo][tamanyo];

        try {
            lineas = Files.readAllLines(pathPartida);
            lineas = lineas.stream()
                    .map(linea -> linea.replaceAll("[|\\-+]", " "))
                    .filter(linea ->!linea.trim().isEmpty())
                    .toList();
            for (int i = 0; i < tamanyo; i++) {

                int[] numeros = obtenerNumerosDeLinea(lineas.get(i), tamanyo);
                if (Arrays.stream(numeros).count() != 0) {

                    System.arraycopy(numeros, 0, tablero[i], 0, tamanyo);
                } else return new int[tamanyo][tamanyo];
            }
        } catch (IOException e) {
            LOGGER.error("Hubo un error al cargar el sudoku.\nUUID: {}\nMensaje: {}", uuidSudoku, e.getMessage());
            return null;
        }
        return tablero;
    }

    private int[] obtenerNumerosDeLinea(String linea, int tamanyo) {
        int[] numeros = new int[tamanyo];
        String[] valores = linea.trim().split("\\s+");
        for (int j = 0; j < tamanyo; j++) {
            if (!valores[j].contains(".")) {
                numeros[j] = Integer.parseInt(valores[j]);
            } else return new int[tamanyo];
        }
        return numeros;
    }

    private boolean guardarSudoku(Sudoku sudoku) {

        Path pathPartida = Paths.get(DIRECTORIO_PARTIDAS + sudoku.getUuid() + ".txt");

        try {
            Files.writeString(pathPartida, sudoku.mostarTableroResolver());
        } catch (FileNotFoundException e) {
            LOGGER.error("No se ha encontrado el fichero en el método 'guardarSudoku' {}", e.getMessage());
            return Boolean.FALSE;
        } catch (IOException e) {
            LOGGER.error("Hubo un error al guardar el sudoku {}", e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean guardarDatosSudoku(Sudoku sudoku) {

        boolean esValido = Boolean.TRUE;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORIO_DATOS + sudoku.getUuid() + ".dat"))) {

            oos.writeObject(sudoku);

        } catch (FileNotFoundException e) {
            LOGGER.error("No se ha encontrado el fichero en el método 'guardarDatosSudoku' {}", e.getMessage());
            esValido = Boolean.FALSE;
        } catch (IOException e) {
            LOGGER.error("Ocurrio un error al guardar el objeto {}", e.getMessage());
            esValido = Boolean.FALSE;
        }

        return esValido;
    }

    private Sudoku cargarDatosSudoku(String uuidSudoku) {

        Sudoku sudoku = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORIO_DATOS + uuidSudoku + ".dat"))) {
            sudoku = (Sudoku) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Hubo un error al cargar los datos del sudoku.\nUUID: {}\nMensaje: {}", uuidSudoku, e.getMessage());
        }

        return sudoku;
    }
}
