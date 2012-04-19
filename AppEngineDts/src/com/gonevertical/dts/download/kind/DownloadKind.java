package com.gonevertical.dts.download.kind;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gonevertical.dts.data.ColumnData;
import org.gonevertical.dts.data.ColumnData.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.data.EntityType;
import com.gonevertical.dts.download.totype.DownloadToFile;
import com.gonevertical.dts.utils.ObjectUtils;
import com.gonevertical.dts.utils.Sharding;
import com.gonevertical.dts.utils.SqlUtils;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.protobuf.ByteString;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

public class DownloadKind {
  
  private static final Logger log = LoggerFactory.getLogger(DownloadKind.class);
  
  private ClientFactory cf;
  
  /**
   * Entity kind
   */
  private String kind;
  
  /**
   * sharding control
   */
  private Sharding shard = new Sharding();

  /**
   * file utils
   */
  private DownloadToFile fileUtils;
  
  /**
   * constructor - init this 
   * @param utils
   */
  public DownloadKind(ClientFactory cf) {
    this.cf = cf;
  }
  
  public void setKind(String kind) {
    this.kind = kind;
  }
  
  public void setLimit(long offset, int limit, Long finish, Long total) {
    shard.setLimit(offset, limit, finish);
    shard.setCounts(total);
  }
  
  /**
   * lets start
   */
  public void run() {
    if (kind == null) {
      return;
    }
    
    if (fileUtils == null) {
      fileUtils = new DownloadToFile(cf);
    }
    
    boolean b = fileUtils.openFile(kind);
    if (b == false) {
      log.error("Exiting download of kind=" + kind + " becuase there is something wrong with the file writer. Check the directory permissions?");
      return;
    }
    
    loop();
    
    fileUtils.closeFile();
  }
  
  private void loop() {
    int shards = shard.getShards();
    for (int i=0; i < shards; i++) {
      long[] range = shard.getRange(i);
      processRange(range);
    }
  }

  private void processRange(long[] range) {
    long start = range[0];
    long finish = range[1]; 
    log.info("kind=" + kind + " Range= start=" + start + " finish=" + finish);
    
    ArrayList<Entity> entities = getDataQuery(start, shard.getLimit());
    Iterator<Entity> itr = entities.iterator();
    int i = 0;
    while(itr.hasNext()) {
      Entity e = itr.next();
      save(i+range[0], e);
      i++;
    }
  }
  
  private ArrayList<Entity> getDataQuery(long offset, long limit) {
    log.info("kind=" + kind + "  offset=" + offset + " finish=" + limit);
    RemoteApiInstaller datastoreAccess = null;
    try {
      datastoreAccess = cf.getAppEngineUtils().open();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (datastoreAccess == null) {
      return null; 
    }
    ArrayList<Entity> r = null;
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      Query q = new Query(kind);
      PreparedQuery pq = ds.prepare(q);
      FetchOptions options = FetchOptions.Builder.withOffset((int)offset).limit((int)limit);
      Iterable<Entity> itr = pq.asIterable(options);
      for (Entity e : itr) {
        if (r == null) {
          r = new ArrayList<Entity>();
        }
        r.add(e);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("error", e);
    } finally {
      cf.getAppEngineUtils().close(datastoreAccess);
    } 
    return r;
  }

  private void save(long index, Entity entity) {
    saveToFile(index, entity);
  }
  
  private void saveToFile(long index, Entity entity) {
    fileUtils.saveToFile(entity);
  }
  
}
