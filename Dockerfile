# Jenkins JDK 17, JDK 11 is being deprecated
FROM jenkins/jenkins:latest-jdk17

USER root

RUN apt-get update && apt-get install -y lsb-release

RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg

RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

RUN apt-get update && apt-get install -y docker-ce-cli

RUN apt update && apt install tzdata -y
FROM your-base-image
# Add New Relic setup
RUN curl -L https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip -o newrelic-java.zip && \
    unzip newrelic-java.zip -d /opt && \
    rm newrelic-java.zip

ENV NEW_RELIC_APP_NAME="YourAppName"
ENV NEW_RELIC_LICENSE_KEY="NRAK-PISXGOL969BED7QOYP9S7VYRV0Q"

CMD ["java", "-javaagent:/opt/newrelic/newrelic.jar", "-jar", "your-app.jar"]


ENV TZ="Australia"

USER jenkins
