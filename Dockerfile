#添加Java启动的必要镜像
FROM java:8
#创建一个目录存放jar包和配置
RUN mkdir -p /opt/emquantapi-sample
#设置开放端口号
EXPOSE 8090
#添加jar包
ADD ./target/emquantapi-sample-springboot.jar /opt/emquantapi-sample/emquantapi-sample-springboot.jar
#copy量化api库目录
COPY ./emquantapi_library /opt/emquantapi-sample/config/emquantapi_library

#添加进入docker容器后的目录
WORKDIR /opt/emquantapi-sample
#修改文件的创建修改时间
RUN bash -c 'touch /opt/emquantapi-sample/emquantapi-sample-springboot.jar'
#启动容器执行命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/emquantapi-sample/emquantapi-sample-springboot.jar"]