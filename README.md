# emquantapi-sample-springboot

### 下载安装Docker Desktop 本地工具
https://www.docker.com/get-started

### docker 本地私有镜像仓库
docker run -d -v /Users/hucc/docker/registry:/var/lib/registry -p 5000:5000 --name myregistry registry:2

### 人工激活流程
- 修改application.yml：emquantapi.manualactivate.run=true，容器打包运行
- 联系客服人员操作，邮件接收userinfo文件
- userinfo文件（去除txt后缀名）放到本地工程emquantapi_library目录
- 修改application.yml：emquantapi.manualactivate.run=false，容器打包运行


### 非docker运行
mvn package -DskipDocker=true

