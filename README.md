本地起个5000端口的私有镜像仓库
docker run -d -v /Users/hucc/docker/registry:/var/lib/registry -p 5000:5000 --name myregistry registry:2