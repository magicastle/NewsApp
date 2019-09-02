package com.example.newsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.DrawableRes;

public class SettingActivity extends BaseActivity {
    private Switch switch_night;
    private Switch switch_data;
    private Button button_color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        ImageView icon=new ImageView(this);
//        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
//        getSupportActionBar().setCustomView(icon);
        switch_night=findViewById(R.id.switch_night);
        switch_data=findViewById(R.id.switch_data);
        button_color=findViewById(R.id.button_color);

        if(switch_night != null) {
            switch_night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {

                    } else {

                    }
                }
            });
        }
        if(switch_data != null){
            switch_data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {

                    }
                    else
                    {

                    }
                }
            });
        }

        if(button_color!=null)
        {
            button_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String items[] = {"color1", "color2", "color3", "color4"};
                    AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("选择主题颜色")
                            .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(SettingActivity.this, items[which], Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
