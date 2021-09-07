package team.uptech.motionviews.utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * extracting Typeface from Assets is a heavy operation,
 * we want to make sure that we cache the typefaces for reuse
 */
public class FontProvider {

    private static final String DEFAULT_FONT_NAME = "서울한강체";

    private final Map<String, Typeface> typefaces;
    private final Map<String, String> fontNameToTypefaceFile;
    private final Resources resources;
    private final List<String> fontNames;

    public FontProvider(Resources resources) {
        this.resources = resources;

        typefaces = new HashMap<>();

        // populate fonts
        fontNameToTypefaceFile = new HashMap<>();
        fontNameToTypefaceFile.put("부산체", "BusanFont_Provisional.ttf");
        fontNameToTypefaceFile.put("제주고딕", "JejuGothic.ttf");
        fontNameToTypefaceFile.put("제주명조", "JejuMyeongjo.ttf");
        fontNameToTypefaceFile.put("나눔바른펜B", "NanumBarunpenB.ttf");
        fontNameToTypefaceFile.put("나눔고딕B", "NanumGothicBold.ttf");
        fontNameToTypefaceFile.put("나눔손글씨", "NanumPen.ttf");
        fontNameToTypefaceFile.put("서울한강체", "SeoulHangangM.ttf");
        fontNameToTypefaceFile.put("서울남산체", "SeoulNamsanM.ttf");
        fontNameToTypefaceFile.put("경기천년바탕B", "GyoungGi.ttf");

        fontNames = new ArrayList<>(fontNameToTypefaceFile.keySet());
    }

    /**
     * @param typefaceName must be one of the font names provided from {@link FontProvider#getFontNames()}
     * @return the Typeface associated with {@code typefaceName}, or {@link Typeface#DEFAULT} otherwise
     */
    public Typeface getTypeface(@Nullable String typefaceName) {
        if (TextUtils.isEmpty(typefaceName)) {
            return Typeface.DEFAULT;
        } else {
            //noinspection Java8CollectionsApi
            if (typefaces.get(typefaceName) == null) {
                typefaces.put(typefaceName,
                        Typeface.createFromAsset(resources.getAssets(), "font/" + fontNameToTypefaceFile.get(typefaceName)));
            }
            return typefaces.get(typefaceName);
        }
    }

    /**
     * use {@link FontProvider#getTypeface(String) to get Typeface for the font name}
     *
     * @return list of available font names
     */
    public List<String> getFontNames() {
        return fontNames;
    }

    /**
     * @return Default Font Name - <b>Helvetica</b>
     */
    public String getDefaultFontName() {
        return DEFAULT_FONT_NAME;
    }
}