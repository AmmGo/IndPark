package com.hl.indpark.permission;

import net.arvin.permissionhelper.PermissionUtil;

 /**
  * Created by yjl on 2021/3/30 15:36
  * Function：
  * Desc：
  */
public class DefaultResourceProvider implements PermissionUtil.IPermissionTextProvider {
    @Override
    public String getEnsureBtnText() {
        return "确定";
    }

    @Override
    public String getCancelBtnText() {
        return "取消";
    }

    @Override
    public String getSettingMsg() {
        return "设置";
    }

    @Override
    public String getSettingEnsureText() {
        return "确定";
    }

    @Override
    public String getSettingCancelText() {
        return "取消";
    }

    @Override
    public String getInstallAppMsg() {
        return "当前应用缺少必要权限。\n请点击\"设置\"&#8212;\"权限\"&#8212;打开所需权限。";
    }
}
