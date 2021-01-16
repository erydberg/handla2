FROM adoptopenjdk/openjdk13
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENV TZ=Europe/Stockholm
#RUN mv /etc/localtime /etc/localtime.bak && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]