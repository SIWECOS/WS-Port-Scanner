FROM tomcat:latest as BUILDER

WORKDIR /src

RUN apt update \
    && apt-get upgrade -y \
    && apt install -y git maven libcurl3-gnutls default-jdk libgnutls30 procps nmap \
    && dpkg -l | grep libgnutls \
    && rm -r /var/lib/apt/lists/*

COPY . .

RUN mvn clean install -DskipTests=true


FROM tomcat:alpine

RUN apk add nmap \
    && rm /usr/local/tomcat/webapps/ROOT -r -f

COPY --from=BUILDER /src/target/WS-Port-Scanner-*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
