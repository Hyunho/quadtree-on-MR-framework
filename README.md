##developement

Download source files from github.

    $ sudo apt-get install git    
    $ git clone git://github.com/Hyunho/paper2.git

### install utilities for java

    $ sudo apt-get install openjdk-6-jdk
    $ sudo apt-get install maven2
    $ sudo apt-get insatll eclipse  

### Eclipse Project File Generation

We are using Maven so you can easily generate project files for our modules.

First you should make sure you have set up your workspace correctly with maven:

    $ mvn -Declipse.workspace="/home/user/workspace/" eclipse:configure-workspace
  
Note: "/home/user/workspace/" should be the path to your workspace.

You can check if it was successful in Eclipse via:

    Select Window > Preferences
    Select Java > Build Path > Classpath Variables

If you see the M2_REPO variable, it worked correctly.


Now run the following commands:
  
    $ mvn install
    $ mvn eclipse:eclipse

You can now import the projects into your eclipse workspace via:

    File -> Import -> Existing Projects into Workspace -> 
            Choose your workspace as the root directory and import a project.

### Complie
Type the following code

    $ mvn package 
Now, there is a copiled jar file in sub 'target' directory. And, copy a compiled jar file from local file system to server.

    $ scp target/skyline-practice-0.0.1-SNAPSHOT.jar admin01@192.168.59.34:/home/admin01

    


## Dataset generation 

In server in cluster, we generate a dataset, and upload to HDFS.
Copy script folder from source to server. 

    $ scp -r script admin01@192.168.59.34:/home/admin01

### In server machine on cluster 
####install R language

    $ sudo apt-get install r-base
    
Using gen_non-uiform.R command , we generate a non-uniform dataset.                                          
In commands, 1st arugment is file name to write, 2nd argument is number of dimension and 3rd argument is number of recodes.                                                                                                           

    $ script/R/gen_non-uniform.R 2D-100-n.txt 2 10000000
    
Using gen_uniform.R command, we generate an uniform dataset. All arguments are same as above.              

    $ script/R/gen_unoform.R 2D-100-u.txt 10000000

Upload dataset to HDFS                                                                                          

    $ sudo -u hdfs hadoop fs -copyFromLocal *.txt /user/hdfs/input/

After uploading, convert text files on HDFS to a binary files

    $ sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-100-n.txt /user/hdfs/input/2D-100-n.bin
    $ sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-100-n.txt /user/hdfs/input/2D-100-u.bin

##Running

Run a jar file. first argument is input file, second argument is output folder and third argument is number of dimenion of input file.

    $ sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/2D-100-n.bin /user/hdfs/output/temp 2

If you want to re-run a jar file. claer temporary files munaually.
To clear files, type following commands.

clear files on local file system                                                                                  

    $ rm Q*
    $ rm quadtree.data

clear files on Hadoop distributed file system   

    $ sudo -u hdfs hadoop fs -rm /user/hdfs/Q*
    $ sudo -u hdfs hadoop fs -rm /user/hdfs/quadtree.dat
    $ sudo -u hdfs hadoop fs -rm -r /user/hdfs/output/*


