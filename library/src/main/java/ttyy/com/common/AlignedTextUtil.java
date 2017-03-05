package ttyy.com.common;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

/**
 * Author: hujinqi
 * Date  : 2016-07-06
 * Description: TextView文字对齐格式工具
 */
public class AlignedTextUtil {

    static AlignedTextUtil mCurrInstance;

    /**
     * 对其标准文字长度
     */
    int mTextStandardLength;

    private AlignedTextUtil(int standardLength) {
        this.mTextStandardLength = standardLength;
    }

    public static AlignedTextUtil create(int maxStandLength) {

        if (mCurrInstance != null
                && mCurrInstance.mTextStandardLength == maxStandLength)
            return mCurrInstance;

        mCurrInstance = new AlignedTextUtil(maxStandLength);

        return mCurrInstance;
    }

    /**
     * 重新生成一个字符串用来进行对齐
     * @param text
     * @return
     */
    protected String formatStr(String text) {

        int length = text.length();
        // 保证插入的字的相对大小一定要小于原字体大小
        float relSize = (float) (mTextStandardLength - text.length()) / (text.length() - 1);
        // 当relSize > 1时那么就增加插入的字数数量
        int insertNum = (int) Math.ceil(relSize);
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < (length - 1); i++) {

            int index = i + insertNum * i + 1;
            for(int j = 0 ; j < insertNum ; j++){
                // 正 正方形字体大小很容易规范
                sb.insert(index, "正");
            }
        }

        return sb.toString();
    }

    /**
     * 格式化字符串
     * @param text
     * @return
     */
    public SpannableString formatText(String text) {

        if (TextUtils.isEmpty(text))
            return null;

        int length = text.length();
        if (length < 2
                || length >= mTextStandardLength)
            return new SpannableString(text);

        String formattedStr = formatStr(text);
        // 保证插入的字的相对大小一定要小于原字体大小
        float relSize = (float) (mTextStandardLength - text.length()) / (text.length() - 1);
        // 当relSize > 1时那么就增加插入的字数数量
        // Math.ceil 向上凑整 2.1 -> 3
        int insertNum = (int) Math.ceil(relSize);
        relSize = relSize / insertNum;
        SpannableString sbs = new SpannableString(formattedStr);
        for (int i = 0; i < (length - 1); i++) {

            int index = i + insertNum * i + 1;
            // 调整插入字的大小从而使文字显示上对齐
            sbs.setSpan(new RelativeSizeSpan(relSize),
                    index,
                    index + insertNum,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            // 插入的字调整透明色
            sbs.setSpan(new ForegroundColorSpan(Color.TRANSPARENT),
                    index,
                    index + insertNum,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return sbs;
    }

}
