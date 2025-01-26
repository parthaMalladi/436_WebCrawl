import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

public class WebCrawl {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java WebCrawl <starting URL> <num_hops>");
            return;
        }

        String url = args[0];
        int hops = Integer.parseInt(args[1]);
        Set<String> visited = new HashSet<>();

        String s = getHTML(url);
        extractURL(s);
    }

    private static void crawl(String link, int numHops, Set<String> mem) {
        String curr = link;

        for (int i = 0; i < numHops; i++) {
            if (mem.contains(curr)) {
                return;
            }

            String htmlBody = getHTML(curr);


        }
    }

    private static String getHTML(String link) {
        try {
            URL url = new URL(link);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }
                scanner.close();
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            System.out.println("The URL is malformed: " + e.getMessage());
        } catch (ProtocolException e) {
            System.out.println("Invalid HTTP method: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
        }

        return "";
    }

    // private static void extractURL(String htmlContent) {
    //     int index = 0;

    //     while ((index = htmlContent.indexOf("http", index)) != -1) {
    //         int counter = index;

    //         while (htmlContent.charAt(counter) != '"') {
    //             counter++;
    //         }

    //         String tempLink = htmlContent.substring(index, counter);
    //         System.out.println(tempLink);
    //         index = counter;
    //     }
    // }

    private static String extractURL(String htmlContent, Set<String> mem) {
        Pattern pattern = Pattern.compile("<a\\s+href\\s*=\\s*\"(http[^\"]+)\"");
        Matcher matcher = pattern.matcher(htmlContent);
        String ans = "";

        while (matcher.find()) {
            ans = matcher.group(1);
            if (mem.contains(ans)) {
                continue;
            } else {
                break;
            }
        }

        return ans;
    }
}
