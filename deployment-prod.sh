#!/bin/sh
echo "Starting container"

dockerFileNameENV="docker-compose.yml"
projectName="hackathondb"

echo "Stopping old container"
docker-compose --project-name=$projectName -f dockerFileNameENV stop
docker-compose --project-name=$projectName -f dockerFileNameENV rm

printf 'y' | docker image prune -a


docker-compose -f $dockerFileNameENV pull
docker-compose --project-name=$projectName -f $dockerFileNameENV up -d

echo "Waiting for new container to spin up ..."
sleep 30s

