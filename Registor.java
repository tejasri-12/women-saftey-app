package com.example.t;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Registor extends AppCompatActivity {
    Button b1,b2,b3;
    EditText e1,e2;
    ListView listView;
    SQLiteOpenHelper s1;
    SQLiteDatabase sqlitedb;
    DatabaseHandler myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        e2=findViewById(R.id.name);
        e1=findViewById(R.id.phone);
        b1=findViewById(R.id.add);
        b2=findViewById(R.id.delete);
        b3=findViewById(R.id.view);

        myDB=new DatabaseHandler(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sr2=e2.getText().toString();
                String sr=e1.getText().toString();
                addData(sr,sr2);
                Toast.makeText(Registor.this,"Data added",Toast.LENGTH_SHORT).show();
                e1.setText( "" );
                e2.setText("");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlitedb=myDB.getWritableDatabase();
                String x=e1.getText().toString();
                String y=e2.getText().toString();
                DeleteData(x,y);
                Toast.makeText(Registor.this,"Data Deleted",Toast.LENGTH_SHORT).show();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ){
                loadData();
            }
        });
        

    }

    private void loadData() {
        ArrayList<String> theList=new ArrayList<>();
        TextView textView=findViewById(R.id.list);
        Cursor data= myDB.getListContents();
        if(data.getCount()==0){
            Toast.makeText(this,"no content to show",Toast.LENGTH_SHORT).show();
        }
        else {
            String number="";
            while (data.moveToNext()){
                theList.add(data.getString(1));
                number=number+data.getString(1)+(data.isLast()?"":";");
                textView.setText(number);
            }
        }
    }


    private boolean DeleteData(String x,String y) {
        return (sqlitedb.delete(DatabaseHandler.TABLE_NAME,DatabaseHandler.COL2+"=?",new String[]{x}))>0 && (sqlitedb.delete(DatabaseHandler.TABLE_NAME,DatabaseHandler.COL2+"=?",new String[]{y}))>0;
    }


    private void addData(String p,String name) {
        boolean insertData=myDB.addData(name);
        boolean inp=myDB.addData(p);
        if (insertData==true && inp==true){
            Toast.makeText(Registor.this,"Data added",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Registor.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
        }
    }
}