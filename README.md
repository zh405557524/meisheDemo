# MediaPicker 视频编辑功能

## 功能说明

本项目已完成视频数据加载功能的实现，主要包括以下组件：

### 1. 数据模型类

- **VideoDataModel**: 视频数据模型，包含音频路径、视频列表和字幕列表
- **VideoItem**: 视频项数据模型，包含视频的高度、宽度和URL
- **CartoonTimelineItem**: 字幕项数据模型，包含时间信息和字幕内容

### 2. 加载对话框工具

- **LoadingDialogUtil**: 加载对话框工具类，提供显示和隐藏加载框的功能
- 支持自定义加载消息
- 防止重复显示

### 3. ViewModel 层

- **VideoEditViewModel**: 视频编辑的ViewModel
- 实现了异步加载视频数据的功能
- 使用协程处理异步操作
- 提供回调接口返回加载结果

### 4. Activity 层

- **VideoEditActivity**: 视频编辑Activity
- 在初始化时自动调用数据加载
- 处理加载成功和失败的回调

## 使用方法

### 1. 在VideoEditActivity中调用

```kotlin
// 在initData()方法中已经自动调用
videoEditViewModel.loadVideoData(this, object : VideoEditViewModel.OnVideoDataLoadListener {
    override fun onSuccess(videoDataModel: VideoDataModel) {
        // 处理加载成功
        LogUtil.i(TAG, "视频数据加载成功: 音频路径=${videoDataModel.audioPath}, 视频数量=${videoDataModel.videoList.size}, 字幕数量=${videoDataModel.subtitleList.size}")
    }
    
    override fun onError(errorMessage: String) {
        // 处理加载失败
        Toast.makeText(this@VideoEditActivity, errorMessage, Toast.LENGTH_SHORT).show()
    }
    
    override fun onProgress(message: String) {
        // 处理进度更新
        LogUtil.i(TAG, "加载进度: $message")
    }
})
```

### 2. 数据来源

- **音频数据**: 来自 `audioPath` 变量定义的网络URL，下载到本地缓存
- **视频数据**: 来自 `assets/video.json` 文件中的网络URL，下载到本地缓存
- **字幕数据**: 来自 `assets/srt.json` 文件，直接读取无需下载

### 3. 异步处理

- 使用Kotlin协程处理异步操作
- 在IO线程中读取文件和下载资源
- 在主线程中更新UI和显示结果
- 支持下载进度回调

## 文件结构

```
app/src/main/java/com/soul/mediapicker/module/videoedit/
├── VideoEditViewModel.kt          # ViewModel层
├── VideoEditActivity.kt           # Activity层
├── VideoDataModel.kt              # 数据模型
└── PlayerFunction.kt              # 播放器功能

app/src/main/java/com/soul/mediapicker/utils/
└── LoadingDialogUtil.kt           # 加载对话框工具

app/src/main/res/
├── layout/dialog_loading.xml      # 加载对话框布局
└── values/themes.xml              # 对话框样式
```

## 依赖

项目已添加以下依赖：

- `kotlinx-coroutines-android`: 协程支持
- `gson`: JSON解析
- `downloadlibrary`: 文件下载库

## 注意事项

1. 确保 `assets/video.json` 和 `assets/srt.json` 文件存在且格式正确
2. 网络权限已在AndroidManifest.xml中配置
3. 加载过程中会显示加载对话框，防止用户重复操作
4. 所有异步操作都在协程中处理，确保UI线程不被阻塞 