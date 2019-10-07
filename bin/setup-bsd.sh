#!/bin/sh

#
# Install all necessary tools on a fresh BSD box.
#

set -eu

pkg install -y \
    tmux \
    nano \
    git \
    openjdk8-jre
