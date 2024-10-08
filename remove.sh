#!/bin/bash
docker kill platform-frontend-services
docker container rm platform-frontend-services
docker rmi -f platform-frontend-services

