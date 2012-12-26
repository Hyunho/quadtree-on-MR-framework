#! /bin/bash

clear_file () { 
# clear files on local file system 
    rm Q* 
    rm quadtree.data 
# clear files on Hadoop file system
    sudo -u hdfs hadoop fs -rm /user/hdfs/Q* 
    sudo -u hdfs hadoop fs -rm /user/hdfs/quadtree.dat
    sudo -u hdfs hadoop fs -rm -r /user/hdfs/output/* 
}

upload() {
    sudo -u hdfs hadoop fs -copyFromLocal *.txt /user/hdfs/input/
}

#convert string files to binary files
# ex)8D-1-n.txt to 
convert() {
#    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-100-n.txt /user/hdfs/input/2D-100-n.bin
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-250-n.txt /user/hdfs/input/2D-250-n.bin
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-500-n.txt /user/hdfs/input/2D-500-n.bin

    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/3D-100-n.txt /user/hdfs/input/3D-100-n.bin
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/4D-100-n.txt /user/hdfs/input/4D-100-n.bin
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/5D-100-n.txt /user/hdfs/input/5D-100-n.bin
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/6D-100-n.txt /user/hdfs/input/6D-100-n.bin
}

execute1() {

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/2D-100-n.bin /user/hdfs/output/temp 2
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/2D-250-n.bin /user/hdfs/output/temp 2
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/2D-500-n.bin /user/hdfs/output/temp 2
    date
    echo 'end********************************'


    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/3D-100-n.bin /user/hdfs/output/temp 3
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/4D-100-n.bin /user/hdfs/output/temp 4
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/5D-100-n.bin /user/hdfs/output/temp 5
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/6D-100-n.bin /user/hdfs/output/temp 6
    date
    echo 'end********************************'
}

execute2() {

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/2D-100-n.bin /user/hdfs/output/temp 2
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/2D-250-n.bin /user/hdfs/output/temp 2
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/2D-500-n.bin /user/hdfs/output/temp 2
    date
    echo 'end********************************'


    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/3D-100-n.bin /user/hdfs/output/temp 3
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/4D-100-n.bin /user/hdfs/output/temp 4
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/5D-100-n.bin /user/hdfs/output/temp 5
    date
    echo 'end********************************'

    clear_file
    echo "start============================="
    date
    sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadTreeDriverWithSample /user/hdfs/input/6D-100-n.bin /user/hdfs/output/temp 6
    date
    echo 'end********************************'
}

upload
convert
execute1
execute2
