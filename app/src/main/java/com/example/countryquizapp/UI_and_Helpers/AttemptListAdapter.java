package com.example.countryquizapp.UI_and_Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.countryquizapp.Model.Attempt;
import com.example.countryquizapp.Model.CountryDetails;
import com.example.countryquizapp.R;

import java.util.ArrayList;

class AttemptReportAdapter extends BaseAdapter {
    ArrayList<Attempt> listOfAttempts;
    Context activity_Context;

    AttemptReportAdapter(ArrayList<Attempt> list, Context activity_context){
        listOfAttempts = list;
        activity_Context = activity_context;
    }

   /* public interface ListClickListener{
        void onAttemptClicked(Attempt clickedAttempt);}

    public ListClickListener listener;*/

    @Override
    public int getCount() {
        return listOfAttempts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // in each row I have one donation item form the array list;
    // 10 donations
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(activity_Context).inflate(R.layout.attempt_item,null);
        TextView attemptName = view.findViewById(R.id.attempt);
        TextView correctAnswer = view.findViewById(R.id.correctAns);
        TextView point = view.findViewById(R.id.point);
        attemptName.setText(listOfAttempts.get(i).getName()+"");
        correctAnswer.setText(listOfAttempts.get(i).getCorrectAnswer()+" correct answers");
        point.setText(listOfAttempts.get(i).getPoints()+" points");
        return view;
    }


}

/*public class AttemptListAdapter extends RecyclerView.Adapter<AttemptListAdapter.viewHolder> {

    Context aContext;
    List<Attempt> allAttemptList;

    public AttemptListAdapter(Context c, List<Attempt> list) {
        aContext = c;
        allAttemptList = list; }


    public interface ListClickListener{
        void onAttemptClicked(Attempt clickedAttempt);}

    public ListClickListener listener;
    // inner class
    // View Holder = Row in the table
    // static = able to access it from the class without creating object
    public static class viewHolder extends RecyclerView.ViewHolder{
        private final TextView aName;
        private final TextView aAns;
        private final TextView aPoint;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            aName=itemView.findViewById(R.id.attempt);
            aAns=itemView.findViewById(R.id.correctAns);
            aPoint=itemView.findViewById(R.id.point);
        }

        public TextView getaName() {return aName;}
        public TextView getaAns() {return aAns;}
        public TextView getaPoint() {return aPoint;}
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(aContext).inflate(R.layout.activity_attempts_report,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.getaName().setText(allAttemptList.get(position).getName());
        holder.getaAns().setText(allAttemptList.get(position).getCorrectAnswer());
        holder.getaPoint().setText(allAttemptList.get(position).getPoints());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAttemptClicked(allAttemptList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {return allAttemptList.size(); }
}*/