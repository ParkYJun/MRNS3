package com.mrns.invest.view.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.invest.record.RnsNpSttusInqBf;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.InsertUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_invest_insert_etc)
public class InvestInsertNcpEtc extends AppCompatActivity {
    //! Extras
    private long exStdrspotSn;
    private boolean exBOnline;
    private int exIndexInsert;

    //! 뷰 커넥팅
    @ViewById(R.id.editbox_etc)
    EditText editboxEtc;
    @ViewById(R.id.editbox_location_detail)
    EditText editboxLocationDetail;
    @ViewById(R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(R.id.btn_complete)
    Button btnComplete;
    @ViewsById({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    List<ImageButton> arrayImageBtn;
    @ViewById(R.id.btnNavNext)
    ImageView btn_navNext;
    @ViewById(R.id.btnNavBack)
    ImageView btn_navBack;
    @ViewById(R.id.full_addr)
    TextView fullAddr;
    @ViewById(R.id.points_path)
    TextView points_path;
    //! 멤버변수
    private RnsNpSttusInqBf mItem;
    private RnsNpUnity item;
    private InsertUtil mInsertUtil;
    boolean isNext;
    @AfterViews
    protected void InitView() {
        isNext = false;
        //! Extra 및 멤버변수 초기화
        exStdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
        exBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        exIndexInsert = getIntent().getIntExtra(getString(R.string.key_index_insert), 0);
        mInsertUtil = new InsertUtil(getApplicationContext(), exStdrspotSn, exIndexInsert, exBOnline);
        mItem = RnsNpSttusInqBf.FindByNpuSn(exStdrspotSn);
        item = RnsNpUnity.FindByNpuSn(exStdrspotSn);
        points_path.setText("기준점 경로");
        //! 뷰 초기 설정(타이틀, 라디오버튼)

        if (mItem != null) {
            String pointKind = item.getPointsKndCd() != null ? DBUtil.getCmmnCode(getString(R.string.code_point_kind), item.getPointsKndCd()) : getString(R.string.empty);
            String pointNm = item.getPointsNm() != null ? item.getPointsNm() : getString(R.string.empty);
            textviewSubTitle.setText(getString(R.string.title_points, pointNm, pointKind));

            fullAddr.setText(item.getFullAddr());
            editboxLocationDetail.setText(mItem.getPointsPath());
            editboxEtc.setText(mItem.getPtclmatr());

            String[] array =  mItem.getProgression().split("/", -1);
            String arrTrue = "";
            for(int i = 0; i < array.length; i++){
                if(array[i].indexOf("1") == 0){
                    arrTrue += i;
                }
            }
/*            if (arrTrue.contains("1") && arrTrue.contains("2") && arrTrue.contains("4") && arrTrue.contains("5")) {
                btnComplete.setVisibility(View.VISIBLE);
            }*/
            if (mInsertUtil.setProcessBtns(arrayImageBtn, mItem.getProgression()) || arrTrue.contains("1") && arrTrue.contains("2") && arrTrue.contains("4") && arrTrue.contains("5") ) {
                btnComplete.setVisibility(View.VISIBLE);
            }
        }
    }

    //! @brief 액티비티 이동 전 저장
    //! @param 해당 기준점의 조사상태
    private void onSave(String inqSttus) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));
     /*   if(null == mItem){
            mItem = new RnsNpSttusInqBf();
        }*/
        mItem.setInqDe(simpleDateFormat.format(Calendar.getInstance().getTime()));
        mItem.setPtclmatr(editboxEtc.getText().toString());
        mItem.setPointsPath(editboxLocationDetail.getText().toString());
        mItem.setInqSttus(inqSttus);
        mItem.setProgression(mInsertUtil.changeProgression(mItem.getProgression()));
        mItem.save();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
    }

    //! @brief 기준점 상세조회 화면으로 이동
    @Click(R.id.layout_back_home)
    protected void onBackHome() {
        startActivity(mInsertUtil.getBackHomeIntentNcp());
    }

    //! @brief 현황조사 완료 버튼 이벤트 처리
    @Click(R.id.btn_complete)
    protected void onComplete() {
        onSave(getString(R.string.code_invest_insert_complete));
        startActivity(mInsertUtil.getResultIntent(true));
    }

    //! @brief 프로세스 버튼 이벤트 처리
    //! @param 클릭한 버튼
    @Click({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    protected void onProcessBtns(View view) {
        onSave(getString(R.string.code_invest_insert_ongoing));
        Intent moveIntent = mInsertUtil.getMoveIntentNcp(view.getId());

        startActivity(moveIntent);

        int moveIndex = moveIntent.getIntExtra(getString(R.string.key_index_insert), -1);
        int enterAnim = 0;
        int exitAnim = 0;

        if (exIndexInsert > moveIndex) {
            enterAnim = R.anim.slide_right_enter;
            exitAnim = R.anim.slide_right_exit;
        } else if (exIndexInsert < moveIndex) {
            enterAnim = R.anim.slide_left_enter;
            exitAnim = R.anim.slide_left_exit;
        }

        overridePendingTransition(enterAnim, exitAnim);
    }
    @Click(R.id.btnNavNext)
    public void onNext() {
        isNext = true;
        onSave("1");
        Intent intentTest = mInsertUtil.getMoveIntentNcp(R.id.imgbtn_kimg);
        startActivity(intentTest);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
    @Click(R.id.btnNavBack)
    public void onBack() {
        onBackPressed();
    }
}
