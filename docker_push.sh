#!/bin/bash
docker build -t vinnycrm/anchel-core:1.0 .
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD";
docker push vinnycrm/anchel-core:1.0