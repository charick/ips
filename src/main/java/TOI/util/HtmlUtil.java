package TOI.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-4-29
 * Time: 上午1:57
 * To change this template use File | Settings | File Templates.
 */


public class HtmlUtil {

    public static String getHtmlContent(String url0) throws IOException {
        String content;
        URL url;

            url = new URL(url0);
            HttpURLConnection httpConn = (HttpURLConnection) url
                    .openConnection();
            InputStreamReader input = new InputStreamReader(
                    httpConn.getInputStream(), "utf-8");
            BufferedReader bufReader = new BufferedReader(input);
            String line;
            StringBuilder contentBuf = new StringBuilder();
            while ((line = bufReader.readLine()) != null) contentBuf.append(line);

            content = contentBuf.toString();


        return content;
    }
}
