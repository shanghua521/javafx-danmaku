package com.wang.javafxdanmaku.entity;


import java.io.Serializable;

/**
 * @author BanqiJane
 */
public class HostServer implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 8555167206959414211L;
  private String host;
  private Integer port;
  private Integer ws_port;
  private Integer wss_port;

  public HostServer() {
    super();
  }

  public HostServer(String host, Integer port, Integer ws_port, Integer wss_port) {
    super();
    this.host = host;
    this.port = port;
    this.ws_port = ws_port;
    this.wss_port = wss_port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Integer getWs_port() {
    return ws_port;
  }

  public void setWs_port(Integer ws_port) {
    this.ws_port = ws_port;
  }

  public Integer getWss_port() {
    return wss_port;
  }

  public void setWss_port(Integer wss_port) {
    this.wss_port = wss_port;
  }

  @Override
  public String toString() {
    return "HostServer [host=" + host + ", port=" + port + ", ws_port=" + ws_port + ", wss_port=" + wss_port + "]";
  }

}
