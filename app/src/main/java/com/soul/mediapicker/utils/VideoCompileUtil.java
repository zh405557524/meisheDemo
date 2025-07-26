package com.soul.mediapicker.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsVideoResolution;

import java.io.File;
import java.util.Hashtable;

/**
 * Created by admin on 2018/11/13.
 */

public class VideoCompileUtil {

    /*
     * 获取视频生成目录
     * Get video generation directory
     * */
    public static String getVideoCompileDirPath() {
        //return getFolderDirPath(VIDEOCOMPILE_DIRECTORY);
        File dstFileDir = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera");
        if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
            return null;
        }
        return dstFileDir.getAbsolutePath();
    }
    public static String getCompileVideoPath(Context context) {
        String compilePath = getVideoCompileDirPath();
//        String compilePath = Global.getExternalCacheDir();
        if (compilePath == null)
            return null;
        long currentMilis = System.currentTimeMillis();
//        String videoName = "/meicam_" + String.valueOf(currentMilis) + ".mp4";
//        compilePath += videoName;
        return compilePath;
    }

}
