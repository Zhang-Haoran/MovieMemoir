package com.example.moviememoir.ServerConnection;


import com.example.moviememoir.Model.Cinematable;
import com.example.moviememoir.Model.Credentialstable;
import com.example.moviememoir.Model.Memoirtable;
import com.example.moviememoir.Model.Usertable;
import com.example.moviememoir.ScreenController.Maps;
import com.example.moviememoir.ScreenController.Signin;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import static com.example.moviememoir.ScreenController.Report.chartDataMap;
import static com.example.moviememoir.ScreenController.Report.dataSets;
import static com.example.moviememoir.ScreenController.Report.initBarDataSet;
import static com.example.moviememoir.ScreenController.Report.xValue;
import static com.example.moviememoir.ScreenController.Report.yValue;

public class Server {
    public static final String BASE_URL = "http://192.168.1.103:8080/FIT5046Assignment1/webresources/";
    private static final String apiKey = "78324a2485620d39b0d6d391ac0573e6";
    private static final String googleapiKey = "AIzaSyBIeGgwS8d-kgu7j9ooH-AWk46QCmPTcVo";
    private static String getMethod(String methodPath, String parameterInput){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            url = new URL(BASE_URL + methodPath + parameterInput);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(httpURLConnection.getInputStream());
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
            return result;
    }

    private static void postMethod(String methodPath, String jsonToPost){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try{
            url = new URL(BASE_URL + methodPath);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setFixedLengthStreamingMode(jsonToPost.getBytes().length);
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            PrintWriter output = new PrintWriter(httpURLConnection.getOutputStream());
            output.print(jsonToPost);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpURLConnection.disconnect();//if not, can not go into onPost
        }

    }
//sign up get user id count
    public static String findCurrentUseridCount(){
        final String methodPath = "fit5046assignment1.usertable/count/";
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String result = "";
        //Making HTTP request
        try {
            url = new URL(Server.BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to text
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return result;
    }



//home get cinema
    public static String findCinema() {
    return getMethod("fit5046assignment1.cinematable/","");
    }

//movie memoir screen
    public static String findAllMovieMemoir() {
        return getMethod("fit5046assignment1.memoirtable/","");
    }
//sign in page method
    public static String findByUsernameAndPassword(String username, String password){
        return getMethod("fit5046assignment1.credentialstable/findByUsernameAndPassword/",username + "/" + password);
    }
//sign up page method
    public static String findAllUsername(){
        return getMethod("fit5046assignment1.credentialstable/findByAllUsername/","");
    }
//post data into user table
    public static void postUsertable(Usertable usertable){
        Gson gson = new Gson();
        String usertableJson = gson.toJson(usertable);
        postMethod("fit5046assignment1.usertable/",usertableJson);

    }
//post data into credentials table
    public static void postcredentialstable(Credentialstable credentialstable){
        Gson gson = new Gson();
        String credentialstableJson = gson.toJson(credentialstable);
        postMethod("fit5046assignment1.credentialstable/",credentialstableJson);

    }

    //add to movie memoir screen get cinema
    public static String findAllCinema(){
        return getMethod("fit5046assignment1.cinematable/","");
    }
    public static void  addCinema(Cinematable cinematable){
        Gson gson = new Gson();
        String cinematableJson = gson.toJson(cinematable);
        postMethod("fit5046assignment1.cinematable/",cinematableJson);
    }

    public static void postMemoirtable(Memoirtable memoirtable){
        Gson gson = new Gson();
        String memoirtableJson = gson.toJson(memoirtable);
        postMethod("fit5046assignment1.memoirtable/",memoirtableJson);
    }


//home page method
    public static String findHighRatingMovieNameByUserid(int userid){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            url = new URL(BASE_URL + "fit5046assignment1.memoirtable/findHighRatingMovieNameByUserid/" + userid);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(httpURLConnection.getInputStream());
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }


    //connect to the search movie api
    public static String searchMovie(String searchInput){
        searchInput = searchInput.trim().replace(" ","%20");

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            url = new URL("https://api.themoviedb.org/3/search/movie?api_key="+apiKey+"&language=en-US&query="+searchInput+"&page=1&include_adult=false");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(httpURLConnection.getInputStream());
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }
    //get the data for searched movie
    public static List<String> getSnippet(String result){
        List<String> snippet = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));
            for (int i = 0; i< jsonArray.length();i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                snippet.add(object.getString("title"));
                try {
                    snippet.add(object.getString("release_date"));
                } catch (JSONException e) {
                    snippet.add("not release");
                }
                snippet.add(object.getString("poster_path"));
                snippet.add(object.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return snippet;
    }
    //connect the movie detail api
    public static String getMovieDetail(String movieID){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            url = new URL("https://api.themoviedb.org/3/movie/"+movieID+"?api_key=78324a2485620d39b0d6d391ac0573e6&language=en-US");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(httpURLConnection.getInputStream());
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }
    //get movie detail information as required
    public static List<String> getDetails(String result){
        List<String> details = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("production_companies"));
            JSONObject object = (JSONObject) jsonArray.get(0);
            details.add(object.getString("origin_country"));
            JSONArray jsonArray2 = new JSONArray(jsonObject.getString("genres"));
            JSONObject objec2 = (JSONObject) jsonArray2.get(0);
            details.add(objec2.getString("name"));
            details.add(jsonObject.getString("release_date"));
            details.add(jsonObject.getString("overview"));
            details.add(jsonObject.getString("vote_average"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }
    //get credit data which store cast and director
    public static String getCredit(String movieID){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            url = new URL("https://api.themoviedb.org/3/movie/"+ movieID + "/credits?api_key=78324a2485620d39b0d6d391ac0573e6");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(httpURLConnection.getInputStream());
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }

    //get cast from the Movie DB api in credit
    public static List<String> getCast(String result){
        List<String> details = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("cast"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                details.add(object.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }
    //get direct name from the Movie DB api in credit
    public static List<String> getDirector(String result){
        List<String> details = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("crew"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                if (object.has("job")) {
                    if (object.getString("job").equals("Director")) {
                        details.add(object.getString("name"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }


    //connect to the google map to search location
    public static String findLocation(String searchInput){

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";
        try {
            url = new URL("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+searchInput+"&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key="+googleapiKey);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(httpURLConnection.getInputStream());
            while (inStream.hasNextLine()) {
                result += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }

    //connect to the google map to search location
    public static Void findCinemaByName(String cinemaName){
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            String result = "";
            try {
                url = new URL("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+cinemaName+"&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key="+googleapiKey);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");

                Scanner inStream = new Scanner(httpURLConnection.getInputStream());
                while (inStream.hasNextLine()) {
                    result += inStream.nextLine();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray candidates = new JSONArray(jsonObject.getString("candidates"));
                JSONObject object = (JSONObject) candidates.get(0);
                JSONObject geometry = new JSONObject(object.getString("geometry"));
                JSONObject location = new JSONObject(geometry.getString("location"));
                Maps.cinemaLat = location.getString("lat");
                Maps.cinemaLng = location.getString("lng");
            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }


    //report findByUseridANDStartingdateANDEnddate
    public static List<PieEntry> findByUseridANDStartingdateANDEnddate(String startingdate, String endingdate){
        List<PieEntry> pieEntryList = new ArrayList<>();
        String result = getMethod("fit5046assignment1.cinematable/findByUseridANDStartingdateANDEnddate/", Signin.usertable.getUserid() +"/"+startingdate+"/"+endingdate);
        try{
            JSONArray jsonArray = new JSONArray(result);
            for (int j = 0; j<jsonArray.length();j++){
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                PieEntry pieEntry = new PieEntry(Integer.parseInt(jsonObject.getString("totalnumber")),jsonObject.getString("suburb"));
                pieEntryList.add(pieEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pieEntryList;
    }

    //report findByUseridANDYear
    public static String findByUseridANDYear(String year){
        String result = getMethod("fit5046assignment1.memoirtable/findByUseridANDYear/",Signin.usertable.getUserid()+"/"+year);
        try{
            JSONArray jsonArray = new JSONArray(result);
            for (int j = 0; j< jsonArray.length();j++){
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                xValue.add(jsonObject.getString("month"));
                yValue.add(Integer.parseInt(jsonObject.getString("totalnumber")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chartDataMap.put("Month", yValue);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        int currentPosition = 0;
        for (LinkedHashMap.Entry<String, List<Integer>> entry : chartDataMap.entrySet()) {
            String name = entry.getKey();
            List<Integer> yValueList = entry.getValue();
            List<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < yValueList.size(); i++) {
                entries.add(new BarEntry(i, yValueList.get(i)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, name);
            initBarDataSet(barDataSet, colors.get(currentPosition));
            dataSets.add(barDataSet);

            currentPosition++;
        }
        return result;
    }

}
