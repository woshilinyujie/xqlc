package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.IconFontTextView;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.AppVersionUtils;
import com.rongxun.xqlc.Util.UpdateApk;


/**
 * Created by oguig on 2017/7/10.
 */

public class MoreActivity extends MyBaseActivity implements View.OnClickListener {

    public static final int PERMISSION_S = 110;//存储卡权限
    private final String TAG = "更多";
    private RelativeLayout activity_more_feedback_row, activity_more_activity_about_us_row, activity_more_activity_update_row;
    private TextView more_update_current_version;
    private IconFontTextView more_toolbar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_more);
        initView();
        initdata();

    }

    private void initView() {
        more_toolbar_back=(IconFontTextView)findViewById(R.id.more_toolbar_back);
        activity_more_feedback_row = (RelativeLayout) findViewById(R.id.activity_more_feedback_row);
        activity_more_activity_about_us_row = (RelativeLayout) findViewById(R.id.activity_more_activity_about_us_row);
        activity_more_activity_update_row = (RelativeLayout) findViewById(R.id.activity_more_activity_update_row);
        more_update_current_version=(TextView)findViewById(R.id.more_update_current_version);
        more_update_current_version.setText(AppVersionUtils.getVersionName(MoreActivity.this));
    }

    private void initdata() {
        more_toolbar_back.setOnClickListener(this);
        activity_more_feedback_row.setOnClickListener(this);
        activity_more_activity_about_us_row.setOnClickListener(this);
        activity_more_activity_update_row.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_toolbar_back:
                finish();
                break;
            case R.id.activity_more_feedback_row:
                Intent intent = new Intent(MoreActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_more_activity_about_us_row:
                Intent h5 = new Intent(MoreActivity.this, H5JSActivity.class);
                h5.putExtra("web_url", "https://m.hzjcb.com/html/aboutus/aboutus.html");
                startActivity(h5);
                break;
            case R.id.activity_more_activity_update_row:
                // TODO: 2017/8/8 0008 版本更新
                //更新完成，不在弹出
                UpdateApk.getInstance().startUpdate(this, AppConstants.UPADTE_APK, UpdateApk.MORE,null);
                break;
        }

    }

    /**
     * @param requestCode
     * @param permissions  权限数组
     * @param grantResults 授权结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_S:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意
                    // TODO: 2017/8/8 0008 直接下载
                    UpdateApk.getInstance().update();
                } else {
                    // 权限被用户拒绝了
                    Toast.makeText(this, "没权限，无法下载，请到应用管理打开权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
    }


}
