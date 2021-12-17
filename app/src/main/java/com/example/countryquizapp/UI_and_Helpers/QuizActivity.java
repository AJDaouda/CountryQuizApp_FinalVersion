package com.example.countryquizapp.UI_and_Helpers;

import static com.example.countryquizapp.Database.DatabaseManager.getDBInstance;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.countryquizapp.Database.AttemptDatabase;
import com.example.countryquizapp.Database.DatabaseManager;
import com.example.countryquizapp.Model.Attempt;
import com.example.countryquizapp.Model.CountryDetails;
import com.example.countryquizapp.Model.QuestionManager;
import com.example.countryquizapp.Model.QuizQuestion;
import com.example.countryquizapp.Model.myApp;
import com.example.countryquizapp.R;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements NetworkService.NetworkingListener {
    FragmentManager fm = getSupportFragmentManager();
    NetworkService networkingService;
    QuestionManager qManager;
    DatabaseManager dbManager;
    AttemptDatabase db;
    AlertDialog.Builder builder;
   // Intent toReport;

    //Instance variables
    int index=0;
    int randomNum1,randomNum2;
    int[] randomNumArray = new int[2];
    int progressMaxValue;
    int correctAnswers=0;
    int points=0;

    String countryCode;
    String countryName;
    String playerChoice;

    CountryDetails selectedCountry;
    ArrayList<CountryDetails> countriesDetails = new ArrayList<CountryDetails>(0);

    QuizQuestion countryCodeQuiz;
    QuizQuestion currencyCodeQuiz;
    QuizQuestion populationQuiz;
    QuizQuestion capitalCityQuiz;
    ArrayList<QuizQuestion> selectedCountryQuestionBank;

    Attempt currentAttempt;


    //Layout Widgets declaration
    ImageView flagImage;
    RadioButton ans_1,ans_2,ans_3;
    ProgressBar myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        networkingService = ((myApp)getApplication()).getNetworkingService();
        qManager = ((myApp)getApplication()).getqManager();
        dbManager =  ((myApp)getApplication()).getdbManager();
        db = dbManager.getDBInstance(this);


        flagImage = (ImageView) findViewById(R.id.quiz_flag_image);
        ans_1 = (RadioButton) findViewById(R.id.Answer_1);
        ans_2 = (RadioButton) findViewById(R.id.Answer_2);
        ans_3 = (RadioButton) findViewById(R.id.Answer_3);
        myProgress =  (ProgressBar) findViewById(R.id.pbar);
        builder = new AlertDialog.Builder(this);
        //myProgress.setMax(progressMaxValue);

        //Getting country code to fetch country flag
        Intent fromCountryListActivity = getIntent();
        if(!(fromCountryListActivity.getStringExtra("Country flag")==null)){
            countryCode = this.getIntent().getStringExtra("Country flag");
            System.out.println("My country code is: \n"+ countryCode);}
        else if(!(this.getIntent().getStringExtra("Country flag")==null)){
            countryCode = this.getIntent().getStringExtra("Country flag");
            System.out.println("My country code is: \n"+ countryCode);}

        //Fetching country flag
        networkingService.getCountryFlag(countryCode);
        networkingService.listener =this;

        //Getting "selected country details"
        if(!(fromCountryListActivity.getExtras().get("Selected country details")==null)){
            selectedCountry = (CountryDetails) this.getIntent().getExtras().get("Selected country details");
            countryName = selectedCountry.getCountryName().substring(0,selectedCountry.getCountryName().length()-6);
            System.out.println("My received country details are: \n"+ selectedCountry.toString());
        }
        else{
            selectedCountry = (CountryDetails) this.getIntent().getExtras().get("Selected country details");
            countryName = selectedCountry.getCountryName().substring(0,selectedCountry.getCountryName().length()-6);
            System.out.println("My received country details are: \n"+ selectedCountry.toString());}

        //Getting an array of countries details
        if(!(this.getIntent().getExtras().getParcelableArrayList("listOfCountriesDetails")==null)){
            countriesDetails = this.getIntent().getExtras().getParcelableArrayList("listOfCountriesDetails");
            System.out.println("My received arraylist is: \n"+ countriesDetails);}
        else if(!(this.getIntent().getExtras().getParcelableArrayList("ListOfProd")==null)){
            countriesDetails = this.getIntent().getExtras().getParcelableArrayList("listOfCountriesDetails");
            System.out.println("My received arraylist is: \n"+ countriesDetails);}

        selectedCountryQuestionBank = qManager.getQuestionBank();
        createQBankForSelectedCountry();
        progressMaxValue=qManager.getQuestionBank().size();
        myProgress.setMax(progressMaxValue);

        System.out.println("My quizBank is : \n"+ selectedCountryQuestionBank);

        if(savedInstanceState!= null){
            index = savedInstanceState.getInt("current");
            updateFragment(qManager.getQuestionBank().get(index).getQuestion());
            System.out.println("my list is: " + qManager.getQuestionBank().get(index).getQuestion());
        }
        else{
            index =0;
            updateFragment(qManager.getQuestionBank().get(index).getQuestion());
           // System.out.println("Something went wrong");
            updatePotentialAns();
        }


       /* String potentialAns1 =qManager.getQuestionBank().get(index).getPotentialAnswers()[0];
        String potentialAns2 =qManager.getQuestionBank().get(index).getPotentialAnswers()[1];
        String potentialAns3 =qManager.getQuestionBank().get(index).getPotentialAnswers()[2];
        ans_1.setText(potentialAns1);
        ans_2.setText(potentialAns2);
        ans_3.setText(potentialAns3);*/

    }

    public void updateFragment(String question) {
        fm.findFragmentById(R.id.quiz_frame_id);
        QuestionFragment qFragment = QuestionFragment.newInstance(question);
        fm.beginTransaction().replace(R.id.quiz_frame_id,qFragment).commit();}

    @Override
    public void APINetworkListenerForCountryData(String jsonString) { }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
        //Displaying country flag
        flagImage.setImageBitmap(image);}

    private int[] getPotentialAnsArray(){
        while((countriesDetails.indexOf(selectedCountry) != randomNum1)&&(countriesDetails.indexOf(selectedCountry) != randomNum2)){
            randomNum1 = (int)Math.floor(Math.random()*(countriesDetails.size()));
            randomNum2 = (int)Math.floor(Math.random()*(countriesDetails.size()));
            randomNumArray[0]=randomNum1;
            randomNumArray[1]=randomNum2;
            break; }
        return randomNumArray;}

    private void createQBankForSelectedCountry(){
        createCountryCodeQuiz();
        createCapitalCityQuiz();
        createCurrencyCodeQuiz();
        createPopulationQuiz();
        selectedCountryQuestionBank.add(countryCodeQuiz);
        selectedCountryQuestionBank.add(capitalCityQuiz);
        selectedCountryQuestionBank.add(currencyCodeQuiz);
        selectedCountryQuestionBank.add(populationQuiz);}

        private void createCountryCodeQuiz(){
            String countryCodeQ = "What is the country code of "+countryName+"?";
            String countryCodeCorrectAns = selectedCountry.getCode();
            String[] countryCodePotentialAns ;
            getPotentialAnsArray();
            if ((countriesDetails.get(randomNumArray[0]).getCode()!=selectedCountry.getCode())&&
                    (countriesDetails.get(randomNumArray[1]).getCode()!=selectedCountry.getCode())){
                countryCodePotentialAns= new String[]{selectedCountry.getCode(), countriesDetails.get(randomNumArray[0]).getCode(),
                        countriesDetails.get(randomNumArray[1]).getCode()};
                countryCodeQuiz = new QuizQuestion(countryCodeQ,countryCodeCorrectAns,countryCodePotentialAns);  }
        }

        private void createCurrencyCodeQuiz(){
            String currencyCodeQ = "What is the currency code of "+countryName+"?";
            String currencyCodeCorrectAns = selectedCountry.getCountryCurrencyCode();
            String[] currencyCodePotentialAns ;
            getPotentialAnsArray();
            while ((countriesDetails.get(randomNumArray[0]).getCountryCurrencyCode()!=selectedCountry.getCountryCurrencyCode())&&
                    (countriesDetails.get(randomNumArray[1]).getCountryCurrencyCode()!=selectedCountry.getCountryCurrencyCode())){
                currencyCodePotentialAns= new String[]{countriesDetails.get(randomNumArray[0]).getCountryCurrencyCode(),selectedCountry.getCountryCurrencyCode(),
                        countriesDetails.get(randomNumArray[1]).getCountryCurrencyCode()};
                currencyCodeQuiz = new QuizQuestion(currencyCodeQ,currencyCodeCorrectAns,currencyCodePotentialAns);
            break;}
            }

        private void createPopulationQuiz(){
            String populationQ = "What is the population number of "+countryName+"?";
            String populationCorrectAns = selectedCountry.getPopulation()+" people";
            String[] populationPotentialAns ;
            getPotentialAnsArray();
            if ((countriesDetails.get(randomNumArray[0]).getPopulation()!=selectedCountry.getPopulation())&&
                    (countriesDetails.get(randomNumArray[1]).getPopulation()!=selectedCountry.getPopulation())){
                populationPotentialAns= new String[]{countriesDetails.get(randomNumArray[0]).getPopulation() +" people",
                        countriesDetails.get(randomNumArray[1]).getPopulation()+" people",selectedCountry.getPopulation()+" people"};
                populationQuiz = new QuizQuestion(populationQ,populationCorrectAns,populationPotentialAns); }
            }

        private void createCapitalCityQuiz(){
            String capitalCityQ = "What is the capital city of "+countryName+"?";
            String capitalCityCorrectAns = selectedCountry.getCapitalCity();
            String[] capitalCityPotentialAns ;
            getPotentialAnsArray();
            if ((countriesDetails.get(randomNumArray[0]).getCapitalCity()!=selectedCountry.getCapitalCity())&&
                    (countriesDetails.get(randomNumArray[1]).getCapitalCity()!=selectedCountry.getCapitalCity())){
                capitalCityPotentialAns= new String[]{ countriesDetails.get(randomNumArray[0]).getCapitalCity(),selectedCountry.getCapitalCity(),
                        countriesDetails.get(randomNumArray[1]).getCapitalCity()};
                capitalCityQuiz = new QuizQuestion(capitalCityQ,capitalCityCorrectAns,capitalCityPotentialAns); }

            }

    private void updatePotentialAns(){
        String potentialAns1 =qManager.getQuestionBank().get(index).getPotentialAnswers()[0];
        String potentialAns2 =qManager.getQuestionBank().get(index).getPotentialAnswers()[1];
        String potentialAns3 =qManager.getQuestionBank().get(index).getPotentialAnswers()[2];
        ans_1.setText(potentialAns1);
        ans_2.setText(potentialAns2);
        ans_3.setText(potentialAns3); }

    private void resetRadioBtn() {
       ans_1.setChecked(false);
       ans_2.setChecked(false);
       ans_3.setChecked(false); }

    private void getAnswer(){
        //if (playerChoice != (qManager.getQuestionBank().get(index).getCorrectAnswer())){
        if (!playerChoice.equals(qManager.getQuestionBank().get(index).getCorrectAnswer())){
            System.out.println("The user's answer is: "+ playerChoice);
            System.out.println("The correct answer is: "+ qManager.getQuestionBank().get(index).getCorrectAnswer().toString());
            Toast.makeText(this,"Incorrect answer",Toast.LENGTH_SHORT).show();}
        else{
            System.out.println("The user's answer is: "+ playerChoice);
            System.out.println("The correct answer is: "+ qManager.getQuestionBank().get(index).getCorrectAnswer().toString());
            Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
            correctAnswers++;
            points++;}
    }

    public void onRadioButtonClicked(View view) {
        //playerChoice = String.valueOf((RadioButton)view.get)
        // Is the button now checked?
        playerChoice = ((RadioButton) view).getText().toString();
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        //showNextOption();
        switch(view.getId()) {
            case R.id.Answer_1:
            case R.id.Answer_2:
            case R.id.Answer_3:
                if(index==3 && checked){
                    getAnswer();
                    myProgress.setProgress(progressMaxValue);
                    showNextOption();
                    System.out.println("Your current index is: "+ index);
                    System.out.println("The number of correct answer is: "+ correctAnswers +
                            ", and you have accumulated "+points+ " points");
                }
                else if (index<3){
                   // if (checked){
                        System.out.println("Your current index is: "+ index);
                        //playerChoice = ((RadioButton) view).getText().toString();
                        getAnswer();
                        index++;
                        resetRadioBtn();
                        updateFragment(qManager.getQuestionBank().get(index).getQuestion());
                        myProgress.setProgress(index);
                        updatePotentialAns();
                    System.out.println("The number of correct answer is: "+ correctAnswers +
                            ", and you have accumulated "+points+ " points");
                       // }
                }
                else{
                    System.out.println("We are qt index>3 ");
                }
                break;}

    }


    private void showNextOption(){
        builder.setTitle(getResources().getString(R.string.nextStep_text));
        builder.setMessage("To save this attempt, click on Save"+
                        "\nTo ignore and take another quiz, click on Ignore");
        builder.setPositiveButton(getResources().getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentAttempt=new Attempt(countryName,correctAnswers,points);
                //db.getAttemptDAO().insertAttempt(currentAttempt);
                dbManager.insertNewAttempt(currentAttempt);
                index=0;
                toAttemptsReport();
                System.out.println("Your current attempt is: "+ currentAttempt);
                //Intent toReport = new Intent(this,AttemptsReportActivity.class);
                //startActivity(toReport);
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.btn_ignore), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"IGNORE clicked",Toast.LENGTH_SHORT).show();
                BackToMainActivity();
                index=0;
            }
        });
        AlertDialog alertDialog=builder.create();
        builder.show();

    }

    private void BackToMainActivity(){
        Intent backToMain = new Intent(QuizActivity.this,MainActivity.class);
        startActivity(backToMain);
        Toast.makeText(getApplicationContext(),"finished",Toast.LENGTH_SHORT).show();
    }

   private void toAttemptsReport(){
        Intent toAttemptsReport = new Intent(this,AttemptsReportActivity.class);
        startActivity(toAttemptsReport); }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current", index);
    }
}
