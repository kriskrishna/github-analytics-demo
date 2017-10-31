#!/usr/bin/env bash

# Forward the port first
# kubectl port-forward --namespace=sc-pipelines-prod $( kubectl get pods --namespace=sc-pipelines-prod | grep github-webhook | head -1 | awk '{print $1}' ) 9876:8080

curl -X POST http://localhost:9876/ -d @../github-webhook/src/test/resources/github-webhook-input/issue-created.json \
--header "Content-Type: application/json"
