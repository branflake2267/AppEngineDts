package com.gonevertical.dts.download.kind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;

public class DownloadKindManager {
  
  private static final Logger log = LoggerFactory.getLogger(DownloadKindManager.class);
  
  private ClientFactory cf;
  
  private int threadQuanity = 1;

  private String kind;

  /**
   * init
   * @param cf
   */
  public DownloadKindManager(ClientFactory cf) {
    this.cf = cf;
  }
  
  public void setThreadQuantity(int quantity) {
    this.threadQuanity = quantity;
  }
  
  public void setKind(String kind) {
    this.kind = kind;
  }
  
  public void run() {
    if (kind == null) {
      return;
    }
    
    EntityStat es = cf.getAppEngineUtils().getStat(kind);
    if (es.getCount() == 0) {
      return;
    }
    
    Long total = es.getCount();
    long offset = 0;
    int limit = cf.getShardLimit();
    Long finish = cf.getFinish(); // force a finish?
    
    download(offset, limit, finish, total);
  }

  private void download(long offset, int limit, Long finish, Long total) {
    log.info("starting download kind=" + kind + " offset=" + offset + " finish=" + finish);
    
    DownloadKind dk = new DownloadKind(cf);
    dk.setKind(kind);
    dk.setLimit(offset, limit, finish, total);
    dk.run();
    
    log.info("finished download kind=" + kind + " offset=" + offset + " finish=" + finish);
  }
  
}
