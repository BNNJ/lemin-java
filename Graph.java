import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representation of the maze
 * the nodes are stored as an ArrayList,
 * the edges as an adjacency matrix, which also serves as a flow capacity matrix
 * and a 'snapshot' of a certain state of the graph.
 */
public class Graph {

	private ArrayList<Node>	nodes;
	private	int[][]			edges;
	private int				nbNodes;

	public 			Graph() {
		nodes = new ArrayList<Node>();
		edges = null;
		nbNodes = 0;
	}

	/**
	 * Makes a new directed graph from an undirected one
	 *
	 * Each node is split into two nodes, one for input and one for output:
	 * The edges to its neighbors are attached to the output,
	 * and the edges from its neighbors point to the input.
	 * And edge is added from each input to its corresponding output.
	 */
	public Graph	splitNodes() {
		Graph g = new Graph();

		for (Node n : nodes) {
			g.addNode(new Node(n.getName() + "_in", g.getNbNodes()));
			g.addNode(new Node(n.getName() + "_out", g.getNbNodes()));
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

	public void		unlinkBounds(int start, int end) {
		for (int i = 0; i < nbNodes; ++i) {
			edges[start - 1][i] = 0;
			edges[i][start - 1] = 0;
			edges[end + 1][i] = 0;
			edges[i][end + 1] = 0;
		}
	}

	public int[]	bfs(int start, int end) {
		int[]				path = new int[nbNodes];
		LinkedList<Integer>	queue = new LinkedList<Integer>();
		int					currId = start;

		for (Node n : nodes)
			n.removeStatus(Node.VISITED);

		queue.addLast(start);
		while (!queue.isEmpty()) {
			currId = queue.poll();
			Node	currNode = nodes.get(currId);

//			if (currNode.getStatus(Node.VISITED))
//				continue ;
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
		System.out.println("### BFS END ##");
		return (currId == end ? path : null);
	}

	public int	makePath(int[] path, int start, int end) {
		int	current = end;

		while (true) {
			int	prev = path[current];
			--edges[prev][current];
			++edges[current][prev];
			System.out.println(">> " + nodes.get(current).getName()
							+ " <- " + nodes.get(prev).getName());
			nodes.get(current).addStatus(Node.USED);
			if (prev == start)
				return (current);
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

	public int[][]	copyMatrix() {
		int[][]	copy = new int[nbNodes][nbNodes];

		for (int i = 0; i < nbNodes; ++i)
			for (int j = 0; j < nbNodes; ++j)
				copy[i][j] = edges[i][j];
		return (copy);
	}

	public void		replaceMatrix(int[][] matrix) {
		for (int i = 0; i < nbNodes; ++i)
			for (int j = 0; j < nbNodes; ++j)
				edges[i][j] = matrix[i][j];
	}

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
