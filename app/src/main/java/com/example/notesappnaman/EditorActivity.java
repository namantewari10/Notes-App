package com.example.notesappnaman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {
    String content;
    EditText editText;
    int placeNumber;
    boolean isNewNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editText=(EditText) findViewById(R.id.editText);
        Intent intent=getIntent();
        placeNumber=intent.getIntExtra("placeNumber",0);
        if(placeNumber==(MainActivity.title.size())) {
            isNewNote=true;
            content = "";
        }
        else {
            isNewNote=false;
            content = MainActivity.content.get(placeNumber);
        }
        editText.setText(content);
    }

    @Override
    public void onBackPressed() {
        String newContent=editText.getText().toString();
        if(newContent.equals(content))
        {
            Toast.makeText(this, "Nothing modified or added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(isNewNote)
            {
            MainActivity.content.add(newContent);
                if (MainActivity.content.get(placeNumber).length() > 40) {
                    MainActivity.title.add(MainActivity.content.get(placeNumber).substring(0, 40)+"...");
                } else {
                    MainActivity.title.add(MainActivity.content.get(placeNumber));
                }
            MainActivity.arrayAdapter.notifyDataSetChanged();
            }
            else
            {
                MainActivity.content.set(placeNumber, newContent);
                if (MainActivity.content.get(placeNumber).length() > 40) {
                    MainActivity.title.set(placeNumber, MainActivity.content.get(placeNumber).substring(0, 40)+"...");
                } else {
                    MainActivity.title.set(placeNumber, MainActivity.content.get(placeNumber));
                }
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }
            try {
                MainActivity.sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.title)).apply();
                MainActivity.sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.content)).apply();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }
}
