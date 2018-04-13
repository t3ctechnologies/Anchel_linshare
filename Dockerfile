# Docker file for linshare core
FROM tomcat:8.0.43-jre8

LABEL Gopal Kollengode <gopal.kollengode.t3c.io>

ADD . /usr/local/my_app/
RUN find /usr/local/my_app/ -iname '*.war' -exec cp {} /usr/local/tomcat/webapps/ \;
RUN chmod +x /usr/local/tomcat/bin/catalina.sh
EXPOSE 8080
CMD ["catalina.sh", "run"]