#!/usr/bin/env bash

CONTAINER_NAME=$1
IMAGE_NAME=$2

tryToCleanContainer(){
#docker 容器 Id，筛选 包含 IMAGE_NAME 的项，并打印返回该项的第 1 个参数
#完整样例 → 147371c73384        mysql:5.7           "docker-entrypoint.s…"   3 hours ago         Up 3 hours          0.0.0.0:3306->3306/tcp, 33060/tcp   Mysql
containerId=$(docker ps -a | grep ${CONTAINER_NAME} |awk '{print $1}')

if [ "${containerId}" != "" ]
then
  docker-compose stop "${containerId}"
	docker-compose rm "${containerId}"
	echo "Remove Container ${CONTAINER_NAME}-${containerId}"
fi
}

tryToCleanImage(){
#docker 镜像 Id，筛选包含 IMAGE_NAME 的项，并打印返回该项的第 3 个参数
#完整样例 → mysql               5.7                 718a6da099d8        13 days ago         448MB
imageId=$(docker images | grep ${IMAGE_NAME} |awk '{print $3}')

if [ "${imageId}" != "" ]
then
	docker-compose rmi "${imageId}"
	echo "Remove image ${IMAGE_NAME}-${imageId}"
fi
}

#———————————————————————————函数主体开始———————————————————————————
#容器名称赋初始值，如果传入了参数，则以传入的为准
if [ "${CONTAINER_NAME}" = "" ]
then
#  默认 "Mysql"
  CONTAINER_NAME="Mysql"
else
  CONTAINER_NAME=$1
fi

#镜像名称赋初始值，如果传入了参数，则以传入的为准
if [ "${IMAGE_NAME}" = "" ]
then
#  默认 "mysql"
  IMAGE_NAME="mysql"
else
  IMAGE_NAME=$2
fi

# 尝试删除容器
tryToCleanContainer
# 尝试删除镜像
tryToCleanImage