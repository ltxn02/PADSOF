JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
LIB = lib/TeleChargeAndPaySystem8.jar
MAIN = main

compile:
	mkdir -p $(BIN_DIR)
	$(JAVAC) -cp "$(LIB)" -d $(BIN_DIR) $(SRC_DIR)/*.java

run:
	$(JAVA) -cp "$(BIN_DIR):$(LIB)" $(MAIN)

clean:
	rm -rf $(BIN_DIR)/*