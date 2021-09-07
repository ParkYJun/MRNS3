package com.mrns.invest.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrns.invest.adapter.PenAdapter;
import com.mrns.invest.util.DrawingView;
import com.mrns.invest.util.EmulatorPath;
import com.mrns.invest.util.FileUtil;
import com.mrns.invest.util.FineDialog;
import com.mrns.invest.util.ImageUtil;
import com.mrns.main.R;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import team.uptech.motionviews.ui.StickerSelectActivity;
import team.uptech.motionviews.ui.TextEditorDialogFragment;
import team.uptech.motionviews.ui.adapter.FontsAdapter;
import team.uptech.motionviews.utils.FontProvider;
import team.uptech.motionviews.viewmodel.Font;
import team.uptech.motionviews.viewmodel.Layer;
import team.uptech.motionviews.viewmodel.TextLayer;
import team.uptech.motionviews.widget.entity.ImageEntity;
import team.uptech.motionviews.widget.entity.MotionEntity;
import team.uptech.motionviews.widget.entity.PathEntity;
import team.uptech.motionviews.widget.entity.TextEntity;


@EActivity(R.layout.activity_invest_insert_image_detail)
public class InvestInsertNcpImagesDetail extends AppCompatActivity
        implements TextEditorDialogFragment.OnTextLayerCallback {
    private String title;
    private String fileName;

    @ViewById(R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(R.id.layout_image_wrap)
    RelativeLayout layoutImageWrap;
    @ViewById(R.id.layout_image)
    RelativeLayout layoutImage;
    @ViewById(R.id.imageview_image)
    ImageView imageviewImage;
    @ViewById(R.id.btn_pen)
    ImageButton btnPen;
    @ViewById(R.id.btn_erase)
    ImageButton btnErase;
    @ViewById(R.id.layout_pen_options)
    LinearLayout layoutPentOptions;
    @ViewById(R.id.layout_text_options)
    LinearLayout layoutTextOptions;
    @ViewById(R.id.layout_icon_options)
    LinearLayout layoutIconOptions;

    DrawingView mView;

    private final static int MODE_USE_PEN = 0;
    private final static int MODE_ADD_TEXT = 1;
    private final static int MODE_ADD_ICON = 2;
    private final static int MODE_USE_ERASE = 3;

    public static final int SELECT_STICKER_REQUEST_CODE = 123;

    private final DrawingView.DrawingViewCallback drawingViewCallback = new DrawingView.DrawingViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
            if (!(entity instanceof PathEntity)) {
                int drawingMode;

                if (null == entity && null != mView) {
                    drawingMode = mView.getmDrawingMode();

                    if (MODE_ADD_TEXT == drawingMode || MODE_ADD_ICON == drawingMode) {
                        drawingMode = MODE_USE_PEN;
                    }
                } else if (entity instanceof TextEntity) {
                    drawingMode = MODE_ADD_TEXT;
                } else {
                    drawingMode = MODE_ADD_ICON;
                }

                setBtnsAndLayout(drawingMode);
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            onTextEdit();

        }
    };
    private FontProvider fontProvider;

    @AfterViews
    protected void InitView() {
        fileName = getIntent().getStringExtra(getString(R.string.key_file_name));
        title = getIntent().getStringExtra(getString(R.string.key_title));
        fontProvider = new FontProvider(getResources());

        ViewTreeObserver vto = layoutImageWrap.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    layoutImageWrap.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else {
                    layoutImageWrap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                InputStream open;
                try {
                    String filePath = EmulatorPath.getPath() + File.separator + getString(R.string.mrns) + File.separator;
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath + fileName + getResources().getString(R.string.f_jpg));

                    if(bitmap == null) {
                        open = getAssets().open(getString(R.string.no_image_file));
                        bitmap = BitmapFactory.decodeStream(open);
                    }

                    int sizeValue1 = Integer.valueOf(getString(R.string.imagesize_3));
                    int sizeValue2 = Integer.valueOf(getString(R.string.imagesize_4));

                    if(bitmap.getWidth()  > bitmap.getHeight()) {
                        int temp = sizeValue1;
                        sizeValue1 = sizeValue2;
                        sizeValue2 = temp;
                    }

                    HashMap<String,Integer> sizeValues = ImageUtil.getBitmapSize(bitmap, sizeValue1, sizeValue2);

                    int width = sizeValues.get(getString(R.string.key_width));
                    int height = sizeValues.get(getString(R.string.key_height));

                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    imageviewImage.setImageBitmap(bitmap);

                    mView = new DrawingView(InvestInsertNcpImagesDetail.this, sizeValues);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }

                    layoutImage.addView(mView, new RelativeLayout.LayoutParams(width, height));
                    layoutImage.getLayoutParams().width = width;
                    layoutImage.getLayoutParams().height = height;
                    mView.setmDrawingViewCallback(drawingViewCallback);
                }
                catch (IOException e) {
                    Log.e(getString(R.string.iOException), "이미지 없음");
                }
            }
        });

        textviewSubTitle.setText(title);
        setBtnsAndLayout(MODE_USE_PEN);
    }

    @UiThread
    public void loadingDialog(boolean view, String message) {
        if (view)
            FineDialog.showProgress(InvestInsertNcpImagesDetail.this, false, message);
        else
            FineDialog.hideProgress();
    }

    //! 상단 버튼들 클릭 이벤트

    @Click({R.id.btn_back_home, R.id.layout_title})
    public void onBack() {
        onBackPressed();
    }

    @Click({R.id.imagebtn_delete_text, R.id.imagebtn_delete_icon})
    public void onDeleteEntity() {
        MotionEntity entity = mView.getmSelectedEntity();

        if (null != entity) {
            mView.deleteEntity();
        }
    }

    @Click(R.id.imagebtn_save)
    public void onSave() {
        //loadingDialog(true, getResources().getString(R.string.str_invest_saveloading));

        if (null != mView) {
            mView.selectEntity(null, true);
            mView.destroyDrawingCache();
        }

        layoutImage.destroyDrawingCache();
        layoutImage.buildDrawingCache();

        Bitmap captureView = layoutImage.getDrawingCache();
        FileUtil.saveBitmaptoJpeg(captureView, getString(R.string.mrns), fileName);
        //loadingDialog(false, getString(R.string.empty));
        //
        setResult(RESULT_OK, getIntent());
        finish();
    }

    //! 하단 버튼들 클릭 이벤트

    @Click(R.id.btn_pen)
    public void onPen() {
        if (null != mView) {
            mView.selectEntity(null, true);
        }
        //mView.getmSelectedEntity().setIsSelected(false);
        setBtnsAndLayout(MODE_USE_PEN);
    }

    @Click(R.id.btn_text)
    public void onText() {
        if (null != mView) {
            mView.selectEntity(null, false);
        }

        setBtnsAndLayout(MODE_ADD_TEXT);
        addText();
    }

    @Click(R.id.btn_icon)
    public void onIcon() {
        if (null != mView) {
            mView.selectEntity(null, false);
        }

        setBtnsAndLayout(MODE_ADD_ICON);
        Intent intent = new Intent(this, StickerSelectActivity.class);
        startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
    }

    @Click(R.id.btn_erase)
    public void onErase() {
        mView.selectEntity(null, true);
        setBtnsAndLayout(MODE_USE_ERASE);
    }

    @Click(R.id.btn_undo)
    public void undo() {
        mView.onUndo();
    }

    @Click(R.id.btn_redo)
    public void redo() {
        mView.onRedo();
    }

    @Click(R.id.btn_clear)
    public void onClear() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvestInsertNcpImagesDetail.this);
        alertDialogBuilder
                .setTitle(R.string.str_detail_claertitle)
                .setMessage(R.string.str_detail_claermessage)
                .setPositiveButton(R.string.str_invest_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onClear();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.str_invest_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //! 펜 옵션 버튼들 클릭 이벤트 처리
    @Click({R.id.btn_pen_color, R.id.imagebtn_font_color})
    public void onPenColor(View view) {
        SpectrumDialog.Builder spectBuilder = new SpectrumDialog.Builder(getApplicationContext());
        String strTitle = getString(R.string.select_font_color);
        final boolean bIsPenColor;

        if (R.id.btn_pen_color == view.getId()) {
            strTitle = getString(R.string.select_pen_color);
            bIsPenColor = true;

            if (null != mView) {
                spectBuilder.setSelectedColor(mView.getmPenColor());
            }
        } else {
            bIsPenColor = false;
            TextEntity textEntity = currentTextEntity();

            if (textEntity != null) {
                spectBuilder.setSelectedColor(textEntity.getLayer().getFont().getColor());
            }
        }

        spectBuilder.setColors(getResources().getIntArray(R.array.array_color_id))
                .setOutlineWidth(1)
                .setTitle(strTitle)
                .setNegativeButtonText(getString(R.string.str_invest_cancel))
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (bIsPenColor) {
                            mView.onChangePenColor(color);
                        } else {
                            TextEntity textEntity = currentTextEntity();

                            if (textEntity != null) {
                                textEntity.getLayer().getFont().setColor(color);
                                textEntity.updateEntity();
                                mView.invalidate();
                            }
                        }

                    }
                })
                .build()
                .show(getSupportFragmentManager(), getString(R.string.empty));
    }

    @Click({R.id.btn_pen_type, R.id.btn_dash_type, R.id.btn_pen_width})
    public void onPenOptions(View view) {
        final int viewId = view.getId();
        String strTitle;
        int optionIndex;

        switch (viewId) {
            case R.id.btn_pen_type:
                strTitle = getString(R.string.select_pen_type);
                optionIndex = getResources().getInteger(R.integer.index_pen_type);
                break;
            case R.id.btn_dash_type:
                strTitle = getString(R.string.select_dash_type);
                optionIndex = getResources().getInteger(R.integer.index_dash_type);
                break;
            case R.id.btn_pen_color:
                strTitle = getString(R.string.select_pen_color);
                optionIndex = getResources().getInteger(R.integer.index_pen_color);
                break;
            default:
                strTitle = getString(R.string.select_pen_width);
                optionIndex = getResources().getInteger(R.integer.index_pen_width);
                break;
        }

        PenAdapter penAdapter = new PenAdapter(this, optionIndex);

        new AlertDialog.Builder(this)
                .setTitle(strTitle)
                .setAdapter(penAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (viewId) {
                            case R.id.btn_pen_type:
                                mView.onChangePenType(which);
                                break;
                            case R.id.btn_dash_type:
                                mView.onChangeDashType(which);
                                break;
                            case R.id.btn_pen_color:
                                mView.onChangePenColor(which);
                                break;
                            default:
                                mView.onChangePenWidth(which);
                                break;
                        }
                    }
                }).show();
    }

    //! 텍스트 옵션 버튼들 클릭 이벤트 처리

    @Click({R.id.imagebtn_font_size_decrease, R.id.imagebtn_font_size_increase})
    public void onFontSize(View view) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null && mView != null) {
            if (R.id.imagebtn_font_size_decrease == view.getId()) {
                textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
            } else {
                textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
            }

            textEntity.updateEntity();
            mView.invalidate();
        }
    }

    @Click(R.id.imagebtn_font_type)
    public void onFontType() {
        final List<String> fonts = fontProvider.getFontNames();
        FontsAdapter fontsAdapter = new FontsAdapter(this, fonts, fontProvider);
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_font)
                .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null && mView != null) {
                            textEntity.getLayer().getFont().setTypeface(fonts.get(which));
                            textEntity.updateEntity();
                            mView.invalidate();
                        }
                    }
                })
                .show();
    }

    @Click(R.id.imagebtn_text_edit)
    public void onTextEdit() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }
        Toast.makeText(getApplicationContext(), getString(R.string.information_add_text), Toast.LENGTH_SHORT).show();


    }

    //! @brief drawing 모드에 따라 버튼과 레이아웃을 컨트롤
    private void setBtnsAndLayout(int mode) {
        int btnPenImageResource = R.drawable.btn_mapedit_pen_p;
        int btnEraseImageResource = R.drawable.btn_mapedit_eraser_p;
        int layoutPenVisibility = View.GONE;
        int layoutTextVisibility = View.GONE;
        int layoutIconVisibility = View.GONE;
        int drawingMode = MODE_USE_PEN;
        boolean notAddMode = false;

        switch (mode) {
            case MODE_USE_PEN:
                btnPenImageResource = R.drawable.btn_mapedit_pen_n;
                layoutPenVisibility = View.VISIBLE;
                notAddMode = true;
                break;
            case MODE_ADD_TEXT:
                layoutTextVisibility = View.VISIBLE;
                drawingMode = MODE_ADD_TEXT;
                break;
            case MODE_ADD_ICON:
                layoutIconVisibility = View.VISIBLE;
                drawingMode = MODE_ADD_ICON;
                break;
            case MODE_USE_ERASE:
                btnEraseImageResource = R.drawable.btn_mapedit_eraser_n;
                drawingMode = MODE_USE_ERASE;
                notAddMode = true;

                break;
        }

        btnPen.setImageResource(btnPenImageResource);
        btnErase.setImageResource(btnEraseImageResource);
        layoutPentOptions.setVisibility(layoutPenVisibility);
        layoutTextOptions.setVisibility(layoutTextVisibility);
        layoutIconOptions.setVisibility(layoutIconVisibility);

        if (null != mView) {
            mView.onChangeDrawingMode(drawingMode);

            if (notAddMode) {
                mView.unselectEntity();
            }
        }
    }

    private void addIcon(final int stickerResId) {
        mView.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();
                Bitmap pica = BitmapFactory.decodeResource(getResources(), stickerResId);

                ImageEntity entity = new ImageEntity(layer, pica, mView.getWidth(), mView.getHeight());

                mView.addEntityAndPosition(entity);
            }
        });
    }

    protected void addText() {

        if (mView != null) {
            TextLayer textLayer = createTextLayer();
            TextEntity textEntity = new TextEntity(textLayer, mView.getWidth(),
                    mView.getHeight(), fontProvider);
            mView.addEntityAndPosition(textEntity);

            // move text sticker up so that its not hidden under keyboard
            PointF center = textEntity.absoluteCenter();
            center.y = center.y * 0.5F;
            textEntity.moveCenterTo(center);

            // redraw
            mView.invalidate();

            onTextEdit();
        }
    }

    private TextLayer createTextLayer() {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);
        textLayer.setText("");
//        textLayer.setText(getString(R.string.information_add_text));


        return textLayer;
    }

    private TextLayer createTextLayer1() {
        TextLayer textLayer = new TextLayer();

        Font font = new Font();

        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);
        textLayer.setText("");


        return textLayer;
    }

    @Nullable
    private TextEntity currentTextEntity() {
        if (mView != null && mView.getmSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) mView.getmSelectedEntity());
        } else {
            return null;
        }
    }

    @Override
    public void textChanged(@NonNull String text) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null && mView != null) {
            TextLayer textLayer = textEntity.getLayer();
            if (!text.equals(textLayer.getText())) {
                textLayer.setText(text);
                textEntity.updateEntity();
                mView.invalidate();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_STICKER_REQUEST_CODE) {
                if (data != null) {
                    int stickerId = data.getIntExtra(StickerSelectActivity.EXTRA_STICKER_ID, 0);

                    if (stickerId != 1) {
                        addIcon(stickerId);
                    }
                }
            }
        }
    }
}
