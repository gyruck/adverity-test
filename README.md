# Adverity homework
This project is a basic implementation of an ETL task.

The following has been used to accomplish the task:
* Java
* Spring Boot 2 (Web, JPA, RestDocs)
* Gradle
* Docker, Kubernetes

## Running the application

In order to load the database, first you have to call the following:
```shell
$ curl 'http://localhost:8080/database/load-from-s3' -i -X GET
```

### Spring boot
The simplest way of running the application is using the gradle wrapper and embedded webserver:
```shell
$ ./gradlew bootRun
```

### Docker
First let's build the docker image:
```shell
$ ./gradlew bootBuildImage --imageName=adverity/task/gruck
```

Running the image
```shell
$ docker run -p 8080:8080 -it docker.io/adverity/task/gruck:latest
```

### Kubernetes
Creating deployment and service
```shell
$ kubectl create deployment gruck-test --image=docker.io/adverity/task/gruck:latest --dry-run=client -o=yaml > deployment.yaml
$ echo --- >> deployment.yaml
$ kubectl create service clusterip gruck-test --tcp=8080:8080 --dry-run=client -o=yaml >> deployment.yaml
```

Deploying the application and service to your k8s cluster
```shell
$ kubectl apply -f deployment.yaml
```

## Testing
The project contains the following for static code analysis:
* Checkstyle
* PMD
* SpotBugs

in order to run them use the following depending on your target:
```shell
$ ./gradlew check
$ ./gradlew checkstyle
$ ./gradlew pmdMain
$ ./gradlew spotbugsMain
```

## API documentation
For API contract documentation purpose spring-restdocs is used.
It generates the document from the test code
where snippets are saved in `build/generated-snippets` folder.

Generating asciidoc
```shell
$ ./gradlew asciidoc
```

Once generation happens the following HTML will contain the documentation:
`build/docs/asciidoc/index.html`

The docs are copied to `docs` folder which is used by Git pages:
https://gyruck.github.io/adverity-test/

## CI
Currently, Git Actions is used for building and testing the project:
https://github.com/gyruck/adverity-test/actions