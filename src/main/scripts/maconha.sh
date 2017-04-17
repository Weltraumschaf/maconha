#!/usr/bin/env sh

#
# This is the Unix wrapper script to run Maconha.
#

set -e

# JVM settings.
jvm_min_heap_space="32m"
jvm_max_heap_space="128m"
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
if [[ -n "${JAVA_HOME}" ]]; then
    java="${JAVA_HOME}/bin/java"
fi

if [[ -n "${MACONHA_DEBUG}" ]] && [[ "" != "${MACONHA_DEBUG}" ]] ; then
    jvm_options=" -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
fi

profiles="prod"
if [ "" != "${1}" ]; then
    profiles="${1}"
    echo "Using custom profile: ${profiles}."
fi

programDirectory=$(dirname "${program}")

exec "$java" ${jvm_options} -jar "${program}" --spring.profiles.active=${profiles} --bin=${programDirectory}
exit 1