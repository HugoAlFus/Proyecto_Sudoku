package es.cheste.Utilidad;

import es.cheste.Objetos.Sudoku;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorSudokus {

    private static final String DIRECTORIO_FICHEROS = "src/main/resources/partidas/Sudoku_";
    private static final String DIRECTORIO_DATOS = "src/main/resources/datos/Sudoku_";
    private static final Logger LOGGER = LogManager.getLogger(GestorSudokus.class);

    private boolean guardarDatos(Sudoku sudoku) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORIO_DATOS + sudoku.getUuid() + ".dat"))) {

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void comenzarSudoku() {

        Sudoku sudoku = new Sudoku();
        sudoku.generar(20);

        guardarSudoku(sudoku);
    }

    private boolean guardarSudoku(Sudoku sudoku) {

        Path pathPartida = Paths.get(DIRECTORIO_FICHEROS + sudoku.getUuid() + ".txt");

        try {
            Files.writeString(pathPartida, sudoku.toString());

        } catch (IOException e) {
            LOGGER.error("Hubo un error al guardar el sudoku {}", e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
