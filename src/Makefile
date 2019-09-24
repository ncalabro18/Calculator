#call "make <build>" to build the program
#call "java builds.<build>" to run the program (first letter of the build capitalized)

#To use a bin folder for compiler output, remove the '#'
#This requires a lot of directory switching for testing/building on a commandline (also must manually make directory),
#but keeps the directories clean
FLAGS = # -d ../bin/


#Boot I just used to test code, feel free to edit the main method and do the same
Boot: 
	javac $(FLAGS) builds/Boot.java
	

#can solve integrals on the commandline
IntegralTool:
	javac $(FLAGS) builds/IntegralTool.java


#Simple GUI that handles input/output to parse equations
ValueCalculator:
	javac $(FLAGS) builds/ValueCalculator.java

