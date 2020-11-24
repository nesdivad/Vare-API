ARG VERSION=8u151

FROM openjdk:${VERSION}-jdk as BUILD

COPY . /src
WORKDIR /src
RUN ./gradlew --no-daemon shadowJar

FROM openjdk:${VERSION}-jre

COPY --from=BUILD /src/build/libs/Vare-API-0.0.1-all.jar /bin/runner/run.jar
WORKDIR /bin/runner

ENTRYPOINT ["java", "-jar", "run.jar"]