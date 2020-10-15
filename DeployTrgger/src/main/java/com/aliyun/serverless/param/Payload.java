package com.aliyun.serverless.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Example:
 * {
 *     "push_data": {
 *         "digest": "sha256:457f4aa83fcxxxxxxxx12a943ed5bf2c1747c58d48bbb4917",
 *         "pushed_at": "2016-11-29 12:25:46",
 *         "tag": "latest"
 *     },
 *     "repository": {
 *         "date_created": "2016-10-28 21:31:42",
 *         "name": "repoTest",
 *         "namespace": "namespace",
 *         "region": "cn-hangzhou",
 *         "repo_authentication_type": "NO_CERTIFIED",
 *         "repo_full_name": "namespace/repoTest",
 *         "repo_origin_type": "NO_CERTIFIED",
 *         "repo_type": "PUBLIC"
 *     }
 * }
 *
 *
 *
 * @author: luoyu
 * @create: 2020-10-14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private PushData pushData;
    private Repository repository;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PushData{
        String digest;
        String pushedAt;
        String tag;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repository{
        private String dateCreated;
        private String name;
        private String namespace;
        private String region;
        private String repoAuthenticationType;
        private String repoFullName;
        private String repoOriginType;
        private String repoType;
    }


}
