import java.util.ArrayList;

public class	Solver {

	private static ArrayList<Path>	paths;
	private static int				nbPaths;
	private static int				nbSteps = Integer.MAX_VALUE;
	private static int[][]			pathMatrix;

	public static void	solve(Graph g, int start, int end, int nbAnts) {
		g = g.splitNodes();
		start = (start << 1) + 1;
		end = (end << 1);
		g.unlinkBounds(start, end);

		paths = new ArrayList<Path>();
		pathMatrix = new int[g.getNbNodes()][g.getNbNodes() 	];
		findPaths(g, start, end, nbAnts);

		for (Path p : paths) {
			int	curr = p.getStart();
			System.out.println("\npath length: " + p.getLength());
			while (curr != end) {
				System.out.println("> " + g.nodeAt(curr).getName());
				curr = g.nodeAt(curr).getNext();
			}
		}
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
		int		current = start;
		int		next;

		while (current != end) {
			next = g.nodeAt(current).getNext();
			m[current][next] = m[next][current] ^ 1;
			m[next][current] = 0;
			current = next;
		}
		return (new Path(g.nodeAt(start).getNext()));
	}

	public static void	updatePaths(Graph g, int[][] m, int start, int end) {
		for (Path p : paths) {
			int		len = 0;
			int		currId = p.getStart();
			Node	currNode = g.nodeAt(currId);

			while (currId != end) {
				for (int n : currNode.getNeighbors()) {
					if (m[currId][n] == 1) {
						currNode.setNext(n);
						break ;
					}
				}
				currId = currNode.getNext();
				currNode = g.nodeAt(currId);
				++len;
			}
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

}
