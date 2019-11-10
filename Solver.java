import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class	Solver {

	private static List<Path>	paths;
	private static int			nbPaths;
	private static int			nbSteps = Integer.MAX_VALUE;
	private static int[][]		pathMatrix;

	public static void	solve(Graph g, int start, int end, int nbAnts) {
		g = g.splitNodes();
		start = (start << 1) + 1;
		end = (end << 1);
		g.unlinkBounds(start, end);
	//	g.print();

		paths = new ArrayList<>();
		pathMatrix = new int[g.getNbNodes()][g.getNbNodes() 	];
		findPaths(g, start, end, nbAnts);

		reducePaths(end);
		for (Path p : paths) {
			Node	curr = p.getStart();
			System.out.println("\npath length: " + p.getLength());
			while (curr.getId() != end) {
				System.out.println("> " + curr.getName());
				curr = curr.getNext();
			}
		}
		sendAnts(nbAnts, end);
	}

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
