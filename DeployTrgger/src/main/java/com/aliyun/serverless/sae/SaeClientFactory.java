package com.aliyun.serverless.sae;

import com.aliyun.fc.runtime.Credentials;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.AlibabaCloudCredentials;
import com.aliyuncs.profile.DefaultProfile;

/**
 * @author: luoyu
 * @create: 2020-10-13
 **/
public class SaeClientFactory {


    public static IAcsClient getClient(String regionId, Credentials credentials){
        DefaultProfile profile = DefaultProfile.getProfile(
                regionId,
                credentials.getAccessKeyId(),
                credentials.getAccessKeySecret(),
                credentials.getSecurityToken());
        return new DefaultAcsClient(profile);
    }


}
