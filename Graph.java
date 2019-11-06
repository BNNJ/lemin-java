import java.util.ArrayList;

public class Graph {

	public Graph() {
		nodes = new ArrayList<Node>();
		nbNodes = 0;
		start = -1;
		end = -1;
		adjacencyMatrix = null;
		nbAnts = 0;
	}

	public void	initMatrix() {
		adjacencyMatrix = new int[nbNodes][nbNodes];
	}

	public void	addNode(String nodeName, int status)
			throws InvalidLineException {
		if (nodeName.charAt(0) == 'L')
			throw new InvalidLineException("Node name cannot start with 'L");
		nodes.add(new Node(nodeName, nbNodes));
		if (status == 1)
			start = nbNodes;
		else if (status == 2)
			end = nbNodes;
		++nbNodes;
	}

	public void addEdge(String link)
			throws InvalidLineException {
		int	nodeA;
		int	nodeB;
		int	separator = link.indexOf('-');

		nodeA = findNode(link.substring(0, separator));
		nodeB = findNode(link.substring(separator + 1));
		if (nodeA < 0 || nodeB < 0)
			throw new InvalidLineException("Node not found");
		adjacencyMatrix[nodeA][nodeB] = 1;
		adjacencyMatrix[nodeB][nodeA] = 1;
	}

	public int	findNode(String node) {
		for (Node n : nodes)
			if (node.equals(n.getName()))
				return (n.getId());
		return (-1);
	}

	public void	display() {
		System.out.println(nbAnts);
		for (Node n : nodes)
			System.out.println(n.getName() + " " + n.getId());
		for (int i = 0; i < nbNodes; ++i) {
			for (int j = 0; j < nbNodes; ++j)
				System.out.print(adjacencyMatrix[j][i] + " ");
			System.out.print("\n");
		}
	}

	public void	setAnts(int n) {
		nbAnts = n;
	}

	public int	getAnts() {
		return (nbAnts);
	}

	public boolean	isValid() {
		if (nbAnts <= 0) {
			errorMessage = "Invalid number of ants: " + nbAnts;
			return (false);
		}
		else if (nbNodes <= 2) {
			errorMessage = "Not enough rooms: " + nbNodes;
			return (false);
		}
		return (true);
	}

	public String	getError() {
		return (errorMessage);
	}

	private int				nbNodes;
	private ArrayList<Node>	nodes;
	private int[][]			adjacencyMatrix;
	private int				start;
	private int				end;
	private int				nbAnts;
	private String			errorMessage;
}
