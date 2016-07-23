package com.omenacle.bamzu;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by omegareloaded on 7/24/16.
 */
public class SignInActivity extends AppCompatActivity {

    EditText mEmailAddress, mPassword;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
       changeStatusBarColor();
        getSupportActionBar().hide();

        mEmailAddress = (EditText)findViewById(R.id.editText_email);
        mPassword = (EditText)findViewById(R.id.editText_password);

        tintView(mEmailAddress,R.color.colorPrimaryLight);
        tintView(mPassword,R.color.colorPrimaryLight);




    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Set backgroundTint to {@link View} across all targeting platform level.
     * @param view the {@link View} to tint.
     * @param color color used to tint.
     */
    public static void tintView(View view, int color) {
        final Drawable d = view.getBackground();
        final Drawable nd = d.getConstantState().newDrawable();
        nd.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_IN));
        view.setBackground(nd);
    }
}
