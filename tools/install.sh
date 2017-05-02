#!/bin/sh

set -e
set -u

version="1.0.0-SNAPSHOT"

prefix="${MACONHA_PREFIX-/usr/local}"

distUrl="https://ci.weltraumschaf.de/job/maconha/lastSuccessfulBuild/artifact/target"
dist="maconha-${version}"
tarball="maconha-dsitribution-${version}.tar"

echo "Downloading distribution ..."
curl -kfsSL "${distUrl}/${tarball}" > "${tarball}"
tar xvf "${tarball}"
rm -fv "${tarball}"

echo "Install into ${prefix} ..."
targetEtcDir="${prefix}/etc"
targetBinDir="${prefix}/bin"

if [ ! -e "${targetEtcDir}" ]; then
    mkdir -pv "${targetEtcDir}"
fi

if [ ! -e "${targetBinDir}" ]; then
    mkdir -pv "${targetBinDir}"
fi

cp -rv "${dist}/etc/"* "${targetEtcDir}"
chmod a+x "${dist}/bin/"*
cp -rv "${dist}/bin/"* "${targetBinDir}"
rm -rv "${dist}"

echo "Done :-)"
