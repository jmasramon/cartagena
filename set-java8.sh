#!/bin/bash
# Script to ensure Java 8 is used for this project

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre
export PATH=$JAVA_HOME/bin:$PATH

echo "Java version set for this project:"
java -version

# Run the command passed as arguments
if [ $# -gt 0 ]; then
    exec "$@"
fi
