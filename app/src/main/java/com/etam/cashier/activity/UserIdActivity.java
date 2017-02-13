package com.etam.cashier.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVInstallation;
import com.etam.cashier.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserIdActivity extends AppCompatActivity {

    @BindView(R.id.et_user_id)
    EditText etUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_submit_user_id)
    public void onClick() {
        if (!TextUtils.isEmpty(etUserId.getText().toString().trim())) {
            AVInstallation.getCurrentInstallation().put("userId",etUserId.getText().toString().trim() );
            AVInstallation.getCurrentInstallation().saveInBackground();

            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString("userId",etUserId.getText().toString().trim()).apply();
            setResult(RESULT_OK);
            onBackPressed();
        }else {
            Toast.makeText(this,"userId不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
