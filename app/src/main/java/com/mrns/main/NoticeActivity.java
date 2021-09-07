package com.mrns.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.notice.NoticeDialog;
import com.mrns.notice.NoticeRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callMobileNotice();
    }

    private void callMobileNotice() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(NoticeActivity.this);
        String url = getString(R.string.server_ip) + getString(R.string.req_mobile_notice);

        Request request = new StringRequest(Request.Method.POST, url, successNoticeListener, failNoticeListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                return new HashMap<>();
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successNoticeListener = new Response.Listener<String>()
    {
        @Override
        public void onResponse(String response)
        {
            try{
                //JSONObject set
                JSONObject jObj = new JSONObject(response);

                JSONObject mainData = jObj.getJSONObject("listXmlReturnVO");
                JSONArray jsonArray = new JSONArray(mainData.getJSONArray("resultList").toString());

                //! sharedPreferences에서 배제할 공지사항 리스트를 get, 실제 받아온 공지사항 중 해당되지 않는 공지사항만 띄운다.
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.key_shared_preferences), MODE_PRIVATE);
                String strKey = sharedPreferences.getString(getString(R.string.key_exclude_notice), null);
                ArrayList<Integer> arrayKey = new ArrayList<>();
                Boolean isShow;

                if (null != strKey) {
                    for (String key : strKey.split(getString(R.string.seperator))) {
                        arrayKey.add(Integer.valueOf(key));
                    }
                }

                for(int i = 0; i < jsonArray.length(); i++) {
                    Context context = NoticeActivity.this;
                    isShow = true;

                    NoticeRecord noticeRecord = new NoticeRecord(jsonArray.getJSONObject(i));

                    for (int key : arrayKey) {
                        if (key == noticeRecord.getMobileNoticeSn()) {
                            isShow = false;
                            break;
                        }
                    }

                    if (isShow) {
                        NoticeDialog noticeDialog = new NoticeDialog(context, noticeRecord);
                        noticeDialog.show();
                    }
                }
            } catch (JSONException e) {
                Log.e(getString(R.string.jSONException), e.getMessage());
            }finally {
                Log.i(getString(R.string.done), "requestMainInfo done");
            }
        }
    };

    private Response.ErrorListener failNoticeListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
            //Log.e(getString(R.string.message_error_timeout), e.getMessage());
        }
    };
}
