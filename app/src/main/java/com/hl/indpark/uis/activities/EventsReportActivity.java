package com.hl.indpark.uis.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.permission.DefaultResourceProvider;
import com.hl.indpark.uis.fragments.MediaFragment;
import com.hl.indpark.utils.SelectDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.permissionhelper.PermissionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EventsReportActivity extends BaseActivity {
    @BindView(R.id.tv_count)
    TextView tvCount;
    boolean islMaxCount;
    private MediaFragment mediaFragment;

    @OnTextChanged(value = R.id.ed_event_decs, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        tvCount.setText(detailLength + "/300");
        if (detailLength == 299) {
            islMaxCount = true;
        }
        if (detailLength == 300 && islMaxCount) {
            ToastUtil.showToast(EventsReportActivity.this, "最多输入300个字");
            islMaxCount = false;
        }
    }

    @BindView(R.id.img_add)
    ImageView imageView;

    @OnClick({R.id.img_add, R.id.tv_report})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                addImageDialog(PHOTO, PHOTOLIB);
                break;
            case R.id.tv_report:
                break;
            default:
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_events;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initPermissionConfig();
        uploadFile = new ArrayList<>();
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("事件上报");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initData();
    }

    private PermissionUtil permissionUtil;
    private static String PHOTO = "拍照";
    private static String PHOTOLIB = "相册";
    private int themeId;

    private void initPermissionConfig() {
        PermissionUtil.setPermissionTextProvider(new DefaultResourceProvider());
        permissionUtil = new PermissionUtil.Builder().with(this).build();
    }

    private Uri photoUri;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> mediaList = new ArrayList<>();
    private List<File> uploadFile;

    private void addImageDialog(String one, String two) {
        List<String> names = new ArrayList<>();
        names.add(one);
        names.add(two);
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        permissionUtil.request("需要文件读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void callback(boolean granted, boolean isAlwaysDenied) {
                                if (granted) {
//                                    PictureSelector.create(EventsReportActivity.this)
//                                            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                                            .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
//                                            //.theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
//                                            .setPictureUIStyle(mSelectorUIStyle)
//                                            //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
//                                            //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
//                                            .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
//                                            .isWeChatStyle(isWeChatStyle)// 是否开启微信图片选择风格
//                                            .isUseCustomCamera(cb_custom_camera.isChecked())// 是否使用自定义相机
//                                            .setLanguage(language)// 设置语言，默认中文
//                                            .isPageStrategy(cbPage.isChecked())// 是否开启分页策略 & 每页多少条；默认开启
//                                            .setRecyclerAnimationMode(animationMode)// 列表动画效果
//                                            .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
//                                            .isMaxSelectEnabledMask(cbEnabledMask.isChecked())// 选择数到了最大阀值列表是否启用蒙层效果
//                                            //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
//                                            //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
//                                            //.setOutputCameraPath(createCustomCameraOutPath())// 自定义相机输出目录
//                                            //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
//                                            .maxSelectNum(maxSelectNum)// 最大图片选择数量
//                                            .minSelectNum(1)// 最小选择数量
//                                            .maxVideoSelectNum(1) // 视频最大选择数量
//                                            //.minVideoSelectNum(1)// 视频最小选择数量
//                                            //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
//                                            .imageSpanCount(4)// 每行显示个数
//                                            .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
//                                            .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
//                                            .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
//                                            //.isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
//                                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
//                                            .isOriginalImageControl(cb_original.isChecked())// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
//                                            //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
//                                            //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
//                                            //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
//                                            //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
//                                            //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
//                                            //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
//                                            .selectionMode(cb_choose_mode.isChecked() ?
//                                                    PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
//                                            .isSingleDirectReturn(cb_single_back.isChecked())// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
//                                            .isPreviewImage(cb_preview_img.isChecked())// 是否可预览图片
//                                            .isPreviewVideo(cb_preview_video.isChecked())// 是否可预览视频
//                                            //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
//                                            .isEnablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
//                                            .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
//                                            //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
//                                            //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
//                                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
//                                            .isEnableCrop(cb_crop.isChecked())// 是否裁剪
//                                            //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
//                                            .isCompress(cb_compress.isChecked())// 是否压缩
//                                            //.compressQuality(80)// 图片压缩后输出质量 0~ 100
//                                            .synOrAsy(false)//同步true或异步false 压缩 默认同步
//                                            //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
//                                            //.compressSavePath(getPath())//压缩图片保存地址
//                                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
//                                            //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
//                                            .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                                            .hideBottomControls(!cb_hide.isChecked())// 是否显示uCrop工具栏，默认不显示
//                                            .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                                            //.isWebp(false)// 是否显示webp图片,默认显示
//                                            //.isBmp(false)//是否显示bmp图片,默认显示
//                                            .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
//                                            .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                                            //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
//                                            //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
//                                            //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
//                                            .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                                            .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                                            .isOpenClickSound(cb_voice.isChecked())// 是否开启点击声音
//                                            .selectionData(mAdapter.getData())// 是否传入已选图片
//                                            //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                                            //.videoMinSecond(10)// 查询多少秒以内的视频
//                                            //.videoMaxSecond(15)// 查询多少秒以内的视频
//                                            //.recordVideoSecond(10)//录制视频秒数 默认60s
//                                            //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                                            //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
//                                            .cutOutQuality(90)// 裁剪输出质量 默认100
//                                            .minimumCompressSize(100)// 小于多少kb的图片不压缩
//                                            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                                            //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                                            //.rotateEnabled(false) // 裁剪是否可旋转图片
//                                            //.scaleEnabled(false)// 裁剪是否可放大缩小图片
//                                            //.videoQuality()// 视频录制质量 0 or 1
//                                            //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
//                                            .forResult(new MyResultCallback(mAdapter));
                                }
                            }
                        });
                        break;
                    case 1:
                        permissionUtil.request("需要拍照权限", Manifest.permission.CAMERA, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void callback(boolean granted, boolean isAlwaysDenied) {
                                if (granted) {
                                    PictureSelector.create(EventsReportActivity.this)
                                            .openGallery(PictureMimeType.ofImage())
                                            .isCamera(false)
                                            .compress(true)
                                            .minimumCompressSize(100)// 是否压缩
                                            .maxSelectNum(9)
                                            .forResult(PictureConfig.REQUEST_CAMERA);
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(EventsReportActivity.this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!EventsReportActivity.this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 媒体选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mediaList.addAll(selectList);
                    uploadFile.clear();
                    mediaFragment.setActivity(this, mediaList);
                    for (int i = 0; i < mediaList.size(); i++) {
                        uploadFile.add(new File(mediaList.get(i).getCompressPath()));
                    }
                    break;
                default:
            }
        }
    }

    public void initData() {
        ArticlesRepo.getReportType().observe(this, new ApiObserver<List<ReportTypeEvent>>() {
            @Override
            public void onSuccess(Response<List<ReportTypeEvent>> response) {
                Log.e("事件类型", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
        ArticlesRepo.getPhoneEvent().observe(this, new ApiObserver<List<PhoneEvent>>() {
            @Override
            public void onSuccess(Response<List<PhoneEvent>> response) {
                Log.e("人员列表", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
        ArticlesRepo.getSelfReportEvent().observe(this, new ApiObserver<SelfReportEvent>() {
            @Override
            public void onSuccess(Response<SelfReportEvent> response) {
                Log.e("我的上报", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
}
