package com.example.disasterfasoulisproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnReprotFire;
    ContacsDbHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReprotFire = findViewById(R.id.btnFire);
        mDBHelper = new ContacsDbHelper(this);

        btnReprotFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FireActivity.class));
            }
        });
    }

    //------------------------------Επιστροφή όλων των επαφών απο την βάση-------------------------------
    public List<Contacs> getAllContacts() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<Contacs> contacsList = new ArrayList<Contacs>();
        String[] projection = {
                ContacsDbContract.ContacsEntry._ID,
                ContacsDbContract.ContacsEntry.COLUMN_NAME_CONTACT_TITLE,
                ContacsDbContract.ContacsEntry.COLUMN_NAME_CONTACT_PHONE
        };
        Cursor c = db.query(
                ContacsDbContract.ContacsEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // null columns means all
                null,                                     // null values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );
        while (c.moveToNext()) {
            Contacs contacs = new Contacs(c.getInt(c.getColumnIndex(ContacsDbContract.ContacsEntry._ID)),
                    c.getString(c.getColumnIndex(ContacsDbContract.ContacsEntry.COLUMN_NAME_CONTACT_TITLE)),
                    c.getString(c.getColumnIndex(ContacsDbContract.ContacsEntry.COLUMN_NAME_CONTACT_PHONE)));
            contacsList.add(contacs);
        }
        db.close();
        return contacsList;
    }
//----------------------------------------------------------------------------------------------------------------//

    public void messageshow(String message){
        AlertDialog.Builder abuilder;
        abuilder = new AlertDialog.Builder(this);
        abuilder.setTitle("Movies");
        abuilder.setMessage(message);
        abuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = abuilder.create();
        dialog.show();
    }
//---------------------------------------------------------------------------------------------------------------------//

    public void viewall(View view){
        List<Contacs> contactslist = getAllContacts();
        if (contactslist != null){
            StringBuffer buffer = new StringBuffer();
            for (Contacs contact : contactslist) {
                buffer.append(contact.getId()+"\n");
                buffer.append(contact.getName()+"\n");
                buffer.append(contact.getPhoneNumber()+"\n\n");
            }
            messageshow(buffer.toString());
        }
    }




}