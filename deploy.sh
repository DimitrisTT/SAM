#!/usr/bin/env bash
VERSION="0.16.2"
scp -C schedulerapi/target/scheduler-api-${VERSION}.jar nellis@ec2-54-91-172-100.compute-1.amazonaws.com:/usr/local/scheduler
scp -C schedulerservice/target/scheduler-service-${VERSION}.jar nellis@ec2-54-91-172-100.compute-1.amazonaws.com:/usr/local/scheduler
ssh nellis@ec2-54-91-172-100.compute-1.amazonaws.com 'rm -f /usr/local/scheduler/scheduler-api.jar'
ssh nellis@ec2-54-91-172-100.compute-1.amazonaws.com 'rm -f /usr/local/scheduler/scheduler-service.jar'
ssh nellis@ec2-54-91-172-100.compute-1.amazonaws.com ln -s /usr/local/scheduler/scheduler-api-${VERSION}.jar /usr/local/scheduler/scheduler-api.jar
ssh nellis@ec2-54-91-172-100.compute-1.amazonaws.com ln -s /usr/local/scheduler/scheduler-service-${VERSION}.jar /usr/local/scheduler/scheduler-service.jar
