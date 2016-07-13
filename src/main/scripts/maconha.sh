#!/usr/bin/env sh

#
# This is the Unix wrapper script to run Maconha.
#

# JVM settings.
JVM_MIN_HEAP_SPACE="128m"
JVM_MAX_HEAP_SPACE="1024m"
JVM_OPTIONS="-Xms${JVM_MIN_HEAP_SPACE} -Xmx${JVM_MAX_HEAP_SPACE}"

PROGRAM="${0}"

while [ -h "${PROGRAM}" ]; do
  LS=`ls -ld "${PROGRAM}"`
  LINK=`expr "${LS}" : '.*-> \(.*\)$'`

  if expr "${LINK}" : '.*/.*' > /dev/null; then
    PROGRAM="${LINK}"
  else
    PROGRAM=`dirname "${PROGRAM}"`/"${LINK}"
  fi
done

PROGRAM_DIRECTORY=`dirname "${PROGRAM}"`
PROGRAM_DIRECTORY=`realpath "${PROGRAM_DIRECTORY}"`

JAR="${PROGRAM_DIRECTORY}/maconha.jar"

if [ ! -f "${JAR}" ] ; then
    PROJECT_DIR=`dirname "${PROGRAM_DIRECTORY}"`
    echo "ERROR: JAR file ${JAR} not pressent!"
    echo "Invoke 'mvn clean install' in the project base directory: ${PROJECT_DIR}."
    exit 1
fi

if [ "" != "${MACONHA_DEBUG}" ] ; then
    JVM_OPTIONS=" -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
fi

java ${JVM_OPTIONS} -jar "${JAR}" --spring.profiles.active=prod --bin=${PROGRAM_DIRECTORY}