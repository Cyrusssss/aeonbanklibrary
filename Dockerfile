FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache tzdata dumb-init

ENV TZ=Asia/Kuala_Lumpur

EXPOSE 8080

RUN mkdir /appdir
RUN mkdir /appdir/log

RUN chown -R root /appdir

WORKDIR /appdir

COPY target/*.jar app.jar

ENTRYPOINT ["/usr/bin/dumb-init", "--"]

CMD ["java","-jar","app.jar"]
