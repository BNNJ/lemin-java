import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser {

/**
 * Simple parser for our input maze, read from stdin
 *
 * Except for comments, starting with '#', which can be anywhere,
 * the number of ants must come first, in the form of an positive integer,
 * Then the room declaration in the form [roomname x y],
 *  where x and y are the coordinates (ignored here),
 * Then the paths between rooms in the form [room1-room2].
 * ##start and ##end instructions are possible during room declaration,
 *  ignored otherwise.
 * @return an undirected graph representation of the maze.
 */
	public static Graph parse() {
		Graph			g = new Graph();
		Scanner			in = new Scanner(System.in);
		final Pattern	nodePattern = Pattern.compile("[\\w]+ [\\d]+ [\\d]+");
		final Pattern	edgePattern = Pattern.compile("[\\w]+-[\\w]+");
		final Pattern	commentPattern = Pattern.compile("##?.*");
		String			line;

		/* Ants parsing	*/
		while (in.hasNext()) {
			line = in.nextLine();
			if (!commentPattern.matcher(line).matches()) {
				if (Pattern.matches("[\\d]+", line)) {
					System.out.println(line);
					Lemin.setAnts(Integer.parseInt(line));
					break ;
				}
				else
					return (g);
			}
		}

		/* Nodes parsing	*/
		while (in.hasNext() && !in.hasNext(edgePattern)) {
			line = in.nextLine();
			System.out.println(line);
			if (nodePattern.matcher(line).matches()) {
				String	nodeName = line.substring(0, line.indexOf(' '));
				if (nodeName.charAt(0) != 'L' && g.indexOf(nodeName) < 0)
					g.addNode(new Node(nodeName, g.getNbNodes()));
				else
					System.err.println("Warning: Invalid node name: " + nodeName);
			}
			else if (commentPattern.matcher(line).matches()) {
				if (line.toLowerCase().equals("##start"))
					Lemin.setStart(g.getNbNodes());
				else if (line.toLowerCase().equals("##end"))
					Lemin.setEnd(g.getNbNodes());
			} else
				System.err.println("Warning: invalid line: " + line);
		}
		
		/* Edges parsing	*/
		while (in.hasNext()) {
			line = in.nextLine();
			System.out.println(line);
			if (edgePattern.matcher(line).matches()){
				int	separatorIndex = line.indexOf('-');
				int	from = g.indexOf(line.substring(0, separatorIndex));
				int	to = g.indexOf(line.substring(separatorIndex + 1));
				if (from < 0 || to < 0)
					System.err.println("Warning: Invalid edge: " + line);
				else {
					g.addEdge(from, to, 1);
					g.addEdge(to, from, 1);
				}
			}
			else if (!commentPattern.matcher(line).matches())
				System.err.println("Warning: invalid line: " + line);
		}
		in.close();
		return (g);
	}
}
