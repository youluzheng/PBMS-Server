FROM adoptopenjdk/openjdk11:latest

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

EXPOSE 8080

ENV PBMS_ROOTPATH=/pbms/upload

ADD .docker/application-template.yml /pbms/application.yml
ADD ./target/pbms-server-*.jar /pbms/pbms-server.jar

WORKDIR /pbms

ENTRYPOINT [ "java", "-jar", "pbms-server.jar"]