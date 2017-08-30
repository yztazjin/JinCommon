package ttyy.com.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * author: admin
 * date: 2017/2/4 0004
 * version: 0
 * mail: hujinqi@hy.com
 * desc: AppPromise
 */
public class AppPromise {

    private static final int MAX_VALUE = 20;

    /* 线程安全 */
    private BlockingDeque<Action> mProQueue;

    private Action mErrorAction;

    private boolean isBreak;
    // 所有任务都完成后 自动结束还是继续等待新任务
    private boolean isAutoFinish;

    private Handler mHandler;

    private LoopingThread mLoopingThread;

    private AppPromise() {
        mProQueue = new LinkedBlockingDeque<>(MAX_VALUE);
        mHandler = new Handler(Looper.getMainLooper());
        isAutoFinish = true;
    }

    public static AppPromise get() {

        return new AppPromise();
    }

    public AppPromise setAutoFinish(boolean value){
        this.isAutoFinish = value;
        return this;
    }

    public final AppPromise then(Action action) {
        if (action == null) {
            return this;
        }
        action.setActionType(Action.NOR);
        action.bind(this);
        mProQueue.add(action);
        return this;
    }

    public final AppPromise then(Action action, Action error) {
        if (action != null) {
            error.setActionType(Action.ERR);
            action.setSelfErrorAction(error);
            then(action);
        }
        return this;
    }

    public final AppPromise err(Action error) {
        if (error == null) {
            return this;
        }

        error.setActionType(Action.ERR);
        error.bind(this);
        mErrorAction = error;
        return this;
    }

    public final void start() {
        if (mLoopingThread == null || isBreak) {
            isBreak = false;
            mLoopingThread = new LoopingThread();
            mLoopingThread.start();
        }
    }

    public final void stop() {
        isBreak = true;

        if (mProQueue.size() == 0) {
            mProQueue.add(EMPTY);
        }

        mHandler.removeCallbacks(null);
    }

    protected Action getProQueueFirstAction() {
        return mProQueue.getFirst();
    }

    protected Action getErrorAction() {
        return mErrorAction;
    }

    protected Handler getHandler() {
        return mHandler;
    }

    private class LoopingThread extends Thread {
        @Override
        public void run() {
            super.run();
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            while (true) {
                try {

                    // 获取不到时会阻塞
                    Action proAction = mProQueue.take();

                    if (isBreak && !proAction.isTypeErr()) {
                        break;
                    }

                    if (proAction.isTypeErr()) {
                        isBreak = true;
                        if(mProQueue.size() == 0){
                            mProQueue.add(EMPTY);
                        }
                    }

                    execute(proAction);

                    if(isAutoFinish
                            && mProQueue.size() == 0){
                        Log.i("AppPromise", "tasks empty and auto finish looping");
                        break;
                    }

                } catch (InterruptedException e) {
                    Log.w("P", "Looping Thread Is Interrupted!");
                }
            }

            mProQueue.clear();
            mErrorAction = null;

        }

        private void execute(Action action) throws InterruptedException {

            if (action == null) {
                return;
            }

            if (action instanceof UI) {

                synchronized (this) {
                    mHandler.postDelayed(action, action.mDelayedMillions);

                    wait();
                }

            } else if (action instanceof IO) {

                action.run();
            }
        }

    }

    private Action EMPTY = new Action() {
        @Override
        public void run(Object... objs) {

        }
    };

    protected static abstract class Action implements Runnable {

        private static final int NOR = 0;
        private static final int ERR = 1;

        private int mActionType = NOR;
        protected long mDelayedMillions;
        protected AppPromise mPromise;
        protected Object[] mParams;
        protected Action mSelfErrorAction;

        public abstract void run(Object... objs);

        @Override
        public void run() {
            run(mParams);
        }

        private Action bind(AppPromise promise) {
            mPromise = promise;
            return this;
        }

        private Action bindParams(Object... objs) {
            mParams = objs;
            return this;
        }

        private Action setActionType(int type) {
            this.mActionType = type;
            return this;
        }

        private Action setSelfErrorAction(Action mSelfErrorAction) {
            this.mSelfErrorAction = mSelfErrorAction;
            return this;
        }

        private boolean isTypeErr() {
            return mActionType == ERR;
        }

        protected final void then(Object... objs) {
            if (mPromise.getProQueueFirstAction() != null) {
                mPromise.getProQueueFirstAction().bindParams(objs);
            }
        }

        protected final void error(Object... objs) {

            Action errorAction = mPromise.getErrorAction();
            if (mSelfErrorAction != null) {
                errorAction = mSelfErrorAction;
            }

            if (errorAction != null) {
                errorAction.bindParams(objs);
                mPromise.mProQueue.addFirst(errorAction);
            }

        }

        protected final void breakAll() {
            mPromise.stop();
        }
    }

    public static abstract class UI extends Action {

        protected UI setDelayMillions(long millions) {
            this.mDelayedMillions = millions;
            return this;
        }

        @Override
        public final void run() {
            super.run();

            if (mPromise.mLoopingThread != null) {
                synchronized (mPromise.mLoopingThread) {
                    // 唤醒继续执行
                    mPromise.mLoopingThread.notify();
                }
            }
        }
    }

    public static abstract class IO extends Action {
        @Override
        public final void run() {
            super.run();
        }
    }


}
