package ttyy.com.common.text.selection;

/**
 * Author: hjq
 * Date  : 2018/09/08 14:11
 * Name  : SelectionInfo
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SelectionInfo {

    int mStart;
    int mEnd;

    SelectionInfo(){
        mStart = -1;
        mEnd = -1;
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(int mStart) {
        this.mStart = mStart;
    }

    public int getEnd() {
        return mEnd;
    }

    public void setEnd(int mEnd) {
        this.mEnd = mEnd;
    }

}
