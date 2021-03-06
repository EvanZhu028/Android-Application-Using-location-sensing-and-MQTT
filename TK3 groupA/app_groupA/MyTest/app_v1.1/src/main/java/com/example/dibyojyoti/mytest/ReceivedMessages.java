package com.example.dibyojyoti.mytest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ReceivedMessages extends AppCompatActivity {
    ArrayList<String> addArray = new ArrayList<String>();
    Button refershBbutton;
    DbHelperFrndList dbHelperMsg;
    ArrayList<String> msgsdetails;
    HashMap<Integer,String> msgpos;
    String msgid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_messages);
     // dbHelperMsg1.insertMsg("017665617079","Dibyojyoti","Hello This is a help message","R","06/22/2016","10:17 PM");

        refreshfriendslist();
        refershBbutton = (Button) findViewById(R.id.button_rm);
        refershBbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshfriendslist();
            }
        });

        ListView list = (ListView) findViewById(R.id.listViewrm);
        list.setLongClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                msgid = msgpos.get(position);
                new AlertDialog.Builder(ReceivedMessages.this)
                        .setTitle("Deleting Message")
                        .setMessage("" + "" + "" + "" +
                                "Are you sure you want to delete this Message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelperMsg = new DbHelperFrndList(ReceivedMessages.this);
                                dbHelperMsg.deleteMessage(msgid);
                                refreshfriendslist();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long arg){
                Intent intent = new Intent(ReceivedMessages.this,popup.class);
                msgid = msgpos.get(position);
                dbHelperMsg = new DbHelperFrndList(ReceivedMessages.this);
                Cursor c = dbHelperMsg.getDataMsg(msgid);
                c.moveToFirst();
                String message = c.getString(4);
                c.close();
                intent.putExtra("messageText", message);
                startActivity(intent);
            }
        });
    }
    void refreshfriendslist(){
        dbHelperMsg = new DbHelperFrndList(getApplicationContext());
        HashMap<String,DbHelperFrndList.DispMessage> msgslist = dbHelperMsg.getAllRecevMsgs();
        Iterator myVeryOwnIterator = msgslist.keySet().iterator();
        msgsdetails = new ArrayList<String>();
        msgpos = new HashMap<Integer, String>();
        DbHelperFrndList.DispMessage dm;
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            dm = (DbHelperFrndList.DispMessage) msgslist.get(key);
            msgpos.put(msgsdetails.size(),key);
            msgsdetails.add(dm.getName() +" # "+ dm.getPh() +" - "+dm.getDate()+"-"+dm.getTime() );
        }
        ListView listView = (ListView) findViewById(R.id.listViewrm);
        Log.d("AddFriend size:", "" + msgsdetails.size());
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgsdetails);
        listView.setAdapter(adapter);
    }
}
