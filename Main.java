// 435 Extra credit mrohrer gdf73278

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] topics = {"tennis", "grand slam"};
        WikiCrawler w = new WikiCrawler("/wiki/Tennis", topics, 100, "WikiTennisGraph.txt");
        try {
            w.crawl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
