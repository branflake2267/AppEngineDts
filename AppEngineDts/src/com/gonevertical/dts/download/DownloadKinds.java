package com.gonevertical.dts.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;

public class DownloadKinds {
  
  private static final Logger log = LoggerFactory.getLogger(DownloadKinds.class);
  
  private ClientFactory cf;
  
  private int threadQuanity = 1;

  private String[] kind;

  /**
   * init
   * @param cf
   */
  public DownloadKinds(ClientFactory cf) {
    this.cf = cf;
  }
  
  public void setKind(String[] kind) {
    this.kind = kind;
  }
  
  public void run() {
    if (kind == null || kind.length == 0) {
      return;
    }
    
    
    
  }
  
}
