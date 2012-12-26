

all:
	echo "Nothing to do by default"
	echo "Try 'make help'"

compile:
	mvn clean package

data: compile

	#There are too many time to generate a datasets
	#First, using gen_non-uiform.R command, we generate a non-uniform dataset.
	#In commands, 1st arugment is file name to write, 2nd argument is number of dimension and 3rd argument is number of recodes.
	script/R/gen_non-uniform.R 2D-100-n.txt 2 10000000
	#Second, using gen_uniform.R command, we generate an uniform dataset. All arguments are same as above.
	script/R/gen_unoform.R 2D-100-u.txt 10000000

	#upload text files to HDFS		
	sudo -u hdfs hadoop fs -copyFromLocal *.txt /user/hdfs/input/

	#convert text files on HDFS to a binary files
# sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-100-n.txt /user/hdfs/input/2D-100-n.bin
# sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar hadoop.StringToBinaryConverter /user/hdfs/input/2D-100-n.txt /user/hdfs/input/2D-100-u.bin



run:	
	#copy a jar file to a machine in cluster
	
	sudo -u hdfs hadoop jar skyline-practice-0.0.1-SNAPSHOT.jar mapreduce.example.quadtree.QuadtreeDriverWithoutSample /user/hdfs/input/2D-100-n.bin /user/hdfs/output/temp 2

		
clean:
	# clear files on local file system 
	rm Q* 
	rm quadtree.data 
	
	# clear files on Hadoop distributed file system
	sudo -u hdfs hadoop fs -rm /user/hdfs/Q* 
	sudo -u hdfs hadoop fs -rm /user/hdfs/quadtree.dat
	sudo -u hdfs hadoop fs -rm -r /user/hdfs/output/* 

help: 
	egrep "^# target:" [Mm]akefile