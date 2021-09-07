package com.mrns.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mrns.common.Common;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by eined on 2016. 1. 21..
 */
public class GsonRequest<T> extends Request<T> {
    private Gson mGson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Response.Listener<T> listener;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        mGson = new Gson();

        this.setRetryPolicy(new DefaultRetryPolicy(Common.TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> params,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
        this.headers = null;
        mGson = new Gson();

        this.setRetryPolicy(new DefaultRetryPolicy(Common.TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }




    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(T response) {

        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {

            String json = new String(response.data, "UTF-8");

            return Response.success(mGson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}
