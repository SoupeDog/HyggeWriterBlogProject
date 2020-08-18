#!/usr/bin/env bash

mvn -e -am -Dmaven.test.skip=true install

echo "install dependence of Service-Blog"

cd Service-Blog

mvn -am -Dmaven.test.skip=true clean package

echo "packaged Service-Blog"

cd ../

mv Service-Blog/target/*.jar Deploy/Service-Blog/service-blog.jar

echo "move service-blog.jar to Deploy/Service-Blog/"