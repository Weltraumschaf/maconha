#!/usr/bin/env sh

#
# Helper script to install all necessary things on fesh FreeBSD jail.
#

set -e
set -u

cd /usr/ports/ports-mgmt/portmaster
make install
make clean
rehash

portmaster ftp/curl editors/nano sysutils/tmux java/openjdk8-jre databases/mysql57-server
sysrc mysql_enable=yes
service mysql-server start
mysql_secure_installation
