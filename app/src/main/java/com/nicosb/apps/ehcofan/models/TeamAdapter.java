package com.nicosb.apps.ehcofan.models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nico on 28.07.2016.
 */
public class TeamAdapter extends ArrayAdapter<Team> {
    private Activity activity;
    private ArrayList<Team> lTeam = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public TeamAdapter(Activity activity, int resource, ArrayList<Team> lTeam) {
        super(activity, resource, lTeam);
        try {
            this.activity = activity;
            this.lTeam = lTeam;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount(){
        return lTeam.size();
    }

    public Team getItem(Team position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder{
        public TextView txt_rank;
        public TextView txt_name;
        public TextView txt_goals;
        public TextView txt_points;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        final ViewHolder holder;
        try{
            if(convertView == null) {
                vi = inflater.inflate(R.layout.listitem_team, parent);
                holder = new ViewHolder();

                holder.txt_rank = (TextView) vi.findViewById(R.id.txt_rank);
                holder.txt_name = (TextView) vi.findViewById(R.id.txt_team_name);
                holder.txt_goals = (TextView) vi.findViewById(R.id.txt_goals);
                holder.txt_points = (TextView) vi.findViewById(R.id.txt_points);

                vi.setTag(holder);
            }else{
                holder = (ViewHolder)vi.getTag();
            }

            holder.txt_rank.setText(String.format(Locale.GERMANY, "%d.", position));
            holder.txt_name.setText(lTeam.get(position).getName());
            holder.txt_goals.setText(String.format(Locale.GERMANY, "%d:%d", lTeam.get(position).getGoals_for(), lTeam.get(position).getGoals_against()));
            holder.txt_points.setText(lTeam.get(position).getPoints());
        }catch (Exception e){
            e.printStackTrace();
        }
        return vi;
    }
}