package com.example.ramshark.tombradarg;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataDead> data= Collections.emptyList();
    DataDead current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Adapter(Context context, List<DataDead> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataDead current=data.get(position);
        myHolder.textFishName.setText(current.lname );
        myHolder.textSize.setText(current.fname);


        if(current.bdate.equals("0000-00-00")){
            myHolder.textType.setText("Birthday: " + "NO DATA");
        }
        else{

            myHolder.textType.setText("Birthday: " + current.bdate);
        }

        if(current.ddate.equals("0000-00-00")){
            myHolder.textPrice.setText("Date of Death: " + "NO DATA" );
        }
       else {

            myHolder.textPrice.setText("Date of Death: " + current.ddate );
        }

        myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

       final String name=current.lname+", "+current.fname;

       final String lat=current.lat;
       final String lang=current.lang;

       final String bdate=current.bdate;
       final String ddate=current.ddate;
       final String area=current.area;
       final String blk=current.blk;
       final String lot=current.lot;







       /*
        // load image into imageview using glide
        Glide.with(context).load("http://tombradar.000webhostapp.com/pics/grave.jpg" )
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(myHolder.ivFish);
    */


        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if(!lat.equals("0")  || !lang.equals("0")){



                   Context context = view.getContext();


                   Intent intent = new Intent(context, search_map.class);
                   intent.putExtra("name",name);



                   intent.putExtra("lat",lat);
                   intent.putExtra("lang",lang);

                   if(bdate.equals("0000-00-00")){
                       intent.putExtra("bdate","NO DATA");
                   }
                   else{

                       intent.putExtra("bdate",bdate);
                   }

                   if(ddate.equals("0000-00-00")) {
                       intent.putExtra("ddate","NO DATA");
                   }
                   else {

                       intent.putExtra("ddate",ddate);
                   }


                   intent.putExtra("area",area);
                   intent.putExtra("blk",blk);
                   intent.putExtra("lot",lot);

                   context.startActivity(intent);

                   }

               }
           });


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textFishName;
        //ImageView ivFish;
        TextView textSize;
        TextView textType;
        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textFishName= (TextView) itemView.findViewById(R.id.textFishName);
         //   ivFish= (ImageView) itemView.findViewById(R.id.ivFish);
            textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
        }

    }

    public void setfilter(List<DataDead> listitem)
    {
        data=new ArrayList<>();
        data.addAll(listitem);
        notifyDataSetChanged();
    }


}