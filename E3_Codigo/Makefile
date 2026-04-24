JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
# Al poner lib/* cogerá tanto la pasarela de pago como JUnit
LIB = lib/*

# Tus dos puntos de entrada (Consola y GUI)
MAIN_CONSOLA = ui.main
MAIN_GUI = gui.VentanaPrincipal

# Esta línea mágica le dice a Make que estos nombres son COMANDOS, no carpetas
.PHONY: compile run run-gui test doc clean

compile:
	mkdir -p $(BIN_DIR)
	$(JAVAC) -cp "$(LIB)" -d $(BIN_DIR) $(shell find $(SRC_DIR) -name "*.java")
	cp -r src/foto $(BIN_DIR)/

# Comando para ejecutar la consola antigua
run: compile
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" $(MAIN_CONSOLA)

# Comando NUEVO para ejecutar la interfaz gráfica con Swing
run-gui: compile
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" $(MAIN_GUI)

# Comando para ejecutar los tests en JUnit 5 Standalone
#      make test CLASS=tests.<nombreArchivoTest>
test: compile
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" org.junit.platform.console.ConsoleLauncher -c $(CLASS)

# Generar la documentación Javadoc
doc:
	mkdir -p doc
	javadoc -Xdoclint:none -d doc -sourcepath $(SRC_DIR) -cp "$(LIB)" -encoding utf8 -docencoding utf8 -charset utf8 $(shell find $(SRC_DIR) -name "*.java")

clean:
	rm -rf $(BIN_DIR)/*