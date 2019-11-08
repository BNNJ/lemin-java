import java.util.ArrayList;

public class	Solver {

	private static int		nbSteps = Integer.MAX_VALUE;
	private static int		nbPaths;
	private static int[][]	savedState;

	public static void	solve(Graph g, int start, int end, int nbAnts) {
//		g.print();
		g = g.splitNodes();
		start = (start << 1) + 1;
		end = (end << 1);
		g.unlinkBounds(start, end);
//		g.print();


		System.out.println("> start: " + g.nodeAt(start).getName()
							+ " end: " + g.nodeAt(end).getName());

		ArrayList<Path> paths = findPaths(g, start, end, nbAnts);

		for (Path p : paths) {
			p.print(g);
			System.out.println("#############");
		}
	}

	public static ArrayList<Path>	findPaths(Graph g, int start, int end,
			int nbAnts) {
		ArrayList<Path>	paths = new ArrayList<Path>();

		for (int i = 0; i < nbAnts; ++i) {
			System.err.println("######## E-K LOOP START ######");
			int	p[] = g.bfs(start, end);
			if (p == null)
				break ;
			paths.add(new Path(g.makePath(p, start, end)));
			updatePaths(g, paths, end);
			int	tmpSteps = calculateSteps(paths, nbAnts);
			if (tmpSteps < 0)
				break ;
			else if (tmpSteps < nbSteps) {
				savedState = g.copyMatrix();
				nbSteps = tmpSteps;
				nbPaths = paths.size();
			System.err.println("######## E-K LOOP END ######");
			}
		}
//		g.print();
		g.replaceMatrix(savedState);
		while (paths.size() != nbPaths)
			paths.remove(paths.size() - 1);
		updatePaths(g, paths, end);
		return (paths);
	}

	public static void	updatePaths(Graph g, ArrayList<Path> paths, int end) {
		for (Path p : paths) {
			int		len = 1;
			int		currId = p.getStart();
			Node	currNode = g.nodeAt(currId);

			System.err.println("## UPDATE START ##");
			while (currId != end) {
				for (int n : currNode.getNeighbors()) {
					if (g.getEdge(currId, n) <= 0
							&& g.nodeAt(n).getStatus(Node.USED)) {
						currNode.setNext(n);
						break ;
					}
				}
				currId = currNode.getNext();
				currNode = g.nodeAt(currId);
				++len;
			}
			System.err.println("## UPDATE END ##");
			p.setLength(len / 2);
		}
	}

	public static int	calculateSteps(ArrayList<Path> paths, int nbAnts) {
		int	longest = 0;
		int	nbRooms = 0;

		for (Path p : paths){
			if (p.getLength() > longest)
				longest = p.getLength();
			nbRooms += p.getLength();
		}

		int	baseAnts = longest * paths.size() - nbRooms;
		int	extraAnts = nbAnts - baseAnts;
		return (extraAnts < 0 ? -1
			: longest - 1 + extraAnts / paths.size()
				+ (extraAnts % paths.size() != 0 ? 1 : 0));
	}

}
