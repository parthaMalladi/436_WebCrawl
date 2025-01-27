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

        for (int i = 0; i < hops; i++) {
            if (s.equals("invalid")) {
                System.out.println("Invalid Request or Malformed URL");
                break;
            }

            visited.add(url);
            String newURL = extractURL(s, visited);
            
            if (!newURL.equals("")) {
                url = newURL;
                System.out.println(url);
                s = getHTML(url);
            }
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
            } else {
                return "invalid";
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (ProtocolException e) {
            System.out.println("Protocol Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Exception: " + e.getMessage());
        }

        return "";
    }

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
