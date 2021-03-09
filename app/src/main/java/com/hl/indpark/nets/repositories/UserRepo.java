package com.hl.indpark.nets.repositories;

import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.LoginResultEntity;
import com.hl.indpark.entities.Response;
import com.hl.indpark.nets.Net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yjl on 2021/3/8 9:36
 * Function：
 * Desc：
 */
public class UserRepo {
    public static LiveData<Resource<Response<LoginResultEntity>>> login(Map map) {
        return Net.api().login((HashMap<String, String>) map);
    }

}
