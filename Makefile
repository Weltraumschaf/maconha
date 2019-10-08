PREFIX=/usr/local

all: build

clean:
	@echo "Cleaning Maconha Search ..."
	@cd cli-indexer; lein clean
	@cd web-search; lein clean

clean-local: clean
	@echo "Cleaning the local development files ..."
	@rm $(PROJECT)/bin/maconha-indexer
	@rm $(PROJECT)/bin/maconha-search
	@rm $(PROJECT)/etc/maconha-config.yml

build:
	@echo "Building Maconha Search ..."
	@cd cli-indexer; lein bin
	@cd web-search; lein bin

install:
	@echo "Installing Maconha Search into $(PREFIX) ..."
	@mkdir -p $(PREFIX)/etc
	@cp etc/maconha-config-sample.yml $(PREFIX)/etc/maconha-config.yml
	@mkdir -p $(PREFIX)/bin
	@cp cli-indexer/target/maconha-indexer $(PREFIX)/bin/
	@cp web-search/target/maconha-search $(PREFIX)/bin/

help:
	@echo "Execute one of these targets:"
	@echo "  make clean"
	@echo "  make build (default target)"
	@echo "  make [make PREFIX="/usr/local"] install"
	@echo "Special development targets:"
	@echo "  make clean-local"
