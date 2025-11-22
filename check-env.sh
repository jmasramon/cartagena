#!/bin/bash
# Environment check script for Cartagena project

echo "=== Cartagena Project Environment Check ==="
echo

echo "Java Version:"
java -version
echo

echo "Java Home:"
echo $JAVA_HOME
echo

echo "Leiningen Version:"
lein version
echo

echo "Available Java versions:"
update-alternatives --list java
echo

echo "Project dependencies status:"
lein deps :tree | head -10
echo

echo "=== Environment Status: ✅ Ready ==="
