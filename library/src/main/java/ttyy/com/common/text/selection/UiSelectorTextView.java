package ttyy.com.common.text.selection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import ttyy.com.common.text.selection.base.CursorType;
import ttyy.com.common.text.selection.base.OffsetType;
import ttyy.com.common.text.selection.base.UiSeResources;
import ttyy.com.common.text.selection.base.UiSelector;

/**
 * Author: hjq
 * Date  : 2018/09/09 09:20
 * Name  : UiSelectorTextView
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class UiSelectorTextView implements UiSelector {

    TextView mTextView;

    SelectionInfo mSelectionInfo;
    UiSeResources mResources;

    BackgroundColorSpan mSpan;

    public static UiSelector wrap(TextView tv){
        UiSelectorTextView utv = new UiSelectorTextView(tv);
        utv.initialize();

        utv.mResources = new UiSeResourceDefault();
        utv.mResources.setUiSelector(utv);

        return utv;
    }

    private UiSelectorTextView(TextView tv){
        mTextView = tv;
        mSelectionInfo = new SelectionInfo();
    }

    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    ViewTreeObserver.OnPreDrawListener mOnPreDrawListener;
    ViewTreeObserver.OnWindowAttachListener mOnWindowAttachListener;
    float mTouchX;
    float mTouchY;
    boolean mIsViewTreeScrolling;

    static final int DELAY_DURATION = 100;
    Handler mUiHandler = new Handler(Looper.getMainLooper());
    Runnable mShowCursorAndMenuWork = new Runnable() {
        @Override
        public void run() {

            showUiCursors();
            showUiMenu();

        }
    };
    private void initialize(){
        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!mIsViewTreeScrolling
                || getUiResources().getUiCursor(CursorType.Right).isShowing()
                        || getUiResources().getUiCursor(CursorType.Left).isShowing()){

                    mIsViewTreeScrolling = true;

                   dismissUiCursors();
                   dismissUiMenu();
                }

            }
        };

        mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mIsViewTreeScrolling){
                    mIsViewTreeScrolling = false;
                    mUiHandler.removeCallbacks(mShowCursorAndMenuWork);
                    mUiHandler.postDelayed(mShowCursorAndMenuWork, DELAY_DURATION);
                }
                return true;
            }
        };

        mOnWindowAttachListener = new ViewTreeObserver.OnWindowAttachListener() {
            @Override
            public void onWindowAttached() {

            }

            @Override
            public void onWindowDetached() {
                destroy();
            }
        };

        mTextView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
        mTextView.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
        mTextView.getViewTreeObserver().addOnWindowAttachListener(mOnWindowAttachListener);

        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dismissUiCursors();
                dismissUiMenu();

                int position = mTextView.getOffsetForPosition(mTouchX, mTouchY);
                if (position < 0
                        || position > mTextView.getText().length() - 1){
                    return true;
                }
                setUiSelection(position, position + 1);
                mShowCursorAndMenuWork.run();

                return true;
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTextView.getOffsetForPosition(mTouchX, mTouchY);

                if (position < mSelectionInfo.getStart()
                        || position > mSelectionInfo.getEnd()){

                    clearUiSelection();
                    dismissUiCursors();
                    dismissUiMenu();
                }
            }
        });

        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTouchX = event.getX();
                mTouchY = event.getY();
                return false;
            }
        });

    }

    @Override
    public Context getContext() {
        return mTextView == null ? null : mTextView.getContext();
    }

    @Override
    public SelectionInfo getSelectionInfo() {
        return mSelectionInfo;
    }

    @Override
    public void setUiSelection(int start, int end) {
        if (start < 0
                || end < 0){
            clearUiSelection();
            return;
        }

        clearUiSelection();

        mSelectionInfo.setStart(start);
        mSelectionInfo.setEnd(end);
        if (!(mTextView.getText() instanceof Spannable)){
            mTextView.setText(mTextView.getText(), TextView.BufferType.SPANNABLE);
        }

        Spannable mSpannable = (Spannable) mTextView.getText();
        mSpan = new BackgroundColorSpan(getUiResources().getHighLightColor());

        mSpannable.setSpan(mSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void updateUiSelectionOffsetFromRawPoint(OffsetType type, float rawX, float rawY) {
        int[] point = getRawPointForRealView();

        rawX = rawX - point[0] - mTextView.getPaddingLeft();
        rawY = rawY - point[1] - mTextView.getPaddingTop();

        updateUiSelectionOffsetFromRelPoint(type, rawX, rawY);
    }

    @Override
    public void updateUiSelectionOffsetFromRelPoint(OffsetType type, float relX, float relY) {
        int oldStart = getSelectionInfo().getStart();
        int oldEnd = getSelectionInfo().getEnd();

        int position = mTextView.getOffsetForPosition(relX, relY);
        switch (type){
            case Start:
                if (position > oldEnd - 1){
                    position = oldEnd - 1;
                }

                if (position != oldStart){
                    setUiSelection(position, oldEnd);
                    int start = getSelectionInfo().getStart();
                    float[] startRawPoint = getRawPointForSelectionOffset(start);
                    getUiResources().getUiCursor(CursorType.Left)
                            .updateRawPoint(startRawPoint[0], startRawPoint[1]);
                }

                break;
            case End:
                if (position < oldStart + 1){
                    position = oldStart + 1;
                }

                if (position != oldEnd){
                    setUiSelection(oldStart, position);
                    int end = getSelectionInfo().getEnd();
                    float[] endRawPoint = getRawPointForSelectionOffset(end - 1);
                    float delta = mTextView.getPaint().measureText(mTextView.getText(), end-1, end);
                    getUiResources().getUiCursor(CursorType.Right)
                            .updateRawPoint(endRawPoint[0] + delta, endRawPoint[1]);
                }


                break;
        }

    }

    private void clearUiSelection(){
        if (!(mTextView.getText() instanceof Spannable)){
            mTextView.setText(mTextView.getText(), TextView.BufferType.SPANNABLE);
        }

        if (mSpan != null){
            Spannable mSpannable = (Spannable) mTextView.getText();
            mSpannable.removeSpan(mSpan);
        }

        mSelectionInfo.setStart(-1);
        mSelectionInfo.setEnd(-1);
    }

    @Override
    public UiSeResources getUiResources() {
        return mResources;
    }

    @Override
    public float[] getRelPointForSelectionOffset(int offset) {
        float[] point = new float[2];

        Layout layout = mTextView.getLayout();

        point[0] = layout.getPrimaryHorizontal(offset);
        point[1] = layout.getLineBottom(layout.getLineForOffset(offset));

        return point;
    }

    @Override
    public float[] getRawPointForSelectionOffset(int offset) {
        float[] relPoint = getRelPointForSelectionOffset(offset);

        int[] rawPoint = getRawPointForRealView();

        relPoint[0] += rawPoint[0] + mTextView.getPaddingLeft();
        relPoint[1] += rawPoint[1] + mTextView.getPaddingTop();

        return relPoint;
    }

    @Override
    public int[] getRawPointForRealView() {
        int[] point = new int[2];
        mTextView.getLocationOnScreen(point);
        return point;
    }

    @Override
    public void showUiCursors() {
        int start = getSelectionInfo().getStart();
        float[] startRawPoint = getRawPointForSelectionOffset(start);
        getUiResources().getUiCursor(CursorType.Left)
                .showAtRawPoint(mTextView, startRawPoint[0], startRawPoint[1]);

        int end = getSelectionInfo().getEnd();
        float[] endRawPoint = getRawPointForSelectionOffset(end - 1);
        float delta = mTextView.getPaint().measureText(mTextView.getText(), end-1, end);
        getUiResources().getUiCursor(CursorType.Right)
                .showAtRawPoint(mTextView, endRawPoint[0] + delta, endRawPoint[1]);

    }

    @Override
    public void dismissUiCursors() {
        getUiResources().getUiCursor(CursorType.Right).dismiss();
        getUiResources().getUiCursor(CursorType.Left).dismiss();
    }

    @Override
    public void showUiMenu() {
        int start = getSelectionInfo().getStart();
        float[] startRawPoint = getRawPointForSelectionOffset(start);

        int end = getSelectionInfo().getEnd();
        float[] endRawPoint = getRawPointForSelectionOffset(end - 1);

        float rawMenuX = 0;
        if (endRawPoint[1] != startRawPoint[1]){
            rawMenuX = mTextView.getWidth() / 2 + getRawPointForRealView()[0];
        }else {
            rawMenuX = (endRawPoint[0] + startRawPoint[0]
                    + getUiResources().getUiCursor(CursorType.Left).getCursorWidth()) / 2;
        }

        float rawMenuY = startRawPoint[1] - mTextView.getLineHeight();
        getUiResources().getUiMenu().showAtRawPoint(mTextView, rawMenuX, rawMenuY);
    }

    @Override
    public void dismissUiMenu() {
        getUiResources().getUiMenu().dismiss();
    }

    @Override
    public void destroy() {

    }
}
