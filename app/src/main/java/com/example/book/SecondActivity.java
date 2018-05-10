package com.example.book;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    //三个Tab对应的布局
    private LinearLayout mTabTransfer;
    private LinearLayout mTabSearch;
    private LinearLayout mTabInfo;

    //三个Tab对应的ImageButton
    private ImageButton mImgTransfer;
    private ImageButton mImgSearch;
    private ImageButton mImgInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_second);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
    }


    private void initDatas() {
        mFragments = new ArrayList<>();
        //将三个Fragment加入集合中
        mFragments.add(new TransferFragment());
        mFragments.add(new SearchFragment());
        mFragments.add(new InfoFragment());


        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                //resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        //先将四个ImageButton置为灰色


        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.id_tab_transfer:
                selectTab(0);
                break;
            case R.id.id_tab_search:
                selectTab(1);
                break;
            case R.id.id_tab_info:
                selectTab(2);
                break;

        }
    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色

        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    private void initEvents() {
        //设置三个Tab的点击事件
        mTabTransfer.setOnClickListener(this);
        mTabSearch.setOnClickListener(this);
        mTabInfo.setOnClickListener(this);

    }

    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabTransfer = (LinearLayout) findViewById(R.id.id_tab_transfer);
        mTabSearch = (LinearLayout) findViewById(R.id.id_tab_search);
        mTabInfo = (LinearLayout) findViewById(R.id.id_tab_info);

        mImgTransfer = (ImageButton) findViewById(R.id.id_tab_transfer_img);
        mImgSearch = (ImageButton) findViewById(R.id.id_tab_search_img);
        mImgInfo = (ImageButton) findViewById(R.id.id_tab_info_img);


    }


}
