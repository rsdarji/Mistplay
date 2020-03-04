package com.ravi.mistplay;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Utility {


    public static String getSearchedGameJSONdata(String searchedText, Context context) throws IOException{
        JSONArray jsonArray = new JSONArray();
        String searchedGameJSONData=null;
        String gameJSONData = loadJSONFromAsset(context);

        try {
            if(gameJSONData != null) {
                JSONArray gameData = new JSONArray(gameJSONData);
                if (gameData.length() != 0) {

                    //System.out.println("game111: " + gameData.get(0));

                    JSONObject gameJSONObject;
                    if(!searchedText.equalsIgnoreCase("")) {
                        for (int i = 0; i < gameData.length(); i++) {
                            gameJSONObject = gameData.getJSONObject(i);
                            if (gameJSONObject.getString("subgenre").toLowerCase().startsWith(searchedText.toLowerCase())) {
                                jsonArray.put(gameJSONObject);
                            }
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(context, "Not available any Game data.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray.toString();
    }
    public static String loadJSONFromAsset(Context context) throws IOException {
        String json = null;
        try {
            InputStream is = context.getAssets().open("games.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }
}
