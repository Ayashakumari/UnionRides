package mx.bigapps.unionrides.utils;

/*
* By Jorge E. Hernandez (@lalongooo) 2015
* */

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void createApplicationFolder() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/VideoCompressor/Compressed Videos/");
        if (dir.exists()) {
            Log.e("dir", dir.getAbsolutePath());
            dir.delete();
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    Log.e("file path", new File(dir, children[i]).getAbsolutePath());
                    new File(dir, children[i]).delete();
                }
            }
        }
        if (dir.exists()) {
            Log.e("yes", "yes");
        }
        File dir1 = new File(Environment.getExternalStorageDirectory() + "/VideoCompressor/Temp/");
        if (dir1.exists()) {
            Log.e("dir", dir1.getAbsolutePath());
            dir1.delete();
            if (dir1.isDirectory()) {
                String[] children = dir1.list();
                for (int i = 0; i < children.length; i++) {
                    Log.e("file path", new File(dir1, children[i]).getAbsolutePath());
                    new File(dir1, children[i]).delete();
                }
            }
        }
        if (dir1.exists()) {
            Log.e("yes", "yes");
        }

        File f = new File(Environment.getExternalStorageDirectory(), File.separator + Apis.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + Apis.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Apis.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + Apis.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Apis.VIDEO_COMPRESSOR_TEMP_DIR);
        f.mkdirs();
    }

    public static File saveTempFile(String fileName, Context context, Uri uri) {

        File mFile = null;
        ContentResolver resolver = context.getContentResolver();
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = resolver.openInputStream(uri);

            mFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + Apis.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Apis.VIDEO_COMPRESSOR_TEMP_DIR, fileName);
            out = new FileOutputStream(mFile, false);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            Log.e(TAG, "", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
        }
        return mFile;
    }
}
