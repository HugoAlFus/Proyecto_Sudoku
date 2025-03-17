package es.cheste.Utilidad;

import es.cheste.Objetos.Sudoku;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hugo Almodóvar Fuster
 * @version 1.0
 * La clase GestorSudokus proporciona métodos para cargar y guardar tableros de Sudoku,
 * así como para gestionar los datos de los Sudokus.
 */
public class GestorSudokus {

    private static final String DIRECTORIO_PARTIDAS = Configuracion.getConfiguracion("directorio.partidas") + "Sudoku_";
    private static final String DIRECTORIO_DATOS = Configuracion.getConfiguracion("directorio.datos") + "Sudoku_";
    private static final Logger LOGGER = LogManager.getLogger(GestorSudokus.class);

    /**
     * Obtiene una lista de UUIDs de los Sudokus guardados.
     * @return una lista de UUIDs de los Sudokus guardados.
     */
    public static List<String> obtenerUUIDSudoku() {

        File[] listaFicheros = obtenerListadoPartidas();

        if (listaFicheros != null) {

            List<String> uuidFichero = new ArrayList<>();

            for (File fichero : listaFicheros) {

                String[] ficheroSplit;

                ficheroSplit = fichero.getName().split("_");
                uuidFichero.add(ficheroSplit[1]);
            }

            return uuidFichero;
        }

        return new ArrayList<>();
    }

    /**
     * Obtiene una lista de archivos de partidas guardadas en el directorio de partidas.
     * @return un array de archivos que representan las partidas guardadas.
     */
    private static File[] obtenerListadoPartidas() {

        File directorio = new File(DIRECTORIO_PARTIDAS);

        if (directorio.isDirectory()) {
            return directorio.listFiles();
        }
        return null;
    }

    /**
     * Carga un tablero de Sudoku desde un archivo de texto.
     *
     * @param uuidSudoku el UUID del Sudoku a cargar.
     * @return una matriz bidimensional que representa el tablero de Sudoku.
     */
    public int[][] cargarSudoku(String uuidSudoku) {
        final int tamanyo = Integer.parseInt(Configuracion.getConfiguracion("sudoku.tamanyo"));
        Path pathPartida = Paths.get(DIRECTORIO_PARTIDAS + uuidSudoku + ".txt");
        List<String> lineas;
        int[][] tablero = new int[tamanyo][tamanyo];

        try {
            lineas = Files.readAllLines(pathPartida);
            lineas = lineas.stream()
                    .map(linea -> linea.replaceAll("[|\\-+]", " "))
                    .filter(linea -> !linea.trim().isEmpty())
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

    /**
     * Obtiene los números de una línea de texto que representa una fila del tablero de Sudoku.
     * @param linea la línea de texto.
     * @param tamanyo el tamaño del tablero de Sudoku.
     * @return un array de enteros que representa los números de la fila.
     */
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

    /**
     * Guarda un tablero de Sudoku en un archivo de texto.
     * @param sudoku el objeto Sudoku a guardar.
     * @return true si el Sudoku se guardó correctamente, false en caso contrario.
     */
    public boolean guardarSudoku(Sudoku sudoku) {

        Path pathPartida = Paths.get(DIRECTORIO_PARTIDAS + sudoku.getUuid() + ".txt");

        try {
            Files.writeString(pathPartida, Sudoku.mostrarTablero(sudoku.getTablero()));
        } catch (FileNotFoundException e) {
            LOGGER.error("No se ha encontrado el fichero en el método 'guardarSudoku' {}", e.getMessage());
            return Boolean.FALSE;
        } catch (IOException e) {
            LOGGER.error("Hubo un error al guardar el sudoku {}", e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Guarda los datos de un objeto Sudoku en un archivo binario.
     * @param sudoku el objeto Sudoku a guardar.
     * @return true si los datos se guardaron correctamente, false en caso contrario.
     */
    public boolean guardarDatosSudoku(Sudoku sudoku) {

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

    /**
     * Carga los datos de un objeto Sudoku desde un archivo binario.
     * @param uuidSudoku el UUID del Sudoku a cargar.
     * @return el objeto Sudoku cargado, o null si hubo un error.
     */
    public Sudoku cargarDatosSudoku(String uuidSudoku) {

        Sudoku sudoku = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORIO_DATOS + uuidSudoku + ".dat"))) {
            sudoku = (Sudoku) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Hubo un error al cargar los datos del sudoku.\nUUID: {}\nMensaje: {}", uuidSudoku, e.getMessage());
        }

        return sudoku;
    }

}