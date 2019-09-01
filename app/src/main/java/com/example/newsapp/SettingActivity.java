package com.example.newsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {
    private Switch switch_night;
    private Switch switch_data;
    private Button button_color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
                    final String items[] = {"我是Item一", "我是Item二", "我是Item三", "我是Item四"};
                    AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("单选列表对话框")//设置对话框的标题
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
}
