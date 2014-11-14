Weighted Support Vector Machine
===============================
Implementing a weighted support vector machine based on geometric concepts.
Honours project for Bch.Software Engineering @Monash - 2014 by Ahmed Shifaz

This software is licenced under GNU GPL.
To use the software, run the compiled jar file called WSVM-v1.jar

In Windows just double click the file.
If using command line use the following command:

java -jar WSVM-v1.jar

Similar command should work on linux/mac


There are exectuable two versions of the software:
  1.0 Only 2 dimensional features are enabled.
      WSVM-v1.jar

  2.0 Settings to generate multidimensional datasets are enabled in this version.
      This is a partial implementation of these features, 
      hence this version is unstable but it is provided for demonstration/proof of concept only
      SPLOM feature can be used in this version, to visualize high dimensional dataasets
      However hyperplane shown is not accurate in the rendering, 
      even though the solver finds the correct hyperplane in the backend
    WSVM-v2-Experimental-Multidimension.jar



==================
Building
==================
To build, import the souce code to an eclipse project.

Dependencies: Add these files to eclipse build path to build source code:
(Project Properties->Java Build Path->Libraries->Add Jar file)
This is a modified version of JFreechart.

lib/jfreechart-1.0.19.jar 
lib/jcommon-1.0.23.jar


Need Java 1.7 runtime.








For support with installation/run issues, email
ashi32@student.monash.edu
dotnet54@gmail.com