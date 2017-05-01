#!/usr/bin/env bash

set -e
set -u

usage="Usage: ${0} <dir>";
dir="${1-}"

if [[ "" == "${dir}" ]]; then
    echo "No directory to find extensions given!"
    echo "${usage}"
    exit 1
fi

find "${dir}" -type f -name '*.*' | \
    tr '[:upper:]' '[:lower:]' | \
    sed 's|.*\.||' | \
    sort -u
