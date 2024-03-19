package com.aliyun.serverless;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.HttpRequestHandler;
import com.aliyun.serverless.param.Payload;
import com.aliyun.serverless.sae.SaeClientFactory;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.sae.model.v20190506.DeployApplicationRequest;
import com.aliyuncs.sae.model.v20190506.DeployApplicationResponse;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class DeployTrigger implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws IOException, ServletException {
        String desireToken = System.getenv("ACCESS_TOKEN");
        FunctionComputeLogger logger = context.getLogger();
        String token = request.getParameter("ACCESS_TOKEN");
        // ignore authentication if token is not provided
        if (token != null && !Objects.equals(desireToken, token)) {
            response.setStatus(401);
            response.getOutputStream().write(("Unauthorized token: " + token).getBytes());
            return;
        }
        String appId = request.getParameter("APP_ID");
        ServletInputStream bodyStream = request.getInputStream();
        Payload payload = JSONObject.parseObject(bodyStream, Payload.class);
        logger.info("Payload: " + JSONObject.toJSONString(payload, true));
        if (payload == null || payload.getPushData() == null) {
            response.setStatus(200);
            return;
        }

        String regionId = payload.getRepository().getRegion();
        IAcsClient client = SaeClientFactory.getClient(regionId, context.getExecutionCredentials());
        String imageUrl = getImageUrl(payload);
        DeployApplicationResponse resp;
        try {
            resp = deployApplication(client, appId, imageUrl);
            logger.info(String.format("Trigger deploy image[%s] to application[%s].", appId, imageUrl));
        } catch (Exception e) {
            logger.error(String.format("Trigger deploy image[%s] to application[%s] failed.", appId, imageUrl));
            response.setStatus(500);
            OutputStream out = response.getOutputStream();
            out.write(String.format("Trigger deploy image[%s] to application[%s] failed.\n", appId, imageUrl).getBytes());
            out.write((e.toString()).getBytes());
            out.flush();
            out.close();
            return;
        }
        writeResponse(response, resp);
    }

    private void writeResponse(HttpServletResponse response, DeployApplicationResponse resp) throws IOException {
        if (resp == null) {
            response.setStatus(500);
            return;
        }
        int httpCode = resp.getSuccess() ? 200 : 500;
        response.setStatus(httpCode);
        ServletOutputStream out = response.getOutputStream();
        out.write(JSONObject.toJSONBytes(resp));
        out.flush();
        out.close();
    }

    private String getImageUrl(Payload payload) {
        if (payload == null) {
            return "";
        }
        // China mainland
        // String imageUrl = String.format("registry-vpc.%s.aliyuncs.com/%s:%s",
        String imageUrl = String.format("acr-20231204-registry.%s.aliyuncs.com/%s:%s",
                payload.getRepository().getRegion(),
                payload.getRepository().getRepoFullName(),
                payload.getPushData().getTag());
        return imageUrl;
    }

    private DeployApplicationResponse deployApplication(IAcsClient client, String appId, String imageUrl) throws ClientException {
        DeployApplicationRequest request = new DeployApplicationRequest();
        request.setAppId(appId);
        request.setImageUrl(imageUrl);
        DeployApplicationResponse resp = client.getAcsResponse(request);
        return resp;
    }

}
