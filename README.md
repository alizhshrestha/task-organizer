# TASK ORGANIZER

This is task organizer application that enable user to assign task to users and create, view, update, transition and tract the progress of individual tasks that are created by them or assigned to them.

## GUIDELINES COMMAND TO PACKAGE AND DEPLOY THE APPLICATION

The step by step command to package application from maven and run from docker container.

### COMMAND TO PACKAGE AND DEPLOY TASK ORGANIZER APPLICATION IN DOCKER
1. ```sh
    mvn clean package -Dmaven.test.skip
    ```
2. ```sh
    docker-compose up --build
    ```

