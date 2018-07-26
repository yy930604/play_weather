set -ex
# SET THE FOLLOWING VARIABLES
# docker hub username
# asclepius
USERNAME=weather_wechat
# image name
IMAGE=scala_play
docker build -t $USERNAME/$IMAGE:latest .
version=`cat VERSION`
echo "version: $version"
docker tag $USERNAME/$I:MAGE:latest $USERNAME/$IMAGE:$version
