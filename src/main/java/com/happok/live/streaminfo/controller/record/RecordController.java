package com.happok.live.streaminfo.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/")
public class RecordController {
    @Autowired
    private RestResult restResult = null;

    @Autowired
    private RecordService recordService = null;

    @GetMapping("/records")
    public Object getRecords() {
        return recordService.getRecords();
    }

    @GetMapping("/record/{dirName}")
    public Object getRecord(@PathVariable("dirName") String dirName) {
        return recordService.getRecord(dirName);
    }

    @GetMapping("/record/status/{dirName}")
    public Object getRecordStatus(@PathVariable("dirName") String dirName) {
        return recordService.getRecordStatus(dirName);
    }

    @PostMapping("/record/start")
    public Object Start(@RequestBody String body) {

        JSONObject jsonResult = JSONObject.parseObject(body);
        String dirName = jsonResult.getString("dirName");
        String srcUrl = jsonResult.getString("srcUrl");

        if (null == dirName || null == srcUrl) {
            return restResult.getParamError();
        }

        return recordService.Star(dirName, srcUrl);
    }

    @DeleteMapping("/record/stop/{dirName}")
    public Object Stop(@PathVariable("dirName") String dirName) {
        return recordService.Stop(dirName);
    }

    @DeleteMapping("/records")
    public Object RemoveAllFiles() {
        return recordService.RemoveAllFiles();
    }

    @DeleteMapping("/record/{dirName}")
    public Object RemoveFiles(@PathVariable("dirName") String dirName) {
        return recordService.RemoveFiles(dirName);
    }

    @DeleteMapping("/record/{dirName}/file/{name}")
    public Object RemoveFile(@PathVariable("dirName") String dirName
            , @PathVariable("fileName") String fileName) {
        return recordService.RemoveFile(dirName, fileName);
    }
}
