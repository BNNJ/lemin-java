import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser {

	public static Graph parse() {
		Graph			g = new Graph();
		Scanner			in = new Scanner(System.in);
		final Pattern	nodePattern = Pattern.compile("[\\w]+ [\\d]+ [\\d]+");
		final Pattern	edgePattern = Pattern.compile("[\\w]+-[\\w]+");
		final Pattern	commentPattern = Pattern.compile("##?[\\w\\s]*");
		String			line;
		int				status = 0;

		while (in.hasNext() && !in.hasNext(edgePattern)) {
			line = in.nextLine();
			if (nodePattern.matcher(line).matches())
				try {
					g.addNode(line.substring(0, line.indexOf(' ')), status);
				} catch (InvalidLineException e) {
					System.err.println("Lem-in: " + e.getError());
				}
			else if (commentPattern.matcher(line).matches()) {
				if (line.toLowerCase().equals("##start"))
					status = 1;
				else if (line.toLowerCase().equals("##end"))
					status = 2;
			} else
				System.err.println("Lem-in: invalid line: " + line);
		}
		
		g.initMatrix();
		while (in.hasNext()) {
			line = in.nextLine();
			if (edgePattern.matcher(line).matches())
				try {
					g.addEdge(line);
				} catch (InvalidLineException e) {
					System.err.println("Lem-in: " + e.getError());
				}
			else if (!commentPattern.matcher(line).matches())
				System.err.println("Lem-in: invalid line: " + line);
		}
		in.close();
		return (g);
	}

	private static void	parseNodes(Pattern nodePattern,
				Pattern edgePattern, Pattern commentPattern) {
		
	}

	private static void	parseEdges(Pattern edgePattern, Pattern commentPattern) {
		String	line;

	}
}
