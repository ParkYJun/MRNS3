package com.mrns.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mrns.common.Common;
import com.mrns.invest.record.RnsNpSttusInq;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.util.DBUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownInvestFileAsync extends AsyncTask<String, String, String> {

    Common util = new Common();

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String downfileName;
    private String targetName;
    private boolean mIsSingleNCDownload = false;
    private long mNpuSn;

    public DownInvestFileAsync(Context context) {
        mContext = context;
    }

    public void setNpuSn(long npuSn) {
        this.mNpuSn = npuSn;
    }

    public void setSingleNCDownload(boolean isSingleNCDownload) {
        this.mIsSingleNCDownload = isSingleNCDownload;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("현황조사 대상자료 다운로드");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();

        downfileName = Environment.getExternalStorageDirectory() + "/MRNS.zip";
        targetName = util.getSDPath() + "/MRNS/";

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... aurl) {

        int count;
        try {
            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
//            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(downfileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            mProgressDialog.dismiss();
        }

        return null;
    }

    protected void onProgressUpdate(String... progress) {
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(String unused) {
        try {
            unZipIt(downfileName, targetName);
        } catch (FileNotFoundException e) {
            e.getMessage();
        }finally {
            Toast.makeText(mContext, "다운로드를 완료했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void unZipIt(String zipFile, String outputFolder) throws FileNotFoundException {
        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = null;
            try {
                ze = zis.getNextEntry();
            } catch (IOException e) {
                e.getMessage();
            } finally {
                Log.i("done", "unzip start!!");
            }

            List<String> imageNameList = new ArrayList<>();

            while(ze!=null){
                String fileName = ze.getName();
                imageNameList.add(fileName);

                File newFile = new File(outputFolder + File.separator + fileName);

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            if (mIsSingleNCDownload) {
                updateImageNames(imageNameList);
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            //다운로드 한 zip 파일은 삭제한다.
            File delZip = new File(zipFile);
            if(delZip.isFile()) {
                delZip.delete();
            }

        }//end finally
    }//end unZipIt

    private void updateImageNames(List<String> imageNameList) {
        RnsNpUnity unity = new RnsNpUnity().FindByNpuSn(mNpuSn);
        RnsNpSttusInq inq = new RnsNpSttusInq().findLastInq(mNpuSn);

        if (unity != null) {
            int startIndex;
            int commonStringLength = "image.jpg".length();
            for (String imageName : imageNameList) {
                startIndex = imageName.lastIndexOf("_");

                if (startIndex > -1) {
                    String checkValue = imageName.substring(startIndex + 1, imageName.length() - commonStringLength);

                    switch (checkValue) {
                        case "k":
                            inq.setKImg(imageName);
                            break;
                        case "o":
                            inq.setOImg(imageName);
                            break;
                        case "rog":
                            inq.setRogMap(imageName);
                            break;
                        case "drw":
                            inq.setMsurpointsLcDrw(imageName);
                            break;
                        case "detail":
                            unity.setDetailMapFile(imageName);
                            break;
                        case "unityrog":
                            unity.setRogMapFile(imageName);
                            break;
                    }
                }
            }

            unity.save();
            inq.save();
        }
    }
}//end class

