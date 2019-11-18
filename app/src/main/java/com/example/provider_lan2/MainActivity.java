package com.example.provider_lan2;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtMSSach,edtTenSach,edtTacGia;
    Button btnThem,btnSua,btnXoa,btnHuy;
    ListView lvSach;

    DBHelper dbHelper;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     edtMSSach= findViewById(R.id.edtMSSach);
     edtTacGia= findViewById(R.id.edtTacGia);
     edtTenSach= findViewById(R.id.edtTenSach);
     btnThem= findViewById(R.id.btnThem);
     btnHuy= findViewById(R.id.btnHuy);
     btnSua= findViewById(R.id.btnSua);
     btnXoa= findViewById(R.id.btnXoa);
     lvSach= findViewById(R.id.lvSach);

     danhSachSach();

     btnSua.setEnabled(false);
     btnXoa.setEnabled(false);
     btnHuy.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             setDefaultActivity();
         }
     });

     btnThem.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             ContentValues values= new ContentValues();
             values.put("msSach",edtMSSach.getText().toString());
             values.put("tenSach",edtTenSach.getText().toString());
             values.put("tacGia",edtTacGia.getText().toString());
             try {
                 if (edtMSSach.getText().toString().equals("") ||
                         edtTacGia.getText().toString().equals("") ||
                         edtTenSach.getText().toString().equals("")){
                     Toast.makeText(MainActivity.this, "Yeu cau nhap", Toast.LENGTH_SHORT).show();
                 }

                 Uri insert_uri=getContentResolver().insert(MyContentProvider.CONTENT_URI,values);
                 Toast.makeText(MainActivity.this, "Them thanh cong", Toast.LENGTH_SHORT).show();

                 edtTenSach.getText().clear();
                 edtMSSach.setText("");
                 edtTacGia.setText("");
                 danhSachSach();

             }catch (SQLException ex){
                 Toast.makeText(MainActivity.this, "Luu khong thanh cong", Toast.LENGTH_SHORT).show();
             }

         }
     });

     lvSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             btnThem.setEnabled(false);

             Sach sach= (Sach)lvSach.getItemAtPosition(i);
             edtMSSach.setText(sach.getMsSach()+"");
             edtTacGia.setText(sach.getTacGia());
             edtTenSach.setText(sach.getTenSach());

             btnHuy.setEnabled(true);
             btnSua.setEnabled(true);
             btnXoa.setEnabled(true);
             edtMSSach.setEnabled(false);

         }
     });

     btnSua.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String id= edtMSSach.getText().toString();
             String uri= MyContentProvider.URL+"/"+id;

             Uri content_uri= Uri.parse(uri);
             ContentValues values= new ContentValues();
             values.put("tenSach",edtTenSach.getText().toString());
             values.put("tacGia",edtTacGia.getText().toString());
             int update= getContentResolver().update(content_uri,values,null,null);
             if(update>0){
                 Toast.makeText(MainActivity.this, "Cap nhat Thanh cong", Toast.LENGTH_SHORT).show();
             }else{
                 Toast.makeText(MainActivity.this, "cap nhat that bai", Toast.LENGTH_SHORT).show();
             }
             setDefaultActivity();
             danhSachSach();

         }
     });

     btnXoa.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             xacNhanXoa();

         }
     });


    }

    public void danhSachSach(){
        ArrayList<Sach> listSach= new ArrayList<>();

        Cursor cursor= getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,"tenSach");
        if(cursor !=null && cursor.moveToFirst()){
            do{
                Sach sach= new Sach(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                listSach.add(sach);

            }while (cursor.moveToNext());

            adapter= new ArrayAdapter<Sach>(this,android.R.layout.simple_list_item_1,listSach);
            lvSach.setAdapter(adapter);

        }else
            Toast.makeText(this, "khong co du lieu", Toast.LENGTH_SHORT).show();

    }

    public void setDefaultActivity(){
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);
        btnHuy.setEnabled(false);

        btnThem.setEnabled(true);
        edtTenSach.setText("");
        edtTacGia.setText("");
        edtMSSach.setText("");

        edtMSSach.setEnabled(true);

    }
    public void xacNhanXoa(){
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Xác Nhận xóa");
        builder.setMessage("Xác nhận xóa ?");
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setDefaultActivity();
            }
        });

        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id= edtMSSach.getText().toString();
                String uri= MyContentProvider.URL+"/"+id;

                Uri content_uri= Uri.parse(uri);

                int delete= getContentResolver().delete(content_uri,null,null);
                if(delete>0){
                    Toast.makeText(MainActivity.this, "Xóa Thanh cong", Toast.LENGTH_SHORT).show();
                    danhSachSach();
                }else{
                    Toast.makeText(MainActivity.this, "Xóa that bai", Toast.LENGTH_SHORT).show();
                }

                lvSach.deferNotifyDataSetChanged();
                setDefaultActivity();
            }
        });

        AlertDialog dialog= builder.create();
        builder.show();

    }


}
