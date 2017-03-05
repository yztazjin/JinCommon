package ttyy.com.common.camera;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Author: hujinqi
 * Date  : 2016-08-05
 * Description: 拍照/相册获取照片处理工具类
 */
public class PhotoGetTools {

    // 相册
    static final int FROM_ALBUM = 10001;
    // 相机
    static final int FROM_CAMERA = 10002;
    // 裁剪
    static final int FROM_CROP = 10003;

    /**
     * 当前图片存储的文件Uri
     */
    static Uri mCameraFileUri;
    /**
     * mCameraFileUri对应的物理路径地址
     */
    static String mCameraFilePath;

    /**
     * Camera拍照获取
     * @param activity
     */
    public static void pictureFromCamera(Activity activity) {
        pictureFromCamera(activity, null);
    }

    /**
     * Camera拍照获取
     * @param activity
     * @param path
     */
    public static void pictureFromCamera(Activity activity, String path) {
        mCameraFilePath = null;

        if (TextUtils.isEmpty(path)) {
            path = activity.getExternalFilesDir("photo").getPath() + "/" + System.currentTimeMillis()+".jpg";
        }

        mCameraFileUri = Uri.fromFile(new File(path));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCameraFileUri);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, FROM_CAMERA);
    }

    /**
     * 相册获取照片
     */
    public static void pictureFromAlbum(Activity activity) {
        mCameraFilePath = null;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, FROM_ALBUM);
    }

    /**
     * 裁剪
     * @param activity
     */
    public static void pictureFromCrop(Activity activity){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mCameraFileUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);

        // 这个函数存在问题，裁减时打开图片的uri与保存图片的uri相同，产生冲突，导致裁减完成后图片的大小变成0Byte。
        // 可将相机照片保存在另外的位置，intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri); 换成新的存储路径即可
        String path = activity.getExternalFilesDir("photo").getPath() + "/" + System.currentTimeMillis()+".jpg";
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mCameraFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);

        // 另一种方案 return-data true在uri中接到bitmap 之后导出到文件或者直接运用

        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // no face detection
        activity.startActivityForResult(intent, FROM_CROP);
    }


    /**
     * 在Activity中调用该方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FROM_ALBUM:
                    // 从相册获取照片
                    mCameraFileUri = data.getData();
                    pictureFromCrop(activity);
                    break;
                case FROM_CAMERA:
                    // 从相机获取照片
                    pictureFromCrop(activity);
                    break;
                case FROM_CROP:
                    // 裁剪照片完成后
//                    mCameraFileUri = data.getData();
                    mCameraFilePath = getUriPath(activity, mCameraFileUri);
                    break;
            }
        }
    }

    /**
     * 获取当前的图片路径地址
     * @return
     */
    public static String currentCameraFilePath(){
        return mCameraFilePath;
    }

    /**
     * 清除当前缓存的文件
     * 注意清除完后，就找不到该文件了
     */
    public static void clearCurrentCameraTmpFile(){
        if(mCameraFilePath != null){
            File file = new File(mCameraFilePath);
            // 程序退出后 删除文件
            file.deleteOnExit();
            // 立即删除文件
//            file.delete();
        }
    }

    /**
     * 根据Uri获取path
     * @param context
     * @param uri
     * @return
     */
    static String getUriPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 根据Uri转化具体的路径地址
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

}
