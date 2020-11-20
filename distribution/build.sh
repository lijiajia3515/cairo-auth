docker build -t registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-service:snapshot -f auth-service/Dockerfile ./auth-service
docker build -t registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-server:snapshot -f auth-server/Dockerfile ./auth-server
docker push registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-service:snapshot
docker push registry.cn-beijing.aliyuncs.com/hfhksoft/cairo-auth-server:snapshot
