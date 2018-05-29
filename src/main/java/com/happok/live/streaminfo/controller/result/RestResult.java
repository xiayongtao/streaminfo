package com.happok.live.streaminfo.controller.result;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class RestResult {
    private JSONObject result = new JSONObject(true);

    public JSONObject getSuccess() {
        result.clear();
        result.put("code", RestServiceError.REST_OK.getCode());
        return result;
    }

    public JSONObject getInternalError() {
        result.clear();
        result.put("code", RestServiceError.REST_ERROR.getCode());
        result.put("des", RestServiceError.REST_ERROR.getMsg());
        return result;
    }

    public JSONObject getNotExist() {
        result.clear();
        result.put("code", RestServiceError.REST_NOT_EXISTED.getCode());
        result.put("des", RestServiceError.REST_NOT_EXISTED.getMsg());
        return result;
    }

    public JSONObject getExistd() {
        result.clear();
        result.put("code", RestServiceError.REST_EXISTED.getCode());
        result.put("des", RestServiceError.REST_EXISTED.getMsg());
        return result;
    }

    public JSONObject getParamError() {
        result.clear();
        result.put("code", RestServiceError.REST_PARAM_ERROR.getCode());
        result.put("des", RestServiceError.REST_PARAM_ERROR.getMsg());
        return result;
    }
}
