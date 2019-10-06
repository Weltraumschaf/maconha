PREFIX=/usr/local

all: build

clean:
	@echo "Cleaning Maconha Search ..."
	@cd cli-indexer; lein clean
	@cd web-search; lein clean

build:
	@echo "Building Maconha Search ..."
	@cd cli-indexer; lein bin
	@cd web-search; lein bin

install: build
	@echo "Installing Maconha Search into $(PREFIX) ..."
	@mkdir -p $(PREFIX)/etc
	@cp etc/maconha-config-sample.yml $(PREFIX)/etc/maconha-config.yml
	@mkdir -p $(PREFIX)/bin
	@cp cli-indexer/target/cli-indexer $(PREFIX)/bin/cli-indexer
	@cp web-search/target/web-search $(PREFIX)/bin/web-search

help:
	@echo "Execute one of these targets:"
	@echo "make clean"
	@echo "make build (default target)"
	@echo "make [make PREFIX="/usr/local"] install"
