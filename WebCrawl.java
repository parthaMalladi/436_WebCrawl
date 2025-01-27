import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
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
        if (hops < 0) {
            hops = 0;
        }
        Set<String> visited = new HashSet<>();

        String s = getHTML(url);

        for (int i = 0; i <= hops; i++) {
            if (s.equals("") || s.substring(0,7).equals("invalid")) {
                System.out.println("Invalid Request or Malformed URL: " + url);

                if (!s.equals("")) {
                    System.out.println("Error Code: " + s.substring(7));
                }
                break;
            }

            if (visited.contains(url)) {
                System.out.print("Already Visited: " + url);
                break;
            }

            visited.add(url);
            System.out.println("Hop #" + i + " - Visiting: " + url);
            String newURL = extractURL(s, visited);
            
            if (!newURL.equals("")) {
                url = newURL;
                s = getHTML(url);
            } else {
                System.out.println("No URLs on the webpage");
                break;
            }
        }
    }

    private static String getHTML(String link) {
        try {
            URL url = new URL(link);
            
            URLConnection connection = url.openConnection();

            if (link.charAt(4) == 's') {
                connection = (HttpsURLConnection) connection;
            } else {
                connection = (HttpURLConnection) connection;
            }

            ((HttpURLConnection) connection).setRequestMethod("GET");
            int responseCode = ((HttpURLConnection) connection).getResponseCode();

            if (responseCode == 200) {
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }
                scanner.close();
                return sb.toString();
            } else {
                return "invalid" + responseCode;
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
                System.out.println("Extracted URL is: " + ans);
                break;
            }
        }

        return ans;
    }
}
