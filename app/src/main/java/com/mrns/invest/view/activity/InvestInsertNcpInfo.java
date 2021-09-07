package com.mrns.invest.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.invest.record.CodeRecordList;
import com.mrns.invest.record.RnsNpSttusInqBf;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.record.UserInfoRecord;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.InsertUtil;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_invest_insert_info)
public class InvestInsertNcpInfo extends AppCompatActivity {
    //! Extras
    private long exStdrspotSn;
    private boolean exBOnline;
    private int exIndexInsert;

    //! 뷰 커넥팅
    @ViewById(R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(R.id.btn_complete)
    Button btnComplete;
    @ViewsById({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    List<ImageButton> arrayImageBtn;

    //대표조사자
    @ViewById(R.id.spinner_represent_inq_head_office)
    Spinner spinnerRepresentInqHeadOffice;
    @ViewById(R.id.spinner_represent_inq_branch)
    Spinner spinnerRepresentInqBranch;
    @ViewById(R.id.editbox_represent_inq_name)
    EditText editboxRepresentInqName;
    @ViewById(R.id.spinner_represent_inq_position)
    Spinner spinnerRepresentInqPosition;
    @ViewById(R.id.editbox_represent_inq_id)
    EditText editboxRepresentInqId;

    //실제조사자
    @ViewById(R.id.spinner_real_inq_head_office)
    Spinner spinnerRealInqHeadOffice;
    @ViewById(R.id.spinner_real_inq_branch)
    Spinner spinnerRealInqBranch;
    @ViewById(R.id.editbox_real_inq_name)
    EditText editboxRealInqName;
    @ViewById(R.id.spinner_real_inq_position)
    Spinner spinnerRealInqPosition;
    @ViewById(R.id.editbox_real_inq_id)
    EditText editboxRealInqId;

    @ViewById(R.id.btnNavNext)
    ImageView btn_navNext;
    @ViewById(R.id.btnNavBack)
    ImageView btn_navBack;

    //! 멤버변수
    private RnsNpSttusInqBf mItemBf;
    private InsertUtil mInsertUtil;
    private boolean mBIsComplete = false;
    private CodeRecordList mHeadOfficeInfoList;
    private CodeRecordList mPositionInfoList;
    private CodeRecordList mRepresentBranchInfoList;
    private CodeRecordList mRealBranchInfoList;
    RnsNpUnity mItemUnity;
    SharedPreferences mRepresentInqSession;
    SharedPreferences mRealInqSession;
    private String mProgression;
    private boolean mIsRepresent;
    private String mBranchCode;

    @AfterViews
    protected void InitView() {
        //! Extra 및 멤버변수 초기화
        exStdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
        exBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        exIndexInsert = getIntent().getIntExtra(getString(R.string.key_index_insert), 0);
        mInsertUtil = new InsertUtil(getApplicationContext(), exStdrspotSn, exIndexInsert, exBOnline);
        mItemBf = RnsNpSttusInqBf.FindByNpuSn(exStdrspotSn);

        setTitle(RnsNpUnity.FindByNpuSn(exStdrspotSn));
        String empty = getString(R.string.empty);

        //앱 전반적인 대표조사자 정보 get
        mRepresentInqSession = getSharedPreferences(getString(R.string.key_represent_inq_session), MODE_PRIVATE);
        //앱 전반적인 실제조사자 정보 get
        mRealInqSession = getSharedPreferences(getString(R.string.key_real_inq_session), MODE_PRIVATE);

        mProgression = "0/0/0/0/0/0/0/0";

        if (0 == mRepresentInqSession.getAll().size()) {
            //로그인 정보를 이용해 사용자 정보 get & set
            SharedPreferences prefSession = getApplicationContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);

            UserInfoRecord userInfoRecord = new UserInfoRecord(
                    prefSession.getString(getString(R.string.key_inq_user_id), empty),
                    prefSession.getString(getString(R.string.key_inq_user_name), empty),
                    prefSession.getString(getString(R.string.key_inq_branch_code), empty),
                    prefSession.getString(getString(R.string.key_inq_position_code), empty)
            );

            setSession(mRepresentInqSession, userInfoRecord);
            setSession(mRealInqSession, userInfoRecord);

        } else {
            if (mItemBf != null) {
                mProgression = mItemBf.getProgression();
            }
        }

        //본부 정보 get and set
        ArrayAdapter<String> adapter;
        mHeadOfficeInfoList = DBUtil.getDepartmentInfoList(null);
        adapter = new ArrayAdapter<>(InvestInsertNcpInfo.this, android.R.layout.simple_spinner_dropdown_item, mHeadOfficeInfoList.getCodeValueList());
        spinnerRepresentInqHeadOffice.setAdapter(adapter);
        spinnerRealInqHeadOffice.setAdapter(adapter);

        //직위 정보 get and set
        mPositionInfoList = DBUtil.getCommonCodeList("150");
        adapter = new ArrayAdapter<>(InvestInsertNcpInfo.this, android.R.layout.simple_spinner_dropdown_item, mPositionInfoList.getCodeValueList());
        spinnerRepresentInqPosition.setAdapter(adapter);
        spinnerRealInqPosition.setAdapter(adapter);

        //직위 set
        int index = 0;
        SharedPreferences sessionArray [] = {mRepresentInqSession, mRealInqSession};
        Spinner spinnerArray[] = {spinnerRepresentInqPosition, spinnerRealInqPosition};
        String positionCode;

        for (int i = 0; i < sessionArray.length; i++) {
            positionCode = sessionArray[i].getString(getString(R.string.key_inq_position_code), empty);
            index = getPositionIndex(positionCode);
            spinnerArray[i].setSelection(index);
        }

        //지사 정보 set
        spinnerRepresentInqHeadOffice.setSelected(false);
        spinnerRealInqHeadOffice.setSelected(false);
        setBranchList(true, mRepresentInqSession.getString(getString(R.string.key_inq_branch_code), empty));
        setBranchList(false, mRealInqSession.getString(getString(R.string.key_inq_branch_code), empty));

        //유저 정보 set
        editboxRepresentInqName.setText(mRepresentInqSession.getString(getString(R.string.key_inq_user_name), empty));
        editboxRepresentInqId.setText(mRepresentInqSession.getString(getString(R.string.key_inq_user_id), empty));
        editboxRealInqName.setText(mRealInqSession.getString(getString(R.string.key_inq_user_name), empty));
        editboxRealInqId.setText(mRealInqSession.getString(getString(R.string.key_inq_user_id), empty));

        //리스너 set
        spinnerRepresentInqHeadOffice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setBranchList(true, null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerRealInqHeadOffice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setBranchList(false, null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        editboxRepresentInqId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                setUserName(true, s.toString());
            }
        });

        editboxRealInqId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                setUserName(false, s.toString());
            }
        });

        String[] array =  mProgression.split("/", -1);
        String arrTrue = "";
        for(int i = 0; i < array.length; i++){
            if(array[i].indexOf("1") == 0){
                arrTrue += i;
            }
        }

        mBIsComplete = mInsertUtil.setProcessBtns(arrayImageBtn, mProgression);

        if (mBIsComplete || arrTrue.contains("1") && arrTrue.contains("2") && arrTrue.contains("4") && arrTrue.contains("5") ) {
            btnComplete.setVisibility(View.VISIBLE);
        }
    }

    private int getPositionIndex(String positionCode) {
        int index = 0;

        if (!positionCode.equals(getString(R.string.empty))) {
            for (int j = 0; j < mPositionInfoList.getCodeList().size(); j++) {
                if (positionCode.equals(mPositionInfoList.getCodeList().get(j))) {
                    index = j;
                    break;
                }
            }
        }

        return index;
    }

    //! @brief 본부 콤보박스 변경에 따라 지사 목록 변경
    //! @param isRepresent true : 대표조사자, false : 실제조사자
    //! @param branchName : null이거나 지사명
    private void setBranchList(boolean isRepresent, String branchCode) {
        Spinner upperSpinner;
        Spinner targetSpinner;
        CodeRecordList codeRecordList;
        String headOfficeCode;

        if (isRepresent) {
            upperSpinner = spinnerRepresentInqHeadOffice;
            targetSpinner = spinnerRepresentInqBranch;
        } else {
            upperSpinner = spinnerRealInqHeadOffice;
            targetSpinner = spinnerRealInqBranch;
        }

        if (null == branchCode) {
            headOfficeCode = mHeadOfficeInfoList.getCodeList().get(upperSpinner.getSelectedItemPosition());
        } else {
            headOfficeCode = DBUtil.getHeadOfficeCode(branchCode);
        }

        codeRecordList = DBUtil.getDepartmentInfoList(headOfficeCode);
        ArrayAdapter adapter = new ArrayAdapter<>(InvestInsertNcpInfo.this, android.R.layout.simple_spinner_dropdown_item, codeRecordList.getCodeValueList());
        targetSpinner.setAdapter(adapter);

        if (isRepresent) {
            mRepresentBranchInfoList = codeRecordList;
        } else {
            mRealBranchInfoList = codeRecordList;
        }

        if (mBranchCode != null) {
            for (int i = 0; i < codeRecordList.getCodeList().size(); i++) {
                if (codeRecordList.getCodeList().get(i).equals(mBranchCode)) {
                    targetSpinner.setSelection(i);
                    break;
                }
            }

            mBranchCode = null;
        }

        //스피너 item select
        if (branchCode != null) {
            for (int i = 0; i < mHeadOfficeInfoList.getCodeList().size(); i++) {
                if (mHeadOfficeInfoList.getCodeList().get(i).equals(codeRecordList.getAdditionalcodeList().get(0))) {
                    upperSpinner.setSelection(i, true);
                    break;
                }
            }

            mBranchCode = branchCode;

            for (int i = 0; i < codeRecordList.getCodeList().size(); i++) {
                if (codeRecordList.getCodeList().get(i).equals(branchCode)) {
                    targetSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    //! @brief 액티비티 이동 전 저장
    //! @param 해당 기준점의 조사상태
    private boolean onSave(String inqSttus) {
        String empty = getString(R.string.empty);

        String representInqUserName = editboxRepresentInqName.getText().toString();
        String representInqUserId = editboxRepresentInqId.getText().toString();
        if (representInqUserName.equals(empty) || representInqUserId.equals(empty)) {
            Toast.makeText(InvestInsertNcpInfo.this, getString(R.string.message_error_empty_name, "대표"), Toast.LENGTH_SHORT).show();
            return false;
        }

        String realInqUserName = editboxRealInqName.getText().toString();
        String realInqUserId = editboxRealInqId.getText().toString();
        if (realInqUserName.equals(empty) || realInqUserId.equals(empty)) {
            Toast.makeText(InvestInsertNcpInfo.this, getString(R.string.message_error_empty_name, "실제"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (null == mItemBf) {
            mItemBf = new RnsNpSttusInqBf();
            mItemBf.setNpuSn(exStdrspotSn);
            mItemBf.setProgression(mProgression);
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());

        mItemBf.setInqDe(date.toString());

        String branchCode;
        String branchName;
        String positionCode;

        //대표조사자 저장
        branchCode = mRepresentBranchInfoList.getCodeList().get(spinnerRepresentInqBranch.getSelectedItemPosition());
        branchName = spinnerRepresentInqBranch.getSelectedItem().toString();
        positionCode = mPositionInfoList.getCodeList().get(spinnerRepresentInqPosition.getSelectedItemPosition());

        mItemBf.setInqPsitn(branchName);
        mItemBf.setInqNm(representInqUserName);
        mItemBf.setInqOfcps(positionCode);
        mItemBf.setInqId(representInqUserId);

        UserInfoRecord userInfoRecord = new UserInfoRecord(
                representInqUserId,
                representInqUserName,
                branchCode,
                positionCode
        );

        setSession(mRepresentInqSession, userInfoRecord);
        //실제조사자 저장
        branchCode = mRealBranchInfoList.getCodeList().get(spinnerRealInqBranch.getSelectedItemPosition());
        branchName = spinnerRealInqBranch.getSelectedItem().toString();
        positionCode = mPositionInfoList.getCodeList().get(spinnerRealInqPosition.getSelectedItemPosition());

        mItemBf.setRlInqPsitn(branchName);
        mItemBf.setRlInqNm(realInqUserName);
        mItemBf.setRlInqClsf(positionCode);
        mItemBf.setRlInqId(realInqUserId);

        userInfoRecord = new UserInfoRecord(
                realInqUserId,
                realInqUserName,
                branchCode,
                positionCode
        );

        setSession(mRealInqSession, userInfoRecord);
        mItemBf.setInqSttus(inqSttus);
        mItemBf.setProgression(mInsertUtil.changeProgression(mProgression));
        mItemBf.save();

        return true;
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
        if (onSave(getString(R.string.code_invest_insert_complete))) {
            startActivity(mInsertUtil.getResultIntent(true));
        }
    }

    //! @brief 프로세스 버튼 이벤트 처리
    //! @param 클릭한 버튼
    @Click({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    protected void onProcessBtns(View view) {
        if (onSave(getString(R.string.code_invest_insert_ongoing))) {
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
    }
    @Click(R.id.btnNavBack)
    public void onBack() {
        onBackPressed();
    }
    @Click(R.id.btnNavNext)
    public void onNext() {
        if (onSave("1")) {
            Intent moveIntent = mInsertUtil.getMoveIntentNcp(R.id.imgbtn_mtr_sttus);
            startActivity(moveIntent);
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        }
    }

    void setTitle(RnsNpUnity item) {
        if(item != null) {
            this.mItemUnity = item;
            String pointNm = item.getPointsNm() != null ? item.getPointsNm() : "";
            String pointknd = item.getPointsKndCd() != null ? DBUtil.getCmmnCode("100", item.getPointsKndCd()) : "";
            String title = String.format("%s(%s)", pointNm, pointknd);
            textviewSubTitle.setText(title);
        }
    }

    private void setUserName(boolean isRepresent, String str) {
        int length = str.length();
        int limitLength = 6;
        EditText editboxName;
        EditText editboxId;
        mIsRepresent = isRepresent;

        if (isRepresent) {
            editboxName = editboxRepresentInqName;
            editboxId = editboxRepresentInqId;
        } else {
            editboxName = editboxRealInqName;
            editboxId = editboxRealInqId;
        }

        if (length > limitLength) {
            str = str.substring(0, limitLength);
            editboxId.setText(str);
            //editboxId.clearFocus();
            length = limitLength;
        }

        if (length == limitLength) {
            editboxName.setText(getString(R.string.empty));
            final String userId = str;
            String url = getString(R.string.server_ip) + getString(R.string.url_getUserInfo);
            com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            Request request = new StringRequest(Request.Method.POST, url, successListener, failListener)
            {
                @Override
                public Map<String, String> getParams() throws AuthFailureError
                {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(getString(R.string.key_inq_user_id), userId);
                    return map;
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            queue.add(request);
        }
    }

    private Response.Listener<String> successListener = new Response.Listener<String>()
    {
        @Override
        public void onResponse(String reponse)
        {
        UserInfoRecord userInfo = JsonPaserUtil.getUserInfoJsonPaser(reponse);

        if (null == userInfo) {
            Toast.makeText(InvestInsertNcpInfo.this, getString(R.string.message_invalidate_user_id), Toast.LENGTH_SHORT).show();
        } else {
            //사용자명
            EditText editbox;
            Spinner spinner;

            if (mIsRepresent) {
                editbox = editboxRepresentInqName;
                spinner = spinnerRepresentInqPosition;
            } else {
                editbox = editboxRealInqName;
                spinner = spinnerRealInqPosition;
            }

            editbox.setText(userInfo.getUserName());

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editbox.getWindowToken(), 0);

            //본부 및 지사 설정
            setBranchList(mIsRepresent, userInfo.getBranchCode());
            //직위 설정
            String positionCode = userInfo.getPositionCode();

            if (!positionCode.equals(getString(R.string.empty))) {
                int index = getPositionIndex(positionCode);
                spinner.setSelection(index);
            }
        }
        }
    };

    private Response.ErrorListener failListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
            Toast.makeText(InvestInsertNcpInfo.this, getString(R.string.message_error_server_trans), Toast.LENGTH_SHORT).show();
        }
    };

    private void setSession(SharedPreferences session, UserInfoRecord userInfoRecord) {
        if (session != null && userInfoRecord != null) {
            SharedPreferences.Editor editor = session.edit();

            editor.putString(getString(R.string.key_inq_user_id), userInfoRecord.getUserId());
            editor.putString(getString(R.string.key_inq_user_name), userInfoRecord.getUserName());
            editor.putString(getString(R.string.key_inq_branch_code), userInfoRecord.getBranchCode());
            editor.putString(getString(R.string.key_inq_position_code), userInfoRecord.getPositionCode());
            editor.apply();
        }
    }
}