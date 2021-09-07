package com.mrns.invest.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrns.invest.record.RnsNpSttusInqBf;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.EmulatorPath;
import com.mrns.invest.util.FileUtil;
import com.mrns.invest.util.FineDialog;
import com.mrns.invest.util.InsertUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

@EActivity(R.layout.activity_invest_insert_images)
public class InvestInsertNcpImages extends AppCompatActivity {
    //! Extras
    private long exStdrspotSn;
    private boolean exBOnline;
    private int exIndexInsert;

    //! 뷰 커넥팅
    @ViewById(R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(R.id.btn_camera)
    Button btnCamera;
    @ViewById(R.id.btn_make_roughmap)
    Button btnMakeRoughmap;
    @ViewById(R.id.btn_complete)
    Button btnComplete;
    @ViewById(R.id.layout_image)
    RelativeLayout layoutImage;
    @ViewById(R.id.imageview_image)
    ImageView imageviewImage;
    @ViewById(R.id.textview_image_label)
    TextView textviewImageLabel;
    @ViewsById({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    List<ImageButton> arrayImageBtn;
    @ViewById(R.id.btnNavNext)
    ImageView btn_navNext;
    @ViewById(R.id.btnNavBack)
    ImageView btn_navBack;

    //! 멤버변수
    private RnsNpSttusInqBf mItem;
    private InsertUtil mInsertUtil;
    private String mDate;
    private String mImageName;
    private String mImagePath;
    private String mImageFullName;
    private boolean mBHasBitmap = true;
    private boolean mBIsRoughmap = false;
    private PhotoViewAttacher mAttacher;
    private RnsNpUnity item;

    //! 상수 정의
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    public static final int PICK_MAKE_ROUGHMAP = 2;
    public static final int PICK_MEMO = 3;
    private static final int INDEX_K_IMG = 4;
    private static final int INDEX_O_IMG = 5;
    private static final int INDEX_P_IMG = 6;
    private static final int INDEX_ROUGHMAP = 7;

    @AfterViews
    protected void InitView() {
        //! Extra 및 멤버변수 초기화
        exStdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
        exBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        exIndexInsert = getIntent().getIntExtra(getString(R.string.key_index_insert), 0);
        mInsertUtil = new InsertUtil(getApplicationContext(), exStdrspotSn, exIndexInsert, exBOnline);
        mItem = RnsNpSttusInqBf.FindByNpuSn(exStdrspotSn);
        SimpleDateFormat format1 = new SimpleDateFormat(getString(R.string.simple_date_format2));
        mDate = format1.format(Calendar.getInstance().getTime());
        item = RnsNpUnity.FindByNpuSn(exStdrspotSn);

        if (mItem != null) {
            //! 뷰 초기 설정(타이틀, 라벨, 레이아웃, 이미지뷰 등)

            String[] array =  mItem.getProgression().split("/", -1);
            String arrTrue = "";
            for(int i = 0; i < array.length; i++){
                if(array[i].indexOf("1") == 0){
                    arrTrue += i;
                }
            }

            if (mInsertUtil.setProcessBtns(arrayImageBtn, mItem.getProgression()) || arrTrue.contains("1") && arrTrue.contains("2") && arrTrue.contains("4") && arrTrue.contains("5") ) {
                btnComplete.setVisibility(View.VISIBLE);
            }

            String pointKind = item.getPointsKndCd() != null ? DBUtil.getCmmnCode(getString(R.string.code_point_kind), item.getPointsKndCd()) : getString(R.string.empty);
            String pointNm = item.getPointsNm() != null ? item.getPointsNm() : getString(R.string.empty);
            String title = getString(R.string.title_points, pointNm, pointKind);

            textviewSubTitle.setText(title);
            textviewImageLabel.setText(title);
            textviewImageLabel.setVisibility(View.INVISIBLE);

            String strGetImage = "";
            int btnCameraVisibility = View.VISIBLE;
            int btnMakeMapVisibility = View.GONE;
            String tempImageName = "";
            switch (exIndexInsert) {
                case INDEX_K_IMG:
                    strGetImage = mItem.getKImg();
                    mImageName = getString(R.string.fn_kimage, String.valueOf(mItem.getNpuSn()), mDate);
                    break;
                case INDEX_O_IMG:
                    strGetImage = mItem.getOImg();
                    mImageName = getString(R.string.fn_oimage, String.valueOf(mItem.getNpuSn()), mDate);
                    break;
                case INDEX_P_IMG:
                    strGetImage = mItem.getMsurpointsLcDrw();
                    mImageName = getString(R.string.fn_pimage, String.valueOf(mItem.getNpuSn()), mDate);
                    break;
                case INDEX_ROUGHMAP:
                    mBIsRoughmap = true;
                    btnCameraVisibility = View.GONE;
                    btnMakeMapVisibility = View.VISIBLE;
                    strGetImage = mItem.getRogMap();
                    mImageName = getString(R.string.fn_rogimage, String.valueOf(mItem.getNpuSn()), mDate);
                    break;
            }

            btnCamera.setVisibility(btnCameraVisibility);
            btnMakeRoughmap.setVisibility(btnMakeMapVisibility);
            Bitmap bitmap = null;
            mImagePath = EmulatorPath.getPath() + File.separator + getString(R.string.mrns) + File.separator;
            mImageFullName = mImagePath + mImageName + getString(R.string.f_jpg);

            if (null == strGetImage || strGetImage.equals(getString(R.string.empty))) {
                try {
                    mBHasBitmap = false;
                    bitmap = BitmapFactory.decodeStream(getAssets().open(getString(R.string.no_image_file)));
                } catch (IOException e) {
                    Log.e(getString(R.string.iOException), "이미지 없음");
                }
            } else {
                bitmap = BitmapFactory.decodeFile(mImagePath + strGetImage);
                double height = bitmap.getHeight();
                double width = bitmap.getWidth();

                if (width < 1000) {
                    double adjValue = 1000 / width;

                    bitmap = Bitmap.createScaledBitmap(bitmap, (int)(width * adjValue), (int)(height * adjValue), true);
                }
            }

            imageviewImage.setImageBitmap(bitmap);

            /*if (mBIsRoughmap) {
                mAttacher = new PhotoViewAttacher(imageviewImage);
                mAttacher.update();
            }*/
            if (!exBOnline) {
                btnMakeRoughmap.setTextColor(Color.GRAY);
            }
        }

        layoutImage.setVisibility(View.VISIBLE);
    }



    @UiThread
    protected void loadingDialog(boolean view, String message) {
        if (view)
            FineDialog.showProgress(InvestInsertNcpImages.this, false, message);
        else
            FineDialog.hideProgress();
    }

    private boolean onSave(String inqSttus) {

        if (mBHasBitmap) {
            SimpleDateFormat format1 = new SimpleDateFormat(getString(R.string.simple_date_format));
            String date = format1.format(Calendar.getInstance().getTime());

            loadingDialog(true, getResources().getString(R.string.str_invest_saveloading));
            layoutImage.destroyDrawingCache();
            layoutImage.buildDrawingCache();
            Bitmap captureView = layoutImage.getDrawingCache();
            FileUtil.saveBitmaptoJpeg(captureView, getString(R.string.mrns), mImageName);

            String strImage = mImageName + getString(R.string.f_jpg);
            switch (exIndexInsert) {
                case INDEX_K_IMG:
                    mItem.setKImg(strImage);
                    break;
                case INDEX_O_IMG:
                    mItem.setOImg(strImage);
                    break;
                case INDEX_P_IMG:
                    mItem.setMsurpointsLcDrw(strImage);
                    break;
                case INDEX_ROUGHMAP:
                    mItem.setRogMap(strImage);
                    break;
            }

            mItem.setProgression(mInsertUtil.changeProgression(mItem.getProgression()));
            mItem.setInqDe(date);
            mItem.setInqSttus(inqSttus);
            mItem.save();
            loadingDialog(false, getString(R.string.empty));
        }

        return mBHasBitmap;
    }

    private void showErrorAlert() {
        int nResourceId = mBIsRoughmap ? R.string.str_invest_map : R.string.str_invest_photo;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvestInsertNcpImages.this);
        alertDialogBuilder
                .setTitle(R.string.str_invest_warning)
                .setMessage(nResourceId)
                .setPositiveButton(R.string.str_invest_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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


    @Click(R.id.btn_album)
    protected void onAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Click(R.id.btn_camera)
    protected void onCamera() {
        Uri uri = Uri.fromFile(new File(mImageFullName));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    @Click(R.id.btn_make_roughmap)
    protected void onMakeRoughmap() {
        if (exBOnline) {
            long npuSn = item.getNpuSn();
            Intent movePage = new Intent(getApplicationContext(), MapCaptureNcpActivity_.class);
            movePage.putExtra(getString(R.string.key_type), item.getPointsSecd().equals("0") ? getString(R.string.key_national_point_type_for_map_query) : getString(R.string.key_common_point_type_for_map_query));
            movePage.putExtra(getString(R.string.key_fid),  String.valueOf(npuSn));
            movePage.putExtra(getString(R.string.key_date), mDate);

            startActivityForResult(movePage, PICK_MAKE_ROUGHMAP);
        } else {
            Toast.makeText(getApplicationContext(), "오프라인 모드에서는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.btn_memo)
    protected void onDetail() {
        if (onSave(getString(R.string.code_invest_insert_ongoing))) {
            Intent intent = InvestInsertNcpImagesDetail_.intent(InvestInsertNcpImages.this).get();
            intent.putExtra(getString(R.string.key_title), textviewSubTitle.getText().toString());
            intent.putExtra(getString(R.string.key_file_name), mImageName);
            startActivityForResult(intent, PICK_MEMO);
        } else {
            showErrorAlert();
        }
    }

    //! @brief 현황조사 완료 버튼 이벤트 처리
    @Click(R.id.btn_complete)
    protected void onComplete() {
        if (onSave(getString(R.string.code_invest_insert_complete))) {
            startActivity(mInsertUtil.getResultIntent(true));
        } else {
            showErrorAlert();
        }
    }

    //! @brief 프로세스 버튼 이벤트 처리
    //! @param 클릭한 버튼
    @Click({R.id.imgbtn_mtr_info, R.id.imgbtn_mtr_sttus, R.id.imgbtn_rtk_able, R.id.imgbtn_etc, R.id.imgbtn_kimg, R.id.imgbtn_oimg, R.id.imgbtn_pimg, R.id.imgbtn_roughmap})
    protected void onProcessBtns(View view) {
        if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
            mInsertUtil.setmBHasBitmap(mBHasBitmap);
        }

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

    @Click(R.id.btnNavNext)
    public void onNext() {

        switch (exIndexInsert) {
            case INDEX_K_IMG:
                if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_oimg));
                break;
            case INDEX_O_IMG:
                if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_pimg));
                break;
            case INDEX_P_IMG:
                if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_roughmap));

                break;
            case INDEX_ROUGHMAP:
                onComplete();
                //btn_navNext.setVisibility(View.GONE);
                /*if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntent(R.id.imgbtn_roughmap));*/
                break;
        }
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
    @Click(R.id.btnNavBack)
    public void onBack() {

        switch (exIndexInsert) {
            case INDEX_K_IMG:
                /*if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntent(R.id.imgbtn_oimg));*/
                onBackPressed();
                break;
            case INDEX_O_IMG:
                if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_kimg));
                break;
            case INDEX_P_IMG:
                if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_oimg));

                break;
            case INDEX_ROUGHMAP:
                if (!onSave(getString(R.string.code_invest_insert_ongoing))) {
                    mInsertUtil.setmBHasBitmap(mBHasBitmap);
                }
                startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_pimg));
                break;
        }
    }



    //! @brief 앨범/카메라/메모하기 이후 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0 && requestCode == 0)  {
            AlertDialog.Builder builder = new AlertDialog.Builder(InvestInsertNcpImages.this);
            builder.setMessage("촬영한 이미지가 저장되지 않습니다 \n계속 하시겠습니까?");
            builder.setTitle("");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onCamera();
                }
            });
            builder.create().show();
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_FROM_ALBUM:
                    pickFromAlbum(data);
                    break;
                case PICK_FROM_CAMERA:
                    pickFromCamera();
                    break;
                case PICK_MAKE_ROUGHMAP:
                case PICK_MEMO:
                    pickMemo();
                    break;
            }
        }

    }

    private void pickFromAlbum(Intent data) {
        try {
            imageviewImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));

/*            if (mBIsRoughmap) {
                mAttacher.update();
            }*/

            textviewImageLabel.setVisibility(View.VISIBLE);
            mBHasBitmap = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pickFromCamera() {
         /*               BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImageFullName, bounds);*/
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(mImageFullName, opts);

        try {
            String strOrientation = new ExifInterface(mImageFullName).getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation;

            if (null == strOrientation) {
                orientation = ExifInterface.ORIENTATION_NORMAL;
            } else {
                orientation = Integer.parseInt(strOrientation);
            }

            int rotationAngle = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationAngle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationAngle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotationAngle = 270;
                    break;
            }

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = width;
            int newHeight = height;
            int maxResolution = 1600;
            float rate = 0.0f;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, width / 2, height / 2);

            if (width > height) {
                if (maxResolution < width) {
                    rate = maxResolution / (float)width;
                    newHeight = (int)(height * rate);
                    newWidth = maxResolution;
                }
            } else {
                if (maxResolution < height) {
                    rate = maxResolution / (float)height;
                    newWidth = (int)(width * rate);
                    newHeight = maxResolution;
                }
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight, matrix, true);

        } catch (IOException e) {
            Log.e(getString(R.string.iOException), e.getMessage());
        }

        imageviewImage.setImageBitmap(bitmap);
        textviewImageLabel.setVisibility(View.VISIBLE);
        mBHasBitmap = true;
    }

    private void pickMemo() {
        Bitmap bitmap = BitmapFactory.decodeFile(mImageFullName);

        if (null == bitmap) {
            mBHasBitmap = false;
        } else {
            mBHasBitmap = true;
            imageviewImage.setImageBitmap(bitmap);

            /*if (mBIsRoughmap) {
                mBHasBitmap = true;
                imageviewImage.setImageBitmap(bitmap);

            //    mAttacher.update();
            }*/
        }

    }
}
