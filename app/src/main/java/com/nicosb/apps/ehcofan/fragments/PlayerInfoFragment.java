package com.nicosb.apps.ehcofan.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
        TextView txt_games = (TextView)view.findViewById(R.id.txt_player_games);
        TextView txt_goals = (TextView)view.findViewById(R.id.txt_player_goals);
        TextView txt_assists = (TextView)view.findViewById(R.id.txt_player_assists);
        TextView txt_pim = (TextView)view.findViewById(R.id.txt_player_pim);
        CircularImageView circularImageView = (CircularImageView)view.findViewById(R.id.player_picture);

        assert player != null;
        txt_birthday.setText(player.getGermanBirthdate());
        txt_weight.setText(String.format("%s kg", String.valueOf(player.getWeight())));
        txt_height.setText(String.format("%s cm", String.valueOf(player.getHeight())));
        txt_contract.setText(player.getContract());
        txt_name.setText(player.getFullName());
        txt_games.setText(String.valueOf(player.getGames()));
        txt_goals.setText(String.valueOf(player.getGoals()));
        txt_assists.setText(String.valueOf(player.getAssists()));
        txt_pim.setText(String.valueOf(player.getPim()));
        circularImageView.setImageBitmap(player.getPlayerImage());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getDialog().setCanceledOnTouchOutside(true);
        getDialog().show();
        getDialog().getWindow().setAttributes(lp);

        return view;
    }
}
