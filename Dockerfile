FROM tomcat:8-jdk8-corretto
VOLUME /tmp
#VOLUME /configfiles
ADD target/spring-security-login-and-registration.war /usr/local/tomcat/webapps/ROOT.war

#COPY ./config.json /usr/local/tomcat/webapps/config.json

EXPOSE 8080
VOLUME /upload-dir
VOLUME /upload-dir-admin
CMD ["catalina.sh", "run"]

#ADD ./config.json /usr/local/tomcat/webapps/ROOT/config.json