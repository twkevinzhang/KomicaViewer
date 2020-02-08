package self.nesl.komicaviewer.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * JSONParser class to parse JSON
 *
 * @author Antarix
 *
 */
public class JSONParser {

    // Method type
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int DELETE = 4;

    /**
     * Get response string
     *
     * @param url
     * @param method
     * @return
     */

    public String getResponseString(String url, int method,
                                     String basicAuth) {
        return makeServiceCall(url, method,  basicAuth);
    }

    public JSONObject getJSONFromUrl(String url, int method,
                                      String basicAuth) {

        JSONObject json = null;
        // try parse the string to a JSON object
        try {
            String jsonString = makeServiceCall(url, method,  basicAuth);
            if (jsonString != null) {
                json = new JSONObject(jsonString);
            }
            return json;
        } catch (JSONException e) {
            System.out.println( "JSON Parser :Error parsing data " + e.toString());
            return json;
        }

    }

    /**
     * Get JSONArray
     *
     * @param url
     * @param method
     * @return
     */

    /**
     * Get JSONArray
     *
     * @param url
     * @param method
     * @return
     */
    public JSONArray getJSONArrayFromUrl(String url, int method,
                                          String basicAuth) {

        JSONArray jsonArray = null;
        // try parse the string to a JSON object
        try {
            String jsonString = makeServiceCall(url, method,  basicAuth);
            if (jsonString != null) {
                System.out.println("JsonString:"+ jsonString);
                jsonArray = new JSONArray(jsonString);
            }
            return jsonArray;
        } catch (JSONException e) {
            System.out.println("JSON Parser: Error parsing data " + e.toString());
            return jsonArray;
        }

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    private String makeServiceCall(String address, int method, String basicAuth) {

        String result = null;


        // http client

        URL url = null;

        boolean isSecureCall = address.startsWith("https");

        HttpURLConnection urlConnection = null;

        // String userCredentials = "username:password";
        // String basicAuth = "Basic " + new String(new
        // Base64().encode(userCredentials.getBytes()));

        OutputStream out;
        InputStream in;
        try {

            url = new URL(address);

            if (isSecureCall){
                urlConnection = (HttpsURLConnection)url.openConnection();
            }else{
                urlConnection = (HttpURLConnection) url.openConnection();
            }
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setInstanceFollowRedirects(true);


            // Checking http request method type
            if (method == POST) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
            } else if (method == GET) {
                urlConnection.setRequestMethod("GET");
            } else if (method == PUT) {
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
            } else if (method == DELETE) {
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoOutput(true);
            }


            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            //urlConnection.setRequestProperty( "Content-Type", "application/form-data");
            //urlConnection.setRequestProperty( "Content-Type", "x-www-form-urlencoded");

            urlConnection.setRequestProperty( "charset", "utf-8");
            urlConnection.setRequestProperty("Accept","application/json");

            if (basicAuth != null && basicAuth.length() > 0) {
                urlConnection.setRequestProperty("Authorization", basicAuth);
            }


            urlConnection.connect();

            in = new BufferedInputStream(urlConnection.getInputStream());
            result = inputStreamToString(in);
            System.out.println("ServerResponse:"+ result);
        } catch (Exception e) {
            System.out.println("MakeServiceCall Error : " + e.toString());
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }

        return result;
    }

    private static String inputStreamToString(InputStream in) {
        String result = "";
        if (in == null) {
            return result;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            result = out.toString();
            reader.close();

            return result;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("InputStream Error : " + e.toString());
            return result;
        }

    }
}