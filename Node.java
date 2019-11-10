import java.util.List;
import java.util.ArrayList;

/**
 * The Node class, used to build the Graph.
 */
public class Node {

	private String			name;
	/* The id corresponds to the node's index in the graph's Node array */
	private int				id;
	/* points to the next room in the path */
	private Node			next;
	private List<Integer>	neighbors;
	private int				status;

	public static final int		VISITED = 1;

	public					Node(String nodeName, int nodeId) {
		name = nodeName;
		id = nodeId;
		next = null;
		neighbors = new ArrayList<>();
	}

	public void				addNeighbor(int n) {
		neighbors.add(n);
	}

	public List<Integer>	getNeighbors() {
		return (neighbors);
	}

	public void				setNext(Node n) {
		next = n;
	}

	public Node				getNext() {
		return (next);
	}

	public String			getName() {
		return (name);
	}

	public int				getId() {
		return (id);
	}

	/*
	 * Note about the status:
	 * it is stored as a single int, where each bit is a boolean.
	 * It allows for more statuses to be added without adding accessors,
	 * by simply adding it to the list of flags defined in the private members.
	 *
	 * While overkill in this situation, the first version of this class
	 * initially defined 4 different statuses, to support a prvious
	 * implementation of the bfs and the pathfinding algorithms.
	 */
	public void				addStatus(int st) {
		status |= st;
	}

	public void				removeStatus(int st) {
		status &= ~st;
	}

	public boolean			getStatus(int st) {
		return ((status & st) != 0);
	}
}
