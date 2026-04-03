# Variables
JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
LIB = lib/TeleChargeAndPaySystem8.jar
MAIN = main

# Compilar
compile:
	$(JAVAC) -cp "$(LIB)" -d $(BIN_DIR) $(SRC_DIR)/*.java

# Ejecutar
run: compile
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" $(MAIN)

# Limpiar
clean:
	rm -rf $(BIN_DIR)/*