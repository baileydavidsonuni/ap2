package com.example.list;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NestedActivity extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readNested();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeNested();
                        return true;
                    }

                });
    }

    private void readNested() {
        File filesDir = getFilesDir();
        String title = getIntent().getStringExtra("title");
        File nestedToDoFile = new File(filesDir, title + ".txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(nestedToDoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeNested() {
        File filesDir = getFilesDir();
        String title = getIntent().getStringExtra("title");
        Log.d("Title", title);
        File nestedToDoFile = new File(filesDir, title + ".txt");
        try {
            FileUtils.writeLines(nestedToDoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddNestedItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewNestedItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeNested();
    }

}
