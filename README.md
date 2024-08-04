# AEON Bank Library System
This is technical test assessment for the position Backend Engineer at AEON Bank.



how to build:
mvn clean install -Pdev
docker build -t aeonbanklibrary-app .
docker rm -f aeonbanklibrary-container
docker run -d -p 8080:8080 --name aeonbanklibrary-container aeonbanklibrary-app
