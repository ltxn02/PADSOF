package util;

import logic.Application;
import java.io.*;

public class BinPersistenceStrategy implements IPersistenceStrategy {

    @Override
    public void save(Application app, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(app);
            System.out.println("[Sistema] Datos guardados exitosamente en " + path);
        }
    }

    @Override
    public Application load(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Application) ois.readObject();
        }
    }
}