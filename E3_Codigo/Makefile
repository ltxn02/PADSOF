JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
# Al poner lib/* cogerá tanto la pasarela de pago como JUnit
LIB = lib/*
MAIN = ui.main

compile:
	mkdir -p $(BIN_DIR)
	$(JAVAC) -cp "$(LIB)" -d $(BIN_DIR) $(shell find $(SRC_DIR) -name "*.java")

run:
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" $(MAIN)

# Nuevo comando para ejecutar los tests en JUnit 5 Standalone
test: compile
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" org.junit.platform.console.ConsoleLauncher -c $(CLASS)

clean:
	rm -rf $(BIN_DIR)/*