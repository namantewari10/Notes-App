package com.example.notesappnaman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> title=new ArrayList<>();
    static ArrayList<String> content=new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.newnote:
                Intent intent=new Intent(getApplicationContext(),EditorActivity.class);
                int newPostion=title.size();
                intent.putExtra("placeNumber", newPostion);
                startActivity(intent);
                return true;
            default:
                return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getSharedPreferences("com.example.notesappnaman", Context.MODE_PRIVATE);
        ListView listView = (ListView) findViewById(R.id.listView);


        try {
            title=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("title",ObjectSerializer.serialize(new ArrayList<String >())));
            content=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("content",ObjectSerializer.serialize(new ArrayList<String >())));
        }catch (Exception e){
            e.printStackTrace();
        }

        if(title.size()==0) {
            content.add("Example");
            title.add("Example");
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, title);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),EditorActivity.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                title.remove(position);
                content.remove(position);
                sharedPreferences.edit().clear().commit();
                try {
                    MainActivity.sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.title)).apply();
                    MainActivity.sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.content)).apply();
                }catch (Exception e){
                    e.printStackTrace();
                }
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}
