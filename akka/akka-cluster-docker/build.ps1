docker-compose down
docker rmi acd-seed-node
mvn clean package
docker build --force-rm=true --no-cache=true -t acd-seed-node .