// 435 Extra credit mrohrer gdf73278

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] topics = new String[]{"tennis", "grand slam"};
        WikiCrawler w = new WikiCrawler("/wiki/Tennis", topics, 20, "WikiTennisGraph.txt");

        try {
            w.crawl();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}