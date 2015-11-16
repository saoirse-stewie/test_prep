package com.example.test_prep;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static android.R.layout.simple_list_item_1;

public class MyActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    Button b1;
    Button b2;
    String t;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        b1 = (Button) findViewById(R.id.btn1);
        b2 = (Button) findViewById(R.id.btn2);

        String sd = "data/data/com.example.test_prep";
        String dbpath = sd + "/mydb.db";

        String TABLE_NAME = "list";
        String col_1 = "name";
        String col_2= "author";

        String[] cols = {col_1 , col_2};

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    t = getXml(MyActivity.this);
                    System.out.print(t);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                String[] items = t.split("\n");


                setListAdapter(new ArrayAdapter<String>(MyActivity.this, android.R.layout.simple_list_item_1, items));

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    t = getXml(MyActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                String[] items = t.split("\n");
               // items = t.split("\n");
                //items = t.split(" ");


                db = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                db.execSQL("drop table " + TABLE_NAME + ";");
                db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id  INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + col_1 + " TEXT ," + col_2 + " TEXT );");

               // ContentValues cv = new ContentValues();

               // for(int i =0;i<items.length;i++)
                //    cv.put(col_2, items[1]);
              //  for(int i =0;i<items.length;i++)
                 //   cv.put(col_1,items[0]);

                ContentValues[] cv = new ContentValues[items.length];


                    for (int rows = 0; rows < items.length; rows++) {
                        for(int columns=0;columns<cols.length;columns++) {
                            String temp = items[rows];


                            ContentValues c = new ContentValues();

                            c.put(cols[columns], temp);

                            cv[rows] = c;

                           // db.insert(TABLE_NAME, null, cv[columns]);
                            //cv[rows]=c;
                        }

                    }
                    if (db != null) {
                       for (int i = 0; i < items.length; i++) {
                            db.insert(TABLE_NAME, null, cv[i]);
                        }
                    }

            }

        });

    }

    public String getXml(Activity activity) throws IOException, XmlPullParserException {
        StringBuffer sb = new StringBuffer();
        Resources res = activity.getResources();
        XmlResourceParser xr = res.getXml(R.xml.books);

        xr.next();

        int event = xr.getEventType();
        String author;

        while(event!=xr.END_DOCUMENT)
        {
            if(event == xr.START_TAG)
            {
                if(xr.getName().equals("Item"))
                {
                    sb.append(xr.getAttributeValue(null,"itemNumber") +"\n");

                }
                else if(xr.getName().equals("author"))
                {

                    sb.append(xr.getName() + " " + xr.nextText());
                    //return author;
                }

            }


            event = xr.next();
        }

        return sb.toString();


    }





}
