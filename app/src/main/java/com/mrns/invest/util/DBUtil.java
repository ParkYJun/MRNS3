package com.mrns.invest.util;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mrns.invest.record.CodeRecord;
import com.mrns.invest.record.CodeRecordList;
import com.mrns.main.R;
import com.orm.SugarRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBUtil {
    public static String root = null;
    public static void copyDataBase(Context context) throws IOException {
        //Open your local db as the input stream

        InputStream myInput = context.getApplicationContext().getAssets().open(context.getString(R.string.db_base));
        // Path to the just created empty db
        File outPath = new File(context.getString(R.string.db_path));

        if (!outPath.isDirectory()) {
            outPath.mkdirs();
        }

        File file = new File(context.getString(R.string.db_full_name));
        file.createNewFile();

        OutputStream myOutput = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public static boolean checkDataBase(Context context) {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(context.getString(R.string.db_full_name), null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            return false;
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }

        return checkDB != null;
    }

    public static String getCD(String tableName, String getColmn, String whereColmn, String value) {
        String sql = String.format("select %s from %s where %s = '%s'",getColmn, tableName, whereColmn, value);
        String res = "";
        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        try {
            if(cursor != null ) {
                cursor.moveToFirst();
                res = cursor.getString(0);
                cursor.close();
            } else {
                res = "";
            }

        } catch (CursorIndexOutOfBoundsException e) {
            res = "";
            Log.e("CursorIndexException", e.getMessage());
        } finally {
            Log.i("done", "getCD :" + res);
        }
        return res;

    }
    public static String getCmmnCode(String codeClcd, String code) {
        String sql = String.format("select CODE_VALUE from ST_CMMN_CODE_DTLS where CODE_CLCD = '%s' and CODE ='%s'",codeClcd, code);
        String res = "";
        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        try {
            if(cursor != null ) {
                cursor.moveToFirst();
                res = cursor.getString(0);
                cursor.close();
            } else {
                res = "";
            }
        }  catch (CursorIndexOutOfBoundsException e) {
            res = "";
            Log.e("CursorIndexException", e.getMessage());
        } finally {
            Log.i("done", "getCD :" + res);
        }
        return res;
    }
    public static String[] getCmmnCodeStringArray(String codeClcd) {
        String sql = String.format("select CODE_VALUE from ST_CMMN_CODE_DTLS where CODE_CLCD = '%s'",codeClcd);
        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> strList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            strList.add(cursor.getString(0) != null ? cursor.getString(0) : "");
            cursor.moveToNext();
        }
        cursor.close();
        String[] stockArr = new String[strList.size()];
        stockArr = strList.toArray(stockArr);
        return stockArr;
    }
    public static ArrayList<CodeRecord> getCmmCodeRecord(String codeClcd) {
        String sql = String.format("select CODE, CODE_VALUE from ST_CMMN_CODE_DTLS where CODE_CLCD = '%s'", codeClcd);
        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<CodeRecord> codeList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            CodeRecord record = new CodeRecord(
                    cursor.getString(0) != null ? cursor.getString(0) : "" ,
                    cursor.getString(1) != null ? cursor.getString(1) : "" );
            codeList.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        return codeList;
    }

    public static CodeRecordList getCommonCodeList(String codeClcd) {
        String sql = String.format("select CODE, CODE_VALUE from ST_CMMN_CODE_DTLS where CODE_CLCD = '%s'", codeClcd);
        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        cursor.moveToFirst();
        ArrayList<String> codeList = new ArrayList<>();
        ArrayList<String> codeValueList = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            codeList.add(cursor.getString(0) != null ? cursor.getString(0) : "" );
            codeValueList.add(cursor.getString(1) != null ? cursor.getString(1) : "" );
            cursor.moveToNext();
        }
        cursor.close();
        return new CodeRecordList(codeList, codeValueList);
    }

    public static String getHeadOfficeCode(String branchCode) {
        String headOfficeCode = "";
        String sql = String.format("select upper_dept_code from ST_KCSCIST_DEPT where dept_code = '%s'", branchCode);
        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            headOfficeCode = cursor.getString(0) != null ? cursor.getString(0) : "";
            cursor.moveToNext();
        }
        cursor.close();
        return headOfficeCode;
    }

    public static CodeRecordList getDepartmentInfoList(String headOfficeCode) {
        String sql = "SELECT " +
                "DEPT_CODE as departmentCode, " +
                "DEPT_NM as departmentName, " +
                "UPPER_DEPT_CODE as departmentCode " +
                "FROM ST_KCSCIST_DEPT " +
                "WHERE USE_YN = 'Y' " +
                "AND DEPT_SECD = ";

        if (null == headOfficeCode) {
            sql += "'1' ";
        } else {
            sql += "'2' ";

            if (!headOfficeCode.equals("")) {
                sql += "AND UPPER_DEPT_CODE = "
                        + headOfficeCode;
            }
        }

        sql += " ORDER BY OUTPT_ORDR, DEPT_CODE";

        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        cursor.moveToFirst();

        ArrayList<String> departmentCodeList = new ArrayList<>();
        ArrayList<String> departmentNameList = new ArrayList<>();
        ArrayList<String> upperDepartmentCodeList = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            departmentCodeList.add(cursor.getString(0) != null ? cursor.getString(0) : "");
            departmentNameList.add(cursor.getString(1) != null ? cursor.getString(1) : "");
            upperDepartmentCodeList.add(cursor.getString(2) != null ? cursor.getString(2) : "");
            cursor.moveToNext();
        }

        cursor.close();
        return new CodeRecordList(departmentCodeList, departmentNameList, upperDepartmentCodeList);
    }
}
