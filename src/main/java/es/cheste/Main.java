package es.cheste;

import es.cheste.Objetos.*;
import es.cheste.Utilidad.GestorSudokus;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        GestorSudokus gestorSudokus = new GestorSudokus();
        gestorSudokus.comenzarSudoku();
    }
}