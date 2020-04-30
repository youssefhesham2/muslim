package com.example.muslim.Ui.QuranFragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import com.example.muslim.Data.Cloud.RetrofitClient;
import com.example.muslim.Data.Local.AppDatabase;
import com.example.muslim.Models.QuranArray;
import com.example.muslim.Models.QuranModel;
import com.example.muslim.Models.QuranModelapi;
import com.example.muslim.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuranFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    quranAdapter quranAdapter;
    AppDatabase db;
    SearchView searchView;
    QuranModel[] quranmodel;
    Context context;
    TextView surah_name;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int s;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view=inflater.inflate(R.layout.quran_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialviews();
        intialdatabase();
        getdata();
        searchaya();
        intialshardprefrance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

    }

    private void intialshardprefrance() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    private void getdata() {
        //final int save= preferences.getInt("savelastaya", 0);

        RetrofitClient.getInstance().get().enqueue(new Callback<QuranModel[]>() {
            @Override
            public void onResponse(Call<QuranModel[]> call, Response<QuranModel[]> response) {

                if(response.isSuccessful()&&response.code()==200)
                {
                    quranmodel=response.body();

                    new getquran().execute(quranmodel);
                }
                else {
                    new getquran2().execute();
                }
            }

            @Override
            public void onFailure(Call<QuranModel[]> call, Throwable t) {
                new getquran2().execute();

            }
        });
    }

    private void searchaya() {
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               new search().execute(s);
               return true;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               new search().execute(s);
               return false;
           }
       });

    }

    private void intialdatabase() {
        db= Room.databaseBuilder(getContext(), AppDatabase.class,"db").build();

    }
    private void intialviews() {
        recyclerView=view.findViewById(R.id.Recycler4);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity().getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        searchView=view.findViewById(R.id.searchciew);
        surah_name=view.findViewById(R.id.surah_name);
        Toast.makeText(context, "Press Long Click to Save Aya", Toast.LENGTH_SHORT).show();
    }

    class quranAdapter extends RecyclerView.Adapter<quranAdapter.Viewholde>{



        List<QuranModel> quranModels;

        public quranAdapter(List<QuranModel> quranModels) {
            this.quranModels = quranModels;
        }

        @NonNull
        @Override
        public Viewholde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.quran_fragmet_item,parent,false);
           return new Viewholde(view);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull Viewholde holder, final int position) {
           QuranModel quranModel=quranModels.get(position);

             int suranumber=quranModel.getSurahNumber();
            String surah_namee="";
            switch (suranumber){
                case 1:surah_namee="الفَاتِحَة-مَكيَّة";
                break;
                case 2:surah_namee="البَقَرَة-مَدنيَّة";
                    break;
                case 3:surah_namee="آل عِمرَان-مَدنيَّة";
                    break;
                case 4:surah_namee="النِّسَاء-مَدنيَّة";
                        break;
                case 5:surah_namee="المَائدة-مَدنيَّة";
                    break;
                case 6:surah_namee="الأنعَام-مَكيَّة";
                    break;
                case 7:surah_namee="الأعرَاف-مَكيَّة";
                    break;
                case 8:surah_namee="الأنفَال-مَدنيَّة";
                    break;
                case 9:surah_namee="التوبَة-مَدنيَّة";
                    break;
                case 10:surah_namee="يُونس-مَكيَّة";
                    break;
                case 11:surah_namee="هُود-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 13:surah_namee="الرَّعْد-مَدنيَّة";
                    break;
                case 14:surah_namee="إبراهِيم-مَكيَّة";
                    break;
                case 15:surah_namee="الحِجْر-مَكيَّة";
                    break;

                case 16:surah_namee="النَّحْل-مَكيَّة";
                    break;
                case 17:surah_namee="الإسْرَاء-مَكيَّة";
                    break;
                case 18:surah_namee="الكهْف-مَكيَّة";
                    break;
                case 19:surah_namee="مَريَم-مَكيَّة";
                    break;
                case 20:surah_namee="طه-مَكيَّة";
                    break;
                case 21:surah_namee="الأنبيَاء-مَكيَّة";
                    break;
                case 22:surah_namee="الحَج-مَدنيَّة";
                    break;



                case 23:surah_namee="المُؤمنون-مَكيَّة";
                    break;
                case 24:surah_namee="النُّور-مَدنيَّة";
                    break;
                case 25:surah_namee="الفُرْقان-مَكيَّة";
                    break;
                case 26:surah_namee="الشُّعَرَاء-مَكيَّة";
                    break;
                case 27:surah_namee="النَّمْل-مَكيَّة";
                    break;
                case 28:surah_namee="القَصَص-مَكيَّة";


                    break;
                case 29:surah_namee="العَنكبوت-مَكيَّة";
                    break;
                case 30:surah_namee="الرُّوم-مَكيَّة";
                    break;
                case 31:surah_namee="لقمَان-مَكيَّة";
                    break;
                case 32:surah_namee="السَّجدَة-مَكيَّة";
                    break;
                case 33:surah_namee="الأحزَاب-مَدنيَّة";
                    break;
                case 34:surah_namee="سَبَأ-مَكيَّة";
                    break;

                case 35:surah_namee="فَاطِر-مَكيَّة";
                    break;
                case 36:surah_namee="يس-مَكيَّة";
                    break;
                case 37:surah_namee="الصَّافات-مَكيَّة";
                    break;
                case 38:surah_namee="ص-مَكيَّة";
                    break;
                case 39:surah_namee="الزُّمَر-مَكيَّة";
                    break;
                case 40:surah_namee="غَافِر-مَكيَّة";
                    break;
                case 41:surah_namee="فُصِّلَتْ-مَكيَّة";
                    break;
                case 42:surah_namee="الشُّورَى-مَكيَّة";
                    break;
                case 43:surah_namee="الزُّخْرُف-مَكيَّة";
                    break;
                case 44:surah_namee="الدخَان-مَكيَّة";
                    break;

                case 45:surah_namee="الجَاثيَة-مَكيَّة";
                    break;
                case 46:surah_namee="الأحْقاف-مَكيَّة";
                    break;
                case 47:surah_namee="محَمَّد-مَدنيَّة";
                    break;
                case 48:surah_namee="الفَتْح-مَدنيَّة";
                    break;
                case 49:surah_namee="الحُجرَات-مَدنيَّة";
                    break;
                case 50:surah_namee="ق-مَكيَّة";
                    break;
                case 51:surah_namee="الذَّاريَات-مَكيَّة";
                    break;
                case 52:surah_namee="الطُّور-مَكيَّة";
                    break;
                /*case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;


                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;



                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;



                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;




                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;




                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;



                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;



                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;




                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;
                case 12:surah_namee="يُوسُف-مَكيَّة";
                    break;*/
            }
            String text=quranModel.getText();
            final int verse_number=quranModel.getVerseNumber();
            String translation=quranModel.getTranslation();
            if(surah_namee.isEmpty()){
                surah_name.setVisibility(View.GONE);

            }
            else {
               surah_name.setVisibility(View.VISIBLE);
                surah_name.setText(surah_namee);

            }
            holder.text.setText(text);
            holder.verse_number.setText("-"+verse_number);
            holder.translation.setText(translation);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    editor.putInt("savelastaya",position);
                    editor.commit();
                   view.setBackgroundColor(R.color.savedaya);
                    Toast.makeText(context, "Aya Number "+verse_number+"saved", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            int savedaya = preferences.getInt("savelastaya", 0);
            if(position==savedaya){
                holder.itemView.setBackgroundColor(R.color.savedaya);
            }
        }

        @Override
        public int getItemCount() {
            return quranModels.size();
        }

        class Viewholde extends RecyclerView.ViewHolder{

            TextView text, verse_number, translation;

            public Viewholde(@NonNull View itemView) {
                super(itemView);
                text=itemView.findViewById(R.id.text);
                verse_number=itemView.findViewById(R.id.verse_number);
                translation=itemView.findViewById(R.id.translation);

            }

        }
    }

    class getquran extends AsyncTask<QuranModel,Void,List<QuranModel>>{

        @Override
        protected List<QuranModel> doInBackground(QuranModel...quranModels) {
                db.dao().deletequran();
                db.dao().insertquran(quranModels);
            List<QuranModel> quranModelss=db.dao().getallquran();
                return quranModelss;
        }


        @Override
        protected void onPostExecute(List<QuranModel> quranModels) {
            super.onPostExecute(quranModels);
             quranAdapter=new quranAdapter(quranModels);
            recyclerView.setAdapter(quranAdapter);
        }
    }
    class getquran2 extends AsyncTask<Void,Void,List<QuranModel>>{


        @Override
        protected List<QuranModel> doInBackground(Void... quranModels) {

            List<QuranModel> quranModelss=db.dao().getallquran();
            return quranModelss;
        }

        @Override
        protected void onPostExecute(List<QuranModel> quranModels) {
            super.onPostExecute(quranModels);

            quranAdapter=new quranAdapter(quranModels);
            recyclerView.setAdapter(quranAdapter);
        }
    }

    class search extends AsyncTask<String,Void,List<QuranModel>>{


        @Override
        protected List<QuranModel> doInBackground(String... strings) {
            List<QuranModel> quranModels= db.dao().searchInQuran(strings[0]);
            return quranModels;
        }

        @Override
        protected void onPostExecute(List<QuranModel> quranModels) {
            super.onPostExecute(quranModels);
            quranAdapter=new quranAdapter(quranModels);
            recyclerView.setAdapter(quranAdapter);
        }
    }
}
