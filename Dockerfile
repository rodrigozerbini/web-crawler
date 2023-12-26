FROM maven:3.6.3-jdk-14

ADD . /usr/src/guerrero
WORKDIR /usr/src/guerrero
EXPOSE 4567
ENTRYPOINT ["mvn", "clean", "verify", "exec:java"]