#!/usr/bin/env sh

# Helper to run the application local in dev mode.
# Requires exported variables from .envrc.

set -e
set -u

${PROJECT}/bin/maconha \
    --spring.config.location=${PROJECT}/etc/maconha.properties \
    --spring.profiles.active=dev