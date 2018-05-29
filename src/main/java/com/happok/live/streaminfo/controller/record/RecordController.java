package com.happok.live.streaminfo.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/")
public class RecordController {

    @Autowired
    private RecordService recordService = null;

    @GetMapping("/records")
    public Object getRecords() {
        return recordService.getRecords();
    }

    @GetMapping("/record/{id}")
    public Object getRecord(@PathVariable("id") Integer id) {

        return recordService.getRecord(id);
    }

    @PostMapping("/record/start")
    public Object Start(@RequestBody String body) {

        JSONObject jsonResult = JSONObject.parseObject(body);
        return recordService.Star(jsonResult);
    }

    @DeleteMapping("/record/stop/{id}")
    public Object Stop(@PathVariable("id") Integer id) {

        return recordService.Stop(id);
    }

    @DeleteMapping("/record/{id}")
    public Object Delete(@PathVariable("id") Integer id) {

        return recordService.Delete(id);
    }
}
