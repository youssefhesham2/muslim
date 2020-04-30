package com.example.muslim.Ui.AzkarFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.muslim.Data.Local.AppDatabase;
import com.example.muslim.Models.AzkarArray;
import com.example.muslim.Models.EntintyModel;
import com.example.muslim.R;
import com.example.muslim.Ui.AzanFragment.AzanFrgment;
import com.example.muslim.Ui.zakerdisplay.ZakerDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AzkarFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<String> azkartypelist;
    AppDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.azkar_fragment_xml,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntialRecycler();
        intialdata();
        intialdatabase();
        AzkarTypAdpter adpter=new AzkarTypAdpter(azkartypelist);
        recyclerView.setAdapter(adpter);
    }

    private void intialdatabase() {
        db= Room.databaseBuilder(getContext(),AppDatabase.class,"db").build();

    }

    private void intialdata() {
        azkartypelist=new ArrayList<>();
        azkartypelist.add("حديث");
        azkartypelist.add("أذكار الصباح");
        azkartypelist.add("أذكار المساء");
        azkartypelist.add("أذكار بعد الصلاة");
        azkartypelist.add("تسابيح");
        azkartypelist.add("أذكار الإستيقاظ");
        azkartypelist.add("أذكار الآذان");
        azkartypelist.add("أذكار المسجد");
        azkartypelist.add("أذكار الوضوء");
    }

    private void IntialRecycler() {
        recyclerView=view.findViewById(R.id.Recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

    }

    class AzkarTypAdpter extends RecyclerView.Adapter<AzkarTypAdpter.ViewHolder>{
        List<String> list;

        public AzkarTypAdpter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.azkar_typ_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.zakrname.setText(list.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position){
                        case 0:
                            new add().execute(new AzkarArray().hadith());
                            replace(new ZakerDisplay());
                            break;
                        case 1:
                            new add().execute(new AzkarArray().azkarelsba7s());
                            replace(new ZakerDisplay());
                            break;
                        case 2:
                            new add().execute(new AzkarArray().azkarelmsaa());
                            replace(new ZakerDisplay());
                            break;
                        case 3:
                            new add().execute(new AzkarArray().azkaresala());
                            replace(new ZakerDisplay());
                            break;
                        case 4:
                            new add().execute(new AzkarArray().tsabee7());
                            replace(new ZakerDisplay());
                            break;
                        case 5:
                            new add().execute(new AzkarArray().azkarestekaz());
                            replace(new ZakerDisplay());
                            break;
                        case 6:
                            new add().execute(new AzkarArray().azan());
                            replace(new ZakerDisplay());
                            break;
                        case 7:
                            new add().execute(new AzkarArray().masged());
                            replace(new ZakerDisplay());
                            break;
                        case 8:
                            new add().execute(new AzkarArray().wdo2());
                            replace(new ZakerDisplay());
                            break;
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView zakrname;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                zakrname=itemView.findViewById(R.id.zkr_label);
            }
        }
    }

    void replace(Fragment fragment){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fram,fragment);
        fragmentTransaction.commit();

    }

    class add extends AsyncTask<EntintyModel,Void,Void>{

        @Override
        protected Void doInBackground(EntintyModel... entintyModels) {
            db.dao().delete();
            db.dao().insert(entintyModels);
            return null;
        }
    }
}
