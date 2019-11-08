import java.util.ArrayList;

public class Node {

	private String				name;
	private int					id;
	private int					next;
	private ArrayList<Integer>	neighbors;
	private int					status;

	public static final int		VISITED = 1;
	public static final int		QUEUED = 2;
	public static final int		CROSSROAD = 4;
	public static final int		USED = 8;

	public Node(String nodeName, int nodeId) {
		name = nodeName;
		id = nodeId;
		next = -1;
		neighbors = new ArrayList<Integer>();
	}

	public void	addNeighbor(int n) {
		neighbors.add(n);
	}

	public ArrayList<Integer>	getNeighbors() {
		return (neighbors);
	}

	public void	setNext(int n) {
		next = n;
	}

	public int	getNext() {
		return (next);
	}

	public String getName() {
		return (name);
	}

	public int getId() {
		return (id);
	}

	public void	addStatus(int st) {
		status |= st;
	}

	public void	removeStatus(int st) {
		status &= ~st;
	}

	public boolean	getStatus(int st) {
		return ((status & st) != 0);
	}
}
