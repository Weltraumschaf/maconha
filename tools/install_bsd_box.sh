#!/bin/sh

# Update ports.
portsnap fetch
portsnap extract

# Install portmaster.
cd /usr/ports/ports-mgmt/portmaster
make
make install
rehash
cd -

# Install stuff.
portmaster --no-confirm -yd \
    sysutils/tmux \
    editors/nano \
    databases/mysql57-server \
    java/openjdk8-jre \
    ftp/curl