package com.nicosb.apps.ehcofan.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Player;

/**
 * Created by Nico on 23.07.2016.
 */
public class PlayerView extends LinearLayout{
    private Player player;

    public PlayerView(Context context, Player player) {
        super(context);
        this.player = player;
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_player, this);
        TextView txt_player_name = (TextView)findViewById(R.id.txt_player_name);
        TextView txt_player_number = (TextView)findViewById(R.id.txt_player_number);
        TextView txt_player_position = (TextView)findViewById(R.id.txt_player_position);
        CircularImageView img_player_image = (CircularImageView)findViewById(R.id.img_player_image);

        txt_player_name.setText(player.getFullName());
        txt_player_number.setText(String.valueOf(player.getNumber()));
        txt_player_position.setText(player.getPosition());
        img_player_image.setImageBitmap(player.getPlayerImage());
     }
}
