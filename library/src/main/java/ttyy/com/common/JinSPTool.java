package ttyy.com.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by justin on 15/12/29.
 */
public class JinSPTool {

    protected SharedPreferences sp;

    private JinSPTool(String name, Context context){
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 新建指定名称的sharepreferences
     * @param name
     * @param context
     * @return
     */
    public static JinSPTool create(String name, Context context){
        JinSPTool ppsp = new JinSPTool(name,context);
        return ppsp;
    }

    /**
     * 新建默认的sharepreferences
     * 名称默认包名（.替换为_）
     * @param context
     * @return
     */
    public static JinSPTool createDefault(Context context){
        String name = context.getPackageName().replaceAll(".", "_");
        JinSPTool ppsp = create(name,context);
        return ppsp;
    }

    public final JinSPTool putString(String key, String value){
        value = value == null ? "":value;
        sp.edit().putString(key, value).commit();
        return this;
    }

    public final JinSPTool putStringSet(String key, Set<String> values){
        sp.edit().putStringSet(key, values);
        return this;
    }

    public final JinSPTool putInteger(String key, int value){
        sp.edit().putInt(key, value).commit();
        return this;
    }

    public final JinSPTool putBoolean(String key, boolean value){
        sp.edit().putBoolean(key, value).commit();
        return this;
    }

    public final JinSPTool putFloat(String key, float value){
        sp.edit().putFloat(key, value).commit();
        return this;
    }

    public final JinSPTool putLong(String key, long value){
        sp.edit().putLong(key, value).commit();
        return this;
    }

    /**
     * 根据Key获取SharedPreferences中的Int值
     * 默认-1
     * @param key
     */
    public final int getInteger(String key){
        return getInteger(key, -1);
    }

    /**
     * 根据Key获取SharedPreferences中的Int值
     * @param key
     * @param defualtValue
     */
    public final int getInteger(String key, int defualtValue){
        return sp.getInt(key, defualtValue);
    }

    /**
     * 根据key获取String值
     * 默认值null
     * @param key
     */
    public final String getString(String key){
        return getString(key, null);
    }

    /**
     * 根据key获取String值
     * @param key
     * @param defaultValue 默认值
     */
    public final String getString(String key, String defaultValue){
        return sp.getString(key, defaultValue);
    }

    /**
     * 根据Key获取Set<String>
     * 默认Null
     * @param key
     * @return
     */
    public final Set<String> getStringSet(String key){
        return getStringSet(key,null);
    }

    /**
     * 根据Key获取Set<String>
     * @param key
     * @param defaultValues 默认值
     */
    public final Set<String> getStringSet(String key, Set<String> defaultValues){
        return sp.getStringSet(key,defaultValues);
    }

    /**
     * 根据Key获取Float值
     * 默认-1
     * @param key
     */
    public final float getFloat(String key){
        return getFloat(key, -1);
    }

    /**
     * 根据key获取Float值
     * @param key
     * @param defaultValue 默认值
     */
    public final float getFloat(String key, float defaultValue){
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 根据key获取Long值
     * 默认-1
     * @param key
     */
    public final long getLong(String key){
        return getLong(key, -1);
    }

    /**
     * 根据key获取Long值
     * @param key
     * @param defaultValue 默认值
     */
    public final long getLong(String key, long defaultValue){
        return sp.getLong(key, defaultValue);
    }

    /**
     * 根据key获取boolean值
     * 默认false
     * @param key
     * @return
     */
    public final boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    /**
     * 根据key获取Long值
     * @param key
     * @param defaultValue 默认值
     */
    public final boolean getBoolean(String key, boolean defaultValue){
        return sp.getBoolean(key, defaultValue);
    }

    // 获取所有的键值对
    public final Map<String,?> getAll(){
        return sp.getAll();
    }

    /**
     * 清空sp中的值
     */
    public final void clearAll(){
        sp.edit().clear().commit();
    }

    /**
     * 清除sp中keys的键值
     * @param keys
     */
    public final void clearWith(String...keys){
        if(keys == null){
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        for(String key : keys){
            editor.remove(key);
        }
        editor.commit();
    }

    /**
     * 清除sp中的所有值保留containKeys中的键值
     * @param containKeys
     */
    public final void clearAllWithout(String...containKeys){
        if(containKeys == null){
            clearAll();
            return;
        }

        Map<String,?> values = sp.getAll();
        if(values == null){
            clearAll();
            return;
        }

        // 所有Sp中的Key
        Set<String> valueKeys = values.keySet();
        // 移除需要保留的key
        for(String key : containKeys){
            valueKeys.remove(key);
        }
        // 将剩下的key从sp中移除掉
        SharedPreferences.Editor editor = sp.edit();
        for(String key : valueKeys){
            editor.remove(key);
        }
        editor.commit();
    }
}
