# Docker file for linshare core
FROM tomcat:8.0.43-jre8

LABEL Gopal Kollengode <gopal.kollengode.t3c.io>

ADD . /usr/local/my_app/
RUN find /usr/local/my_app/ -iname '*.war' -exec cp {} /usr/local/tomcat/webapps/ \;
ENV JPDA_ADDRESS="8000"
ENV JPDA_TRANSPORT="dt_socket"
EXPOSE 8080 8000
RUN chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["catalina.sh", "jpda", "run"]
