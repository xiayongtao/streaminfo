package com.happok.live.streaminfo.entity.srs;

public class Kbps {
    private Integer recv_30s;
    private Integer send_30s;

    public Kbps() {
    }

    public Integer getRecv_30s() {
        return recv_30s;
    }

    public void setRecv_30s(Integer recv_30s) {
        this.recv_30s = recv_30s;
    }

    public Integer getSend_30s() {
        return send_30s;
    }

    public void setSend_30s(Integer send_30s) {
        this.send_30s = send_30s;
    }
}
