import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representation of the maze
 *
 * the nodes are stored as an ArrayList,
 * the edges as an adjacency matrix, which also serves as a flow capacity matrix
 */
public class Graph {

	private List<Node>	nodes;
	private	int[][]		edges;
	private int			nbNodes;

	public 			Graph() {
		nodes = new ArrayList<>();
		edges = null;
		nbNodes = 0;
	}

	/**
	 * Makes a new grah where each node has been split to simulate node capacity
	 *
	 * Each node V is split into two nodes Vin and Vout
	 * All of its edges are also duplicated:
	 * incoming edges point to Vin with capacity 1 and Vout with capacity 0
	 * outgoing edges point to Vin with capacity 0 and Vout with capacity 1
	 * An edge is added between Vin and Vout,
	 * with capacity 1 from Vin to Vout and 0 from Vout to Vin.
	 *
	 * @return the new version of the graph
	 */
	public Graph	splitNodes() {
		Graph g = new Graph();

		for (Node n : nodes) {
			g.addNode(new Node(n.getName(), g.getNbNodes()));
			g.addNode(new Node(n.getName(), g.getNbNodes()));
		}
		for (int i = 0; i < nbNodes; i++){
			int from = i << 1;
			g.addEdge(from, from + 1, 1);
			g.addEdge(from + 1, from, 0);
			for (int j = 0; j < nbNodes; ++j){
				if (edges[i][j] == 1) {
					int to = j << 1 ;
					g.addEdge(from + 1, to, 1);
					g.addEdge(to, from + 1, 0);
				}
			}
		}
		return (g);
	}

	/**
	 * Start in and End out are dissociated from the rest of the graph
	 * We don't want to completely remove them, as it would mess up the indexing
	 * of all the other nodes.
	 */
	public void		unlinkBounds(int start, int end) {
		for (int i = 0; i < nbNodes; ++i) {
			edges[start - 1][i] = 0;
			edges[i][start - 1] = 0;
			edges[end + 1][i] = 0;
			edges[i][end + 1] = 0;
		}
	}

	/**
	 * A Breadth First Search algorithm that uses the flow capacity to
	 * find the shortest path between two nodes.
	 *
	 * @return An array of ints, representing the path found
	 */
	public int[]	bfs(int start, int end) {
		int[]				path = new int[nbNodes];
		LinkedList<Integer>	queue = new LinkedList<>();
		int					currId = start;

		for (Node n : nodes)
			n.removeStatus(Node.VISITED);

		queue.addLast(start);
		while (!queue.isEmpty()) {
			currId = queue.poll();
			Node	currNode = nodes.get(currId);

			if (currId == end)
				break ;
			currNode.addStatus(Node.VISITED);
			for (int i : currNode.getNeighbors()) {
				if (edges[currId][i] > 0
						&& !nodes.get(i).getStatus(Node.VISITED)) {
					queue.addLast(i);
					path[i] = currId;
				}
			}
		}
		return (currId == end ? path : null);
	}

	/**
	 * Follows a path and sets each nodes' next, so the path can be retrieved
	 * later by following the next pointers.
	 * Edges capacity are updated along the way.
	 */
	public void 	updateCapacity(int[] path, int start, int end) {
		int	current = end;

		while (current != start) {
			int	prev = path[current];
			--edges[prev][current];
			++edges[current][prev];
			nodes.get(prev).setNext(nodes.get(current));
			current = prev;
		}
	}

	public void		addNode(Node n) {
		nodes.add(n);
		++nbNodes;
	}

	public Node		nodeAt(int index) {
		return (nodes.get(index));
	}

	/**
	 * Finds the index of a node using its name.
	 *
	 * @return the index of the node if it was found, -1 otherwise.
	 */
	public int		indexOf(String name) {
		for (Node n : nodes)
			if (n.getName().equals(name))
				return (n.getId());
		return (-1);
	}

	public int		getNbNodes() {
		return (nbNodes);
	}

	public void		addEdge(int from, int to, int capacity) {
		if (edges == null)
			edges = new int[nbNodes][nbNodes];
		edges[from][to] = capacity;
		nodes.get(from).addNeighbor(to);
	}

	public int		getEdge(int from, int to) {
		return (edges[from][to]);
	}

	/**
	 * Prints the graph, for debugging purposes.
	 */
	public void		print() {
		System.err.println("number of nodes: " + nbNodes);
		for (Node n : nodes) {
			System.err.println(n.getId() + ": " + n.getName());
			for (int i : n.getNeighbors())
				System.err.println("> " + nodes.get(i).getId()
					+ ": " + nodes.get(i).getName()
					+ " - " + edges[n.getId()][i]);
		}
		System.err.println();
		for (int i = 0; i < nbNodes; ++i)
			for (int j = 0; j < nbNodes; ++j)
				System.err.print(edges[i][j] + (j == nbNodes - 1 ? "\n" : " "));
		System.err.println();
	}
}
