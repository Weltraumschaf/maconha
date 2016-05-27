#!/usr/bin/env bash

if [ ! -d "${2}" ] ; then
    mkdir -p "${2}"
fi

while IFS='' read -r line || [[ -n "${line}" ]]; do
    file="${2}/${line}"
    dir="$(dirname "${file}")"
    
    if [ ! -d "${dir}" ] ; then
        echo "Crete dir ${dir}"
        mkdir -p "${dir}"
    fi
    
    echo "Touching file: ${file}"
    touch "${file}"

done < "${1}"
