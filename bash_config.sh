unalias -a

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