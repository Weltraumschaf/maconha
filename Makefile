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

install:
	@echo "Installing Maconhs Search into $(PREFIX) ..."

help:
	@echo "Execute one of these targets:"
	@echo " - clean"
	@echo " - build"
	@echo " - install"
