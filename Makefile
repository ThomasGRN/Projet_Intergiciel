BUILD_DIR = go/
JAR_FILE = canaux.jar
SOURCES_FILE = sources.txt

# Cible par défaut
all: clean compile jar

# Supprime tous les fichiers.jar existants
clean:
	rm -f $(JAR_FILE)
	rm -f $(SOURCES_FILE)
	find $(BUILD_DIR) -name "*.class" -type f -exec rm {} \;

# Compilation des fichiers.java
compile:
	@find $(BUILD_DIR) -name "*.java" > $(SOURCES_FILE)
	@javac @$(SOURCES_FILE)

# Génération du fichier.jar
jar:
	@jar -c -f $(JAR_FILE) $(BUILD_DIR)/*.class $(BUILD_DIR)/**/*.class
