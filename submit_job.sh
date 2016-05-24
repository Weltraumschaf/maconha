#!//usr/bin/env bash

data="{\"name\": \"${1}\"}"

curl -i -X POST \
    --data "${data}" \
    -H "Content-Type: application/json;charset=UTF-8" \
    http://localhost:8080/api/jobs