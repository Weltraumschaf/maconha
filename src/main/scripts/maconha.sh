#!/usr/bin/env sh

#
# This is the Unix wrapper script to run Maconha.
#

set -e

# JVM settings.
jvm_min_heap_space="128m"
jvm_max_heap_space="1024m"
jvm_options="-Xms${jvm_min_heap_space} -Xmx${jvm_max_heap_space}"

program="${0}"
while [ -h "${program}" ]; do
  ls=`ls -ld "${program}"`
  link=`expr "${ls}" : '.*-> \(.*\)$'`

  if expr "${link}" : '.*/.*' > /dev/null; then
    program="${link}"
  else
    program=`dirname "${program}"`/"${link}"
  fi
done
program=$(realpath "${program}")

java=java
if [ -n "${JAVA_HOME}" ]; then
    java="${JAVA_HOME}/bin/java"
fi

if [ -n "${MACONHA_DEBUG}" ] && [ "true" == "${MACONHA_DEBUG}" ] ; then
    jvm_options=" -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
fi

programDirectory=$(dirname "${program}")

exec "$java" ${jvm_options} -jar "${program}" \
    --bin.dir=${programDirectory} \
    "$@"
exit 1