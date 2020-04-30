package com.example.muslim.Ui.zakerdisplay;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.muslim.Data.Local.AppDatabase;
import com.example.muslim.Models.EntintyModel;
import com.example.muslim.R;

import java.util.List;

public class ZakerDisplay extends Fragment {
    View view;
    AppDatabase db;
    RecyclerView recyclerView;
    zakerAdapter adapter;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view=inflater.inflate(R.layout.zaker_display_xml,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialrecycler();
        intialdatabase();
        new get().execute();
    }

    private void intialdatabase() {
        db= Room.databaseBuilder(getContext(),AppDatabase.class,"db").build();

    }

    private void intialrecycler() {
        recyclerView=view.findViewById(R.id.Recycler2);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity().getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);


    }

    class zakerAdapter extends RecyclerView.Adapter<zakerAdapter.viewholder>{
        List<EntintyModel> entintyModels;

        public zakerAdapter(List<EntintyModel> entintyModels) {
            this.entintyModels = entintyModels;
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.zakerdisplay_item,parent,false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final viewholder holder, int position) {
             EntintyModel entintyModel=entintyModels.get(position);
              final int counternumber=entintyModel.getLoop();
            String bsmla=entintyModel.getBsmla();
            String zkr=entintyModel.getZkr();
            String detils=entintyModel.getZkrdetils();

            if (bsmla.isEmpty()){
                holder.bsmla.setVisibility(View.GONE);
            }
            else {
                holder.bsmla.setVisibility(View.VISIBLE);
            }
            if (detils.isEmpty()){
                holder.detils.setVisibility(View.GONE);
            }
            else {
                holder.detils.setVisibility(View.VISIBLE);
            }
            holder.bsmla.setText(bsmla);
            holder.zkr.setText(zkr);
            holder.detils.setText(detils);
            holder.counternumber.setText(String.valueOf(counternumber));
            if(counternumber==0){
                holder.counter.setVisibility(View.GONE);
            }
            holder.counter.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int value= Integer.parseInt( holder.counternumber.getText().toString());

                   if(value>0) {

                       value = value - 1;
                       holder.counternumber.setText(String.valueOf(value));
                   }
                }

            });
            holder.reloadcounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.counternumber.setText(String.valueOf(counternumber));

                }
            });

        }

        @Override
        public int getItemCount() {
            return entintyModels.size();
        }

        class viewholder extends RecyclerView.ViewHolder{

            TextView counternumber,bsmla,zkr,detils;
            ViewGroup counter;
            ImageView reloadcounter;

            public viewholder(@NonNull View itemView) {
                super(itemView);
                counter=itemView.findViewById(R.id.counter);
                counternumber=itemView.findViewById(R.id.counternumber);
                bsmla=itemView.findViewById(R.id.bsmla);
                zkr=itemView.findViewById(R.id.zkr);
                detils=itemView.findViewById(R.id.detils);
                reloadcounter=itemView.findViewById(R.id.reloadcounter);
            }
        }
    }

    class get extends AsyncTask<Void,Void,List<EntintyModel>>{

        @Override
        protected List<EntintyModel> doInBackground(Void... voids) {
            List<EntintyModel> entintyModels=db.dao().getall();
            return entintyModels;
        }

        @Override
        protected void onPostExecute(List<EntintyModel> entintyModels) {
            super.onPostExecute(entintyModels);
            adapter=new zakerAdapter(entintyModels);
            recyclerView.setAdapter(adapter);
        }
    }
}
