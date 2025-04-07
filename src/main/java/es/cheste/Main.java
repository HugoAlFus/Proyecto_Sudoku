package es.cheste;

import es.cheste.Objetos.Sudoku;
import es.cheste.Utilidad.Configuracion;
import es.cheste.Utilidad.GestorSudokus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Hugo Almodóvar Fuster
 * @version 1.0
 *
 * La clase Main es el punto de entrada de la aplicación de Sudoku.
 * Proporciona un menú para que el usuario elija entre jugar un nuevo Sudoku o comprobar uno existente.
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static Scanner sc = new Scanner(System.in);

    /**
     * Método principal que inicia la aplicación de Sudoku.
     * @param args los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {

        char eleccion = elegirOpcion();
        GestorSudokus gestorSudokus = new GestorSudokus();

        switch (eleccion) {
            case '0':
                System.err.println("Ocurrio un error inesperado, cerrando programa.");
                break;
            case '1':
                Sudoku sudoku = new Sudoku();
                int numHuecos = elegirDificultad();
                sudoku.generar(numHuecos);
                System.out.println(gestorSudokus.guardarSudoku(sudoku) ? "El sudoku se guardo correctamente"
                        : "Hubo un error y no se puedo guardar el sudoku");
                System.out.println(gestorSudokus.guardarDatosSudoku(sudoku) ? "Los datos del sudoku se han guardado correctamente"
                        : "Hubo un error al guardar los datos");
                break;
            case '2':

                String uuid = elegirFichero();

                if(uuid != null){
                    System.out.println(gestorSudokus.comprobarSudokus(uuid));
                }else System.err.println("Hubo un error no esperado");

                break;
        }
    }

    /**
     * Muestra un menú para que el usuario elija un archivo de Sudoku.
     * @return el nombre del archivo seleccionado.
     */
    private static String elegirFichero() {

        int opcion;
        StringBuilder sb = new StringBuilder("--Listado Sudokus--\n");
        List<String> listaUUID = GestorSudokus.obtenerUUIDSudoku();

        for (int i = 0; i < listaUUID.size(); i++) {
            sb.append((i + 1)).append(". ").append(listaUUID.get(i)).append("\n");
        }
        sb.append("Elija el sudoku indicando el número de delante: ");

        do {
            try {
                System.out.println(sb);
                opcion = sc.nextInt();
            } catch (NoSuchElementException | IllegalStateException e) {
                LOGGER.error("Ocurrio un error al elegir el sudoku {}", e.getMessage());
                return null;
            }

            if (opcion < 1 || opcion > listaUUID.size()) {
                System.err.println("Por favor, elija un número correcto");
            }

        } while (opcion < 1 || opcion > listaUUID.size());

        return listaUUID.get(opcion - 1);
    }

    /**
     * Muestra un menú para que el usuario elija la dificultad del Sudoku.
     * @return el número de huecos en el tablero según la dificultad elegida.
     */
    private static int elegirDificultad() {
        char opcion;

        do {
            System.out.println("""
                    Elija la dificultad del sudoku.
                    Por favor, introduzca el valor indicado:
                    1. Fácil
                    2. Media
                    3. Difícil
                    4. Salir""");
            try {
                opcion = sc.next().charAt(0);
            } catch (NoSuchElementException | IllegalStateException e) {
                LOGGER.error("Ocurrio un error al elegir la dificultad del sudoku {}", e.getMessage());
                opcion = '0';
            }

            if (opcion != '1' && opcion != '2' && opcion != '3' && opcion != '4') {
                System.out.println("Por favor, introduzca un valor válido");
            }

        } while (opcion != '1' && opcion != '2' && opcion != '3' && opcion != '0' && opcion != '4');

        return obtenerNumeroHuecos(opcion);
    }

    /**
     * Obtiene el número de huecos en el tablero según la dificultad elegida.
     * @param dificultad la dificultad elegida.
     * @return el número de huecos en el tablero.
     */
    private static int obtenerNumeroHuecos(char dificultad) {

        Random random = new Random();

        return switch (dificultad) {
            case '1' -> Integer.parseInt(Configuracion.getConfiguracion("sudoku.hueco.facil")) + random.nextInt(5);
            case '2' -> Integer.parseInt(Configuracion.getConfiguracion("sudoku.hueco.medio")) + random.nextInt(5);
            default -> Integer.parseInt(Configuracion.getConfiguracion("sudoku.hueco.dificil")) + random.nextInt(8);
        };
    }

    /**
     * Muestra un menú para que el usuario elija entre jugar un nuevo Sudoku o comprobar uno existente.
     * @return la opción elegida por el usuario.
     */
    private static char elegirOpcion() {

        char opcion;

        do {
            System.out.println("""
                    Elija si quiere jugar a un nuevo sudoku o quiere comprobar uno existente.
                    Por favor, introduzca el valor indicado:
                    1. Jugar nuevo sudoku
                    2. Comprobar sudoku
                    3. Salir""");
            try {
                opcion = sc.next().charAt(0);
            } catch (NoSuchElementException | IllegalStateException e) {
                LOGGER.error("Ocurrio un error al elegir la opcion de sudoku {}", e.getMessage());
                opcion = '0';
            }

            if (opcion != '1' && opcion != '2' && opcion != '3') {
                System.out.println("Por favor, introduzca un valor válido");
            }

        } while (opcion != '1' && opcion != '2' && opcion != '3' && opcion != '0');

        return opcion;
    }
}