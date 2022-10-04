#!/bin/bash
echo "Removing Old Class Files..."
rm src/*.class
echo "Old Class Files Removed..."
echo "Compiling CarHeartBeat Source Files..."
javac src/*.java
echo "Compilation Complete..."
echo "Running Class Files..."
cd src/
java NavigationMonitor