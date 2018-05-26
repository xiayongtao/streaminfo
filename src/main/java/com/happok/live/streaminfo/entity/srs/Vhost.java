package com.happok.live.streaminfo.entity.srs;

public class Vhost {

    private Integer id;
    private String name;
    private Boolean enabled;
    private Integer clients;
    private  Integer streams;
    private Integer send_bytes;
    private  Integer recv_bytes;
    private  Kbps kbps;

    public Vhost() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getClients() {
        return clients;
    }

    public void setClients(Integer clients) {
        this.clients = clients;
    }

    public Integer getStreams() {
        return streams;
    }

    public void setStreams(Integer streams) {
        this.streams = streams;
    }

    public Integer getSend_bytes() {
        return send_bytes;
    }

    public void setSend_bytes(Integer send_bytes) {
        this.send_bytes = send_bytes;
    }

    public Integer getRecv_bytes() {
        return recv_bytes;
    }

    public void setRecv_bytes(Integer recv_bytes) {
        this.recv_bytes = recv_bytes;
    }

    public Kbps getKbps() {
        return kbps;
    }

    public void setKbps(Kbps kbps) {
        this.kbps = kbps;
    }
}
