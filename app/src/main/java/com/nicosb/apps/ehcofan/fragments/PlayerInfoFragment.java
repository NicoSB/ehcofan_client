package com.nicosb.apps.ehcofan.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Player;


/**
 * Created by Nico on 25.07.2016.
 */
public class PlayerInfoFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_playerinfo, container, false);

        Player player = getArguments().getParcelable("player");

        TextView txt_birthday = (TextView)view.findViewById(R.id.txt_birthday);
        TextView txt_weight = (TextView)view.findViewById(R.id.txt_weight);
        TextView txt_height = (TextView)view.findViewById(R.id.txt_height);
        TextView txt_contract = (TextView)view.findViewById(R.id.txt_contract);
        TextView txt_name = (TextView)view.findViewById(R.id.player_name);
        CircularImageView circularImageView = (CircularImageView)view.findViewById(R.id.player_picture);

        txt_birthday.setText(player.getGermanBirthdate());
        txt_weight.setText(String.format("%s kg", String.valueOf(player.getWeight())));
        txt_height.setText(String.format("%s cm", String.valueOf(player.getHeight())));
        txt_contract.setText(player.getContract());
        txt_name.setText(player.getFullName());
        circularImageView.setImageBitmap(player.getPlayerImage());
        getDialog().setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().show();
        getDialog().getWindow().setAttributes(lp);
        return view;
    }
}
