FROM tomcat:latest as BUILDER

WORKDIR /src

RUN apt update \
    && apt-get upgrade -y \
    && apt install -y git maven libcurl3-gnutls default-jdk libgnutls30 procps nmap \
    && dpkg -l | grep libgnutls \
    && rm -r /var/lib/apt/lists/*

RUN git clone https://github.com/SIWECOS/WS-Port-Scanner.git \
    && cd /src/WS-Port-Scanner && mvn clean install -DskipTests=true



FROM tomcat:alpine

RUN apk add nmap \
    && rm /usr/local/tomcat/webapps/ROOT -r -f

COPY --from=BUILDER /src/WS-Port-Scanner/target/WS-Port-Scanner-*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
