FROM gradle:jdk17 as BUILD
WORKDIR /build
COPY --chown=gradle:gradle src /build/src
COPY --chown=gradle:gradle build.gradle settings.gradle /build/
RUN gradle --no-daemon shadowJar

FROM openjdk:17-slim
WORKDIR /app
COPY --from=BUILD /build/build/libs/sinon.jar sinon.jar
WORKDIR /app/work
ENTRYPOINT java -jar /app/sinon.jar
