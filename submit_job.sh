#!//usr/bin/env bash

contentType="Content-Type: application/json;charset=UTF-8"
url="http://localhost:8080/api/jobs"
name=${1}
data=${2}

if [ "" == "${name}" ] ; then
    echo "No jobname given."
    curl -i -X GET -H "${contentType}" ${url}
else
    echo "Requesting ${url}"

    if [ "" == "${data}" ] ; then
        data="{}"
    fi

    curl -i -X POST --data "${data}" -H "${contentType}" ${url}?name=${name}
fi