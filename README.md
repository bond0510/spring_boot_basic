# spring-boot-api-template

This project is for spring-boot-api-template.


## Getting Started ##

### Prerequisites ###

* Git
* JDK 1.8
* Spring Boot 2.6.6
* Maven 3.0 or later


### Clone ###

To get started you can simply clone this repository using git:

```
git clone git@bitbucket.org:entercard/spring-boot-api-template.git
cd spring-boot-api-template
```


### Build and run application locally

You can run the application from the command line using:

```
mvn spring-boot:run
```

Or you can build a single executable JAR file that contains all the necessary dependencies, classes, and resources with:

```
mvn clean package
```

Then you can run the JAR file with:

```
java -jar target/spring-boot-api-template-bootified.jar
```

*Instead of `mvn` you can also use the maven-wrapper `./mvnw` to ensure you have everything necessary to run the Maven build.*

Also you can execute the main method in the

```
ec.msplatform.Application
```

 class from your IDE.

To test that it works, open a browser tab at

```
http://localhost:8081/basic/actuator/health
```


Here are some endpoints you can call:

<b>Get information about system health, configurations, etc.</b>

```
http://localhost:8081/basic/actuator/health/liveness
http://localhost:8081/basic/actuator/health/readiness
http://localhost:8081/basic/actuator/env
```

## Docker image ##

The **test-test-basic-app** image is created and pushed to *OCIR (Oracle Cloud Infrastructure Registry)* through Jenkins pipeline.


## Kubernetes artifacts ##

The following Kubernetes artifacts are used to setup the **spring-boot-api-template** micro-service in *OKE (Oracle Container Engine for Kubernetes)*  environment :

| Artifact Name  | Description |
| :--- | :---|
| test-basic-app.yaml  | This is responsible to create *test-basic-app* , *test-basic-deployment*  and  *test-basic-service*  kubernetes Pod,Deploment,Service objects respectively . |
| test-basic-configmap.yaml|This is responsible to create *test-basic-configmap*  kubernetes ConfigMap object ,which holds application properties.|
| test-basic-secret.yaml| This is responsible to create *test-basic-secret*  kubernetes Secret object ,which holds application secret properties.|

