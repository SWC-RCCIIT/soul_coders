package com.example.nahihoga;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
public class registration extends AppCompatActivity {
    private Button b1;
    String bankvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Spinner spinner = findViewById(R.id.noOfbanks);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankvalue = parent.getItemAtPosition(position).toString();
                //  Toast.makeText(parent.getContext(), "Selected: " + bankvalue,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        b1=(Button) findViewById(R.id.nextPage);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {

                Intent intent = new Intent(registration.this,bankform.class);
                intent.putExtra("bankVal",bankvalue);
                startActivity(intent);

            }
        });

    }

}
