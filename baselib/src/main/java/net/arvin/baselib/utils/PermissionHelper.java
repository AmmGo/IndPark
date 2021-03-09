package net.arvin.baselib.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import net.arvin.permissionhelper.PermissionUtil;


 /**
  * Created by yjl on 2021/3/8 10:03
  * Function：
  * Desc：
  */
public class PermissionHelper {
    private PermissionUtil permissionUtil;

    public PermissionHelper(FragmentActivity activity) {
        permissionUtil = build(new PermissionUtil.Builder().with(activity));
    }

    public PermissionHelper(Fragment fragment) {
        permissionUtil = build(new PermissionUtil.Builder().with(fragment));
    }

    private PermissionUtil build(PermissionUtil.Builder builder) {
        return builder.setTitleText("提示")
                .setSettingMsg("当前应用缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。")
                .setInstallAppMsg("允许安装来自此来源的应用")
                .build();
    }

    public void request(String msg, String permission, PermissionUtil.RequestPermissionListener listener) {
        if (permissionUtil == null) {
            return;
        }
        permissionUtil.request(msg, permission, listener);
    }

    public void request(String msg, String[] permissions, PermissionUtil.RequestPermissionListener listener) {
        if (permissionUtil == null) {
            return;
        }
        permissionUtil.request(msg, permissions, listener);
    }

    public void onDestroy() {
        if (permissionUtil != null) {
            permissionUtil.removeListener();
            permissionUtil = null;
        }
    }
}
