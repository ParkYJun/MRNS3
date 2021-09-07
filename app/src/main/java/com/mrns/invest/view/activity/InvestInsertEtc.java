package com.mrns.invest.view.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.record.RnsSttusInqBefore;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.InsertUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@EActivity(com.mrns.main.R.layout.activity_invest_insert_etc)
public class InvestInsertEtc extends AppCompatActivity {
    //! Extras
    private long exStdrspotSn;
    private boolean exBOnline;
    private int exIndexInsert;

    //! 뷰 커넥팅
    @ViewById(com.mrns.main.R.id.editbox_etc)
    EditText editboxEtc;
    @ViewById(com.mrns.main.R.id.editbox_location_detail)
    EditText editboxLocationDetail;
    @ViewById(com.mrns.main.R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(com.mrns.main.R.id.btn_complete)
    Button btnComplete;
    @ViewsById({com.mrns.main.R.id.imgbtn_mtr_info, com.mrns.main.R.id.imgbtn_mtr_sttus, com.mrns.main.R.id.imgbtn_rtk_able, com.mrns.main.R.id.imgbtn_etc, com.mrns.main.R.id.imgbtn_kimg, com.mrns.main.R.id.imgbtn_oimg, com.mrns.main.R.id.imgbtn_pimg, com.mrns.main.R.id.imgbtn_roughmap})
    List<ImageButton> arrayImageBtn;
    @ViewById(com.mrns.main.R.id.btnNavNext)
    ImageView btn_navNext;
    @ViewById(com.mrns.main.R.id.btnNavBack)
    ImageView btn_navBack;
    @ViewById(com.mrns.main.R.id.full_addr)
    TextView fullAddr;
    //! 멤버변수
    private RnsSttusInqBefore mItem;
    private RnsStdrspotUnity addr;
    private InsertUtil mInsertUtil;
    boolean isNext;
    @AfterViews
    protected void InitView() {
        isNext = false;
        //! Extra 및 멤버변수 초기화
        exStdrspotSn = getIntent().getLongExtra(getString(com.mrns.main.R.string.key_stdr_spot_sn), 0);
        exBOnline = getIntent().getBooleanExtra(getString(com.mrns.main.R.string.key_bonline), false);
        exIndexInsert = getIntent().getIntExtra(getString(com.mrns.main.R.string.key_index_insert), 0);
        mInsertUtil = new InsertUtil(getApplicationContext(), exStdrspotSn, exIndexInsert, exBOnline);
        mItem = RnsSttusInqBefore.FindByStdrspotSn(exStdrspotSn);
        addr = RnsStdrspotUnity.FindByStdrspotSn(exStdrspotSn);

        //! 뷰 초기 설정(타이틀, 라디오버튼)

        if (mItem != null) {
            String pointKind = mItem.getPointsKndCd() != null ? DBUtil.getCmmnCode(getString(com.mrns.main.R.string.code_point_kind), mItem.getPointsKndCd()) : getString(com.mrns.main.R.string.empty);
            String pointNm = mItem.getPointsNm() != null ? mItem.getPointsNm() : getString(com.mrns.main.R.string.empty);
            textviewSubTitle.setText(getString(com.mrns.main.R.string.title_points, pointNm, pointKind));
            fullAddr.setText(addr.getLocplcLnm());
            editboxLocationDetail.setText(mItem.getLocplcDetail());
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(com.mrns.main.R.string.simple_date_format));
        mItem.setInqDe(simpleDateFormat.format(Calendar.getInstance().getTime()));
        mItem.setPtclmatr(editboxEtc.getText().toString());
        mItem.setLocplcDetail(editboxLocationDetail.getText().toString());
        mItem.setInqSttus(inqSttus);
        mItem.setProgression(mInsertUtil.changeProgression(mItem.getProgression()));
        mItem.save();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(com.mrns.main.R.anim.slide_right_enter, com.mrns.main.R.anim.slide_right_exit);
    }

    //! @brief 기준점 상세조회 화면으로 이동
    @Click(com.mrns.main.R.id.layout_back_home)
    protected void onBackHome() {
        startActivity(mInsertUtil.getBackHomeIntent());
    }

    //! @brief 현황조사 완료 버튼 이벤트 처리
    @Click(com.mrns.main.R.id.btn_complete)
    protected void onComplete() {
        onSave(getString(com.mrns.main.R.string.code_invest_insert_complete));
        startActivity(mInsertUtil.getResultIntent(true));
    }

    //! @brief 프로세스 버튼 이벤트 처리
    //! @param 클릭한 버튼
    @Click({com.mrns.main.R.id.imgbtn_mtr_info, com.mrns.main.R.id.imgbtn_mtr_sttus, com.mrns.main.R.id.imgbtn_rtk_able, com.mrns.main.R.id.imgbtn_etc, com.mrns.main.R.id.imgbtn_kimg, com.mrns.main.R.id.imgbtn_oimg, com.mrns.main.R.id.imgbtn_pimg, com.mrns.main.R.id.imgbtn_roughmap})
    protected void onProcessBtns(View view) {
        onSave(getString(com.mrns.main.R.string.code_invest_insert_ongoing));
        Intent moveIntent = mInsertUtil.getMoveIntent(view.getId());

        startActivity(moveIntent);

        int moveIndex = moveIntent.getIntExtra(getString(com.mrns.main.R.string.key_index_insert), -1);
        int enterAnim = 0;
        int exitAnim = 0;

        if (exIndexInsert > moveIndex) {
            enterAnim = com.mrns.main.R.anim.slide_right_enter;
            exitAnim = com.mrns.main.R.anim.slide_right_exit;
        } else if (exIndexInsert < moveIndex) {
            enterAnim = com.mrns.main.R.anim.slide_left_enter;
            exitAnim = com.mrns.main.R.anim.slide_left_exit;
        }

        overridePendingTransition(enterAnim, exitAnim);
    }
    @Click(com.mrns.main.R.id.btnNavNext)
    public void onNext() {
        isNext = true;
        onSave("1");
        Intent intentTest = mInsertUtil.getMoveIntent(com.mrns.main.R.id.imgbtn_kimg);
        startActivity(intentTest);
        overridePendingTransition(com.mrns.main.R.anim.slide_left_enter, com.mrns.main.R.anim.slide_left_exit);
    }
    @Click(com.mrns.main.R.id.btnNavBack)
    public void onBack() {
        onBackPressed();
    }
}
