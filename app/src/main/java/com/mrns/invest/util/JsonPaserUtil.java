package com.mrns.invest.util;

import android.util.Log;

import com.mrns.invest.record.CodeRecordList;
import com.mrns.invest.record.RnsNpRslt;
import com.mrns.invest.record.RnsNpSttusInq;
import com.mrns.invest.record.RnsNpSttusInqBf;
import com.mrns.invest.record.RnsNpSubPath;
import com.mrns.invest.record.RnsNpTraverRslt;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.record.RnsSttusInqBefore;
import com.mrns.invest.record.UserInfoRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonPaserUtil {
    public static RnsStdrspotUnity investUnityJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsStdrspotUnity unity = new RnsStdrspotUnity(
                    data.getLong("stdrspot_sn"),
                    data.getString("base_point_no"),
                    data.getString("points_knd_cd"),
                    data.getString("points_nm"),
                    data.getString("jgrc_cd"),
                    data.getString("brh_cd"),
                    data.getString("points_no"),
                    data.getString("mline_grad_cd"),
                    data.getString("mtr_cd"),
                    data.getString("instl_se_cd"),
                    data.getString("bis_se_cd"),
                    data.getString("bis_nm"),
                    data.getString("instl_de"),
                    data.getString("instl_instt_cd"),
                    data.getString("instl_psitn"),
                    data.getString("instl_clsf"),
                    data.getString("instl_nm"),
                    data.getString("drw_no"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("msurpoints_lc_map"),
                    data.getString("adm_cd"),
                    data.getString("locplc_lnm"),
                    data.getString("locplc_rd_nm"),
                    data.getString("nearby_locplc_lnm"),
                    data.getString("nearby_locplc_rd_nm"),
                    data.getString("notify_no"),
                    data.getString("notify_de"),
                    data.getString("rslt_doc_file"),
                    data.getString("surv_mth"),
                    data.getString("cal_mth"),
                    data.getString("cal_psitn"),
                    data.getString("cal_clsf"),
                    data.getString("cal_nm"),
                    data.getString("mline_nm"),
                    data.getString("ksp_file"),
                    data.getString("cro_point"),
                    data.getString("use_bhf_points"),
                    data.getString("begin_points"),
                    data.getString("begin_mach_points"),
                    data.getString("arrv_points"),
                    data.getString("arrv_mach_points"),
                    data.getString("before_points"),
                    data.getString("after_points"),
                    data.getString("azimuth"),
                    data.getDouble("dstnc"),
                    data.getDouble("dist_x"),
                    data.getDouble("dist_y"),
                    data.getString("trgnpt_cd"),
                    data.getDouble("points_x"),
                    data.getDouble("points_y"),
                    data.getString("lat"),
                    data.getString("lon"),
                    data.getDouble("h"),
                    data.getString("wd_h_se_cd"),
                    data.getString("wd_eh"),
                    data.getString("meridian_sucha"),
                    data.getString("instl_rslt_acptnc_yn"),
                    data.getString("instl_rslt_acptnc_de"),
                    data.getString("instl_rslt_acptnc_jgrc"),
                    data.getString("instl_rslt_acptnc_nm"),
                    data.getString("instl_rslt_acptnc_rtrn_resn"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_clsf"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("sttus_rslt_acptnc_yn"),
                    data.getString("sttus_rslt_acptnc_de"),
                    data.getString("sttus_rslt_acptnc_jgrc"),
                    data.getString("sttus_rslt_acptnc_nm"),
                    data.getString("sttus_rslt_acptnc_rtrn_resn"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("wd_trgnpt_cd"),
                    data.getString("wd_points_x"),
                    data.getString("wd_points_y"),
                    data.getString("wd_lat"),
                    data.getString("wd_lon"),
                    data.getString("wd_h"),
                    data.getString("locplc_detail")
            );
            return unity;

        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public static RnsStdrspotUnity investUnityArrJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONArray dataArr = bDetail.getJSONArray("resultList");
            JSONObject data = dataArr.getJSONObject(0);
            RnsStdrspotUnity unity = new RnsStdrspotUnity(
                    data.getLong("stdrspot_sn"),
                    data.getString("base_point_no"),
                    data.getString("points_knd_cd"),
                    data.getString("points_nm"),
                    data.getString("jgrc_cd"),
                    data.getString("brh_cd"),
                    data.getString("points_no"),
                    data.getString("mline_grad_cd"),
                    data.getString("mtr_cd"),
                    data.getString("instl_se_cd"),
                    data.getString("bis_se_cd"),
                    data.getString("bis_nm"),
                    data.getString("instl_de"),
                    data.getString("instl_instt_cd"),
                    data.getString("instl_psitn"),
                    data.getString("instl_clsf"),
                    data.getString("instl_nm"),
                    data.getString("drw_no"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("msurpoints_lc_map"),
                    data.getString("adm_cd"),
                    data.getString("locplc_lnm"),
                    data.getString("locplc_rd_nm"),
                    data.getString("nearby_locplc_lnm"),
                    data.getString("nearby_locplc_rd_nm"),
                    data.getString("notify_no"),
                    data.getString("notify_de"),
                    data.getString("rslt_doc_file"),
                    data.getString("surv_mth"),
                    data.getString("cal_mth"),
                    data.getString("cal_psitn"),
                    data.getString("cal_clsf"),
                    data.getString("cal_nm"),
                    data.getString("mline_nm"),
                    data.getString("ksp_file"),
                    data.getString("cro_point"),
                    data.getString("use_bhf_points"),
                    data.getString("begin_points"),
                    data.getString("begin_mach_points"),
                    data.getString("arrv_points"),
                    data.getString("arrv_mach_points"),
                    data.getString("before_points"),
                    data.getString("after_points"),
                    data.getString("azimuth"),
                    data.getDouble("dstnc"),
                    data.getDouble("dist_x"),
                    data.getDouble("dist_y"),
                    data.getString("trgnpt_cd"),
                    data.getDouble("points_x"),
                    data.getDouble("points_y"),
                    data.getString("lat"),
                    data.getString("lon"),
                    data.getDouble("h"),
                    data.getString("wd_h_se_cd"),
                    data.getString("wd_eh"),
                    data.getString("meridian_sucha"),
                    data.getString("instl_rslt_acptnc_yn"),
                    data.getString("instl_rslt_acptnc_de"),
                    data.getString("instl_rslt_acptnc_jgrc"),
                    data.getString("instl_rslt_acptnc_nm"),
                    data.getString("instl_rslt_acptnc_rtrn_resn"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_clsf"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("sttus_rslt_acptnc_yn"),
                    data.getString("sttus_rslt_acptnc_de"),
                    data.getString("sttus_rslt_acptnc_jgrc"),
                    data.getString("sttus_rslt_acptnc_nm"),
                    data.getString("sttus_rslt_acptnc_rtrn_resn"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("wd_trgnpt_cd"),
                    data.getString("wd_points_x"),
                    data.getString("wd_points_y"),
                    data.getString("wd_lat"),
                    data.getString("wd_lon"),
                    data.getString("wd_h"),
                    data.getString("locplc_detail")
            );
            return unity;

        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public static RnsSttusInqBefore investBeforeArrJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONArray dataArr = bDetail.getJSONArray("resultList");
            JSONObject data = dataArr.getJSONObject(1);
            RnsSttusInqBefore before = new RnsSttusInqBefore(
                    data.getLong("sttus_bf_sn"),
                    data.getLong("sttus_inq_stdr_points_list"),
                    data.getLong("stdrspot_sn"),
                    data.getString("base_point_no"),
                    data.getString("points_knd_cd"),
                    data.getString("points_nm"),
                    data.getString("jgrc_cd"),
                    data.getString("brh_cd"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_clsf"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("sttus_rslt_acptnc_yn"),
                    data.getString("sttus_rslt_acptnc_de"),
                    data.getString("sttus_rslt_acptnc_jgrc"),
                    data.getString("sttus_rslt_acptnc_nm"),
                    data.getString("sttus_rslt_acptnc_rtrn_resn"),
                    data.getString("sttus_rslt_acptnc_sttus"),
                    data.getString("sttus_rslt_acptnc_cancel"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("inq_sttus"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("locplc_detail"),
                    data.getString("progression"),
                    data.getString("rl_inq_psitn"),
                    data.getString("rl_inq_clsf"),
                    data.getString("rl_inq_nm"),
                    data.getString("rl_inq_id")
            );
            return before;
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investBeforeJsonPaser done");
        }
    }

    /**
     * 국가기준점
     */
    public static RnsNpUnity investNcpUnityJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsNpUnity rnsNpUnityunity = new RnsNpUnity(
                    data.getLong("npu_sn"),
                    data.getLong("npu_bf_sn"),
                    data.getString("points_knd_cd"),
                    data.getString("points_nm"),
                    data.getString("points_sttus_cd"),
                    data.getString("lay_de"),
                    data.getString("notify_de"),
                    data.getString("notify_no"),
                    data.getString("instl_instt_nm"),
                    data.getString("rslt_mgt_nm"),
                    data.getString("sttus_inq_nm"),
                    data.getString("map_nm"),
                    data.getString("mtr_cd"),
                    data.getString("inq_de"),
                    data.getString("points_legaldong_code"),
                    data.getString("points_regstr_secd"),
                    data.getString("points_locplc_mnnm"),
                    data.getString("points_locplc_slno"),
                    data.getString("road_nm_code"),
                    data.getString("road_nm"),
                    data.getString("buildng_nm"),
                    data.getString("points_path"),
                    data.getString("traverse_table_path"),
                    data.getString("rog_map_file"),
                    data.getString("detail_map_file"),
                    data.getString("observe_nm"),
                    data.getString("observe_de"),
                    data.getString("regist_id"),
                    data.getString("regist_ofcps_cd"),
                    data.getString("regist_psitn_cd"),
                    data.getString("regist_de"),
                    data.getString("update_id"),
                    data.getString("update_ofcps_cd"),
                    data.getString("update_psitn_cd"),
                    data.getString("update_de"),
                    data.getString("acptnc_yn"),
                    data.getString("use_yn"),
                    data.getString("rm"),
                    data.getString("points_secd"),
                    data.getString("full_addr"),
                    data.getString("full_road_addr")
            );
            return rnsNpUnityunity;

        } catch(JSONException e){
            Log.e("JSonException", e.getMessage());
            return null;
        } finally{
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public static RnsNpSttusInqBf investRnsNpSttusInqBfJsonPaser(String str) {
        try {

            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject dataArr = bDetail.getJSONObject("resultVO");
            JSONObject data = dataArr.getJSONObject(String.valueOf(0));
            RnsNpSttusInqBf rnsif = new RnsNpSttusInqBf(
                    data.getLong("npsi_bf_sn"),
                    data.getLong("npu_sn"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_ofcps"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("acptnc_yn"),
                    data.getString("use_yn"),
                    data.getString("sttus_rslt_acptnc_sttus"),
                    data.getString("rog_map_file"),
                    data.getString("k_img_file"),
                    data.getString("o_img_file"),
                    data.getString("msurpoints_lc_drw_file"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("inq_sttus"),
                    data.getString("progression"),
                    data.getString("rl_inq_psitn"),
                    data.getString("rl_inq_clsf"),
                    data.getString("rl_inq_nm"),
                    data.getString("rl_inq_id"),
                    data.getString("points_path")
            );
            return rnsif;

        } catch(JSONException e){
            Log.e("JSonException", e.getMessage());
            return null;
        } finally{
            Log.i("done", "investUnityJsonPaser done");
        }
    }
    public static RnsNpSttusInqBf investRnsNpSttusInqBfJsonPaser2(String str) {
        try {

            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsNpSttusInqBf rnsif = new RnsNpSttusInqBf(
                    data.getLong("npsi_bf_sn"),
                    data.getLong("npu_sn"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_ofcps"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("acptnc_yn"),
                    data.getString("use_yn"),
                    data.getString("sttus_rslt_acptnc_sttus"),
                    data.getString("rog_map_file"),
                    data.getString("k_img_file"),
                    data.getString("o_img_file"),
                    data.getString("msurpoints_lc_drw_file"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("inq_sttus"),
                    data.getString("progression"),
                    data.getString("rl_inq_psitn"),
                    data.getString("rl_inq_clsf"),
                    data.getString("rl_inq_nm"),
                    data.getString("rl_inq_id"),
                    data.getString("points_path")
            );
            return rnsif;

        } catch(JSONException e){
            Log.e("JSonException", e.getMessage());
            return null;
        } finally{
            Log.i("done", "investUnityJsonPaser done");
        }
    }
    public static RnsNpRslt investRnsNpRsltJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsNpRslt runity = new RnsNpRslt(
                    data.getLong("npu_sn"),
                    data.getLong("npr_sn"),
                    data.getString("trgnpt_cd"),
                    data.getDouble("points_x"),
                    data.getDouble("points_y"),
                    data.getString("lat"),
                    data.getString("lon"),
                    data.getDouble("h"),
                    data.getString("eh"),
                    data.getDouble("gravity"),
                    data.getString("meridian_sucha"),
                    data.getString("geodetic_survey")
            );
            return runity;

        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public static RnsNpTraverRslt investRnsNpTraverRsltJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsNpTraverRslt runity = new RnsNpTraverRslt(
                    data.getLong("nptr_sn"),
                    data.getLong("npu_sn"),
                    data.getString("geodetic_survey"),
                    data.getString("g_points_nm"),
                    data.getString("lat"),
                    data.getString("lon"),
                    data.getString("eh"),
                    data.getString("azimuth"),
                    data.getString("direction_angle"),
                    data.getString("dstnc")
            );
            return runity;

        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }

  /*  public static RnsNpSubPath investRnsNpSubPathJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONArray dataArr = bDetail.getJSONArray("resultList");
            JSONObject data = null;//= dataArr.getJSONObject(1);
            ArrayList<JSONArray> datas = new ArrayList<>();
            for(int i=0; i < dataArr.length(); i++){
                data.getLong(dataArr.getJSONObject(i).getLong("npu_sn"));
                dataArr.getJSONObject(i).getLong("npsp_sn");
                dataArr.getJSONObject(i).getString("geodetic_survey");
                dataArr.getJSONObject(i).getString("sub_points_nm");
                dataArr.getJSONObject(i).getString("sub_points_path");
                dataArr.getJSONObject(i).getDouble("relative_height");
                dataArr.getJSONObject(i).getString("lat");
                dataArr.getJSONObject(i).getString("lon");
                datas.add(dataArr);
            }
            //JSONObject jObj = new JSONObject(str);
            //JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            //JSONObject data = bDetail.getJSONObject("resultVO");
            *//*RnsNpSubPath runity = new RnsNpSubPath(
                    data.getLong("npu_sn"),
                    data.getLong("npsp_sn"),
                    data.getString("geodetic_survey"),
                    data.getString("sub_points_nm"),
                    data.getString("sub_points_path"),
                    data.getDouble("relative_height"),
                    data.getString("lat"),
                    data.getString("lon")
            );*//*
            return runity;

        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }*/

    public static RnsNpSubPath investRnsNpSubPathJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsNpSubPath runity = new RnsNpSubPath(
                    data.getLong("npu_sn"),
                    data.getLong("npsp_sn"),
                    data.getString("geodetic_survey"),
                    data.getString("sub_points_nm"),
                    data.getString("sub_points_path"),
                    data.getDouble("relative_height"),
                    data.getString("lat"),
                    data.getString("lon")
            );
            return runity;

        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public static RnsNpSttusInq investRnsNpSttusInqJsonPaser(String str) {
        try {

            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            RnsNpSttusInq rnsNpSttusInq = new RnsNpSttusInq(
                data.getLong("npsi_sn"),
                data.getLong("npu_sn"),
                data.getString("inq_de"),
                data.getString("inq_instt_cd"),
                data.getString("inq_psitn"),
                data.getString("inq_ofcps"),
                data.getString("inq_nm"),
                data.getString("inq_id"),
                data.getString("inq_mtr_cd"),
                data.getString("mtr_sttus_cd"),
                data.getString("rtk_posbl_yn"),
                data.getString("ptclmatr"),
                data.getString("inq_mtr_etc"),
                data.getString("mtr_sttus_etc"),
                data.getString("rtk_posbl_etc"),
                data.getString("acptnc_yn"),
                data.getString("use_yn"),
                data.getString("sttus_rslt_acptnc_sttus"),
                data.getString("rog_map_file"),
                data.getString("k_img_file"),
                data.getString("o_img_file"),
                data.getString("msurpoints_lc_drw_file"),
                data.getString("rog_map"),
                data.getString("k_img"),
                data.getString("o_img"),
                data.getString("msurpoints_lc_drw"),
                data.getString("inq_sttus"),
                data.getString("progression"),
                data.getString("rl_inq_psitn"),
                data.getString("rl_inq_clsf"),
                data.getString("rl_inq_nm"),
                data.getString("rl_inq_id"),
                data.getString("points_path")
            );
            return rnsNpSttusInq;

        } catch(JSONException e){
            Log.e("JSonException", e.getMessage());
            return null;
        } finally{
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public static UserInfoRecord getUserInfoJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");

            UserInfoRecord userInfoRecord = new UserInfoRecord(
                "",
                    data.getString("user_nm"),
                    data.getString("dept_code"),
                    data.getString("ofcps_clcd")
            );

            return userInfoRecord;
        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
            return null;
        } finally {
            Log.i("done", "getUserNameJsonPaser done");
        }
    }

    public static CodeRecordList getCodeRecordList(String response) {
        CodeRecordList result = null;
        JSONObject jObject;
        try {
            JSONObject jObj = new JSONObject(response);
            JSONObject obj = jObj.getJSONObject("listXmlReturnVO");
            JSONArray returnArray = new JSONArray(obj.getJSONArray("resultList").toString());
            ArrayList<String> codeList = new ArrayList<>();
            ArrayList<String> codeValueList = new ArrayList<>();

            for(int i=0; i< returnArray.length(); i++) {
                jObject = returnArray.getJSONObject(i);
                codeList.add(jObject.getString("code"));
                codeValueList.add(jObject.getString("codeValue"));
            }

            if (codeList != null) {
                result = new CodeRecordList(codeList, codeValueList);
            }
        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
        } finally {
            Log.i("done", "getCodeRecordList done");
        }

        return result;
    }
}
