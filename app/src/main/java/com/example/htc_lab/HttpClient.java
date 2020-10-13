/*
package network;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String GET = "GET";
    private final JsonParser jsonParser;

    public HttpClient(){
        jsonParser = new JsonParser();
    }

    public Company readUserInfo(long userId) throws IOException, JSONException {
        String requestUrl = "http://www.mocky.io/v2/5ddcd3673400005800eae483";

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        InputStream in;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }

        Company user = jsonParser.getUser(response);

        return user;
    }
}
*/
