package com.hl.indpark.utils;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

 /**
  * Created by yjl on 2021/3/30 15:38
  * Function：服务
  * Desc：
  */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AliveJobService extends JobService {
    public static void startJobScheduler(Context context) {
        try {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), AliveJobService.class.getName()));
            // 设置设备重启依然执行
            builder.setPersisted(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //7.0以上延迟1s执行
                builder.setMinimumLatency(1000);
            } else {
                //每隔1s执行一次job
                builder.setPeriodic(1000);
            }
            jobScheduler.schedule(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("dsfadf", "开启job");
        // 7.0 以上开启轮询
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startJobScheduler(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}