/**
 *
package util;

import logic.Application;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Clase encargada de la persistencia de datos de la aplicación.
 * Utiliza el patrón Strategy para permitir diferentes formatos de guardado
 * (Serialización, JSON, BD) delegando la carga y escritura de la instancia de Application.
 * * @author Taha Ridda En Naji
 * @version 3.0
 *//*
public class SaveLoader {
    private static final String FILE_PATH = "data/rongero_data.dat";
    private IPersistenceStrategy strategy;

    /**
     * Constructor que define la estrategia de persistencia a utilizar.
     * * @param strategy Implementación de IPersistenceStrategy (ej. Binario o XML).
     * @author Taha Ridda En Naji
     *//*
    public SaveLoader(IPersistenceStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Almacena el estado actual de la aplicación en el disco duro.
     * * @param app Instancia de Application que contiene todos los datos del sistema.
     * @author Taha Ridda En Naji
     *//*
    public void saveData(Application app) {
        try {
            strategy.save(app, FILE_PATH);
        } catch (IOException e) {
            System.err.println("[!] Error crítico al guardar: " + e.getMessage());
        }
    }

    /**
     * Recupera los datos almacenados desde el archivo definido en FILE_PATH.
     * Gestiona específicamente el caso donde el archivo no existe (primera ejecución)
     * devolviendo null para que el sistema inicie de cero.
     * * @return Instancia de Application recuperada o null si no hay datos previos.
     * @author Taha Ridda En Naji
     *//*
    public Application loadData() {
        try {
            return strategy.load(FILE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("[i] No se encontró archivo de datos previo. Iniciando sistema nuevo.");
            return null;
        } catch (Exception e) {
            System.err.println("[!] Error inesperado al cargar datos: " + e.getMessage());
            return null;
        }
    }
}
**/