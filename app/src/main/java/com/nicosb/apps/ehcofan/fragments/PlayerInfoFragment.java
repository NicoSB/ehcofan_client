package com.nicosb.apps.ehcofan.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Player;


/**
 * Created by Nico on 25.07.2016.
 */
public class PlayerInfoFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_playerinfo, container, false);

        Player player = getArguments().getParcelable("player");

        TextView txt_birthday = (TextView)view.findViewById(R.id.txt_birthday);
        TextView txt_weight = (TextView)view.findViewById(R.id.txt_weight);
        TextView txt_height = (TextView)view.findViewById(R.id.txt_height);
        TextView txt_contract = (TextView)view.findViewById(R.id.txt_contract);
        WebView web_ep = (WebView)view.findViewById(R.id.web_ep);

        txt_birthday.setText(player.getGermanBirthdate());
        txt_weight.setText(String.format("%s kg", String.valueOf(player.getWeight())));
        txt_height.setText(String.format("%s cm", String.valueOf(player.getHeight())));
        txt_contract.setText(player.getContract());

        getDialog().setTitle(player.getFullName());
        getDialog().setCanceledOnTouchOutside(true);

        if(player.getEp_id() == 0){
            web_ep.setVisibility(View.GONE);
        }
        else {
            web_ep.loadData("<iframe src=\"http://www.eliteprospects.com/iframe_player_stats_small.php?player=" + String.valueOf(player.getEp_id()) + "\" width=\"100%\" height=\"460\" scrolling=\"yes\" frameborder=\"0\" ></iframe>", "text/html", null);
        }
        return view;
    }
}
