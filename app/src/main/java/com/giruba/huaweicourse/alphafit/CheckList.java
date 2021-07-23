package com.giruba.huaweicourse.alphafit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class CheckList extends AppCompatActivity {

    private ListView citylist;
    private EditText editcity;
    private Button addcity;
    private Button sort_ascending;
    private Button sort_descending;
    private Button clearall;
    ArrayAdapter<String> adapt;
    ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        citylist = findViewById(R.id.citieslist);
        editcity = findViewById(R.id.addcities);
        addcity = findViewById(R.id.addbutton);
        sort_ascending = findViewById(R.id.ascending);
        sort_descending = findViewById(R.id.descending);
        clearall = findViewById(R.id.clearlist);



        //user input add cities
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityname = editcity.getText().toString();
                editcity.getText().clear(); //after type, clear the line
                if(cityname.length()>0)
                {
                    //DATABASE

                    //common query
                    cities.add(cityname);
                    citylist.setAdapter(adapt); //make new row
                    adapt.notifyDataSetChanged();
                }
            }
        });

        //convert to ascending
        sort_ascending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckList.this, "Sorted in ascending order", Toast.LENGTH_SHORT).show();
                Collections.sort(cities);
                adapt.notifyDataSetChanged();
            }
        });

        //sort by descending order, can revert back to ascending
        sort_descending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(cities); //able to revert back to ascending
                adapt.notifyDataSetChanged();
                Toast.makeText(CheckList.this, "Press again to convert back", Toast.LENGTH_SHORT).show();
            }
        });

        //delete items in list
        citylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int pos = position;

                new AlertDialog.Builder(CheckList.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete " +cities.get(position)+" ?" )
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String city = cities.get(pos); //store the city name in here first(prev position)
                                cities.remove(pos);
                                Toast.makeText(CheckList.this, city + " Deleted", Toast.LENGTH_SHORT).show();
                                adapt.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("no",null)
                        .show();
                return true;
            }
        });

        // clear everything in list
        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CheckList.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete All ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cities.clear();
                                Toast.makeText(CheckList.this, "List Cleared", Toast.LENGTH_SHORT).show();
                                adapt.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

        //adapter
        adapt = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                cities
        );

    }
}