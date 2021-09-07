package com.mrns.downdata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.common.NetcheckMoniter;
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.sqllite.DBHelper;
import com.mrns.sqllite.DatabaseManager;
import com.mrns.updata.UploadActivity;
import com.mrns.utils.BaseTabActivity;
import com.neokree.materialtabs.MaterialTab;
import com.neokree.materialtabs.MaterialTabHost;
import com.neokree.materialtabs.MaterialTabListener;

public class DownActivity extends BaseTabActivity {

    private boolean bOnline;
    ViewPager downPager;
    ViewPagerDownAdapter pagerDownAdapter;
    MaterialTabHost dTabhost;
    ImageView btn_back;
    TextView down_title;
    Intent intent;
    ProgressDialog dialog;

    SQLiteDatabase db;
    DBHelper helper;

    String USER_TABLE_NAME = "RNS_STTUS_INQ_BEFORE";
    Intent movePage;

    private NetcheckMoniter networkMoniter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);

        Intent intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        dTabhost = (MaterialTabHost) this.findViewById(R.id.tabhost);
        downPager = (ViewPager) this.findViewById(R.id.pager);

        //ViewPage create
        pagerDownAdapter = new ViewPagerDownAdapter(getSupportFragmentManager());
        downPager.setAdapter(pagerDownAdapter);
        downPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dTabhost.setSelectedNavigationItem(position);

            }
        });

        int tabColor = getResources().getColor(R.color.downTab);
        dTabhost.setTextColor(Color.WHITE);
        dTabhost.setPrimaryColor(tabColor);

        for (int i = 0; i < pagerDownAdapter.getCount(); i++) {
            MaterialTab tab = dTabhost.newTab();
            tab.setText(pagerDownAdapter.getPageTitle(i));
            tab.setTabListener(new downTabListener());

            dTabhost.addTab(tab);
        }

        dTabhost.setSelectedNavigationItem(0);

        //뒤로가기 버튼
        btn_back = (ImageView) findViewById(R.id.btn_nav_back);
        down_title = (TextView) findViewById(R.id.titleTextView);

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_nav_back:
                    case R.id.titleTextView:
                        btn_back.setImageResource(R.drawable.nav_btn_back_p);
                        movePage = new Intent(DownActivity.this, MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                        break;
                }//end switch
            }//end Onclick
        };

        btn_back.setOnClickListener(listener);
        down_title.setOnClickListener(listener);

        ckFinishInvest();
    }//end onCreate

    /**
     * 탭을 선택했을 때 처리할 리스너 정의
     */
    private class downTabListener implements MaterialTabListener {

        public downTabListener() {
//            Toast.makeText(DownActivity.this, "downTabListener : " + dTabhost.getX(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTabSelected(MaterialTab tab) {
            downPager.setCurrentItem(tab.getPosition());

        }

        @Override
        public void onTabReselected(MaterialTab tab) {
//            Toast.makeText(DownActivity.this, "onTabSelected" + tab.getPosition(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onTabUnselected(MaterialTab tab) {

        }

    }

    /**
     * 뷰페이저 어댑터를 정의합니다.
     */
    private class ViewPagerDownAdapter extends FragmentStatePagerAdapter {

        public ViewPagerDownAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int index) {
            Fragment frag = null;

            if (index == 0) {
                frag = new DownPointFragment();

            } else if (index == 1) {
                //frag = new DownMapFragment();
                frag = new DownNcpPiFragment();
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
                    //return "현황 조사자료";
                    return "지적 기준점";
                case 1:
                    //return "지도자료";
                    return "국가 기준점";
                default:
                    return null;
            }

        }

    }//end ViewPagerDownAdapter


    public void ckFinishInvest() {
        helper = new DBHelper(DownActivity.this, getString(R.string.db_full_name), null, 1);
        DatabaseManager.initializeInstance(helper);
        db = DatabaseManager.getInstance().openDatabase();

        String sql = "SELECT count(sttus_bf_sn) from " + USER_TABLE_NAME + " WHERE INQ_STTUS = 2";
        Cursor result = db.rawQuery(sql, null);
        result.moveToFirst();
        int count = result.getInt(0);
        result.close();

        if (count > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("현황자료 미전송 알림!")
                    .setMessage(getString(R.string.downCkData))
//                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            movePage = new Intent(DownActivity.this, UploadActivity.class);
                            movePage.putExtra(getString(R.string.key_bonline), bOnline);
                            startActivity(movePage);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }//end if
    }//end ckFinishInvest


}
