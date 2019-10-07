# Maconha

## Build and Install

### Prerequisites

The project is written in [Clojure](https://clojure.org/) and requires

- [Leiningen](https://leiningen.org/),
- [GNU make](https://www.gnu.org/software/make/) and
- [Direnv](https://direnv.net/)

to build.

For execution [Java 8](https://adoptopenjdk.net/) is sufficient.

### Make the Project

Simply run the make targets (use `make help` for help):

```text
make clean && make install
```

The `install` target installs the binaries into `$PREFIX/bin` and configuration files into `$PREFIX/etc`. To use a different location set the `PREFIX` variable:

```text
make clean && make PREFIX=/usr/local install
```
