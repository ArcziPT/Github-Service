#!/bin/bash
rm -rf target
mvn clean install
sudo docker build -t arczipt/github-service .