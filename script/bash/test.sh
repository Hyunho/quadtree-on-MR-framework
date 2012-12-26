#! /bin/bash

clear() {
    # clear files on local file system
    rm Q*
    rm quadtree.data
    # clear files on Hadoop file system 
    sudo -u hdfs hadoop fs -rm /user/hdfs/Q*
    sudo -u hdfs hadoop fs -rm /user/hdfs/quadtree.dat
    sudo -u hdfs hadoop fs -rm -r /user/hdfs/output/*
}


init() {
    # download a jar file from host2
    scp hyunho@host2.dke.kut.ac.kr:workspace/quad-paper/target/skyline-practice-0.0.1-SNAPSHOT.jar .
}

search() {
    # execute range query
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample input/2D-10-n.bin output/2D-10-n-3-s 2
}

clear
$1