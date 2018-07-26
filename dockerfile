FROM ysihaoy/scala-play:2.12.3-2.6.2-sbt-0.13.15 as builder

# caching dependencies
COPY ["build.sbt", "/tmp/build/"]
COPY ["project/plugins.sbt", "project/build.properties", "/tmp/build/project/"]
RUN cd /tmp/build && \
 sbt compile && \
 sbt test:compile && \
 rm -rf /tmp/build

# copy code
ADD . /root/app/
WORKDIR /root/app
RUN sbt compile && sbt test:compile && sbt dist

FROM openjdk:8u151
COPY --from=builder /root/app/target/universal/asclepius_backend_tongue_wechat-1.0.zip /opt/
WORKDIR /opt/
RUN unzip ./asclepius_backend_tongue_wechat-1.0.zip ; \
 chmod +x asclepius_backend_tongue_wechat-1.0/bin/asclepius_backend_tongue_wechat
CMD /opt/asclepius_backend_tongue_wechat-1.0/bin/asclepius_backend_tongue_wechat -Dplay.http.secret.key=abcdefghijk -Dhttp.port=80