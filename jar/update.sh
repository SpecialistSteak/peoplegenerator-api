#!/bin/bash
JAR_VERSION='1.2.0'
OLD_JAR_VERSION=$(grep -oP '(?<=peoplegeneratorapi-).*(?=\.jar)' peoplegeneratorapi-*.jar)
killall screen
echo "Updating to version $JAR_VERSION from $OLD_JAR_VERSION"
cd ~/ || exit
cd MAIN_APP || exit
echo "Deleting old jar..."
sudo rm peoplegeneratorapi-"$OLD_JAR_VERSION".jar
echo "Downloading new jar..."
wget https://github.com/SpecialistSteak/peoplegenerator-api/raw/master/jar/peoplegeneratorapi-"$JAR_VERSION".jar
echo "Downloaded! Restarting nginx..."
sudo systemctl restart nginx
echo "Restarted!"
screen
echo "Starting run.sh..."
sudo sh run.sh