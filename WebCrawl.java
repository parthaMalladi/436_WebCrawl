import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class WebCrawl {
    public static void main(String[] args) {
        URL url = new URL("https://en.wikipedia.org/wiki/Northrop_B-2_Spirit");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.hasNext()) {
                sb.append(scanner.nextLine()).append("\n");
            }

            System.out.println(sb);
        }
    }
}