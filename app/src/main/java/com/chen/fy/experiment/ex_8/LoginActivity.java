package com.chen.fy.experiment.ex_8;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.chen.fy.experiment.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etUsername;
    private ImageView ivPwdSwitch;
    private CheckBox cbRememberPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex8_login_layout);

        initView();

        //初始化登入状态
        initLoginType();
    }

    private void initView() {

        etPwd = findViewById(R.id.et_pwd_ex8);
        etUsername = findViewById(R.id.et_username_ex8);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch_ex8);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        Button btnLogin = findViewById(R.id.btn_login_ex8);

        ivPwdSwitch.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pwd_switch_ex8:     //改变密码明文状态
                bPwdSwitch = !bPwdSwitch;
                if (bPwdSwitch) {
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);   //显示密码
                } else {
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_off_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_CLASS_TEXT);     //隐藏密码
                    etPwd.setTypeface(Typeface.DEFAULT);    //设置字体样式
                }
                break;

            case R.id.btn_login_ex8:
                saveLoginInfo();
                break;
        }
    }

    /**
     * 保存登入信息
     */
    private void saveLoginInfo() {
        SharedPreferences.Editor editor = getSharedPreferences("sp_login", MODE_PRIVATE).edit();
        if (cbRememberPwd.isChecked()) {   //记住用户
            String username = etUsername.getText().toString();
            String pwd = etPwd.getText().toString();

            editor.putString(getResources().getString(R.string.userNameKey), username);
            editor.putString(getResources().getString(R.string.pwdKey), pwd);
            editor.putBoolean(getResources().getString(R.string.rememberKyd), true);
        } else {
            editor.remove(getResources().getString(R.string.userNameKey));
            editor.remove(getResources().getString(R.string.pwdKey));
            editor.remove(getResources().getString(R.string.rememberKyd));
        }
        editor.apply();
    }

    /**
     * 初始化登入状态
     */
    private void initLoginType() {
        SharedPreferences spLogin = getSharedPreferences("sp_login", MODE_PRIVATE);
        String username = spLogin.getString(getResources().getString(R.string.userNameKey), "");
        String pwd = spLogin.getString(getResources().getString(R.string.pwdKey), "");
        boolean remember = spLogin.getBoolean(getResources().getString(R.string.rememberKyd), false);

        if (!TextUtils.isEmpty(username)) {
            etUsername.setText(username);
        }
        if (!TextUtils.isEmpty(pwd)) {
            etPwd.setText(pwd);
        }
        cbRememberPwd.setChecked(remember);
    }
}
