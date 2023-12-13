import org.jsoup.Jsoup;

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

    public void crawl() {

    }

}
