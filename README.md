# TrackTikOptaPlanner

## Running OptaPlanner locally

Before you start, make sure:
1. You have the latest JDK build of JAVA installed AND pointing to your PATH. 
  To verify build: ```$ git java -version```
2. You've installed Apache Maven to be able to build the JAVA project. Make sure again to point it to your PATH
  To verify build: ```$ git mvn -version```
3. You've installed the latest version of Docker. Docker is used to create, deploy, and run applications by using containers

### In one terminal:
1. Clone the remote repository on your machine
```
$ git clone https://github.com/TrackTik/TrackTikOptaPlanner.git
```
2. Build the project in Maven (with the option to skip tests so it builds quickly)
```
$ mvn clean install -DskipTests
```
3. Run multi-container application via Docker in the background, print new container names.
```
$ docker-compose up -d
```
### In another terminal:
1. Start the scheduler service
```
$ java -jar schedulerservice/target/scheduler-service-0.16.2.jar
```
What to expect:
The output in the second terminal (the one where you ran schedulerservice) will show the logs as it runs.
### In yet another terminal:
2. Call the scheduler API
```
$ java -jar schedulerapi/target/scheduler-api-0.16.2.jar
```
What to expect:
The two should each print out the Spring logo in ascii characters, and the scheduler service will log three things, ending in "Started Application in 0.00 seconds"

Now send the export.json file found in data folder to port 8080 where the API is waiting to send it to the service
```
$ ./test_rest_post.sh
```
Alternatively, you can have the output generated in a text file. Note, the java service needs to be stopped manually (ctrl+c) as it is still running waiting for the next command.  
```
$ java -jar schedulerservice/target/scheduler-service-0.16.2.jar > file.txt
```

## Writing into a file
curl -i -X POST -H "Content-Type: application/json" --data-binary "@C:/Users/Dimitris Lianoudakis/ML_framework/tt-product/TrackTikOptaPlanner/data/export.json" localhost:8080/schedule
