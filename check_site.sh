#!/usr/bin/env bash

curl -s -o "/dev/null" http://ec2-54-91-172-100.compute-1.amazonaws.com:8080/swagger-ui.html#/scheduling-controller || sudo systemdctl restart scheduler && sudo systemdctl restart schedulerapi
