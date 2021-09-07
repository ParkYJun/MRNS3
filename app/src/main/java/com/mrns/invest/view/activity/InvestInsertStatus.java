package com.mrns.invest.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrns.invest.record.CodeRecord;
import com.mrns.invest.record.RnsSttusInqBefore;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.InsertUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@EActivity(R.layout.activity_invest_insert_status)
public class InvestInsertStatus extends AppCompatActivity {
    //! Extras
    private long exStdrspotSn;
    private boolean exBOnline;
    private int exIndexInsert;

    //! 뷰 커넥팅
    @ViewById(R.id.editbox_etc)
    EditText editboxEtc;
    @ViewById(R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(R.id.spinner_mtr_sttus)
    Spinner spinnerMtrSttus;
    @ViewsById({R.id.radiobtn0, R.id.radiobtn1, R.id.radiobtn2, R.id.radiobtn3})
    List<RadioButton> arrayRadiobtn;
    @ViewById(R.id.btn_complete)
    Button btnComplete;
    @ViewById(R.id.layout_mtr_part)
    LinearLayout layoutMtrPart;
    @ViewsById({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    List<ImageButton> arrayImageBtn;
    @ViewById(R.id.btnNavNext)
    ImageView btn_navNext;
    @ViewById(R.id.btnNavBack)
    ImageView btn_navBack;

    //! 멤버변수
    private RnsSttusInqBefore mItem;
    private ArrayList<CodeRecord> mMtrCodeRecords;
    private String[] mArrayRadioCode;
    private InsertUtil mInsertUtil;
    private int mRadioIndex = -1;
    private boolean mBIsMtrSttus = true;
    private boolean mBIsComplete = false;
    boolean isNext;
    boolean pages;
    @AfterViews
    protected void InitView() {
        isNext = false;
        pages = false;
        //! Extra 및 멤버변수 초기화
        exStdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
        exBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        exIndexInsert = getIntent().getIntExtra(getString(R.string.key_index_insert), 0);
        mInsertUtil = new InsertUtil(getApplicationContext(), exStdrspotSn, exIndexInsert, exBOnline);
        mItem = RnsSttusInqBefore.FindByStdrspotSn(exStdrspotSn);
        mArrayRadioCode = getResources().getStringArray(R.array.radio_code_invest_insert);

        if (getResources().getInteger(R.integer.index_insert_rtk_able) == exIndexInsert) {
            mBIsMtrSttus = false;
            layoutMtrPart.setVisibility(View.GONE);

            for (RadioButton radioButton : arrayRadiobtn) {
                ViewGroup.LayoutParams layoutParams = radioButton.getLayoutParams();
                layoutParams.height = 300;
                radioButton.setLayoutParams(layoutParams);
            }
        }


        String[] array =  mItem.getProgression().split("/", -1);
        String arrTrue = "";
        for(int i = 0; i < array.length; i++){
            if(array[i].indexOf("1") == 0){
                arrTrue += i;
            }
        }

        if (mItem != null) {
            //! 뷰 초기 설정(타이틀, 라디오버튼)
            mBIsComplete = mInsertUtil.setProcessBtns(arrayImageBtn, mItem.getProgression());
            /*if (mBIsComplete) {
                btnComplete.setVisibility(View.VISIBLE);
            }*/
            if (mBIsComplete || arrTrue.contains("1") && arrTrue.contains("2") && arrTrue.contains("4") && arrTrue.contains("5")) {
                btnComplete.setVisibility(View.VISIBLE);
            }

            String pointKind = mItem.getPointsKndCd() != null ? DBUtil.getCmmnCode(getString(R.string.code_point_kind), mItem.getPointsKndCd()) : getString(R.string.empty);
            String pointNm = mItem.getPointsNm() != null ? mItem.getPointsNm() : getString(R.string.empty);

            String radioValueCode = mItem.getRtkPosblYn();
            String[] arrayRadiobtnText = getResources().getStringArray(R.array.radiobtn_rtk_able_text_array);

            if (mBIsMtrSttus) {
                radioValueCode = mItem.getMtrSttusCd();
                arrayRadiobtnText = getResources().getStringArray(R.array.radiobtn_mtr_sttus_text_array);
                mMtrCodeRecords = DBUtil.getCmmCodeRecord(getString(R.string.code_mtr_sttus));
                String[] arrayMtrCode = DBUtil.getCmmnCodeStringArray(getString(R.string.code_mtr_sttus));

                // 2019-06-20 kth Spinner 에 초기값 "선택하세요" 추가
////////////////////////////////////////////////////////////////////////////////////////////////
                ArrayList<String> strList = new ArrayList<>();
                strList.add("선택하세요.");
                for (String value : arrayMtrCode) {
                    strList.add(value);
                }
                arrayMtrCode = strList.toArray(arrayMtrCode);
////////////////////////////////////////////////////////////////////////////////////////////////

                ArrayAdapter<String> adapter = new ArrayAdapter<>(InvestInsertStatus.this, android.R.layout.simple_spinner_dropdown_item, arrayMtrCode);
                spinnerMtrSttus.setAdapter(adapter);

                String inqMtrCode = mItem.getInqMtrCd();

                if (null == inqMtrCode || inqMtrCode.equals(getString(R.string.empty))) {
                    // 2019-06-20 kth 초가값 설정하지 않음
//                    mItem.setInqMtrCd(mArrayRadioCode[0]);
                } else {
                    for (int i = 0; i < mMtrCodeRecords.size(); i++) {
                        if (inqMtrCode.equals(mMtrCodeRecords.get(i).getCode())) {
                            // 2019-06-20 kth Spinner 에 초기값 "선택하세요" 추가. 그래서 초기값 설정시 +1
                            spinnerMtrSttus.setSelection(i+1);
                            break;
                        }
                    }
                }
            }

            textviewSubTitle.setText(getString(R.string.title_points, pointNm, pointKind));

            if (null != radioValueCode) {
                for (int i = 0; i < mArrayRadioCode.length; i++) {
                    if (radioValueCode.equals(mArrayRadioCode[i])) {
                        mRadioIndex = i;
                        arrayRadiobtn.get(mRadioIndex).setChecked(true);
                       // break;
                    }else{
                        arrayRadiobtn.get(i).setChecked(false);
                      //  break;
                    }
                }
            }

            for (int i = 0; i < arrayRadiobtnText.length; i++) {
                arrayRadiobtn.get(i).setText(arrayRadiobtnText[i].toString());
            }

//            arrayRadiobtn.get(mRadioIndex).setChecked(true);

            if (getResources().getInteger(R.integer.radio_index_insert_etc) == mRadioIndex) {
                editboxEtc.setText(mItem.getMtrSttusEtc());
                editboxEtc.setVisibility(View.VISIBLE);
            } else if (mBIsMtrSttus && getResources().getInteger(R.integer.radio_index_insert_mtr_sttus_loss) == mRadioIndex) {
                //btnComplete.setVisibility(View.VISIBLE);
            }
        }
    }

    //! @brief 액티비티 이동 전 저장
    //! @param 해당 기준점의 조사상태
    private void onSave(String inqSttus) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));
        mItem.setInqDe(simpleDateFormat.format(Calendar.getInstance().getTime()));

        if (mRadioIndex != -1) {
            if (mBIsMtrSttus) {
                mItem.setMtrSttusCd(mArrayRadioCode[mRadioIndex]);
                mItem.setMtrSttusEtc(editboxEtc.getText().toString());
                int selectedIndex = spinnerMtrSttus.getSelectedItemPosition();
                // 2019-06-20 kth Spinner 에 초기값 "선택하세요" 추가.
////////////////////////////////////////////////////////////////////////////
                if (selectedIndex == 0) {
                    selectedIndex += 1;
                }
                mItem.setInqMtrCd(mMtrCodeRecords.get(selectedIndex-1).getCode());
////////////////////////////////////////////////////////////////////////////
            } else {
                mItem.setRtkPosblYn(mArrayRadioCode[mRadioIndex]);
                mItem.setRtkPosblEtc(editboxEtc.getText().toString());
            }

            mItem.setProgression(mInsertUtil.changeProgression(mItem.getProgression()));
            // mItem.setProgression(mInsertUtil.changeProgression("0/0/0/0/0/0"));
            mItem.setInqSttus(inqSttus);
            mItem.save();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
    }

    //! @brief 라디오버튼 클릭 이벤트
    //! @param 클릭된 버튼
    @Click({R.id.radiobtn0, R.id.radiobtn1, R.id.radiobtn2, R.id.radiobtn3})
    protected void onClick(View view) {
        int editboxVisible = View.GONE;
        int completeVisible = View.GONE;
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (view.getId()) {
            case R.id.radiobtn0:
                mRadioIndex = 0;
                arrayRadiobtn.get(0).setChecked(true);
                arrayRadiobtn.get(1).setChecked(false);
                arrayRadiobtn.get(2).setChecked(false);
                arrayRadiobtn.get(3).setChecked(false);
                editboxEtc.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(editboxEtc.getWindowToken(), 0);
                break;
            case R.id.radiobtn1:
                mRadioIndex = 1;
                arrayRadiobtn.get(0).setChecked(false);
                arrayRadiobtn.get(1).setChecked(true);
                arrayRadiobtn.get(2).setChecked(false);
                arrayRadiobtn.get(3).setChecked(false);
                editboxEtc.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(editboxEtc.getWindowToken(), 0);
                completeVisible = View.VISIBLE;
                break;
            case R.id.radiobtn2:
                mRadioIndex = 2;
                arrayRadiobtn.get(0).setChecked(false);
                arrayRadiobtn.get(1).setChecked(false);
                arrayRadiobtn.get(2).setChecked(true);
                arrayRadiobtn.get(3).setChecked(false);
                editboxEtc.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(editboxEtc.getWindowToken(), 0);
                break;
            case R.id.radiobtn3:
                mRadioIndex = 3;
                arrayRadiobtn.get(0).setChecked(false);
                arrayRadiobtn.get(1).setChecked(false);
                arrayRadiobtn.get(2).setChecked(false);
                arrayRadiobtn.get(3).setChecked(true);

                editboxEtc.setVisibility(View.VISIBLE);
                editboxEtc.requestFocus();
                editboxEtc.setCursorVisible(true);

                imm.showSoftInput(editboxEtc, InputMethodManager.SHOW_FORCED);

                break;
        }

        if (mBIsMtrSttus && !mBIsComplete) {
            //btnComplete.setVisibility(completeVisible);
        }
    }

    //! @brief 기준점 상세조회 화면으로 이동
    @Click(R.id.layout_back_home)
    protected void onBackHome() {
        startActivity(mInsertUtil.getBackHomeIntent());
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
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editboxEtc.getWindowToken(), 0);
        onSave(getString(R.string.code_invest_insert_ongoing));
        Intent moveIntent = mInsertUtil.getMoveIntent(view.getId());

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
    public void onNext(View view) {

        // 2019-06-20 kth Spinner 에 초기값 "선택하세요" 추가.
        // 값을 선택하시 않았을시 예외처리 추가
////////////////////////////////////////////////////////////////
        if(mBIsMtrSttus) {
            if (spinnerMtrSttus != null) {
                int selectedIndex = spinnerMtrSttus.getSelectedItemPosition();
                if (selectedIndex == 0) {
                    Toast.makeText(InvestInsertStatus.this, "표지재질을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
////////////////////////////////////////////////////////////////

        isNext = true;
        onSave("1");

        if(mBIsMtrSttus){
            Intent intentTest = mInsertUtil.getMoveIntent(R.id.imgbtn_rtk_able);
            startActivity(intentTest);
           // layoutMtrPart.setVisibility(View.GONE);
        }else{
            Intent intentTest = mInsertUtil.getMoveIntent(R.id.imgbtn_etc);
            startActivity(intentTest);
        }
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
    @Click(R.id.btnNavBack)
    public void onBack() {
        if(!mBIsMtrSttus){
            Intent intentTest = mInsertUtil.getMoveIntent(R.id.imgbtn_mtr_sttus);
            startActivity(intentTest);
            layoutMtrPart.setVisibility(View.GONE);
        }else{
            onBackPressed();
        }
    }
}
