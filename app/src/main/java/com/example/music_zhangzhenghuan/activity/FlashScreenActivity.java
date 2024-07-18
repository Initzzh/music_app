package com.example.music_zhangzhenghuan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_zhangzhenghuan.R;

public class FlashScreenActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String  FIRST_LAUNCH = "firstLAUNCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        boolean isFirstLaunch = isFirstLaunch();
        if(isFirstLaunch){
            showDialog();
        }else{
            Intent IndexIntent = new Intent(getApplicationContext(),IndexActivity.class);
            startActivity(IndexIntent, null);
            finishActivity(0);
        }
    }


    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View diaglogView = inflater.inflate(R.layout.dialog_statement, null);
//        textContent.setText("欢迎使用音乐社区，我们将严格遵守相关法律和隐私政策保护您的个人隐私，请您阅读并同意《用户协议》与《隐私政策》。");
        builder.setView(diaglogView);

        TextView textContent = diaglogView.findViewById(R.id.text_content);

        String text = "欢迎使用音乐社区，我们将严格遵守相关法律和隐私政策保护您的个人隐私，请您阅读并同意《用户协议》与《隐私政策》。";

        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(colorSpan1, 41, 47, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan1, 48, 54,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                Toast.makeText(widget.getContext() , "用户协议", Toast.LENGTH_SHORT).show();
            }
        }, 41,47, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(widget.getContext() , "隐私政策", Toast.LENGTH_SHORT).show();
            }
        }, 48, 54, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textContent.setMovementMethod(LinkMovementMethod.getInstance());
        textContent.setText(spannableString);

        TextView disAgreeText = diaglogView.findViewById(R.id.text_disagree);
        disAgreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        Button btn = diaglogView.findViewById(R.id.button_agree);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(FIRST_LAUNCH, false);
                editor.apply();

                Intent IndexIntent = new Intent(getApplicationContext(),IndexActivity.class);
                startActivity(IndexIntent, null);
                // 取消对话框
//                if (getCurrentFocus() != null) {
//                    ((AlertDialog) getCurrentFocus()).dismiss();
//                }

            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean isFirstLaunch(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true);
    }


}