#basierend auf:
#https://developers.redhat.com/articles/2023/10/19/containerize-spring-boot-application-podman-desktop#building_the_container_image_with_podman_desktop

#Java image als Grundlage
FROM eclipse-temurin:17-jdk-jammy

#standart Ordner festlegen
WORKDIR /app

#Maven properties übertragen
COPY .mvn/ .mvn/

#Pom und maven übertragen
COPY mvnw pom.xml ./

#maven wrapper ausführbar machen
RUN chmod +x mvnw

#alle Abhängigkeiten mit maven importieren
RUN ./mvnw dependency:resolve

#Quellcode in das Image kopieren
COPY src/ src

#Port freigeben
Expose 8089

#App mit maven starten
CMD ["./mvnw", "spring-boot:run"]
