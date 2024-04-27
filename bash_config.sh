#!/bin/bash

# author: Elliot Holmes

# run from the base 
# directory of the 
# project by typing
# ". bash_config.sh"
# do this every time  
# you open a new terminal

# after running once in 
# the base directory you
# can run from any dir
# by running "config_bash"
# unless you open a new 
# terminal in which case 
# you would need to run 
# ". bash_config.sh"
# from the base directory again

# if you "unalias -a" you 
# will need to run 
# ". $bash_config"
# in order to get the 
# "config_bash" alias back
# along with all other
# aliases

# "$base_dir" will be replaced
# by the absolute path
# to the home directory

# if you accidentally run
# this program from
# somewhere other than 
# the base directory of 
# your project you can
# open a new terminal
# and run this file in the base
# directory or you could
# run "unset _already_run"
# then run this file in 
# the base directory

# to access stdlib put the line
# import stdlib.*;
# to any files which need to 
# access classes in the stdlib
# you may also only want to import
# specific classes from stdlib
# this can be done like so
# ex: import stdlib.StdDraw;
# ex: import stdlib.StdIn;
# if you've successfully 
# configured bash by
# executing this program,
# javac and java will both
# know where to look for stdlib

# you may want to ignore 
# most of this file 
# and simply adjust the
# lines beneath the # aliases
# and # classpath environment var
# but you also may benefit
# from adjusting the behavior
# of this program to your
# liking

# success does not 
# necessarily mean that
# no errors occured, but
# rather that nothing
# stopped the execution
# of the file

unalias -a

# any code you only wish to execute
# the first time should be placed
# within this if statement
if [ "$_already_run" != 1 ]; then 
    export base_dir=`pwd`
    echo now defined: config_bash, "$"base_dir, "$"bash_config
fi
export _already_run=1
alias config_bash=". $base_dir/bash_config.sh"
export bash_config="$base_dir/bash_config.sh"


# classpath environment var
export CLASSPATH=".:$base_dir/libs"

# aliases
alias jdb="jdb -sourcepath $CLASSPATH"


echo success

# ------ADVANCED------

# To make your own library,
# create a directory within the
# the libs directory and name it
# whatever you want your library to be called
# then, whenever you make a java file 
# within that directory, add the line
# "package <INSERT_PACKAGE_NAME>";
# replace <INSERT_PACKAGE_NAME> with
# the name of your library, which once
# again should also be the name of the
# directory. You can access any libraries placed
# in the libs directory the same way you can
# access stdlib.

# You may also create subpackages to divide
# up your library into smaller peices.
# To do so, create a directory within
# your library's directory that has the same
# name that you wish to call the subsection of 
# your library. So, if you have a library called
# drawing, and you wanted it to have a subsection
# called math, you would create a directory called
# drawing in the libs directory, and within that 
# directory called drawing you would create 
# another directory called math. Then, if you wanted
# a class called Trigonometry, you would create
# a file called Trigonometry.java. You must add make the class
# public if you wish programs outside that package to access it.
# You also must add the line "package drawing.math.Trigonometry;"
# Then, assuming you successfully implement the Trigonometry 
# class, just how you want it, you can compile it with 
# "javac Trigonometry.java".
# Now, to access the file from anywhere else in your 
# your file structure, add the line below to any
# java file that needs access to the Trigonometry class.
# "import drawing.math.Trigonometry;"
# This template contains an example that follows these
# instructions. It may be beneficial to look at that.