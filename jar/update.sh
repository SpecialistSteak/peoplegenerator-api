#!/bin/bash
JAR_VERSION='1.1.3'
OLD_JAR_VERSION=$(grep -oP '(?<=peoplegeneratorapi-).*(?=\.jar)' peoplegeneratorapi-*.jar)
echo "Updating to version $JAR_VERSION from $OLD_JAR_VERSION"
cd ~/ || exit
cd MAIN_APP || exit
echo "Deleting old jar..."
sudo rm -f peoplegeneratorapi-"$OLD_JAR_VERSION".jar
echo "Downloading new jar..."
wget https://github.com/SpecialistSteak/peoplegenerator-api/raw/master/jar/peoplegeneratorapi-"$JAR_VERSION".jar
echo "Downloaded! Restarting nginx..."
sudo systemctl restart nginx
echo "Restarted! Restarting screen..."
killall screen
screen
echo "Starting run.sh..."
sudo sh run.sh