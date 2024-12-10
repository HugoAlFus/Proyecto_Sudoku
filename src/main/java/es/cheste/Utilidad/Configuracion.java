package es.cheste.Utilidad;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuracion {

    private static final Properties CONFIGURACION = new Properties();
    private static final String RUTA_CONFIGURACION = "src/main/resources/properties/configuracion_sudoku.properties";
    private static final Logger LOGGER = LogManager.getLogger(Configuracion.class);

    static {
        try (FileInputStream fis = new FileInputStream(RUTA_CONFIGURACION)) {
            CONFIGURACION.load(fis);
        } catch (IOException e) {
            LOGGER.error("Hubo un error al intentar cargar la configuracion.\nPath: {}\nMensaje: {}", RUTA_CONFIGURACION, e.getMessage());
            throw new ExceptionInInitializerError("No se pudo cargar la configuración");
        }
    }

    public static String getConfiguracion(String key) {
        return CONFIGURACION.getProperty(key);
    }
}
