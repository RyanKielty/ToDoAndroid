package com.theironyard.todoandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    ArrayAdapter<String> items;
    HashMap<Integer, String> notes = new HashMap<>();
    ListView list;
    EditText text;
    Button addButton;
    static final int NOTES_REQUEST = 1;
    final String FILENAME = "todo.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);
        text = (EditText) findViewById(R.id.editText);
        addButton = (Button) findViewById(R.id.button);

        items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        loadToDo();
        list.setAdapter(items);

        addButton.setOnClickListener(this);
        list.setOnItemLongClickListener(this);

        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = text.getText().toString();
        items.add(item);
        text.setText("");

        saveToDo();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String item = items.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To Do");
        builder.setMessage("Are you sure you want to remove this item?");
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                items.remove(item);
                saveToDo();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing here
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, ToDoNotesActivity.class);
        intent.putExtra("todoitem", items.getItem(position));
        intent.putExtra("position", position);
        if(notes.containsKey(position)) {
            intent.putExtra("notes", notes.get(position));
        }
        startActivityForResult(intent, NOTES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NOTES_REQUEST) {
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                String note = data.getStringExtra("notes");
                notes.put(position, note);

                saveToDo();
            }
        }
    }

    private void saveToDo() {
        try {
            FileOutputStream outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            StringBuilder builder = new StringBuilder();
            for(int i = 0;i<items.getCount();i++) {
                String todoItem = items.getItem(i);
                String note = "";
                if(notes.containsKey(i)) {
                    note = notes.get(i);
                }
                builder.append(todoItem + "," + note + "\n");
            }
            outputStream.write(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadToDo() {
        try {
            FileInputStream inputStream = openFileInput(FILENAME);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            int position = 0;
            while(reader.ready()) {
                String todoLine = reader.readLine();
                String lineParts[] = todoLine.split(",");
                items.add(lineParts[0]);
                if(lineParts.length > 1) {
                    notes.put(position, lineParts[1]);
                }
                position++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
