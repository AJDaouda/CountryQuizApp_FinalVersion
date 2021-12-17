package com.example.countryquizapp.UI_and_Helpers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countryquizapp.Model.CountryDetails;
import com.example.countryquizapp.Model.myApp;
import com.example.countryquizapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkService.NetworkingListener, CountryListAdapter.ListClickListener {

    NetworkService networkingService;
    JsonService jsonService;

    //Layout Widgets declaration
    // TextView countrytextview;
    RecyclerView countryRecyclerview;

    //ArrayList<Country> countrydata = new ArrayList<Country>(0);
    //ArrayList<Country> countrydata1 = new ArrayList<Country>(0);

    ArrayList<CountryDetails> countrydata = new ArrayList<CountryDetails>(0);
    ArrayList<CountryDetails> countrydata1 = new ArrayList<CountryDetails>(0);
    //ArrayList<CountryDetails> countriesWithEmojiFlag = new ArrayList<CountryDetails>(0);

    ArrayList<String> countryEmoji1 = new ArrayList<String>(0);


    CountryListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkingService = ((myApp)getApplication()).getNetworkingService();
        jsonService =  ((myApp)getApplication()).getJsonService();

        networkingService.listener =this;
        networkingService.fetchCountryData();
        //countrytextview = findViewById(R.id.intro_textView);
        countryRecyclerview = findViewById(R.id.CountryRecyclerView);
        countryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CountryListAdapter(this, countrydata1);
        System.out.println("****************************THis is my data :" + countrydata.toString()+" ");
        adapter.listener = this;
        countryRecyclerview.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }


    @Override
    public void APINetworkListenerForCountryData(String jsonString) {
        // System.out.println(jsonString);
        Log.d("country details", jsonString);// not parsed yet.
        countrydata = jsonService.parseCountriesFromJsonAPIData(jsonString);
        Log.d("Printing my data", countrydata.get(0).getCountryName() + " " + countrydata.get(1).getCountryName()+ " " +countrydata.get(2).getCountryName());
        Log.d("Printing", countrydata.get(0).getCode() + " " +
                countrydata.get(0).getCountryName()+ " " +countrydata.get(0).getCapitalCity() +
                " " +countrydata.get(0).getPopulation() + " " +countrydata.get(0).getCountryCurrencyCode());

        System.out.println("****************************THis is my data :" + countrydata.toString()+" ");
        createdata1();
        createCountryEmoji();
        createCountriesWithFlag();
        System.out.println("****************************THis is my data :" + countrydata1.toString()+" ");
        adapter.allCountriesList = countrydata1;
        adapter.notifyDataSetChanged(); }


    @Override
    public void onCountrySelected(CountryDetails selectedCountry) {
        String countryCode = selectedCountry.getCode().toString().toLowerCase();
        Intent toQuizActivity = new Intent(this, QuizActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Selected country details",selectedCountry);
        System.out.println("I am sending: \n"+ selectedCountry.toString());
        bundle.putParcelableArrayList("listOfCountriesDetails",countrydata);
        System.out.println("I am sending: \n"+ countrydata.toString());
        toQuizActivity.putExtras(bundle);
        toQuizActivity.putExtra("Country flag",countryCode);
        startActivity(toQuizActivity);
        // System.out.println("My history details are: \n"+ selectedHistory.toString());
        Toast.makeText(this, "Country flag being sent", Toast.LENGTH_SHORT).show(); }

    private void createdata1(){
       for (int i=0;i<=countrydata.size()-1;i++){
           countrydata1.add(countrydata.get(i)); }
    }


    private void createCountriesWithFlag(){
        for (int i=0;i<=countrydata.size()-1;i++){
      // for (int i=0;i<countrydata1.size();i++){
        countrydata.get(i).setCountryName(countrydata.get(i).getCountryName() + "   "+countryEmoji1.get(i)); }
    }

    private void createCountryEmoji(){
        countryEmoji1.add(getResources().getString(R.string.ao_flag));
        countryEmoji1.add(getResources().getString(R.string.bf_flag));
        countryEmoji1.add(getResources().getString(R.string.bi_flag));
        countryEmoji1.add(getResources().getString(R.string.bj_flag));
        countryEmoji1.add(getResources().getString(R.string.bw_flag));
        countryEmoji1.add(getResources().getString(R.string.cd_flag));
        countryEmoji1.add(getResources().getString(R.string.cf_flag));
        countryEmoji1.add(getResources().getString(R.string.cg_flag));
        countryEmoji1.add(getResources().getString(R.string.ci_flag));
        countryEmoji1.add(getResources().getString(R.string.cm_flag));
        countryEmoji1.add(getResources().getString(R.string.cv_flag));
        countryEmoji1.add(getResources().getString(R.string.dj_flag));
        countryEmoji1.add(getResources().getString(R.string.dz_flag));
        countryEmoji1.add(getResources().getString(R.string.eg_flag));
        countryEmoji1.add(getResources().getString(R.string.eh_flag));
        countryEmoji1.add(getResources().getString(R.string.er_flag));
        countryEmoji1.add(getResources().getString(R.string.et_flag));
        countryEmoji1.add(getResources().getString(R.string.ga_flag));
        countryEmoji1.add(getResources().getString(R.string.gh_flag));
        countryEmoji1.add(getResources().getString(R.string.gm_flag));
        countryEmoji1.add(getResources().getString(R.string.gn_flag));
        countryEmoji1.add(getResources().getString(R.string.gq_flag));
        countryEmoji1.add(getResources().getString(R.string.gw_flag));
        countryEmoji1.add(getResources().getString(R.string.ke_flag));
        countryEmoji1.add(getResources().getString(R.string.km_flag));
        countryEmoji1.add(getResources().getString(R.string.lr_flag));
        countryEmoji1.add(getResources().getString(R.string.ls_flag));
        countryEmoji1.add(getResources().getString(R.string.ly_flag));
        countryEmoji1.add(getResources().getString(R.string.ma_flag));
        countryEmoji1.add(getResources().getString(R.string.mg_flag));
        countryEmoji1.add(getResources().getString(R.string.ml_flag));
        countryEmoji1.add(getResources().getString(R.string.mr_flag));
        countryEmoji1.add(getResources().getString(R.string.mu_flag));
        countryEmoji1.add(getResources().getString(R.string.mw_flag));
        countryEmoji1.add(getResources().getString(R.string.mz_flag));
        countryEmoji1.add(getResources().getString(R.string.na_flag));
        countryEmoji1.add(getResources().getString(R.string.ne_flag));
        countryEmoji1.add(getResources().getString(R.string.ng_flag));
        countryEmoji1.add(getResources().getString(R.string.re_flag));
        countryEmoji1.add(getResources().getString(R.string.rw_flag));
        countryEmoji1.add(getResources().getString(R.string.sc_flag));
        countryEmoji1.add(getResources().getString(R.string.sd_flag));
        countryEmoji1.add(getResources().getString(R.string.sh_flag));
        countryEmoji1.add(getResources().getString(R.string.sl_flag));
        countryEmoji1.add(getResources().getString(R.string.sn_flag));
        countryEmoji1.add(getResources().getString(R.string.so_flag));
        countryEmoji1.add(getResources().getString(R.string.ss_flag));
        countryEmoji1.add(getResources().getString(R.string.st_flag));
        countryEmoji1.add(getResources().getString(R.string.sz_flag));
        countryEmoji1.add(getResources().getString(R.string.td_flag));
        countryEmoji1.add(getResources().getString(R.string.tg_flag));
        countryEmoji1.add(getResources().getString(R.string.tn_flag));
        countryEmoji1.add(getResources().getString(R.string.tz_flag));
        countryEmoji1.add(getResources().getString(R.string.ug_flag));
        countryEmoji1.add(getResources().getString(R.string.yt_flag));
        countryEmoji1.add(getResources().getString(R.string.za_flag));
        countryEmoji1.add(getResources().getString(R.string.zm_flag));
        countryEmoji1.add(getResources().getString(R.string.zw_flag)); }


    private void toAttemptsReport(){
        Intent toAttemptsReport = new Intent(getApplicationContext(),AttemptsReportActivity.class);
        startActivity(toAttemptsReport); }
    @Override
    public void APINetworkingListerForImage(Bitmap image) { }
}



