#!/bin/bash

# Whenever running unittest for hadoop on cluster or local 
# dont need to type all script, just type this command script
echo "this script is suitable for this project"

#run manven script
mvn clean package 1> /dev/null

#run hadoop algorithm test
hadoop --config /etc/hadoop/conf.empty/ jar target/skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.test.MRBNLTest







