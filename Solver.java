import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * The solver class
 *
 * Contains the methods used to find the paths and send the ants along them.
 *
 * I am not very satisfied with how procedural this part of the program is...
 */
public class	Solver {

	private static List<Path>	paths;
	private static int			nbPaths;
	private static int			nbSteps = Integer.MAX_VALUE;
	/* the path matrix is used to store the used edges */
	private static int[][]		pathMatrix;

	/**
	 * Solver's entry point.
	 *
	 * The graph is tramsformed to reduce the problem to a simpler one,
	 * Start and end must also be changed to reflect the graph modification.
	 *
	 * After finding the paths, they then must be transformed to
	 * match the original graph, before we can send ants through them
	 */
	public static void	solve(Graph g, int start, int end, int nbAnts) {
		g = g.splitNodes();
		start = (start << 1) + 1;
		end = (end << 1);
		g.unlinkBounds(start, end);

		paths = new ArrayList<>();
		pathMatrix = new int[g.getNbNodes()][g.getNbNodes() 	];
		findPaths(g, start, end, nbAnts);

		reducePaths(end);
		sendAnts(nbAnts, end);
	}

	/**
	 * The Edmonds-Karp based algorithm, the heart of the program
	 *
	 * At each iteration of its main loop, a bfs is run to find
	 * the next augmenting path.
	 * if one is found, the number of steps required to send the ants
	 * in the graph's current state is calculated.
	 * If this number of steps is lower than the previous best,
	 * the current state is saved, so it can be used to restore the
	 * best path combination after we run out of augmenting paths.
	 */
	public static void	findPaths(Graph g, int start, int end, int nbAnts) {
		int[][]	tmpMatrix = new int[g.getNbNodes()][g.getNbNodes()];
		int		tmpSteps;

		for (int i = 0; i < nbAnts; ++i) {
			int[]	p = g.bfs(start, end);
			if (p == null)
				break ;
			
			g.updateCapacity(p, start, end);

			paths.add(makePath(g, tmpMatrix, start, end));
			updatePaths(g, tmpMatrix, start, end);
			
			tmpSteps = calculateSteps(nbAnts);
			if (tmpSteps < 0)
				break ;
			else if (tmpSteps < nbSteps)
				saveState(tmpMatrix, tmpSteps, paths.size(), g.getNbNodes());
		}

		while (paths.size() != nbPaths)
			paths.remove(paths.size() - 1);
		updatePaths(g, pathMatrix, start, end);
	}

	/**
	 * Adds a path to the path matrix
	 *
	 * @return a new path to be added to the path list.
	 */
	public static Path	makePath(Graph g, int[][] m, int start, int end) {
		Node	currNode = g.nodeAt(start);
		int		currId = currNode.getId();
		int		nextId = currNode.getNext().getId();

		while (nextId != end) {
			nextId = currNode.getNext().getId();
			currId = currNode.getId();
			m[currId][nextId] = m[nextId][currId] ^ 1;
			m[nextId][currId] = 0;
			currNode = currNode.getNext();
		}
		return (new Path(g.nodeAt(start).getNext()));
	}

	/**
	 * Sets up the next pointer of each node in each path, and their length.
	 *
	 * The length is divided by two then incremented by one,
	 * to account for the doubled nodes.
	 */
	public static void	updatePaths(Graph g, int[][] m, int start, int end) {
		for (Path p : paths) {
			int		len = 0;
			Node	currNode = p.getStart();
			int		currId = currNode.getId();

			while (currId != end) {
				for (int n : currNode.getNeighbors()) {
					if (m[currId][n] == 1) {
						currNode.setNext(g.nodeAt(n));
						break ;
					}
				}
				currNode = currNode.getNext();
				currId = currNode.getId();
				++len;
			}
			currNode.setNext(g.nodeAt(end));
			p.setLength((len >> 1) + 1);
		}
	}

	public static void	saveState(int[][] m, int s, int p, int size) {
		nbSteps = s;
		nbPaths = p;

		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				pathMatrix[i][j] = m[i][j];
	}

	public static int	calculateSteps(int totalAnts) {
		int	longest = 0;
		int	nbRooms = 0;

		for (Path p : paths){
			if (p.getLength() > longest)
				longest = p.getLength();
			nbRooms += p.getLength();
		}

		int	baseAnts = longest * paths.size() - nbRooms;
		int	extraAnts = totalAnts - baseAnts;
		return (extraAnts < 0 ? -1
			: longest - 1 + extraAnts / paths.size()
				+ (extraAnts % paths.size() != 0 ? 1 : 0));
	}

	/**
	 * Reduces the paths to account for the doubled nodes.
	 * This is done by skipping every other node, which correspond to the
	 * output part of the input/ouput node pair.
	 */
	public static void	reducePaths(int end) {
		for (Path p : paths) {
			Node	current = p.getStart();
			while (current.getId() != end) {
				current.setNext(current.getNext().getNext());
				current = current.getNext();
			}
			current.setNext(null);
		}
	}

	/**
	 * Finally ! Send the ants !
	 *
	 * The ants are split into groups, one for every path.
	 * The size of those groups depend on the length of the paths they'll
	 * have to take.
	 * It is then a simple matter of iterating over each group, moving each
	 * to the next room. until they're arrived at the destination node.
	 * They're then removed from the list to make the processing of the
	 * other ones easier.
	 *
	 * This was first done in an unreadable recursive mess, which didn't
	 * require any data structure (like the Ant class), but this is much cleaner.
	 */
	public static void	sendAnts(int nbAnts, int end) {
		List<List<Ant>>	ants = new ArrayList<>(nbPaths);

		for (int p = 0; p < nbPaths; ++p)
			ants.add(new ArrayList<Ant>());

		for (int antId = 1, p = 0; antId <= nbAnts; ++p) {
			if (p >= nbPaths)
				p = 0;
			if (ants.get(p).size() + paths.get(p).getLength() <= nbSteps) {
				ants.get(p).add(new Ant(antId, paths.get(p).getStart(), end));
				++antId;
			}
		}

		for (int step = 0; step < nbSteps; ++step) {
			for (int p = 0; p < ants.size(); ++p) {
				if (ants.get(p).isEmpty())
					continue ;
				for (int i = 0; i < ants.get(p).size(); ++i) {
					Ant	ant = ants.get(p).get(i);
					if (ant.moveForward())
						break ;
				}
				if (ants.get(p).get(0).isArrived())
					ants.get(p).remove(0);
			}
			System.out.println();
		}
	}

}

class Ant {

	private	String	name;
	private	Node	room;
	private	Node	startRoom;
	private	int		end;

	public			Ant(int id, Node s, int e) {
		if (Lemin.getOption(Lemin.COLOR))
			name = "\u001B[9" + (id % 7 + 1) + "mL" + id + "\u001B[0m";
		else
			name = "L" + id;
		startRoom = s;
		room = s;
		end = e;
	}

	public void		setRoom(Node r) {
		room = r;
	}

	public boolean	isWalking() {
		return (room != startRoom);
	}

	public boolean	isArrived() {
		return (room == null);
	}

	public String	getName() {
		return (name);
	}

	public boolean	moveForward() {
		boolean	isFirstMove = room == startRoom;
		System.out.print(name + "-" + room.getName() + " ");
		room = room.getNext();
		return (isFirstMove);
	}

}
