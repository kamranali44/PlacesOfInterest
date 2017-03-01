package kamranali44.com.placesofinterest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    ListView lvResults = null;
    // the google key

    // ============== YOU SHOULD MAKE NEW KEYS ====================//
    String GOOGLE_KEY = "AIzaSyDe73ubloUXceYt5TLwkq9S1EgFWgynvKU";

    // we will need to take the latitude and the logntitude from a certain point
    // this is the center of New York
    final String latitude = "40.7463956";
    final String longtitude = "-73.9852992";

    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        lvResults = (ListView) findViewById(R.id.resultListView);
        this.GOOGLE_KEY = getResources().getString(R.string.GOOGLE_PLACES_API_KEY);

        // start the AsyncTask that makes the call for the venus search.
        new GooglePlacesAsyncTask().execute();
    }


    public class GooglePlacesAsyncTask extends AsyncTask<View, Void, String> {
        String temp;
        ArrayList<GooglePlace> venuesList;
        String GOOGLE_KEY = "AIzaSyDe73ubloUXceYt5TLwkq9S1EgFWgynvKU";
        final String latitude = "40.7463956";
        final String longtitude = "-73.9852992";




        @Override
        protected String doInBackground(View... urls) {
            this.GOOGLE_KEY = getResources().getString(R.string.GOOGLE_PLACES_API_KEY);
            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longtitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);

            //print the call in the console
            System.out.println("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longtitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right

                // parse Google places search result
                venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);

                List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
                    listTitle.add(i, venuesList.get(i).getName() + "\nOpen Now: " + venuesList.get(i).getOpenNow() + "\n(" + venuesList.get(i).getCategory() + ")");
                }

                // set the results to the list
                // and show them in the xml
                final ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(
                        ResultActivity.this,
                        android.R.layout.simple_expandable_list_item_1,
                        listTitle);
                lvResults.setAdapter(myAdapter);


//            final ArrayAdapter<String> itemsAdapter =
//                    new ArrayAdapter<String>(context, R.layout.lv_adapter, R.id.resultListView, listTitle);


                // lvResults.setAdapter(itemsAdapter);
            }
        }


        public String makeCall(String url) {

            // string buffers the url
            StringBuffer buffer_string = new StringBuffer(url);
            String replyString = "";

            // instanciate an HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // instanciate an HttpGet
            HttpGet httpget = new HttpGet(buffer_string.toString());

            try {
                // get the responce of the httpclient execution of the url
                HttpResponse response = httpclient.execute(httpget);
                InputStream is = response.getEntity().getContent();

                // buffer input stream the result
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(20);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                // the result as a string is ready for parsing
                replyString = new String(baf.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(replyString);

            // trim the whitespaces
            return replyString.trim();
        }

        private ArrayList<GooglePlace> parseGoogleParse(final String response) {

            ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
            try {

                // make an jsonObject in order to parse the response
                JSONObject jsonObject = new JSONObject(response);

                // make an jsonObject in order to parse the response
                if (jsonObject.has("results")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        GooglePlace poi = new GooglePlace();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).optString("name"));
                            poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                            if (jsonArray.getJSONObject(i).has("opening_hours")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                        poi.setOpenNow("YES");
                                    } else {
                                        poi.setOpenNow("NO");
                                    }
                                }
                            } else {
                                poi.setOpenNow("Not Known");
                            }
                            if (jsonArray.getJSONObject(i).has("types")) {
                                JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                                for (int j = 0; j < typesArray.length(); j++) {
                                    poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                                }
                            }
                        }
                        temp.add(poi);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<GooglePlace>();
            }
            return temp;

        }

    }

}
