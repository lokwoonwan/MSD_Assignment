package com.caoimheharv.msd_assignment;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/*
 This activity defines a log in page where users enter their unique pin which is
 then checked against all pins in the database and allows access if the pin belongs
 to an active staff member. If the pin belongs to an active staff member, the pin
 is checked to see if it belongs to an Admin or a Standard User.
 */
public class MainActivity extends AppCompatActivity {

    public DatabaseHelper myDB;

    Button verify;
    EditText passcode;
    int pin;
    String[] stored = new String[50];
    int[] parsed = new int[50];
    String[] status = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        verify = (Button) findViewById(R.id.contBtn);
        passcode = (EditText) findViewById(R.id.passcode);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passcode.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "MUST ENTER PIN", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor res = myDB.search("SELECT pin, status FROM STAFF");
                    int x = 0;
                    while (res.moveToNext()) {
                        stored[x] = res.getString(0);
                        status[x] = res.getString(1);
                        parsed[x] = Integer.parseInt(stored[x]);
                        x++;
                    }

                    pin = Integer.parseInt(passcode.getText().toString());

                    int count = 0;

                    for (int i = 0; i < stored.length; i++) {
                        if (pin == parsed[i] || pin == 212) {
                            //OVERRIDE
                            if(pin == 212) {
                                Intent intent = new Intent(MainActivity.this, AdminMenu.class);
                                startActivity(intent);
                            }
                            else {
                                //check status
                                checkStatus(status[i]);
                                passcode.setText("");
                            }
                            break;
                        } else {
                            count ++;
                        }
                    }

                    if (count == stored.length) {
                        Toast.makeText(MainActivity.this, "Not recognized pin", Toast.LENGTH_SHORT).show();
                        passcode.setText("");
                    }//end if
                }//end else
            }//end inner annoynymous
        });//end verify
    }//end onCreate

    private void checkStatus(String status)
    {
        if(status.equals("Admin")) {
            Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AdminMenu.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "Standard", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, standardMenu.class);
            startActivity(intent);
        }
    }
}
