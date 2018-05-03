package com.gaolei.mvpmodel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gaolei.mvpmodel.R;
import com.gaolei.mvpmodel.activity.ArticleDetailActivity;
import com.gaolei.mvpmodel.adapter.DividerItemDecoration;
import com.gaolei.mvpmodel.adapter.ProjectAdapter;
import com.gaolei.mvpmodel.mmodel.FeedArticleData;
import com.gaolei.mvpmodel.mmodel.ProjectListData;
import com.gaolei.mvpmodel.mpresenter.HomePresenter;
import com.gaolei.mvpmodel.mview.ProjectListView;

import java.util.List;

import butterknife.BindView;


/**
 * @author quchao
 * @date 2018/2/11
 */

public class HomeFragment extends BaseMvpFragment<ProjectListView, HomePresenter> implements ProjectListView {

    @BindView(R.id.project_recyclerview)
    RecyclerView project_recyclerview;
    ProjectAdapter projectAdapter;

    @Override
    public void finishCreateView(Bundle bundle) {
        mPresenter.getProjectInfo(1, 294);
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void reload() {
        mPresenter.getProjectInfo(1, 294);
        ;
    }

    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter();
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    @Override
    public void requstProjectList(ProjectListData listData) {
       final List<FeedArticleData> articleDataList=listData.data.getDatas();
        projectAdapter = new ProjectAdapter(getActivity(), articleDataList);

        project_recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        project_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        project_recyclerview.setAdapter(projectAdapter);
        projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url",articleDataList.get(position).getLink());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
