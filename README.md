# lemin-java
Java version of 42 C project lem-in

Lemin is a path finding program for ants !\
It finds the best combination of paths in order to send ants through a maze in the minimum number of moves\
Only one ant is allowed in each room at any given time\
The maze is read from the standard input

notes:\
This is my first java program. I chose this project because I had been wanting to revisit it for a while with a different algorithm.\
Appart from implementation, this version also has a few minor differences with the one submited to 42:\
The parser is less restrictive, as invalid lines don't raise an error and stop the processing. A simple warning is issued.\
Less options, no visualizer. Use the C version if you want a graphical representation of what is going on.\
Although cleaner, this version is also much slower. Wether this is due to the language or the algorithm, i'm not quite sure.

## Usage
Compile with\
\> javac Lemin.java\
Run with\
\> java Lemin [-h/p/c] [--ants X]

exemple:\
java Lemin < "mapfile"\
cat "mapfile" | java Lemin


| Option | Effect |
|--------|--------|
|-h --help| Display help|
|-p --print| Echo the input to the standard input|
|-c --color| Colorize ants|
|--ants X| Manually assigns number of ants to X, if X is a valid integer|

## How does it work ?

The main algorithm is based on Edmonds-karp algorithm, which is ued to find the maximum flow in a network (represented by a graph), in our case the maze the ants must go though.\
Here's the basics of it:\
1: run a breadth first search on the graph to find the shortest path to the source node to the sink. if a path was found, keep going. Otherwise, there is no augmenting path left, so we stop\
2: update the edges in this way:\
&emsp; for each pair of consecutive nodes u and v of the path found,
&emsp; decrement the capacity of the edge u-v by the flow of that path (1 here),\
&emsp; increment the capacity of the edge v-u.\
&emsp; This opens edges to be used in the opposite direction.\
3: repeat.\
After an augmenting path has been found, a matrix storing which edges are used is updated, and used to update every path.

The probleme in our case is that it finds edge-disjoint paths. We need vertex (node) disjoint paths.\
To fix that, we process the graph. Each node is split into an input node Vin and an output node Vout. Two edges are added between those two nodes which simulates node capacity, Vin to Vout has capacity 1, and Vout to Vin has capacity 0.\
All the edges of the original node are duplicated and attributed to Vin and Vout:\
for Vout outgoing edges have capacity 1, and incoming edges have capacity 0. The opposite is done for Vin.\
Now, the problem has been reduced to finding edge dispoint paths, which Edmonds-Karp does.