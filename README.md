# lemin-java
Java version of 42 C project lem-in

| [subject in english](https://github.com/BNNJ/lemin-java/blob/master/subject/lem-in.en.pdf) | [subject in french](https://github.com/BNNJ/lemin-java/blob/master/subject/lem-in.fr.pdf) |

Lemin is a path finding program for ants !\
It finds the best combination of paths in order to send ants through a maze in the minimum number of moves.\
Only one ant is allowed in each room at any given time.\
The maze is read from the standard input.

***Notes***:\
This was coded with 4 spaces tabs identation, which messes up github formatting.\
This is my first java program. I chose this project because I had been wanting to revisit it for a while with a different algorithm.\
Appart from implementation, this version also has a few minor differences with the one submited to 42:\
The parser is less restrictive, as invalid lines don't raise an error and stop the processing. A simple warning is issued instead.\
Less options, no visualizer. Use the C version if you want a graphical representation of what is going on.\
Although cleaner, this version is also much slower. Whether this is due to the language or the algorithm, i'm not quite sure. I'll answer that after implementing this solution in C or C++.

## Usage
Compile with:
```console
foo@bar:~$ javac Lemin.java
```
Run with:
```console
foo@bar:~$ java Lemin [-h/p/c] [--ants X]
```
Examples: 
```console
foo@bar:~$ java Lemin  --ants 23 < "mapfile"
foo@bar:~$ cat "mapfile" | java Lemin -pc
```

| Option | Effect |
|--------|--------|
|-h --help| Display help|
|-p --print| Echo the input to the standard input|
|-c --color| Colorize ants|
|--ants *int*| Manually set number of ants to *int*, if *int* is a valid integer|

## How does it work ?

The main algorithm is based on Edmonds-Karp algorithm, which is used to find the maximum flow in a network (represented by a graph), in our case the maze the ants must go though.\
Here's the basics of it:
1. run a breadth first search on the graph to find the shortest path from the source node to the sink. If a path was found, keep going. Otherwise, there is no augmenting path left, so we stop.
2. update the edges in this way:\
&emsp; for each pair of consecutive nodes u and v of the path found,\
&emsp; decrement the capacity of the edge u-v by the flow of that path (1 here),\
&emsp; increment the capacity of the edge v-u.\
&emsp; This opens edges to be used in the opposite direction.
3. repeat.

After an augmenting path has been found, a matrix storing which edges are used is updated, and used to update every path.

The problem in our case is that there cannot be more than one ant in a room: rooms have a capacity of 1, thus we need to find vertex-disjoint paths. Edmonds-karp algorithm is typically used to find edge-disjoint paths, in networks where edges have capacity, not rooms.\
To fix that, we transform the graph. Each node is split into an input node Vin and an output node Vout. Two edges are added between those two nodes which simulates node capacity, Vin to Vout has capacity 1, and Vout to Vin has capacity 0.\
All the edges of the original node are duplicated and attributed to Vin and Vout:\
for Vout outgoing edges have capacity 1, and incoming edges have capacity 0. The opposite is done for Vin.\
Now, the problem has been reduced to finding edge dispoint paths, which Edmonds-Karp does.
