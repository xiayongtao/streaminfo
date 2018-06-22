package com.happok.live.streaminfo.service.record;

import java.util.Map;

public class CommandAssemblyLive implements CommandAssembly {
    @Override
    public String assembly(Map<String, String> paramMap) {
        try {
            if (paramMap.containsKey("ffmpegPath")) {
                String ffmpegPath = (String) paramMap.get("ffmpegPath");
                StringBuilder comm = new StringBuilder();
                if (paramMap.containsKey("input")
                        && paramMap.containsKey("output")
                        && paramMap.containsKey("appName")) {

                    String input = (String) paramMap.get("input");
                    String output = (String) paramMap.get("output");
                    String appName = (String) paramMap.get("appName");

                    comm.append(ffmpegPath);
                    if (input.startsWith("rtsp:")){
                        comm.append(" -rtsp_transport tcp ");
                    }

                    comm.append(" -i "+ input);
                    comm.append(" -vcodec copy ");
                    comm.append(" -acodec copy ");
                    comm.append(" -f flv " + output + appName);
                    
                    return comm.toString();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}

