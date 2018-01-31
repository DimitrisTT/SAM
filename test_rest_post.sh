#!/usr/bin/env bash

#curl -i -X POST -H "Content-Type: application/json" --data-binary "@./schedulerapi/src/main/resources/examples.txt" localhost:8080/schedule
curl -i -X POST -H "Content-Type: application/json" --data-binary "@./schedulerapi/src/main/resources/examples.txt" ec2-54-91-172-100.compute-1.amazonaws.com:8080/schedule
