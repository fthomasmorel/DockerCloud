# DockerCloud

## What is DockerCloud
Java program implementing auto-scaling policy through docker. There is one nginx container as a load-balancer, balancing on one or more Apache containers running a PHP application. 
The aim is to adapt the number of Apache containers depending on the entry charge of the load-balancer.

The JAVA part use the Docker REST API to create/start/stop containers. Then through a thread, it analysis response time from the load-balancer to determine if it had to add or remove containers.



## Configuration on OS X

You need to install Docker and boot2docker first, and then activate the REST API on the boot2docker VM.

## TODO (explaination)

To activate the Docker REST API you need this (from [here]("http://stackoverflow.com/questions/26824230/enabling-remote-api-in-docker-on-mac-os-x-boot2docker")) :
```bash
# Connect to boot2docker VM via SSH
boot2docker ssh

#Then type the following lines
cp /etc/init.d/docker ~/docker.bak
sudo sed -i 's/DOCKER_TLS:=auto/DOCKER_TLS:=no/1' /etc/init.d/docker
sudo /etc/init.d/docker stop
sudo /etc/init.d/docker start
```

Once this step is done, you have to configure the images that will be used by the JAVA program. There are 2 images (nginx and apache) with Dockerfile.
You just have to execute the config script as follow :

```bash
cd DockerCloud
./config.sh
```

## Running
TODO - or you can open the project with Eclipse and run

## Graphic Output
TODO


