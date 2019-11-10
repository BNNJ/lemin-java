# lemin-java
Java version of 42 C project lem-in

## Usage
Compile with\
\> javac Lemin.java\
Run with\
\> java Lemin [-h/p/c] [--ants X]\
Reads the maze from standard input. Usage exemple:\
java Lemin < "mapfile"\
cat "mapfile" | java Lemin


| Option | Effect |
|--------|--------|
|-h --help| Display help|
|-p --print| Echo the input to the standard input|
|-c --color| Colorize ants|
|--ants X| Manually assigns number of ants to X, if X is a valid integer|