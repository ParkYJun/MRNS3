package com.mrns.updata;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.common.NetcheckMoniter;
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.utils.BaseTabActivity;
import com.neokree.materialtabs.MaterialTab;
import com.neokree.materialtabs.MaterialTabHost;
import com.neokree.materialtabs.MaterialTabListener;

public class UploadActivity extends BaseTabActivity {

    private boolean bOnline;
    ViewPager uploadPager;
    ViewPagerUploadAdapter pagerUploadAdapter;
    ViewPagerUploadNcpAdapter pagerUploadNcpAdapter;
    MaterialTabHost uTabhost;
    ImageView btn_back;
    TextView upload_title;
    private Intent movePage;
    private NetcheckMoniter networkMoniter = null;
    boolean isStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        isStd = getApplicationContext().getSharedPreferences(getString(R.string.classCheckPref), MODE_PRIVATE).getBoolean(getString(R.string.classCheckKey), true);
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        Intent intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);

        uTabhost = (MaterialTabHost) this.findViewById(R.id.tabhost);
        uploadPager = (ViewPager) this.findViewById(R.id.pager);

        if (isStd) {
            //ViewPage create 지적기준점
            pagerUploadAdapter = new ViewPagerUploadAdapter(getSupportFragmentManager());
            uploadPager.setAdapter(pagerUploadAdapter);
            uploadPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    uTabhost.setSelectedNavigationItem(position);
                }
            });
            int tabColor = getResources().getColor(R.color.uploadTab);
            uTabhost.setTextColor(Color.WHITE);
            uTabhost.setPrimaryColor(tabColor);

            for(int i=0; i<pagerUploadAdapter.getCount(); i++) {
                MaterialTab tab = uTabhost.newTab();
                tab.setText(pagerUploadAdapter.getPageTitle(i));
                tab.setTabListener(new uploadTabListener());

                uTabhost.addTab(tab);
            }

            uTabhost.setSelectedNavigationItem(0);
        } else {
            //국가기준점
            pagerUploadNcpAdapter = new ViewPagerUploadNcpAdapter(getSupportFragmentManager());
            uploadPager.setAdapter(pagerUploadNcpAdapter);
            uploadPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    uTabhost.setSelectedNavigationItem(position);
                }
            });
            int tabColor = getResources().getColor(R.color.uploadTab);
            uTabhost.setTextColor(Color.WHITE);
            uTabhost.setPrimaryColor(tabColor);

            for(int i=0; i<pagerUploadNcpAdapter.getCount(); i++) {
                MaterialTab tab = uTabhost.newTab();
                tab.setText(pagerUploadNcpAdapter.getPageTitle(i));
                tab.setTabListener(new uploadTabListener());

                uTabhost.addTab(tab);
            }

            uTabhost.setSelectedNavigationItem(0);
        }

        //뒤로가기 버튼
        btn_back = (ImageView) findViewById(R.id.btn_nav_back);
        upload_title = (TextView) findViewById(R.id.titleTextView);

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_nav_back :
                    case R.id.titleTextView :
                        btn_back.setImageResource(R.drawable.nav_btn_back_p);


                        Log.i("done", "upload================================"+bOnline);
                        movePage = new Intent(UploadActivity.this, MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                        break;
                }//end switch
            }//end Onclick
        };

        btn_back.setOnClickListener(listener);
        upload_title.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                movePage = new Intent(UploadActivity.this, MainActivity.class);
                Log.i("done", "onKeyDown================================"+bOnline);
                movePage.putExtra(getString(R.string.key_bonline), bOnline);
                startActivity(movePage);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 탭을 선택했을 때 처리할 리스너 정의
     */
    private class uploadTabListener implements MaterialTabListener {
        public uploadTabListener() {

        }

        @Override
        public void onTabSelected(MaterialTab tab) {
            uploadPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabReselected(MaterialTab tab) {

        }

        @Override
        public void onTabUnselected(MaterialTab tab) {

        }

    }

    /**
     * 뷰페이저 어댑터를 정의합니다.
     */
    private class ViewPagerUploadAdapter extends FragmentStatePagerAdapter {

        public ViewPagerUploadAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int index) {
            Fragment frag = null;

            if (index == 0) {
                frag = new UploadWorkFragment();

            } else if (index == 1) {
                frag = new UploadFinishFragment();
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "올리기";
                case 1:
                    return "전송 완료";
                default:
                    return null;
            }

        }

    }//end ViewPagerDownAdapter

    /**
     * 뷰페이저 어댑터를 정의합니다.
     */
    private class ViewPagerUploadNcpAdapter extends FragmentStatePagerAdapter {

        public ViewPagerUploadNcpAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int index) {
            Fragment frag = null;

            if (index == 0) {
                frag = new UploadWorkNcpFragment();

            } else if (index == 1) {
                frag = new UploadFinishNcpFragment();
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "올리기";
                case 1:
                    return "전송 완료";
                default:
                    return null;
            }

        }

    }
}
