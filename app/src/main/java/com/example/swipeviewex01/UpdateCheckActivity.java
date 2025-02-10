package com.example.swipeviewex01;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class UpdateCheckActivity extends AppCompatActivity {

    protected SharedPreferencesAdapter spa;
    protected ThemeAdapter ta;

//    int REQUEST_CODE = 366;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spa = SharedPreferencesAdapter.getInstance();
        String theme = getIntent().getStringExtra("theme");
//        theme = spa.getItem("theme");
        ta = new ThemeAdapter();

        //테마
        ta.ActivityThemeSelector(this, theme);

        setContentView(R.layout.activity_updatecheck);

        LinearLayout title = findViewById(R.id.updatecheck_title);
        TextView ud_v = findViewById(R.id.ud_version);
        TextView os_s = findViewById(R.id.ud_opensource);

        ta.ActivityThemeSelector(this, theme, title);


        os_s.setOnClickListener(v -> {



            startActivity(new Intent(this, OssLicensesMenuActivity.class));
            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이센스");

        });


    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        spa.setItem("pwrock","0");
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

//    private void checkAppUpdate() {
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) { // 업데이트 있음
//                requestUpdate(appUpdateInfo);
//            } else {
//                showSplash();
//            }
//        });
//    }
}
