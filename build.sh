#!/bin/bash
docker build -t platform-frontend-services .
docker run -d -it --restart=always --network=microsrv-net -p 9081:9092 --name platform-frontend-services platform-frontend-services

