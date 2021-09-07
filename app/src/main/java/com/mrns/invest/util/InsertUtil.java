package com.mrns.invest.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.widget.ImageButton;

import com.mrns.invest.view.activity.InvestDetailActivity;
import com.mrns.invest.view.activity.InvestDetailActivity_;
import com.mrns.invest.view.activity.InvestDetailNcpActivity;
import com.mrns.invest.view.activity.InvestDetailNcpActivity_;
import com.mrns.invest.view.activity.InvestInsertEtc;
import com.mrns.invest.view.activity.InvestInsertEtc_;
import com.mrns.invest.view.activity.InvestInsertImages;
import com.mrns.invest.view.activity.InvestInsertImages_;
import com.mrns.invest.view.activity.InvestInsertInfo;
import com.mrns.invest.view.activity.InvestInsertInfo_;
import com.mrns.invest.view.activity.InvestInsertNcpEtc;
import com.mrns.invest.view.activity.InvestInsertNcpEtc_;
import com.mrns.invest.view.activity.InvestInsertNcpImages;
import com.mrns.invest.view.activity.InvestInsertNcpImages_;
import com.mrns.invest.view.activity.InvestInsertNcpInfo;
import com.mrns.invest.view.activity.InvestInsertNcpInfo_;
import com.mrns.invest.view.activity.InvestInsertNcpStatus;
import com.mrns.invest.view.activity.InvestInsertNcpStatus_;
import com.mrns.invest.view.activity.InvestInsertStatus;
import com.mrns.invest.view.activity.InvestResultActivity;
import com.mrns.invest.view.activity.InvestInsertStatus_;
import com.mrns.invest.view.activity.InvestResultActivity_;
import com.mrns.main.R;

import java.util.List;

public class InsertUtil {
    private Context mContext;
    private long mStdrspotSn;
    private int mParentsIndex;
    private boolean mBOnline;
    private SharedPreferences mPref;
    private Resources mResource;
    private boolean mBHasBitmap = true;

    public InsertUtil(Context mContext, boolean mBOnline) {
        this.mContext = mContext;
        this.mBOnline = mBOnline;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mResource = mContext.getResources();
    }

    public InsertUtil(Context mContext, long mStdrspotSn, int mParentsIndex, boolean mBOnline) {
        this.mContext = mContext;
        this.mStdrspotSn = mStdrspotSn;
        this.mParentsIndex = mParentsIndex;
        this.mBOnline = mBOnline;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mResource = mContext.getResources();
    }

    public void setmBHasBitmap(boolean mBHasBitmap) {
        this.mBHasBitmap = mBHasBitmap;
    }

    //! @brief 프로세스 버튼 상태 설정
    //! @return 프로세스의 마지막 단계일 경우 true
    public boolean setProcessBtns(List<ImageButton> listImageBtns, String progression) {
        boolean bIsLastStep = false;
        int countComplete = 0;
        int indexComplete;
        int arrayImageId [][] = {
                  {R.drawable.btn_insert_info_n, R.drawable.btn_insert_info_h, R.drawable.btn_insert_info_p}
                , {R.drawable.btn_insert_mtr_sttus_n, R.drawable.btn_insert_mtr_sttus_h, R.drawable.btn_insert_mtr_sttus_p}
                , {R.drawable.btn_insert_rtk_able_n, R.drawable.btn_insert_rtk_able_h, R.drawable.btn_insert_rtk_able_p}
                , {R.drawable.btn_insert_etc_n, R.drawable.btn_insert_etc_h, R.drawable.btn_insert_etc_p}
                , {R.drawable.btn_insert_kimg_n, R.drawable.btn_insert_kimg_h, R.drawable.btn_insert_kimg_p}
                , {R.drawable.btn_insert_oimg_n, R.drawable.btn_insert_oimg_h, R.drawable.btn_insert_oimg_p}
                , {R.drawable.btn_insert_pimg_n, R.drawable.btn_insert_pimg_h, R.drawable.btn_insert_pimg_p}
                , {R.drawable.btn_insert_roughmap_n, R.drawable.btn_insert_roughmap_h, R.drawable.btn_insert_roughmap_p}
        };

        if (null == progression) {
            progression = mContext.getString(R.string.init_value_progression);
        }
        String arrayProgression[] = progression.split(mContext.getString(R.string.seperator));

        for (int i = 0; i < arrayProgression.length; i++) {
            indexComplete = Integer.valueOf(arrayProgression[i]);
            listImageBtns.get(i).setImageResource(arrayImageId[i][indexComplete]);

            if (mParentsIndex != i && mResource.getInteger(R.integer.index_invest_insert_complete_h) == indexComplete) {
                countComplete++;
            }
        }

        listImageBtns.get(mParentsIndex).setImageResource(arrayImageId[mParentsIndex][mResource.getInteger(R.integer.index_invest_insert_complete_p)]);

        if (countComplete > arrayImageId.length - 2) {
            switch (mParentsIndex) {
                case 3:case 4:case 5:case 6:
                    if (!mBHasBitmap) {
                        break;
                    }
                default:
                    bIsLastStep = true;
            }
        }

        return bIsLastStep;
    }

    private Intent setExtra(Intent intent) {
        intent.putExtra(mContext.getString(R.string.key_bonline), mBOnline);
        intent.putExtra(mContext.getString(R.string.key_stdr_spot_sn), mStdrspotSn);
        intent.putExtra(mContext.getString(R.string.key_index_insert), mParentsIndex);

        return intent;
    }
    private Intent setExtraNcp(Intent intent) {
        intent.putExtra(mContext.getString(R.string.key_bonline), mBOnline);
        intent.putExtra(mContext.getString(R.string.key_npu_sn), mStdrspotSn);
        intent.putExtra(mContext.getString(R.string.key_index_insert), mParentsIndex);

        return intent;
    }
    public Intent getMoveIntent(int nBtnId) {
        Intent intent = null;
        int targetIndex = 0;
        switch (nBtnId) {
            case R.id.imgbtn_mtr_info:
                targetIndex = mResource.getInteger(R.integer.index_insert_mtr_info);
                intent = new Intent(mContext, InvestInsertInfo_.class);
                break;
            case R.id.imgbtn_mtr_sttus:
                targetIndex = mResource.getInteger(R.integer.index_insert_mtr_sttus);
                intent = new Intent(mContext, InvestInsertStatus_.class);
                break;
            case R.id.imgbtn_rtk_able:
                targetIndex = mResource.getInteger(R.integer.index_insert_rtk_able);
                intent = new Intent(mContext, InvestInsertStatus_.class);
                break;
            case R.id.imgbtn_etc:
                targetIndex = mResource.getInteger(R.integer.index_insert_etc);
                intent = new Intent(mContext, InvestInsertEtc_.class);
                break;
            case R.id.imgbtn_kimg:
                targetIndex = mResource.getInteger(R.integer.index_insert_kimg);
                intent = new Intent(mContext, InvestInsertImages_.class);
                break;
            case R.id.imgbtn_oimg:
                targetIndex = mContext.getResources().getInteger(R.integer.index_insert_oimg);
                intent = new Intent(mContext, InvestInsertImages_.class);
                break;
            case R.id.imgbtn_pimg:
                targetIndex = mResource.getInteger(R.integer.index_insert_pimg);
                intent = new Intent(mContext, InvestInsertImages_.class);
                break;
            case R.id.imgbtn_roughmap:
                targetIndex = mResource.getInteger(R.integer.index_insert_roughmap);
                intent = new Intent(mContext, InvestInsertImages_.class);
                break;
        }

        if (intent == null) {
            intent = new Intent();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return setExtra(intent).putExtra(mContext.getString(R.string.key_index_insert), targetIndex);
    }

    //! @brief 데이터 저장시 진행상태를 변경한다.
    public String changeProgression(String progression) {
        String strResult;

        if (null == progression) {
            progression = mContext.getString(R.string.init_value_progression);
        }

        strResult = mContext.getString(R.string.empty);
        String arrayProgression[] = progression.split(mContext.getString(R.string.seperator));

        for (int i = 0; i < arrayProgression.length; i++) {
            if (mParentsIndex == i) {
                strResult += String.valueOf(mResource.getInteger(R.integer.index_invest_insert_complete_h));
            } else {
                strResult += arrayProgression[i];
            }

            if (arrayProgression.length - 1 != i) {
                strResult += mContext.getString(R.string.seperator);
            }
        }
        return strResult;
    }


    public Intent getResultIntent(boolean putEmptyValues) {
        String strEmpty = mContext.getString(R.string.empty);

        if (putEmptyValues) {
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(mContext.getString(R.string.key_adm_code), strEmpty);
            editor.putString(mContext.getString(R.string.key_point_kind_code), strEmpty);
            editor.putString(mContext.getString(R.string.key_start_date), strEmpty);
            editor.putString(mContext.getString(R.string.key_end_date), strEmpty);
            editor.putString(mContext.getString(R.string.key_start_point_name), strEmpty);
            editor.putString(mContext.getString(R.string.key_end_point_name), strEmpty);
            editor.putString(mContext.getString(R.string.key_inquire_descent_sorting), strEmpty);
            editor.putString(mContext.getString(R.string.key_inquire_status), strEmpty);
            editor.putString(mContext.getString(R.string.key_point_name), strEmpty);
            editor.apply();
        }


        Intent intent = new Intent(mContext, InvestResultActivity_.class);
        intent.putExtra(mContext.getString(R.string.key_bonline), mBOnline);
        intent.putExtra(mContext.getString(R.string.key_adm_code), mPref.getString(mContext.getString(R.string.key_adm_code), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_point_kind_code), mPref.getString(mContext.getString(R.string.key_point_kind_code), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_start_date), mPref.getString(mContext.getString(R.string.key_start_date), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_end_date), mPref.getString(mContext.getString(R.string.key_end_date), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_start_point_name), mPref.getString(mContext.getString(R.string.key_start_point_name), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_end_point_name), mPref.getString(mContext.getString(R.string.key_end_point_name), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_point_name), mPref.getString(mContext.getString(R.string.key_point_name), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_inquire_descent_sorting), mPref.getString(mContext.getString(R.string.key_inquire_descent_sorting), strEmpty));
        intent.putExtra(mContext.getString(R.string.key_inquire_status), mPref.getString(mContext.getString(R.string.key_inquire_status), strEmpty));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return intent;
    }

    public Intent getBackHomeIntent() {
        Intent intent = new Intent(mContext, InvestDetailActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return setExtra(intent);
    }
    public Intent getBackHomeIntentNcp() {
        Intent intent = new Intent(mContext, InvestDetailNcpActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return setExtraNcp(intent);
    }
    /**
     * 국가기준점
     * */
    public Intent getMoveIntentNcp(int nBtnId) {
        Intent intent = null;
        int targetIndex = 0;

        switch (nBtnId) {
            case R.id.imgbtn_mtr_info:
                targetIndex = mResource.getInteger(R.integer.index_insert_mtr_info);
                intent = new Intent(mContext, InvestInsertNcpInfo_.class);
                break;
            case R.id.imgbtn_mtr_sttus:
                targetIndex = mResource.getInteger(R.integer.index_insert_mtr_sttus);
                intent = new Intent(mContext, InvestInsertNcpInfo_.class);
                break;
            case R.id.imgbtn_rtk_able:
                targetIndex = mResource.getInteger(R.integer.index_insert_rtk_able);
                intent = new Intent(mContext, InvestInsertNcpStatus_.class);
                break;
            case R.id.imgbtn_etc:
                targetIndex = mResource.getInteger(R.integer.index_insert_etc);
                intent = new Intent(mContext, InvestInsertNcpEtc_.class);
                break;
            case R.id.imgbtn_kimg:
                targetIndex = mResource.getInteger(R.integer.index_insert_kimg);
                intent = new Intent(mContext, InvestInsertNcpImages_.class);
                break;
            case R.id.imgbtn_oimg:
                targetIndex = mContext.getResources().getInteger(R.integer.index_insert_oimg);
                intent = new Intent(mContext, InvestInsertNcpImages_.class);
                break;
            case R.id.imgbtn_pimg:
                targetIndex = mResource.getInteger(R.integer.index_insert_pimg);
                intent = new Intent(mContext, InvestInsertNcpImages_.class);
                break;
            case R.id.imgbtn_roughmap:
                targetIndex = mResource.getInteger(R.integer.index_insert_roughmap);
                intent = new Intent(mContext, InvestInsertNcpImages_.class);
                break;
        }

        if (intent == null) {
            intent = new Intent();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return setExtra(intent).putExtra(mContext.getString(R.string.key_index_insert), targetIndex);
    }
}
