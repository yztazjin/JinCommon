package ttyy.com.common;
        
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * author: admin
 * date: 2017/1/22 0022
 * version: 0
 * mail: hujinqi@hy.com
 * desc: ShellUtil
 */

public class ShellUtil {

    private static final String COMMAND_SU = "su";// root
    private static final String COMMAND_SH = "sh";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_NEW_LINE = "\n";

    private List<String> mCmds;
    private boolean isRootCmd;
    private boolean isNeedResult;
    private CmdCallback mCmdCallback;

    private ShellUtil() {
        mCmds = new ArrayList<>();
        isRootCmd = false;
        isNeedResult = true;
        mCmdCallback = null;
    }

    public static ShellUtil get() {
        return new ShellUtil();
    }

    /**
     * 添加命令
     *
     * @param cmd
     * @return
     */
    public ShellUtil addCmd(String cmd) {
        mCmds.add(cmd);
        return this;
    }

    /**
     * 设置是否Root模式
     *
     * @param isRootCmd
     * @return
     */
    public ShellUtil setIsRootCmd(boolean isRootCmd) {
        this.isRootCmd = isRootCmd;
        return this;
    }

    /**
     * 设置是否需要结果信息
     *
     * @param isNeedResult
     * @return
     */
    public ShellUtil setIsNeedResult(boolean isNeedResult) {
        this.isNeedResult = isNeedResult;
        return this;
    }

    /**
     * 设置结果回调
     *
     * @param callback
     * @return
     */
    public ShellUtil setCmdCallback(CmdCallback callback) {
        this.mCmdCallback = callback;
        return this;
    }

    /**
     * 同步执行
     */
    public CmdResult exec() {
        return execCmds(isRootCmd, isNeedResult, mCmds);
    }

    /**
     * 异步执行
     */
    public void execAysnc() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                exec();
            }
        }).start();
    }

    /**
     * 执行命令
     *
     * @param isRootCmd
     * @param isNeedResult
     * @param cmds
     */
    private CmdResult execCmds(boolean isRootCmd, boolean isNeedResult, List<String> cmds) {

        Process proc = null;

        try {
            proc = Runtime.getRuntime().exec(isRootCmd ? COMMAND_SU : COMMAND_SH);
            DataOutputStream dos = new DataOutputStream(proc.getOutputStream());
            for (String cmd : cmds) {
                dos.writeBytes(cmd);
                dos.writeBytes(COMMAND_NEW_LINE);
                dos.flush();
            }
            dos.writeBytes(COMMAND_EXIT);
            dos.writeBytes(COMMAND_NEW_LINE);
            dos.flush();
            dos.close();

            // 命令执行结果
            // 等待程序执行结果，如果不获取结果 可能命令执行失效
            int resultCode = proc.waitFor();

            if (isNeedResult) {
                CmdResult result = new CmdResult();
                result.nResultCode = resultCode;

                BufferedReader success_reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                StringBuilder success_builder = new StringBuilder();
                String line = "";
                while ((line = success_reader.readLine()) != null) {
                    success_builder.append(line);
                    success_builder.append("\n");
                }
                success_reader.close();

                BufferedReader error_reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                StringBuilder error_builder = new StringBuilder();
                while ((line = error_reader.readLine()) != null) {
                    error_builder.append(line);
                    error_builder.append("\n");
                }
                error_reader.close();

                result.strSuccessMessage = success_builder.toString();
                result.strErrorMessage = error_builder.toString();

                if (mCmdCallback != null) {
                    mCmdCallback.onCmdResult(result);
                }

                return result;
            }

        } catch (IOException e) {
            Log.w("Shell", e.getMessage());
        } catch (InterruptedException e) {
            Log.w("Shell", e.getMessage());
        } finally {
            if (proc != null) {
                proc.destroy();
            }
        }

        return null;
    }

    /**
     * 系统是否被Root
     *
     * @return
     */
    protected boolean isSystemRooted() {
        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception exception) {
            // ignore
            exception.printStackTrace();
        }
        String[] commands = {
                "/system/xbin/which su",
                "/system/bin/which su",
                "which su"
        };
        for (String command : commands) {
            try {
                Runtime.getRuntime().exec(command);
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 命令行结果信息
     */
    public static class CmdResult {

        private int nResultCode = -1;
        private String strSuccessMessage;
        private String strErrorMessage;

        public int getResultCode() {
            return nResultCode;
        }

        public String getSuccessMsg() {
            return strSuccessMessage;
        }

        public String getErrorMsg() {
            return strErrorMessage;
        }

        public boolean isSuccess() {
            return nResultCode == 0;
        }
    }

    /**
     * 命令行执行回调
     */
    public interface CmdCallback {
        void onCmdResult(CmdResult result);
    }

}
