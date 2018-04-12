package farhadarts.birdwatching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchBirdsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_birds2);


        TextView listHeader = new TextView(this);
        listHeader.setText("Birds");
        listHeader.setTextAppearance(this, android.R.style.TextAppearance_Large);
        ListView listView = findViewById(R.id.search_birds_listview);
        listView.addHeaderView(listHeader);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReadTask task = new ReadTask();
        task.execute("http://birdobservationservice.azurewebsites.net/Service1.svc/observations");
    }

    private class ReadTask extends ReadHttpTask {
        @Override
        protected void onPostExecute(CharSequence jsonString) {
            TextView messageTextView = findViewById(R.id.main_message_textview);

            final List<Bird> books = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(jsonString.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    double latitude = obj.getDouble("Latitude");
                    double longitude = obj.getDouble("Longitude");
                    String nameDanish = obj.getString("NameDanish");
                    String nameEnglish = obj.getString("NameEnglish");
                    int birdId = obj.getInt("BirdId");
                    Bird bird = new Bird(birdId, latitude, longitude, nameDanish, nameEnglish);
                    books.add(bird);
                }


                ListView listView = findViewById(R.id.search_birds_listview);
                ArrayAdapter<Bird> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, books);
                //BirdListItemAdapter adapter = new BirdListItemAdapter(getBaseContext(), R.layout.booklist_item, books);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), BirdDetailActivity.class);
                        // Book book = books.get((int) id);
                        // Book book = books[(int) id];
                        Bird bird = (Bird) parent.getItemAtPosition(position);
                        intent.putExtra("BIRD", bird);
                        startActivity(intent);
                    }
                });
            } catch (JSONException ex) {
                messageTextView.setText(ex.getMessage());
                Log.e("BIRD", ex.getMessage());
            }
        }
    }
}
