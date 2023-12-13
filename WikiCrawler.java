// 435 Extra credit mrohrer gdf73278

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
public class WikiCrawler {

    String seedUrl;
    String[] keywords;
    int max;
    String fileName;

    public WikiCrawler(String seedUrl, String[] keywords, int max, String fileName) {

        this.seedUrl = seedUrl;
        this.keywords = keywords;
        this.max = max;
        this.fileName = fileName;

    }

    public void crawl() throws IOException {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(seedUrl);

        while (!queue.isEmpty() && visited.size() < max) {
            String url = queue.poll();
            visited.add(url);

            Document doc = (Document) Jsoup.connect(url).get();
            Elements links = doc.select("p a[href]");

            for (Element link : links) {
                String absUrl = link.attr("abs:href");

                if (!visited.contains(absUrl) && absUrl.startsWith("https://en.wikipedia.org")) {
                    queue.add(absUrl);
                }
            }
        }

        writeGraphToFile(visited);
    }

    private void writeGraphToFile(Set<String> visited) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("graph.txt"));

        writer.write(visited.size() + "\n");

        for (String url : visited) {
            writer.write(url + "\n");
        }

        writer.close();
    }

    public static void main(String[] args) throws IOException {
        WikiCrawler crawler = new WikiCrawler("https://en.wikipedia.org/wiki/Main_Page", 100);[^1^][1]
        crawler.crawl();
    }

    public boolean isInRobots(String endpoint) throws IOException {
        URL url = new URL("https://en.wikipedia.org/robots.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Disallow:")) {
                String disallowedPage = line.substring("Disallow:".length()).trim();
                if (endpoint.startsWith(disallowedPage)) {
                    reader.close();
                    return true;
                }
            }
        }
        reader.close();
        return false;
    }

}
