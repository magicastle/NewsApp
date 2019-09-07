package com.java.wangyan;

import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
