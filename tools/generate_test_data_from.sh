#!/usr/bin/env bash

set -e
set -u

usage="Usage: ${0} <from> <to>";
from="${1-}"
to="${2-}"

if [[ "" == "${from}" ]]; then
    echo "No directory given to find test data from!"
    echo "${usage}"
    exit 1
fi

if [[ "" == "${to}" ]]; then
    echo "No directory given to write test data!"
    echo "${usage}"
    exit 2
fi

mkdir -pv "${to}"

find "${from}" -type f | sed -n "s|^${from}/||p" | while read file; do
    testFile="${to}/${file}"
    dir=$(dirname "${testFile}")

    if [[ ! -e "${dir}" ]]; then
        mkdir -pv "${dir}"
    fi

    touch "${testFile}"
    echo "${RANDOM}" > "${testFile}"
done
