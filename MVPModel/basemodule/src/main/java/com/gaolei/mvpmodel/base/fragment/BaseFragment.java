package com.gaolei.mvpmodel.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gaolei.basemodule.R;
import com.gaolei.mvpmodel.base.utils.NetUtils;
import com.gaolei.mvpmodel.base.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;


public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private FrameLayout mLlContent;
    View subFragmentView;
    private RelativeLayout mLlLoading;
    private Button bt_error_refresh;
    public LinearLayout mErrorPageView;
    private Unbinder mBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_base, container, false);
        initBaseView(mParentView);
        addContentView(inflater);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            bundle = savedInstanceState;
        }
        initData(bundle);
        initView();
        return mParentView;
    }
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initView();
//        initData();
    }
//    protected abstract void initData();

    public abstract void initData(Bundle bundle);
    public abstract void initView();

    private void initBaseView(View view) {
        mLlContent = view.findViewById(R.id.base_fragment_content);
        mErrorPageView = view.findViewById(R.id.ll_base_error_content);
        bt_error_refresh = view.findViewById(R.id.bt_error_refresh);
        mLlLoading = view.findViewById(R.id.ll_loading);
//        if (!NetworkUtil.isNetworkAvailable(getActivity()))
//            showErrorPage(true);
        bt_error_refresh.setOnClickListener(this);
    }

    /**
     * 设置子布局layout
     */
    public abstract int setContentLayout();

    public abstract void reload();

    /**
     * 设置内容
     */
    public void addContentView(LayoutInflater inflater) {
        subFragmentView = inflater.inflate(setContentLayout(), null);
        mLlContent.addView(subFragmentView);
        mBinder = ButterKnife.bind(this, subFragmentView);

    }

    /**
     * 显示加载进度条
     *
     * @param isShow
     */
    public void setLoading(boolean isShow) {
        mLlLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    /**
     * 显示/隐藏 错误页面
     *
     * @param isShow
     */
    public void showErrorPage(boolean isShow) {
        mErrorPageView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_error_refresh) {
            if (NetUtils.isConnected())
                mErrorPageView.setVisibility(View.GONE);
            reload();
        }
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(int resColor) {
        StatusBarUtil.setWindowStatusBarColor(getActivity(), resColor, true);
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (mBinder != null) {
            mBinder.unbind();
        }
    }
}
