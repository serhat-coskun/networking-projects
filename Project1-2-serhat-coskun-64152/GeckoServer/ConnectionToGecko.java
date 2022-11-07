/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

/**
 *
 * @author AbdulrazakZakieh
 */
public class RESTfulAPI {

    /**
     * @param args the command line arguments
     */ 
    public static void main(String[] args) throws MalformedURLException, IOException {
        URL url = new URL("https://api.coingecko.com/api/v3/coins/list");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        //Close the scanner
        scanner.close();  
        System.out.println(inline);
        
        JSONArray jsonObject = new JSONArray(inline);
        for (int i = 0; i < jsonObject.length(); i++)
        {
            String id = jsonObject.getJSONObject(i).getString("id");
            String name = jsonObject.getJSONObject(i).getString("name");
            System.out.println(id + "\t" + name);
        }
    }
    
}
