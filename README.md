# ms-tenpo-test

## Instrucciones para instalar y ejecutar el proyecto

### Install docker

``` 
$ sudo apt-get update

$ sudo apt-get install docker-ce \
    docker-ce-cli \ 
    containerd.io \
    curl
```


### Install docker-compose

```
$ sudo curl -L "https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

$ sudo chmod +x /usr/local/bin/docker-compose
```


###Install Git
```
$ sudo apt install git-all
```

### Clone el repositorio del proyecto
```
$ git clone https://github.com/ceeconro/ms-tenpo-test.git

cd ms-tenpo-test
```

### Ejecutar el proyecto con docker-compose
```
$ docker-compose up -d

curl http://localhost:8080/actuator/health
```

## Instrucciones para probar

### Con Postman
1. Importar la coleccion `postman_collection/ms-tenpo-test.postman_collection.json` en postman

2. Ejecutar los pasos en  el orden en que se encuentran numerados

### Con Swagger
* Acceder a la URL [swagger-ui](http://localhost:8080/swagger-ui/#/)

