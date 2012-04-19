package com.gonevertical.dts.download;

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.download.kind.DownloadKindManager;

public class DownloadKindsManager {
  
  private static final Logger log = LoggerFactory.getLogger(DownloadKindsManager.class);
  
  private ClientFactory cf;

  private ArrayList<String> kinds;

  /**
   * init
   * @param cf
   */
  public DownloadKindsManager(ClientFactory cf) {
    this.cf = cf;
  }

  public void run() {
    
    loadKinds();
    if (kinds == null) {
      log.error("Couldn't find any kinds. Exiting.");
      return;
    }
    
    download();
  }

  /**
   * download kinds
   */
  private void download() {
    Iterator<String> itr = kinds.iterator();
    while(itr.hasNext()) {
      String kind = itr.next();
      download(kind);
    }
  }

  /**
   * download kind
   * @param kind
   */
  private void download(String kind) {
    log.info("Starting download of kind=" + kind);
    DownloadKindManager dkm = new DownloadKindManager(cf);
    dkm.setKind(kind);
    dkm.run();
  }

  private void loadKinds() {
    ArrayList<String> list = cf.getDownloadEntities();
    if (list == null || list.contains("all") == true) {
      kinds = cf.getAppEngineUtils().getKinds(false);
      
    } else {
      kinds = list;
    }
   
  }

  
  
}
