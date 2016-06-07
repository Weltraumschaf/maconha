#!//usr/bin/env bash

contentType="Content-Type: application/json;charset=UTF-8"
name=${1}
data=${2}

if [ "" == "${name}" ] ; then
    echo "No jobname given."
    exit 1
fi

url="http://localhost:8080/api/jobs?name=${name}"
echo "Requesting ${url}"

if [ "" == "${data}" ] ; then
    data="{}"
fi

curl -i -X POST --data "${data}" -H "${contentType}" ${url}

