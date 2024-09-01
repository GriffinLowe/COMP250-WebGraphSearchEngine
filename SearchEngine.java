import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception {
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 *
	 * 	This method will fit in about 30-50 lines (or less)
	 */

	public void crawlAndIndex(String url) throws Exception {

		if(internet == null || internet.getVisited(url))
			return;

		if(!internet.addVertex(url))
			return;

		internet.setVisited(url, true);

		// Adding words to the word index:
		for (String word : parser.getContent(url)) {

			String lower = word.toLowerCase();

			if (!wordIndex.containsKey(lower)) {
				ArrayList<String> new_list = new ArrayList<String>();
				new_list.add(url);
				wordIndex.put(lower, new_list);
			}
			else if(!wordIndex.get(lower).contains(url))
				wordIndex.get(lower).add(url);
		}

		// Updating the web graph and recursive call:
		for (String link : parser.getLinks(url)) {
			crawlAndIndex(link);
			internet.addEdge(url, link);
		}
	}

	/*
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex().
	 * To implement this method, refer to the algorithm described in the
	 * assignment pdf.
	 *
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {

		ArrayList<String> vertex_list = internet.getVertices();
		for (String vertex : vertex_list)
			internet.setPageRank(vertex, 1.0);

		boolean satisfied = false;

		while (!satisfied) {

			satisfied = true;

			ArrayList<Double> new_ranks = computeRanks(vertex_list);
			for (int i = 0; i < vertex_list.size(); i++) {
				if (Math.abs(internet.getPageRank(vertex_list.get(i)) - new_ranks.get(i)) >= epsilon)
					satisfied = false;
				internet.setPageRank(vertex_list.get(i), new_ranks.get(i));
			}
		}
	}


	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls.
	 * Note that the double in the output list is matched to the url in the input list using
	 * their position in the list.
	 *
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {

		ArrayList<Double> new_ranks = new ArrayList<Double>();

		for (String url : vertices) {

			double rank = 0.5;
			ArrayList<String> neighbors = internet.getEdgesInto(url);

			for (String v : neighbors)
				rank += 0.5 * (internet.getPageRank(v) / internet.getOutDegree(v));

			new_ranks.add(rank);
		}

		return new_ranks;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {

		String lower = query.toLowerCase();
		HashMap<String, Double> ranks = new HashMap<>();

		if(!wordIndex.containsKey(lower))
			return new ArrayList<String>();

		for (String url : wordIndex.get(lower)) {
			ranks.put(url, internet.getPageRank(url));
		}
		return Sorting.fastSort(ranks);
	}
}
