package com.example.administrator.needletherapy.Main.FindDoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.needletherapy.R;

public class FindDoctor extends AppCompatActivity {

    String doctorname[]={"张医师","李医师","谢医师","徐医师","刘医师","孙医师","赵医师","钱医师","周医师","吴医师","郑医师"};
    String doctorclass[]={"医师"};
    String doctorabstract[]={""};
    double doctorscore[]={5.0,4.1,4.3,4.7,4.9,3.9,4.4,3.8,4.2,3.6,5.0};

    RecyclerView recyclerView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(FindDoctor.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(FindDoctor.this, LinearLayoutManager.VERTICAL));

        imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(FindDoctor.this).inflate(R.layout.item_doctor_info,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder)holder).doctorname.setText(doctorname[position]);
            ((Holder)holder).doctorscore.setText(Double.toString(doctorscore[position]).substring(0,3));
        }

        @Override
        public int getItemCount() {
            return doctorname.length;
        }

        class Holder extends RecyclerView.ViewHolder{

            TextView doctorname,doctorscore;

            public Holder(View v) {
                super(v);
                doctorname=(TextView) v.findViewById(R.id.doctorname);
                doctorscore=(TextView)v.findViewById(R.id.doctorscore);
            }
        }
    }
}
