#!/usr/bin/env bash

set -e
set -u

if [ "" == "${1}" ]; then
  echo "No base dir given!"
  exit 1
fi

find "${1}" -type f ! -iname ".*" -print0 -exec sh -c 'echo "$RANDOM" > "{}"' -- {} \;
