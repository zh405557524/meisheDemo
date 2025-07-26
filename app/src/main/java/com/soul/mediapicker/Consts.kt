package com.soul.mediapicker

import android.app.Activity
import android.graphics.Color
import android.graphics.PointF
import android.os.Environment
import java.io.File


const val RECHARGE = 1005//充值

object Consts {

    const val KEY = "bundle_key"
    const val KEY2 = "bundle_key2"
    const val KEY3 = "bundle_key3"
    const val CHANNEL_INFO_KEY = "channel_info_key"

    const val User_Is_Tourist = "User_Is_Tourist"

    const val DATABASE_LIST_LENGTH = 10000


    const val RATE_WIDTH = "rateWidth"
    const val RATE_HEIGHT = "rateHeight"
    const val SETTING_COVER_CODE = 123456
    const val AIDRAWREQUEST_CODE = 1234567
    const val SETTING_COVER_PATH = "/app/path"

    val localMaterialGenerateTypeList = listOf(
        LocalMaterialGenerateType.system,
        LocalMaterialGenerateType.origin
    )

    val colorList = listOf(
        "#FFFFFF", "#000000", "#FF0909", "#FF6500", "#FFCB00", "#50DF00",
        "#008EFF", "#4532FF", "#9200FF", "#E400B5", "#FF6891", "#D90015", "#05BC7E",
        "#FFC5AB", "#FFB5AF", "#FFFBB9", "#EAF08F", "#31FF1C", "#30F082", "#00CBBF",
        "#C5D2FF", "#52D6FA", "#E090FC", "#F4A3C0", "#FFC4D4", "#FF9DA6", "#AAFFE0"
    )

    val colorDetailList = listOf(
        "#FFFFFF",
        "#EBEBEB",
        "#D6D6D6",
        "#C2C2C2",
        "#ADADAD",
        "#999999",
        "#858585",
        "#5C5C5C",
        "#474747",
        "#3D3D3D",
        "#333333",
        "#000000",
        "#03374A",
        "#011D55",
        "#1A0A51",
        "#2E063B",
        "#3A061B",
        "#5C0701",
        "#5A1C00",
        "#583300",
        "#563D00",
        "#666100",
        "#4F5504",
        "#263E0F",
        "#004D63",
        "#012F7B",
        "#1A0A50",
        "#450D58",
        "#551029",
        "#831100",
        "#792900",
        "#7A4A00",
        "#785800",
        "#8C8602",
        "#6F760A",
        "#38571A",
        "#006E8E",
        "#0042A8",
        "#2C0977",
        "#61187A",
        "#79193D",
        "#B51A00",
        "#AD3D00",
        "#A96800",
        "#A47A02",
        "#C2BC00",
        "#9AA310",
        "#4E7A27",
        "#008CB4",
        "#0454D6",
        "#371894",
        "#7A219E",
        "#97244F",
        "#E02402",
        "#DA5100",
        "#D18102",
        "#D19D02",
        "#F3EC01",
        "#C1CF18",
        "#669C34",
        "#049FD7",
        "#0161FC",
        "#4D22B2",
        "#982ABA",
        "#B92D5D",
        "#FF4015",
        "#FF6A00",
        "#FDA903",
        "#FBC700",
        "#FCFB42",
        "#D7EC37",
        "#75BB3F",
        "#04C7FA",
        "#3A87FC",
        "#5E30EB",
        "#BE37F1",
        "#E63B7A",
        "#FD604E",
        "#FF8648",
        "#FEB43F",
        "#FDCB3F",
        "#FDF76A",
        "#E3ED64",
        "#96D35F",
        "#52D6FA",
        "#74A5FF",
        "#864DFC",
        "#D357FC",
        "#EE719E",
        "#FD8B82",
        "#FFA57D",
        "#FFC777",
        "#FDD977",
        "#FFF993",
        "#EAF08F",
        "#B0DB89",
        "#92E1FB",
        "#A7C6FF",
        "#B18CFC",
        "#E090FC",
        "#F4A3C0",
        "#FFB5AF",
        "#FFC5AB",
        "#FDD9A8",
        "#FCE2A8",
        "#FFFBB9",
        "#F0F5B6",
        "#CDE8B4",
        "#CBEEFF",
        "#D1E0FF",
        "#D9C7FE",
        "#EFCAFF",
        "#F8D3DE",
        "#FFDBD8",
        "#FFE2D4",
        "#FDEAD2",
        "#FDF2D5",
        "#FCFCDC",
        "#F7F8DA",
        "#DDEED4",
    )
    val borderColorList = listOf(
        "#AAFFE0", "#FF9DA6", "#FFC4D4", "#F4A3C0", "#E090FC", "#52D6FA", "#C5D2FF",
        "#00CBBF", "#30F082", "#31FF1C", "#EAF08F", "#FFFBB9", "#FFB5AF", "#FFC5AB",
        "#05BC7E", "#D90015", "#FF6891", "#E400B5", "#9200FF", "#4532FF", "#008EFF",
        "#50DF00", "#FFCB00", "#FF6500", "#FF0909", "#000000", "#FFFFFF"
    )




    val contentResolutionTypeList = listOf(
        ContentResolutionType.sixteenNine,
        ContentResolutionType.fourThree,
        ContentResolutionType.oneOne
    )

    const val minFontSize = 30
    const val maxFontSize = 130
    const val downloadFailRetryInterval = 1000
    const val downloadFailRetryTimes = 5
    const val microSoftTtsDefaultUrl =
        "wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1"

    const val Key_Text_Split = "Text_Split"

    const val WECHAT_APP_ID = "wx5054dd8a5961b12d"

    const val defaultDynamicWaterMarkFontSize = 22f  //动态水印默认字体大小
    val dynamicWaterMarkDurationArr = arrayOf(28000, 20000, 16000)
    const val dynamicNvsPartNum = 500

    const val defaultAiMashupDuration = 10 // 分钟
    const val defaultAiMashupCount = 3 // 3个

    const val defaultBatchMashupCount = 10

    const val sceneDuring = 400f

    // may change
    var effectDuring = 1200f
    const val coverDuration = 10.0
    val SceneNameArr = arrayOf(
        "", "Fade", "Turning", "Swap", "Stretch In", "Page Curl", "Lens Flare", "Star",
        "Dip To Black",
        "Dip To White", "Push To Right", "Push To Top", "Upper Left Into", "Noise", "Gaussian Blur"
    )

    var autoAiCartoonImport = false  //漫画推文导入是否自动模式
    var cartoonModelId = ""   //漫画模型ID
    var isCartoon = false //是否漫画界面

    val DeduplicationSpeedArr = arrayOf(0.5, 0.8, 1.0, 1.2, 1.5)
    val CustomSpeedArr = arrayOf(1.0f, 1.2f, 1.5f, 1.8f, 2.0f, 2.2f, 2.5f, 2.8f, 3.0f)  //线性速度
    val CustomNonlinearSpeedArr = arrayOf(
        1.369f,
        1.633f,
        2.696f,
        0.681f,
        1.761f,
        1.902f
    )//非线性速度

    //非线性速度值
    val CurvesSpeedArr = arrayOf(
        "(0.0,0.86)(-938665.3,0.86)(938665.3,0.86)(2815996.0,0.86)(1877330.6,0.86)(5032019.5,0.86)(9464066.0,7.33)(7248043.0,7.33)(1.0882795E7,7.33)(1.3720251E7,0.48)(1.2301523E7,0.48)(1.5204032E7,0.48)(1.8171594E7,1.0)(1.6687813E7,1.0)(1.9736526E7,1.0)(2.2866392E7,1.0)(2.130146E7,1.0)(2.4431324E7,1.0)",
        "(0.0,1.0)(-553269.94,1.0)(553269.94,1.0)(1659809.8,1.05)(1106539.9,1.05)(3418176.0,1.05)(6934909.0,5.67)(5176542.5,5.67)(7934528.0,5.67)(9933767.0,0.41)(8934148.0,0.41)(1.0692514E7,0.41)(1.2210009E7,0.42)(1.1451261E7,0.42)(1.3233715E7,0.42)(1.5281128E7,5.61)(1.4257422E7,5.61)(1.729241E7,5.61)(2.1314974E7,1.05)(1.9303692E7,1.05)(2.1832114E7,1.05)(2.2866392E7,1.0)(2.2349252E7,1.0)(2.3383532E7,1.0)",
        "(0.0,5.67)(-3075990.5,5.67)(3075990.5,5.67)(9227971.0,5.61)(6151981.0,5.61)(9451193.0,5.61)(9897637.0,0.6)(9674415.0,0.6)(1.0837038E7,0.6)(1.271584E7,0.61)(1.1776439E7,0.61)(1.2944669E7,0.61)(1.3402326E7,5.48)(1.3173497E7,5.48)(1.6557014E7,5.48)(2.2866392E7,5.36)(1.9711704E7,5.36)(2.602108E7,5.36)",
        "(0.0,0.6)(-3106514.5,0.6)(3106514.5,0.6)(9319543.0,0.58)(6213029.0,0.58)(9909680.0,0.58)(1.1089954E7,5.61)(1.0499817E7,5.61)(1.1752352E7,5.61)(1.3077148E7,0.58)(1.241475E7,0.58)(1.6340229E7,0.58)(2.2866392E7,0.6)(1.960331E7,0.6)(2.6129474E7,0.6)",
        "(0.0,5.36)(-2817468.0,5.36)(2817468.0,5.36)(8452404.0,5.54)(5634936.0,5.54)(9909680.0,5.54)(1.2824233E7,0.99)(1.1366957E7,0.99)(1.617162E7,0.99)(2.2866392E7,1.0)(1.9519006E7,1.0)(2.6213778E7,1.0)",
        "(0.0,1.0)(-2961991.0,1.0)(2961991.0,1.0)(8885973.0,0.99)(5923982.0,0.99)(1.0427555E7,0.99)(1.3510718E7,5.67)(1.1969137E7,5.67)(1.6629276E7,5.67)(2.2866392E7,5.3)(1.9747834E7,5.3)(2.598495E7,5.3)",
    )

    //比例字符串
    val ratioStrArr = arrayOf("9v16", "9v16", "16v9", "1v1", "4v3", "3v4")

    var cartoonDefaultFontPath = ""

    var extractArticleMaxDuration = 1200
    val MAX_VOLUME_ROTE = 3f
}

object EffectId {
    const val none = -1
    const val topBottomLoop = 1
    const val leftRightLoop = 2
    const val minMaxLoop = 3
    const val topBottomMinMaxLoop = 4
    const val topBottomLeftRightLoop = 5
    const val rightToLeft = 6
    const val leftToRight = 7
    const val bottomToTop = 8
    const val topToBottom = 9
    const val scaleMax = 10
    const val scaleMin = 11
}

object StatusType {
    const val load = 0
    const val success = 1
    const val fail = 2
}

object AlignType {
    const val left = 0
    const val center = 1
    const val right = 2
}

fun OutputRatioType.ratioValue(type: Int): Pair<Int, Int>? {
    if (type == type_9_16) {
        return Pair(9, 16)
    }
    if (type == type_16_9) {
        return Pair(16, 9)
    }
    if (type == type_1_1) {
        return Pair(1, 1)
    }
    if (type == type_4_3) {
        return Pair(4, 3)
    }
    if (type == type_3_4) {
        return Pair(3, 4)
    }
    return null
}

fun OutputRatioType.calculateHeight(type: Int, width: Int): Int {
    return when (type) {
        type_16_9 -> {
            width * 9 / 16
        }

        type_9_16 -> {
            width * 16 / 9
        }

        type_4_3 -> {
            width * 3 / 4
        }

        type_3_4 -> {
            width * 4 / 3
        }

        else -> {
            width
        }
    }
}

fun OutputRatioType.width(type: Int?): Int {

    if (type == type_9_16) {
        return 1080
    }
    if (type == type_16_9) {
        return 1920
    }
    if (type == type_1_1) {
        return 1080
    }
    if (type == type_4_3) {
        return 1440
    }
    if (type == type_3_4) {
        return 1080
    }
    return -1
}

fun OutputRatioType.height(type: Int?): Int {

    if (type == type_9_16) {
        return 1920
    }
    if (type == type_16_9) {
        return 1080
    }
    if (type == type_1_1) {
        return 1080
    }
    if (type == type_4_3) {
        return 1080
    }
    if (type == type_3_4) {
        return 1440
    }
    return -1
}

object OutputRatioType {
    const val origin = 0
    const val type_9_16 = 1
    const val type_16_9 = 2
    const val type_1_1 = 3
    const val type_4_3 = 4
    const val type_3_4 = 5
}


object TouchType {
    const val none = 0
    const val srt = 1
    const val title = 2
    const val dynamic = 3
}

object BgmType {
    const val none = 0
    const val server = 1
    const val local = 2
}

object CoverType {
    const val none = 0
    const val server = 1
    const val local = 2
}

object FrameType {
    const val none = 0
    const val server = 1
    const val local = 2
}


object FilterType {
    const val filter = 1//滤镜
    const val special_effect = 2 //特效
    const val frame = 3//边框
    const val split = 4
    const val marketing_account = 5
}


object CartoonType {
    const val typeError = -1//生成失败

    const val typeFinish = 0  // 生成完成
    const val typeSrt = 1     // 字幕预处理
    const val typePicMerge = 2     //图片合并
    const val typePicDrawAuto = 3  //自动模式图片提示词调整
    const val typeCreating = 4  //图片自动生成中
    const val typePicDrawManual = 5 //手动模式图片绘制
    const val typeHandDrawn = 6//简约手绘角色对话

    //二创
    const val typeCreationAgainSrt = 10  //字幕合并拆分调整
    const val typeCreationAgainPicMerge = 11  //图片合并操作
    const val typeCreationAgainPicHandleAi = 12  //图片选择重绘(自动)  要放作品那块
    const val typeCreationAgainPicHandle = 13  //手动选择重绘
    const val typeCreationAgainCreating = 14  //图片自动生成中        要放作品那块
    const val typeCreationAgainExport = 15  //视频导出             要放作品那块

}

object HandPaintType {
    const val auto = 0 //自动
    const val local = 1 //本地图片上传
}

object DrawnType {
    const val normal = 0 //原创，二创
    const val handDrawn = 1 //简约手绘
    const val ancientStyle = 2 //二次元
}

object AddWhiteType {
    const val none = -1
    const val top = 0
    const val bottom = 1
    const val topAndBottom = 2
    const val around = 3
}

object RecognizeType {
    const val default = ""
    const val cartoon = "15"
    const val ai = "17"
}

object CartoonAgainMode {
    const val ai = 0
    const val manual = 1
}

object VoiceType {
    const val normal = 1
    const val premium = 2
}

fun CartoonRationWidth(type: Int): Int {
    return when (type) {
        0 -> 1440
        1 -> 1080
        2 -> 1080
        3 -> 1080
        else -> 1920
    }
}

fun CartoonRationHeight(type: Int): Int {
    return when (type) {
        0 -> 1080
        1 -> 1080
        2 -> 1920
        3 -> 1440
        else -> 1080
    }
}

fun CartoonRatio(type: Int): String {
    return when (type) {
        0 -> "4:3"
        1 -> "1:1"
        2 -> "9:16"
        3 -> "3:4"
        else -> "16:9"
    }
}

object LoadType {
    const val article = 0
    const val link = 1
    const val video = 2
    const val audio = 3
}

object SpeedType {
    const val linear = 0
    const val nonlinear = 1
}

object CollectShowType {
    const val light_white = 0  //浅色带白底
    const val dark = 1         //暗色
    const val light_transparent = 2    //浅色透明底
}

object MaterialScreenType {
    const val Unknown = 0
    const val Horizontal = 1
    const val Vertical = 2
}

object DeduplicationSetType {
    const val Material = 0  //素材推文预览
    const val Deduplication = 1  //素材去重
    const val MaterialSecond = 2  //素材二剪
}


object DeduplicationType {
    const val auto = 0 //自动
    const val manual = 1 //手动
}

object CommentType {
    const val main = 1 //主评论
    const val second = 2 //次评论
}

object PreviewAction {
    const val materialModify = 1 // 素材变换
    const val srtModify = 2 // 修改字幕
    const val srtBatchModify = 3
    const val srtSwitch = 4 // 字幕切换
    const val setPoster = 5 // 设置封面
    const val titleModify = 6 // 修改标题
    const val titleSrtStyle = 7 // 标题样式
    const val textWaterMarkModify = 8 //文本样式调整
    const val effect = 9 //帧动画
    const val scene = 10 //转场
    const val filter = 11 //滤镜,特效
    const val srtAnimation = 12 //字幕动画
    const val titleEffect = 13 //标题动效
    const val changeVoice = 14 //变换音色
    const val dynamicWaterMark = 15 //动态水印
}

object MergeType {
    const val forward = 0
    const val backward = 1
}

object TitleSrtConfig {
    const val title = 1
    const val srt = 2
}

object LocalMaterialGenerateType {
    // 系统策略
    const val system = 1

    // 保持原视频策略
    const val origin = 2
}

object MsVoiceCrawlType {
    const val edge = "edge"
    const val web = "web"
    const val api = "api"
}

fun LocalMaterialGenerateType.toName(type: Int): String {
    return when (type) {
        system -> "使用系统混剪"
        origin -> "保留原视频"
        else -> ""
    }
}

object ResolutionType {
    const val local = -1
    const val nineSixteen = 1
    const val sixteenNine = 2
    const val oneOne = 3
    const val fourThree = 4
    const val threeFour = 5
}

object ContentResolutionType {
    const val unset = 0
    const val sixteenNine = 1
    const val fourThree = 2
    const val oneOne = 3
}

object SceneType {
    const val None = 0
    const val Fade = 1
    const val Turning = 2
    const val Swap = 3
    const val StretchIn = 4
    const val PageCurl = 5
    const val LensFlare = 6
    const val Star = 7
    const val DipToBlack = 8
    const val DipToWhite = 9
    const val PushToRight = 10
    const val PushToTop = 11
    const val UpperLeftInto = 12
}

object EffectType {
    const val none = -1
    const val topToBottom = 0
    const val bottomToTop = 1
    const val leftToRight = 2
    const val rightToLeft = 3
    const val scaleMax = 4
    const val scaleMin = 5
}

object DecodeType {
    const val hardware = 1
    const val software = 2
}

fun DecodeType.toString(type: Int): String {
    return when (type) {
        hardware -> "硬件解码(速度快)"
        software -> "软件解码(兼容好)"
        else -> ""
    }
}

fun defaultPicPath(activity: Activity): String {
    return appCacheDir(activity) + File.separator + "video_bg.jpg"
}

fun defaultOldPicPath(activity: Activity): String {
    return appOldCacheDir(activity) + File.separator + "video_bg.jpg"
}

fun appOldCacheDir(activity: Activity): String {
    return activity.filesDir.path + "/cache/video" + File.separator + "cache"
}

fun appCacheDir(activity: Activity): String {
    return activity.filesDir.path + "/cache/video" + File.separator + "cache"
}

fun srtCacheDir(activity: Activity): String {
    return activity.externalCacheDir?.absolutePath + "/srt"
}


fun buildingFontDir(activity: Activity): String {
    return activity.getExternalFilesDir("workspace")!!.absolutePath + File.separator + "fonts"
}

fun buildingBgmDir(activity: Activity): String {
    return activity.getExternalFilesDir("workspace")!!.absolutePath + File.separator + "bgm"
}

fun buildingWebDir(activity: Activity): String {
    return activity.getExternalFilesDir("workspace")!!.absolutePath + File.separator + "web"
}

fun buildingTtsDir(activity: Activity): String {
    return activity.getExternalFilesDir("workspace")!!.absolutePath + File.separator + "mp3"
}

fun buildingWebArticleDir(activity: Activity): String {
    return buildingWebDir(activity) + File.separator + "walarticle"
}

fun outputDirName(): String {
    return Environment.DIRECTORY_DCIM + File.separator + "FuyaoVideo"
}

fun outputMusicDirName(): String {
    return Environment.DIRECTORY_MUSIC + File.separator + "Novels"
}

fun outputMusicDir(): String {
    return Environment.getExternalStorageDirectory().absolutePath + File.separator + outputMusicDirName()
}


object StorageKey {
    const val apiHostInDebugMode = "apiHostInDebugMode"
    const val device = "device"
    const val user = "user"
    const val userList = "userList"

    const val bgmVolume = "bgmVolume"

    const val requestreadphonestate = "requestReadPhoneState"

    const val customSpeedType = "customSpeedType"
    const val customVolumeType = "customVolumeType"//调节音量
    const val customSpeed = "customSpeed"
    const val cartoonMixPicNum = "cartoonMixPicNum"
    const val cartoonMixStyle = "cartoonMixStyle"
    const val cartoonMixPicList = "cartoonMixPicList"

    const val frame = "frame"

    const val fontLocalPath = "fontLocalPath"


    // cartoon
    const val cartoonSrtConfig = "cartoonSrtConfig"
    const val cartoonTitleConfig = "cartoonTitleConfig"
    const val cartoonBgmInfo = "cartoonBgmInfo"
    const val cartoonBgType = "cartoonBgType"
    const val cartoonMergeStatus = "cartoonMergeStatus"
    const val cartoonDraft = "cartoonDraft"
    const val cartoonDraftNameIndexInDay = "cartoonDraftNameIndexInDay"
    const val cartoonLimitType = "cartoonLimitType"
    const val cartoonTtsVoiceInfo = "cartoonTtsVoiceInfo"
    const val cartoonTtsVolume = "cartoonTtsVolume"
    const val cartoonTtsSpeed = "cartooTtsSpeed"

    const val commentDraft = "commentDraft"
    const val openAiWorkList = "openAiWorkList"

    const val effectId = "effectId"
    const val needEffectNotice = "needEffectNotice"


    // new emoji
    const val emojiSrtConfig = "emojiSrtConfig"
    const val emojiTitleConfig = "emojiTitleConfig"
    const val emojiBgmInfo = "emojiBgmInfo"
    const val emojiBgType = "emojiBgType"
    const val emojiMergeStatus = "emojiMergeStatus"
    const val emojiDraft = "emojiDraft"
    const val emojiBg = "emojiBg"
    const val emojiBgPath = "emojiBgPath"
    const val emojiDraftNameIndexInDay = "emojiDraftNameIndexInDay"
    const val emojiLimitType = "emojiLimitType"
    const val emojiTtsVoiceInfo = "emojiTtsVoiceInfo"
    const val emojiTtsVolume = "emojiTtsVolume"
    const val emojiTtsSpeed = "emojiTtsSpeed"

    const val bountySubmitGuideFlag = "bountySubmitGuideFlag"

    //chat pic
    const val chatPicBg = "chatPicBg"
    const val chatPicLastBg = "chatPicLastBg"
    const val chatPicBgColor = "chatPicBgColor"
    const val chatPicOutputType = "chatPicOutputType"

    const val cashClick = "cashClick"

    const val requestPhoneStateFlag = "requestPhoneStateFlag"

    const val voicePremiumPayInfo = "voicePremiumPayInfo"   //高级配音扣推币信息
    const val cartoonLoraList = "cartoonLoraList"

    const val cartoonCacheSrtList = "CartoonCacheSrtList"
    const val materialSrtAnimationConfig = "materialSrtAnimationConfig"

    const val videoLoadConfig = "videoLoadConfig"
    const val audioUrlByVideo = "audioUrlByVideo"
    const val defaultFontInfo = "defaultFontInfo"

    const val userPhone = "userPhone"
    const val filterList = "filterList"

    const val MarketingAccountTtsVoiceInfo = "MarketingAccountTtsVoiceInfo"
    const val MarketingAccountTtsVolume = "MarketingAccountTtsVolume"
    const val MarketingAccountTtsSpeed = "MarketingAccountTtsSpeed"

    const val BatchGraphicsList = "BatchGraphicsList"
    const val ReWriteStoryStyleList = "ReWriteStoryStyleList"

    const val HandPaintRatio = "HandPaintRatio"
    const val HandPaintStyle = "HandPaintStyle"
    const val HandPaintTtsVoiceInfo = "HandPaintTtsVoiceInfo"
    const val HandPaintTtsSpeed = "HandPaintTtsSpeed"
    const val HandPaintTtsVolume = "HandPaintTtsVolume"
    const val HandPaintPicArr = "HandPaintPicArr"

    const val UserToken = "UserToken"
}


object BgFillMode {
    const val blur = 1
    const val picture = 2
    const val none = 3
}

object ScrollSpeed {
    const val slow = 1
    const val normal = 2
    const val fast = 3
}

fun ScrollSpeed.getDesc(speed: Int?): String {
    return when (speed) {
        slow -> "慢"
        fast -> "快"
        else -> "正常"
    }
}

fun ScrollSpeed.getPixel(speed: Int?): Int {
    return when (speed) {
        slow -> 2
        fast -> 6
        else -> 4
    }
}

fun BgFillMode.toName(mode: Int): String {
    return when (mode) {
        blur -> "模糊"
        picture -> "图片填充"
        none -> "无填充"
        else -> "无填充"
    }
}

object CartoonDraftType {
    const val emoji = 1
    const val cartoon = 2
    const val handPaint = 3
}


object SrtPositionOld {
    const val center = 1
    const val bottom = 2
}

object SrtPositionType {
    // 固定，例如用户采用保留原视频策略时，只能固定字幕位置为底部
    const val fixed = 1

    // 自定义
    const val custom = 2
}

object TitlePositionType {
    // 固定，例如用户采用保留原视频策略时，只能固定字幕位置为顶部
    const val fixed = 1

    // 自定义
    const val custom = 2
}

fun SrtPositionOld.toString(position: Int): String {
    if (position == center) {
        return "视频中间"
    }
    if (position == bottom) {
        return "视频下方"
    }
    return ""
}

object UploadTokenType {
    const val pic = "1"
    const val wall = "16"
    const val srt = "17"
    const val aiPic = "19"
}

object ArticleSearchType {
    const val RECOMMEND = 1
    const val FIND = 2
}

object ArticleGenderType {
    const val MAN = 1
    const val WOMAN = 2
}

fun ArticleGenderType.toString(type: Int): String {
    if (type == MAN) {
        return "男频"
    }
    if (type == WOMAN) {
        return "女频"
    }
    return ""
}


object GenerateType {
    const val NORMAL = 1    //爆文
    const val AUDIO = 2            //MP3转视频   导入音频
    const val ARTICLE = 3           //文字转视频  导入文章
    const val LINK = 4               //链接转视频  导入链接
    const val SCROLL = 5             //滚动推文
    const val MATERIAL_DOWNLOAD = 6   //素材下载
    const val VIDEO = 7        //导入视频
    const val CHAT = 8         //对话推文
    const val EMOJI = 9        //表情包推文
    const val CONVERSATION = 10     //拼接视频
    const val CHAT_GPT = 11         // GPT
    const val BLACK_GLUE = 12           //黑胶推文

    //    const val TELETEXT = 11
    const val LISTEN_BOOK = 13      //听书推文
    const val BATCH_MASHUP = 15   //矩阵视频
    const val CARTOON = 16        //漫话推文
    const val DEDUPLICATION = 17   //视频去重
    const val DELETE_SRT = 18      //视频去字幕
    const val MARKET_FILE = 19    //
    const val CHAT_PIC = 20       //对话图文
    const val MATERIAL_SECOND = 21  //素材二剪
    const val EXTRACT_ARTICLE = 22   //视频提取文案
    const val ARTICLE_REWRITE = 23   //文章改编
    const val FILM = 24  //短剧解说
    const val FILM_DEDUPE = 25  //短剧去重
    const val HAND_PAINT = 26  //手绘推文
}


object ArticleType {
    const val ZHIHU = 1
    const val FANQIE = 2
    const val UC = 3
    const val CUSTOM_AUDIO = -1
    const val CUSTOM_ARTICLE = -2
    const val CUSTOM_LINK = -3
    const val CUSTOM_SCROLL = -4
    const val CUSTOM_DOWNLOAD = -5
    const val CUSTOM_CHAT = -6
    const val CUSTOM_EMOJI = -7
    const val CUSTOM_VIDEO = -8
    const val CUSTOM_BATCH_MASHUP = -9
    const val CUSTOM_CARTOON = -10
    const val CUSTOM_DELETE_SRT = -11
    const val CUSTOM_VIDEO_SPEED = -12
    const val CUSTOM_MARKETING_ACCOUNT = -13
    const val CUSTOM_HAND_PAINT = -14
}

object NovelAccountImageType {
    const val AVATAR = 1
    const val BG = 2
    const val AI_EMOJI = 3   //AI表情包
}

object DrainageMemeType {
    const val ZHIHU = 1
    const val UC = 2
    const val QQ = 3
    const val XIAO_XIANG = 4
    const val QI_DIAN = 5
    const val SHU_QI = 6
    const val ZUI_YOU = 7
    const val FAN_QIE = 8
    const val LOFTER = 9
    const val QI_MAO = 10

    fun toString(type: Int): String {
        if (type == ZHIHU) {
            return "知乎"
        }
        if (type == UC) {
            return "UC"
        }
        if (type == QQ) {
            return "QQ阅读"
        }
        if (type == XIAO_XIANG) {
            return "潇湘"
        }
        if (type == QI_DIAN) {
            return "起点"
        }
        if (type == SHU_QI) {
            return "书旗"
        }
        if (type == ZUI_YOU) {
            return "最右"
        }
        if (type == FAN_QIE) {
            return "番茄"
        }
        if (type == LOFTER) {
            return "LOFTER"
        }
        if (type == QI_MAO) {
            return "七猫"
        }
        return ""
    }


}

object AiTxtToImg {
    const val NONE = 0
    const val SUCCESS = 1
    const val FAIL = 2
}

object AudioBgmStatus {
    const val NONE = 0
    const val CONTAIN = 1
}

fun ArticleType.toString(type: Int): String {
    if (type == ZHIHU) {
        return "知乎"
    }
    if (type == FANQIE) {
        return "番茄畅听"
    }
    if (type == UC) {
        return "UC"
    }
    return ""
}


fun ArticleType.toBgColor(type: Int): Int {
    if (type == ZHIHU) {
        return Color.parseColor("#0084FF")
    }
    if (type == FANQIE) {
        return Color.parseColor("#FF2B00")
    }
    if (type == UC) {
        return Color.parseColor("#FF6400")
    }
    return Color.TRANSPARENT
}