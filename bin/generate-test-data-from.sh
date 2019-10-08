#!/usr/bin/env bash

#
# Script to generate test data from a givne directory.
#
# The script replicates the structure (files/direcotries) of the given source
# direcotry, but the files will only contain a random string as content. This
# is used to have a real direcotry structure to index without all the heavy data.
#

set -eu

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

find "${from}" -type f | sed -n "s|^${from}/||p" | while read -r file; do
    testFile="${to}/${file}"
    dir=$(dirname "${testFile}")

    if [[ ! -e "${dir}" ]]; then
        mkdir -pv "${dir}"
    fi

    echo -n "."
    echo "${RANDOM}" > "${testFile}"
done
