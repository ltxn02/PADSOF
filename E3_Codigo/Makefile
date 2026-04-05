JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
LIB = lib/TeleChargeAndPaySystem8.jar
MAIN = rongero.ui.main

compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -cp "$(LIB)" -d $(BIN_DIR) $(shell find $(SRC_DIR) -name "*.java")

run:
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" $(MAIN)

clean:
	rm -rf $(BIN_DIR)/*