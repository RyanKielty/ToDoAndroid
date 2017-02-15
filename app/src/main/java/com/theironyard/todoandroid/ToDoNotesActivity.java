package com.theironyard.todoandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ToDoNotesActivity extends AppCompatActivity implements View.OnClickListener {

    TextView todo;
    EditText notes;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_notes);

        todo = (TextView) findViewById(R.id.textView);
        notes = (EditText) findViewById(R.id.editText2);

        todo.setText(getIntent().getExtras().getString("todoitem", ""));
        notes.setText(getIntent().getExtras().getString("notes", ""));
        position = getIntent().getExtras().getInt("position", 0);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("notes", notes.getText().toString());
        returnIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
