# Arrêt - relance :
docker stop/start <image-name>

# Logs :
docker logs <image-name>

# Mise à jour (rest-api / ihm) :
1. Arrêt du conteneur
2. Copie des artefacts (wget depuis Nexus ou scp en local) et rm score-ihm + unzip pour l'ihm -> voir ci-dessous
3. Modification de la configuration. IHM : /data/battlecode-2016/score-nginx/score.conf ; REST-API : /data/battlecode-2016/application.properties
4. Démarrage du conteneur



# Installation :

## Docker :
See docker documentation : https://docs.docker.com/engine/installation/linux/
option docker à ajouter si prise en charge :  --restart unless-stopped


## Gitlab :
docker run -d \
  --hostname fedora \
  --name gitlab \
  --publish 443:443 --publish 80:80 --publish 2222:22 \
  --volume /srv/gitlab/config:/etc/gitlab \
  --volume /srv/gitlab/logs:/var/log/gitlab \
  --volume /srv/gitlab/data:/var/opt/gitlab \
  --volume /srv/gitlab/logs/reconfigure:/var/log/gitlab/reconfigure \
  gitlab/gitlab-ce:latest


## Jenkins :
docker run -d \
  --name jenkins \
  --publish 8888:8080 --publish 50000:50000 \
  --volume /srv/jenkins/data:/var/jenkins_home \
  --link gitlab:gitlab \
  jenkins:alpine

+ security, plugins (dsl, node), proxy ?, tools (jgit, maven, node), credentials, slaves & configuration seed ...


## Rest API :
scp score-rest-api/target/score-rest-api/score-rest-api-1.1-SNAPSHOT.jar $user@$hostname:/data/battlecode-2016/score-rest-api.jar
- ou -
wget -O /data/battlecode-2016/score-rest-api/score-rest-api.jar http://softcu-nexus.si.francetelecom.fr/nexus/service/local/repositories/snapshots/content/com/dojocoders/score-rest-api/1.1-SNAPSHOT/score-rest-api-1.1-20161208.182110-2.jar

scp score-rest-api/src/main/resources/application.properties $user@$hostname:/data/battlecode-2016
 + edit

docker run -d \
  --name openjdk8 \
  --publish 7000:8080 \
  --volume /data/battlecode-2016/score-rest-api:/usr/src/myapp --workdir /usr/src/myapp \
  openjdk:8-jre-alpine \
  java -jar score-rest-api.jar


## IHM/Front :
scp score-ihm/target/score-ihm-1.1-SNAPSHOT.zip $user@$hostname:/data/battlecode-2016/score-ihm.zip
- ou -
wget -O /data/battlecode-2016/score-ihm.zip http://softcu-nexus.si.francetelecom.fr/nexus/service/local/repositories/snapshots/content/com/dojocoders/score-ihm/1.1-SNAPSHOT/score-ihm-1.1-20161208.182117-2.zip
cd /data/battlecode-2016 && rm -fr score-ihm score-nginx && unzip -q score-ihm.zip

export SCORE_REST_API_URL=http://dvdsi320w.creteil.francetelecom.fr:7000
envsubst < score-nginx/score.template > score-nginx/score.conf

docker run -d \
  --name nginx \
  --publish 8000:80 \
  --volume /data/battlecode-2016/score-nginx:/etc/nginx/conf.d:ro \
  --volume /data/battlecode-2016/score-ihm:/usr/share/nginx/score:ro \
  nginx:stable-alpine
