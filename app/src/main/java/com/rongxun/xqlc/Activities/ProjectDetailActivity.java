package com.rongxun.xqlc.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.liaoinstan.springview.widget.SpringView;
import com.rongxun.xqlc.Adapters.MaterialShowAdapter;
import com.rongxun.xqlc.Adapters.ProjectDetailAdapter;
import com.rongxun.xqlc.Application.CustomApplication;
import com.rongxun.xqlc.Beans.Borrow.MaterialShowBean;
import com.rongxun.xqlc.Beans.Borrow.ProjectDetailBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.UI.LoadingDialog;
import com.rongxun.xqlc.UI.SpringHeader;
import com.rongxun.xqlc.Util.AppConstants;
import com.rongxun.xqlc.Util.GsonUtils;
import com.rongxun.xqlc.okhttp.OkHttpUtils;
import com.rongxun.xqlc.okhttp.callback.StringCallback;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目详情
 */
public class ProjectDetailActivity extends AppCompatActivity {

    private RelativeLayout relBack;//返回
    private RelativeLayout relNetError;//网络错误
    private RecyclerView rvMaterialShow;//材料公示
    private int markId;
    private LoadingDialog loadingDialog;
    private ListView lv;
    private List<List<String>> data;//数据源
    private ArrayList<MaterialShowBean> materialData;
    private ProjectDetailAdapter projectDetailAdapter;
    private MaterialShowAdapter materialShowAdapter;
    private View footer;//底部-->材料公示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addActivity(this);
        setContentView(R.layout.activity_project_detail);

        markId = getIntent().getIntExtra("id", 0);

        initView();
        initData();
        listener();
    }

    private void listener() {

        // TODO: 2017/7/10 0010
        relBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {

        //项目详情数据
        requestProjectData();
    }


    private void requestProjectData() {


        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.showLoading();

        SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String token = preferences.getString("loginToken", "");

        OkHttpUtils
                .post()
                .url(AppConstants.PROJECT_DETAIL + "/" + markId)
                .addParams("token", token)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        // TODO: 2017/8/7 0007 网络错误

                        setNetErrorBg();
                        //结束dialog
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //结束dialog
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismissLoading();
                        }

                        if (response != null) {

                            ProjectDetailBean detailBean = GsonUtils.GsonToBean(response, ProjectDetailBean.class);
                            //整理数据
                            if (detailBean != null) {

                                switch (detailBean.getRcd()) {
                                    case "R0001":
                                        maksData(detailBean);
                                        //唤醒数据
                                        setNetErrorBg();
                                        projectDetailAdapter.notifyDataSetChanged();
                                        materialShowAdapter.notifyDataSetChanged();
                                        break;
                                    case "E0001":
                                        //抢登
                                        Intent login = new Intent(ProjectDetailActivity.this, LoginActivity.class);
                                        startActivity(login);
                                        break;
                                    case "R0003":
                                        //
                                        Toast.makeText(ProjectDetailActivity.this, "账号被锁定", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        }
                    }
                });
    }

    /**
     * 整理数据
     *
     * @param detailBean
     */
    private void maksData(ProjectDetailBean detailBean) {

        //债儿人信息
        data.add(detailBean.getDebtorInfoArr());
        //项目描述
        data.add(detailBean.getContentArr());
        //资金用途
        data.add(detailBean.getUseReasonArr());
        //还款来源
        data.add(detailBean.getPaymentSourceArr());

        try {
            if (detailBean.getBorImage() != null && !detailBean.getBorImage().equals("")) {
                JSONArray array = new JSONArray(detailBean.getBorImage());
                for (int i = 0, len = array.length(); i < len; i++) {
                    JSONObject object = array.getJSONObject(i);
                    String name = object.getString("name");
                    String url = object.getString("url");
                    materialData.add(new MaterialShowBean(name, url));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        /////////////////////////////////
        // TODO: 2017/8/7 0007 初始化弹性控件  测试
        initSpring();

        relNetError = (RelativeLayout) findViewById(R.id.project_detail_rel_rate_rel_net_error);
        relBack = (RelativeLayout) findViewById(R.id.project_detail_rel_back);
        lv = (ListView) findViewById(R.id.project_detail_lv);
        //加载底部
        initRecyclerView();
        //初始化lv
        data = new ArrayList<>();
        //
        projectDetailAdapter = new ProjectDetailAdapter(this, data);
        lv.addFooterView(footer);
        lv.setAdapter(projectDetailAdapter);

    }

    private void initSpring() {

        SpringView springView = (SpringView) findViewById(R.id.project_detail_spring);
        springView.setGive(SpringView.Give.NONE);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadmore() {
            }
        });
        springView.setHeader(new SpringHeader(this));
        springView.setFooter(new SpringHeader(this));
    }

    private void initRecyclerView() {

        materialData = new ArrayList<>();

        footer = LayoutInflater.from(this).inflate(R.layout.project_detail_bottom, null);
        rvMaterialShow = (RecyclerView) footer.findViewById(R.id.material_show_rv);
        //设置布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMaterialShow.setLayoutManager(manager);
        //添加分割线
        rvMaterialShow.addItemDecoration(
                new VerticalDividerItemDecoration.Builder(this)
                        .color(Color.parseColor("#ffffff"))
                        .size(80)
                        .build());
        //创建适配器
        materialShowAdapter = new MaterialShowAdapter(materialData, this);
        rvMaterialShow.setAdapter(materialShowAdapter);
    }

    /**
     * 设置网络错误的背景
     */
    private void setNetErrorBg() {

        if (data.isEmpty()) {
            relNetError.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
        } else {

            relNetError.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.removeActivity(this);
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
