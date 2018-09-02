package de;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DusTest {

    @Test
    public void scanTest() throws IOException {
/*
        Document post = Jsoup.connect("https://www.dus.com/de-DE/api/flights/departures")
                .header("Accept", "application/json")
                .requestBody("FlightStartTime=2018-09-01T14%3A18%3A22&FlightEndTime=&FlightNumber=&Airport=&Offset=40&Language=de-DE")
                .ignoreContentType(true)
                .post();

        String body = post.body().toString();
        body = body.substring(7, body.length() - 8);

        Gson gson = new Gson();
        gson.fromJson(body, JsonObject.class);

        //JsonObject json = gson.fromJson(reader, JsonObject.class);
        System.out.println();

*/

/*        Gson gson = new Gson();

        Map<String,String> httpHeaders=new HashMap<>();
        httpHeaders.put("Accept", "application/json");
        httpHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");


        InputStream inputStream = urlToInputStream(new URL("https://www.dus.com/de-DE/api/flights/departures"), httpHeaders); //new URL("https://www.dus.com/de-DE/api/flights/departures").openStream().;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject json = gson.fromJson(reader, JsonObject.class);
        System.out.println(json);*/


    }

    private InputStream urlToInputStream(URL url, Map<String, String> args) {
        HttpURLConnection con = null;
        InputStream inputStream = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            if (args != null) {
                for (Map.Entry<String, String> e : args.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());
                }
            }
            con.connect();
            int responseCode = con.getResponseCode();
            /* By default the connection will follow redirects. The following
             * block is only entered if the implementation of HttpURLConnection
             * does not perform the redirect. The exact behavior depends to
             * the actual implementation (e.g. sun.net).
             * !!! Attention: This block allows the connection to
             * switch protocols (e.g. HTTP to HTTPS), which is <b>not</b>
             * default behavior. See: https://stackoverflow.com/questions/1884230
             * for more info!!!
             */
            if (responseCode < 400 && responseCode > 299) {
                String redirectUrl = con.getHeaderField("Location");
                try {
                    URL newUrl = new URL(redirectUrl);
                    return urlToInputStream(newUrl, args);
                } catch (MalformedURLException e) {
                    URL newUrl = new URL(url.getProtocol() + "://" + url.getHost() + redirectUrl);
                    return urlToInputStream(newUrl, args);
                }
            }
            /*!!!!!*/

            inputStream = con.getInputStream();
            return inputStream;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
