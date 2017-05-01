#!/usr/bin/env bash

set -e
set -u

version="1.0.0-SNAPSHOT"

prefix="${1-/usr/local}"
distUrl="https://ci.weltraumschaf.de/job/maconha/lastSuccessfulBuild/artifact/target"
dist="maconha-${version}"
tarball="maconha-dsitribution-${version}.tar.bz2"

echo "Downloading distribution ..."
wget --no-check-certificate "${distUrl}/${tarball}"
tar xjvf "${tarball}"
rm -fv "${tarball}"

echo "Install into ${prefix} ..."
targetEtcDir="${prefix}/etc"
targetBinDir="${prefix}/bin"

if [[ ! -e "${targetEtcDir}" ]]; then
    mkdir -pv "${targetEtcDir}"
fi

if [[ ! -e "${targetBinDir}" ]]; then
    mkdir -pv "${targetBinDir}"
fi

cp -rv "${dist}/etc/"* "${targetEtcDir}"
cp -rv "${dist}/bin/"* "${targetBinDir}"
rm -rv "${dist}"

echo "Done :-)"
