# Define variables
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
JAVA=$(JAVA_HOME)/bin/java
ARGFILE=/tmp/cp_249ymhoe9fumuhrof05l8u2dz.argfile
MAIN_CLASS=grupa5/grupa5.App

# Default target
.PHONY: run

run:
	/usr/bin/env $(JAVA) @$(ARGFILE) -m $(MAIN_CLASS)
