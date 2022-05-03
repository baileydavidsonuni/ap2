package com.example.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        read();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() { // Long clicking a list of tasks deletes it from the database.
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        write();
                        return true;
                    }
                });
        lvItems.setOnItemClickListener( // Clicking a list of tasks lets you see it's "sub-tasks"
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        Intent i = new Intent (getApplicationContext(), NestedActivity.class);
                        String title = ( (TextView) item ).getText().toString();
                        i.putExtra("title", title);
                        startActivity(i);

                    }
                }
        );

    }

    private void read() { // Reading a list from the database
        File filesDir = getFilesDir();
        File listFile = new File(filesDir, "list.txt"); //not an ideal implementation nor strictly functional
        try {
            items = new ArrayList<String>(FileUtils.readLines(listFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void write() { // Writing a list made by the user to the database
        File filesDir = getFilesDir();
        File listFile = new File(filesDir, "list.txt");
        try {
            FileUtils.writeLines(listFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        write();
    }

}
