#!/bin/bash

# Script de setup pour construire et exécuter l'application Multimedia

echo "Installation et configuration de l'application Multimedia..."

# Vérifier si Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "Maven n'est pas installé. Installation de Maven..."
    sudo apt update
    sudo apt install -y maven
fi

# Aller dans le répertoire de l'application
cd "$(dirname "$0")/application"

# Construire le JAR avec Maven
echo "Construction du JAR avec Maven..."
mvn clean package

# Vérifier si le build a réussi
if [ $? -eq 0 ]; then
    echo "Build réussi !"
    echo "JAR créé : target/application-1.0-SNAPSHOT.jar"
    echo ""
    echo "Pour exécuter l'application :"
    echo "java -jar target/application-1.0-SNAPSHOT.jar"
else
    echo "Erreur lors du build. Vérifiez les logs ci-dessus."
    exit 1
fi