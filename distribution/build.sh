docker build -t registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-service:snapshot -f service/Dockerfile ./service
docker build -t registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-server:snapshot -f server/Dockerfile ./server
docker push registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-service:snapshot
docker push registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-server:snapshot
