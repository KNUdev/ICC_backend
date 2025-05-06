#todo improve this dockerfile

FROM openjdk:21-bookworm AS builder

WORKDIR application

LABEL maintainer = "knudev@knu.ua"
LABEL application.name = "icc"
LABEL version = "0.0.1"


#todo on test env we were need 12 line
ARG JAR_FILE=icc-app/target/*.jar
#ARG JAR_FILE=./*.jar

COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract && ls -R application/

FROM openjdk:21-bookworm AS runtime

WORKDIR application

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/security-dependencies/ ./
COPY --from=builder application/report-manager-dependencies/ ./
COPY --from=builder application/application-manager-dependencies/ ./
COPY --from=builder application/employee-manager-dependencies/ ./
#COPY --from=builder application/file-service-dependencies/ ./
COPY --from=builder application/application/ ./

CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]
