#!/usr/bin/env bash

curl -i -X POST -H "Content-Type: application/json" --data-binary "@./data/parameterized.json" localhost:8080/schedule
#curl -i -X POST -H "Content-Type: application/json" --data-binary "@./data/parameterized.json" ec2-54-91-172-100.compute-1.amazonaws.com:8080/schedule
