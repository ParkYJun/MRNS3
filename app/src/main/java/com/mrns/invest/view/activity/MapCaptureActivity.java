package com.mrns.invest.view.activity;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.util.FileUtil;
import com.mrns.invest.util.FineDialog;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_map_capture)
public class MapCaptureActivity extends AppCompatActivity {
    String exType;
    String exFid;
    String exDate;

    @ViewById(R.id.layout_map_capture)
    FrameLayout layoutMapCapture;
    @ViewById(R.id.webview_map_capture)
    WebView webviewMapCapture;

    @AfterViews
    protected void InitView() {
        exType = getIntent().getStringExtra(getString(R.string.key_type));
        exFid = getIntent().getStringExtra(getString(R.string.key_fid));
        exDate = getIntent().getStringExtra(getString(R.string.key_date));

        String url = getString(R.string.server_ip) + getString(R.string.req_capture);
        url += "?type=" + exType + "&fid=" + exFid;

        if (exType.equals(getString(R.string.key_point_type_for_map_query))) {
            long stdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
            RnsStdrspotUnity unity = new RnsStdrspotUnity().FindByStdrspotSn(stdrspotSn);

            if (unity != null) {
                url += "&adm_code=" + unity.getAdmCd() + "&point_name=" + unity.getPointsNm();
            }
        }

        webviewMapCapture.addJavascriptInterface(new WebAppInterface(this), getString(R.string.key_android));
        // javascript set
        webviewMapCapture.getSettings().setJavaScriptEnabled(true);
        //url set
        webviewMapCapture.loadUrl(url);
        //WebView Client set
        webviewMapCapture.setWebViewClient(new WebViewClientClass());
    }

    void onSave() {
        loadingDialog(true, getString(R.string.str_invest_saveloading));
        layoutMapCapture.destroyDrawingCache();
        layoutMapCapture.buildDrawingCache();

        FileUtil.saveBitmaptoJpeg(layoutMapCapture.getDrawingCache(), getString(R.string.mrns), getString(R.string.fn_rogimage, exFid, exDate));
        loadingDialog(false, getString(R.string.empty));
        setResult(RESULT_OK, getIntent());
    }

    //! @brief 기준점 상세조회 화면으로 이동
    @Click({R.id.btn_back_home, R.id.textview_title, R.id.btn_map_save})
    public void onBackHome(View view) {
        if (R.id.btn_map_save == view.getId()) {
            onSave();
        }

        finish();
    }

    @UiThread
    public void loadingDialog(boolean view, String message) {
        if (view)
            FineDialog.showProgress(MapCaptureActivity.this, false, message);
        else
            FineDialog.hideProgress();
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class WebAppInterface extends Activity {

        Context mContext;
        //private GpsInfo gps;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }



        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void setBasePointNo(String bpNo) {

        }

        @JavascriptInterface
        public void setLogMessage(String msg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void setLoadEnd() {
            Toast.makeText(mContext, "GIS INIT!!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
