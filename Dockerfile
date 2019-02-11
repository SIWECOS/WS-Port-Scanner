FROM tomcat:latest
RUN apt update && apt-get upgrade -y && apt install -y git maven libgnutls30 libcurl3-gnutls default-jdk nmap
WORKDIR /src
RUN dpkg -l | grep libgnutls
RUN git clone https://github.com/SIWECOS/WS-Port-Scanner.git
WORKDIR /src/WS-Port-Scanner
RUN git pull
RUN mvn clean install -DskipTests=true
RUN cp target/WS-Port-Scanner-*.war /usr/local/tomcat/webapps/ROOT.war
RUN rm /usr/local/tomcat/webapps/ROOT -r -f
EXPOSE 8080
