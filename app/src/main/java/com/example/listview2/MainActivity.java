package com.example.listview2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final static String TITLE = "title";
    private final static String SUBTITLE = "subtitle";

    private final static String SHARED_NAME = "prefs";
    private final static String KEY = "key";

    private List<Map<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        final ListView listView = findViewById(R.id.list_view);
        final BaseAdapter adapter = createAdapter(list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                init();
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        });
    }

    private void init() {
        SharedPreferences prefs = getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
        String savedString = prefs.getString(KEY, "");
        if (savedString.isEmpty()) {
            String text = getString(R.string.large_text);
            list.addAll(prepareContent(text));
            prefs.edit().putString(KEY, text).apply();
        } else {
            list.addAll(prepareContent(savedString));
        }
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> list) {
        String[] from = {TITLE, SUBTITLE};
        int[] to = {R.id.text, R.id.text_length};
        return new SimpleAdapter(this, list, R.layout.list_item, from, to);
    }

    @NonNull
    private List<Map<String, String>> prepareContent(String value) {
        List<Map<String, String>> list = new ArrayList<>();
        String[] arrayContent = value.split("\n\n");

        for (String s : arrayContent) {
            Map<String, String> map = new HashMap<>();
            map.put(TITLE, s);
            map.put(SUBTITLE, s.length() + "");
            list.add(map);
        }
        return list;
    }
}