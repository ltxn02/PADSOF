package util;
import logic.Application;
import java.io.IOException;

public interface IPersistenceStrategy {
    void save(Application app, String path) throws IOException;
    Application load(String path) throws IOException, ClassNotFoundException;
}