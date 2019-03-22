#! /bin/sh
mvn -Dmaven.test.skip=true package
stty_save=$(stty -g)
tput civis
stty -echo
stty cbreak
java -jar ./target/kerst-1.1-SNAPSHOT-jar-with-dependencies.jar "$@"
stty $stty_save
tput cnorm
# clear

