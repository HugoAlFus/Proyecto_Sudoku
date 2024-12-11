package es.cheste;

import es.cheste.Objetos.Sudoku;
import es.cheste.Utilidad.GestorSudokus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        char eleccion = elegirOpcion();
        GestorSudokus gestorSudokus = new GestorSudokus();

        switch (eleccion) {
            case '0':
                System.err.println("Ocurrio un error inesperado, cerrando programa.");
                break;
            case '1':
                int dificultad = elegirDificultad();
        }

    }

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
        //TODO conseguir la dificultad mediante la elccion
        //TODO Facil -> 32-36 ; Medio -> 37-41 ; Dificil -> 42-49
        return opcion;
    }

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