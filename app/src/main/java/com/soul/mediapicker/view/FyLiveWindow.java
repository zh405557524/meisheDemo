package com.soul.mediapicker.view;

import android.content.Context;
import android.util.AttributeSet;

import com.meicam.sdk.NvsLiveWindow;

public class FyLiveWindow extends NvsLiveWindow {

    public static void setLiveModel(int liveWindowModel) {
        LiveParameter parameter = LiveParameter.getInstance();
        if (parameter != null) {
            parameter.setLiveModel(liveWindowModel);
        }
    }

    /**
     * 单例模式保存liveWindow的参数
     * The singleton mode holds the parameters of the liveWindow
     */
    public static class LiveParameter {
        private int liveModel = NvsLiveWindow.HDR_DISPLAY_MODE_SDR;

        public int getLiveModel() {
            return liveModel;
        }

        public void setLiveModel(int liveModel) {
            this.liveModel = liveModel;
        }

        private LiveParameter() {
        }

        public static LiveParameter getInstance() {
            return ParameterHolder.instance;
        }

        public static class ParameterHolder {
            public static LiveParameter instance = new LiveParameter();
        }

    }

    public FyLiveWindow(Context context) {
        this(context, null);
    }

    public FyLiveWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FyLiveWindow(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LiveParameter parameter = LiveParameter.getInstance();
        if (parameter != null) {
            setHDRDisplayMode(parameter.getLiveModel());
        }
    }
}
