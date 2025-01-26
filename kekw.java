import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class WebCrawl {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java WebCrawl <starting URL> <num_hops>");
            return;
        }

        String startUrl = args[0];
        int numHops;

        try {
            numHops = Integer.parseInt(args[1]);
            if (numHops <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("num_hops must be a positive integer.");
            return;
        }

        Set<String> visitedUrls = new HashSet<>();
        crawl(startUrl, numHops, visitedUrls);
    }

    private static void crawl(String startUrl, int numHops, Set<String> visitedUrls) {
        String currentUrl = startUrl;

        for (int i = 0; i < numHops; i++) {
            if (visitedUrls.contains(currentUrl)) {
                System.out.println("URL already visited: " + currentUrl);
                return;
            }

            visitedUrls.add(currentUrl);
            System.out.println("Visiting: " + currentUrl);

            String htmlContent = fetchHtml(currentUrl);
            if (htmlContent == null) {
                System.out.println("Failed to fetch content from: " + currentUrl);
                return;
            }

            String nextUrl = extractNextUrl(htmlContent, visitedUrls);
            if (nextUrl == null) {
                System.out.println("No further links found.");
                return;
            }

            currentUrl = nextUrl;
        }
    }

    private static String fetchHtml(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode >= 300) {
                System.out.println("HTTP Error: " + responseCode + " for URL: " + urlString);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            return content.toString();
        } catch (IOException e) {
            System.out.println("IOException for URL: " + urlString + " - " + e.getMessage());
            return null;
        }
    }

    private static String extractNextUrl(String htmlContent, Set<String> visitedUrls) {
        Pattern pattern = Pattern.compile("<a\\s+href\\s*=\\s*\"(http[^\"]+)\"");
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            String url = matcher.group(1);
            if (!visitedUrls.contains(url)) {
                return url;
            }
        }

        return null;
    }
}
