package asia.wemap.androidsdktestapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import asia.wemap.androidsdk.WeMap;
import asia.wemap.androidsdk.WePlace;
import asia.wemap.androidsdk.WeSearch;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeMap.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_search);
        EditText searchField = (EditText) findViewById(R.id.editTextSearch);
        ListView listPlaces = (ListView) findViewById(R.id.places);

        WeSearch weSearch = new WeSearch();

        searchField.addTextChangedListener(new TextWatcher() {
            private Timer timer;
            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        WeSearch.WeSearchOptions weSearchOptions = new WeSearch.WeSearchOptions();
                        weSearchOptions.setSize(5);
                        weSearch.search(s.toString(), weSearchOptions, new WeSearch.WeSearchCallBack() {
                            @Override
                            public void onReady(List<WePlace> wePlaces) {
//                                listPlaces.removeAllViews();
                                List<String> statesList = new ArrayList<String>();
                                for (int i = 0; i < wePlaces.size(); i++) {
                                    TextView place = new TextView(SearchActivity.this);
                                    statesList.add(wePlaces.get(i).toString());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this,
                                        android.R.layout.simple_list_item_1, android.R.id.text1, statesList);
                                listPlaces.setAdapter(adapter);
                                listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        int itemPosition = position;
                                        String itemValue = (String) listPlaces.getItemAtPosition(position);

                                        Toast.makeText(getApplicationContext(),
                                                "Location :" + wePlaces.get(position).getLocation(), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            }
                        });
                    }
                }, 200);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }
        });


    }

}