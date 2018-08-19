package com.gaolei.mvpmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gaolei.mvpmodel.activity.BaseActivity;
import com.gaolei.mvpmodel.fragment.BaseMvpFragment;
import com.gaolei.mvpmodel.fragment.KnowledgeFragment;
import com.gaolei.mvpmodel.fragment.HomeFragment;
import com.gaolei.mvpmodel.fragment.NavigationFragment;
import com.gaolei.mvpmodel.fragment.ProjectFragment;
import com.gaolei.mvpmodel.utils.PermissionUtil;
import com.gaolei.mvpmodel.utils.Utils;
import com.gaolei.mvpmodel.view.BottomNavigationViewHelper;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private ArrayList<BaseMvpFragment> mFragments;
    private int mLastFgIndex = 0;
    TextView title;
    //    BottomNavigationView bottomNavigationView;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle bundle) {


        // 取消BottomNavigation大于3个时，动画
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        mFragments = new ArrayList<BaseMvpFragment>();
        title = findViewById(R.id.title);
        mFragments.add(new HomeFragment());
        mFragments.add(new KnowledgeFragment());
        mFragments.add(new NavigationFragment());
        mFragments.add(new ProjectFragment());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果versionCode>=23 则需要动态授权
            boolean isAllGranted= PermissionUtil.checkPermission(this);
            if(isAllGranted)
                switchFragment(0);
        } else {
            switchFragment(0);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_main_pager:
                        title.setText(getString(R.string.home_pager));
                        switchFragment(0);

                        break;
                    case R.id.tab_knowledge_hierarchy:
                        title.setText(getString(R.string.knowledge_hierarchy));
                        switchFragment(1);

                        break;
                    case R.id.tab_navigation:
                        title.setText(getString(R.string.navigation));
                        switchFragment(2);

                        break;
                    case R.id.tab_project:
                        title.setText(getString(R.string.project));
                        switchFragment(3);
                        break;
                }
                return true;
            }
        });

    }

    /**
     * 切换fragment
     *
     * @param position 要显示的fragment的下标
     */
    private void switchFragment(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFg = mFragments.get(position);
        Fragment lastFg = mFragments.get(mLastFgIndex);
        mLastFgIndex = position;
        ft.hide(lastFg);
        if (!targetFg.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(targetFg).commit();
            ft.add(R.id.fragment_group, targetFg);
        }
        ft.show(targetFg);
        ft.commitAllowingStateLoss();
    }



    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtil.MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则显示HomeFragment
                switchFragment(0);
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
//                PermissionUtil.getAppDetailSettingIntent(this);
//                Utils.showToast("App正常使用需要授权" );
                PermissionUtil.showRequestPermissionDialog(this);


            }
        }
    }

    public void toast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }


}
