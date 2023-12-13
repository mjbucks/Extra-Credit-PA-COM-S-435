// 435 Extra credit mrohrer gdf73278

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
//import javax.lang.model.element.Element;
//import javax.lang.model.util.Elements;
//import javax.swing.text.Document;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import java.util.*;

public class WikiCrawler {

    private static final String BASE_URL = "https://en.wikipedia.org";
    private static final String ROBOTS_TXT_URL = "https://en.wikipedia.org/robots.txt";
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


    public void crawlMax(String seedUrl, int max, String fileName) throws IOException {
        final int MAX_REQUESTS = 10;
        int requests = 0;
        Set<String> visited = new HashSet<>();
        Queue<String> toVisit = new LinkedList<>();
        List<String[]> edges = new ArrayList<>();
        toVisit.add(seedUrl);

        while (!toVisit.isEmpty() && visited.size() < max) {
            String currentUrl = toVisit.poll();
            if (!visited.contains(currentUrl) && !isInRobots(currentUrl)) {
                visited.add(currentUrl);
                Document doc = Jsoup.connect(BASE_URL + currentUrl).get();
                Elements links = doc.select("a[href]");
                links.stream().map(link -> link.attr("href"))
                        .filter(link -> link.startsWith("/wiki/") && !link.contains("#") && !link.contains(":"))
                        .forEach(link -> {
                            try {
                                if (!isInRobots(link)) {
                                    toVisit.add(link);
                                    edges.add(new String[]{currentUrl, link});
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                // Politeness policy
                if (++requests % MAX_REQUESTS == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        writeGraphToFile(edges);
    }

    private void writeGraphToFile(List<String> visited) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("graph.txt"));
        writer.write(visited.size() + "\n");

        for (String url : visited) {
            writer.write(url + "\n");
        }

        writer.close();
    }

    public boolean isInRobots(String endpoint) throws IOException {
        URL url = new URL(ROBOTS_TXT_URL);
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