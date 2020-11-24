package com.example.easywallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywallet.Db.SaveDbHelper;
import com.example.easywallet.adapter.SaveListAdapter;
import com.example.easywallet.model.SaveItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button mSaveAddButton,mSaveExitButton;
    private SaveDbHelper mHelper;
    private SQLiteDatabase mDb;
    private ArrayList<SaveItem> mSaveItemsList = new ArrayList<>();
    TextView mLeftTextView;
    private SaveListAdapter mAdapter;
    String sum="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast toast = Toast.makeText(MainActivity.this,"Welcome to CASHFLOW application!",Toast.LENGTH_SHORT); //สร้างข้อความที่จะเด้งขึ้นมาว่า
        // Welcome to CASHFLOW appliclition เป็นแบบ Toast แสดงผลระยะสั้น
        toast.show(); // โชว์ข้อความ toast
        mSaveAddButton = findViewById(R.id.save_add_button);
        mSaveExitButton = findViewById(R.id.save_exit_button);
        mLeftTextView = findViewById(R.id.Left_textView);
        mLeftTextView.setText(sum);
        mHelper = new SaveDbHelper(this); //save เก็บข้อ Database
        mDb = mHelper.getReadableDatabase();
        loadDataFromDb(); // โหลดข้อมูลใน Database
        mAdapter = new SaveListAdapter(
                this,
                R.layout.item, //โชว์ item
                mSaveItemsList
        );

        ListView iv = findViewById(R.id.list_View);
        iv.setAdapter(mAdapter);

        iv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                SaveItem item = mSaveItemsList.get(position); // เก็บ position จากการคลิกค้าง
                dialog.setTitle("Delete "+item.title+" "+item.number+" Bath ?"); // ข้อความม dialog ขึ้นมาว่าจะ Delete สิ่งที่คลิดค้างไว้มั้ย
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { // ถ้ากด Yes
                        SaveItem item = mSaveItemsList.get(position);
                        int phoneId=item.id; //ยอดเงินรวมจะ ลบ/เพิ่ม ตามตำแหน่งที่เราเลือก

                        mDb.delete( // ลบข้อมูลใน Database
                                SaveDbHelper.TABLE_NAME,
                                SaveDbHelper.COL_ID+"=?",
                                new String[]{String.valueOf(phoneId)}
                        );
                        loadDataFromDb();
                        mAdapter.notifyDataSetChanged();
                    }

                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() { // ถ้าดเ No จะไม่มีอะไรเปลีี่ยนแปลง
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                });


                dialog.show();
                return true;
            }
        });
        mSaveAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainSaveActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 001);
            }
        });

        mSaveExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainSaveActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 002);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==001){
            if(resultCode==RESULT_OK){
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
            }
        }
        if(requestCode==002){
            if(resultCode==RESULT_OK){
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadDataFromDb() {
        Cursor cursor =mDb.query(

                SaveDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        String s = sum;
        mSaveItemsList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SaveDbHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(SaveDbHelper.COL_TITLE));
            String number = cursor.getString(cursor.getColumnIndex(SaveDbHelper.COL_NUMBER));
            String picture = cursor.getString(cursor.getColumnIndex(SaveDbHelper.COL_PICTURE));
            String type = cursor.getString(cursor.getColumnIndex(SaveDbHelper.COL_TYPE));
            SaveItem item = new SaveItem(id,title,number,picture,type);
            mSaveItemsList.add(item);
            if(type.equals("1")) {
                s = String.valueOf((Integer.parseInt(s) + Integer.parseInt(number)));
            }
            if(type.equals("2")) {
                s = String.valueOf((Integer.parseInt(s) - Integer.parseInt(number)));
            }
            mLeftTextView.setText(s);
        }
    }

}
