#!/usr/bin/env bash

IMAGE_NAME=$1
#镜像名称赋初始值，如果传入了参数，则以传入的为准
if [ "${IMAGE_NAME}" = "" ]
then
#  默认 "mysql"
  IMAGE_NAME="mysql"
else
  IMAGE_NAME=$1
fi

#docker 容器 Id，筛选 包含 IMAGE_NAME 的项，并打印返回该项的第三个参数
#完整样例 → mysql               5.7                 718a6da099d8        13 days ago         448MB
containerId=$(docker images | grep ${IMAGE_NAME} |awk '{print $3}')

if [ "${containerId}" = "" ]  
then  
	echo "cid is empty"
	exit 0
else    
	echo "cid is ${containerId}"
	exit 1
fi