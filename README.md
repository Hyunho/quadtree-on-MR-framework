#developement

## install utilities for java

    $ sudo apt-get install maven2
    $ sudo apt-get insatll eclipse  

## Eclipse Project File Generation

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

    File -> Import -> Existing Projects into Workspace -> Choose your workspace as the root directory and import the hama-* projects.

## install R language

Type following code, 

    $ sudo apt-get install r-base


