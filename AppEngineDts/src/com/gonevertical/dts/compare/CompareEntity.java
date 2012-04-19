package com.gonevertical.dts.compare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.gonevertical.dts.lib.FileUtil;
import org.gonevertical.dts.lib.FoundData;
import org.gonevertical.dts.lib.datetime.DateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.download.kind.DownloadKind;
import com.gonevertical.dts.download.totype.DownloadToFile;
import com.gonevertical.dts.utils.ObjectUtils;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

public class CompareEntity {

  private static final Logger log = LoggerFactory.getLogger(DownloadKind.class);

  private ClientFactory cf;

  /**
   * Entity kind
   */
  private String kind;

  private long id;

  private RemoteApiInstaller datastoreAccess;

  private String path;

  public CompareEntity(ClientFactory cf) {
    this.cf = cf;
  }

  public void setEntity(String kind, long id) {
    this.kind = kind;
    this.id = id;
  }

  public void run(String path) {
    this.path = path;
    if (kind == null) {
      return;
    }

    open();


    Key key = KeyFactory.createKey(kind, id);

    Entity entityApp = getAppEngineEntity(key);

    Entity entityFile = getFileEntity(key);

    //entityApp.setPropertiesFrom(entityFile);
    
    setProperties(entityApp, entityFile);
    
    persist(entityApp);

    
    close();
  }
  
  private void setProperties(Entity entityApp, Entity entityFile) {

    Map<String, Object> p = entityFile.getProperties();
    Set<String> k = p.keySet();
    Iterator<String> itr = k.iterator();
    while(itr.hasNext()) {
      String key = itr.next();
      Object value = p.get(key);
      
      Class<? extends Object> cl = value.getClass();
      
      if (value instanceof Double) {
        Double oo = (Double) value;
        if (oo != null) {
          Long ll = oo.longValue();
          value = ll;
        } else {
          value = null;
        }
        
        
      } else if (value instanceof ArrayList<?>) {
       
        ArrayList<String> as = null;
        ArrayList<Long> al = null;
        
        ArrayList v = (ArrayList) value;
        Iterator itr2 = v.iterator();
        while(itr2.hasNext()) {
          Object o = itr2.next();
          
          if (o instanceof String) {
            if (as == null) {
              as = new ArrayList<String>();
            }
            as.add((String) o);
            
          } else if (o instanceof Long) {
            if (al == null) {
              al = new ArrayList<Long>();
            }
            al.add((Long) o);
            
          } else if (o instanceof Double) {
            if (al == null) {
              al = new ArrayList<Long>();
            }
            Double oo = (Double) o;
            if (oo != null) {
              Long ll = oo.longValue();
              al.add(ll);
            }
            
          } 
        }
        if (as != null) {
          value = as;
        } else if (al != null) {
          value = al;
        }
      }
      
      if (value instanceof String) {
        DateTimeParser dtp = new DateTimeParser();
        Date d = dtp.getDate((String)value);
        if (d != null) {
          value = d;
        }
      }
      
      entityApp.setProperty(key, value);
    }
    
  }

  public void runFindKeysInDir(File dir) {
    open();
    
    Key key = KeyFactory.createKey(kind, id);
    String skey = ObjectUtils.convertObjectToString(key);

    String regex = "(" + skey + ")";
    regex = regex.replaceAll("\\{", "\\\\{");
    regex = regex.replaceAll("\\}", "\\\\}");
    
    FoundData[] fd = FileUtil.findAllDataInDir(dir, regex);
    for (int i=0; i < fd.length; i++) {
      System.out.println("file=" + fd[i].getFile() + " ::: " + fd[i].getFound());
    }
    
    close();
  }

  private void persist(Entity entity) {

    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Transaction tx = ds.beginTransaction();
    try {
      ds.put(tx, entity);
      tx.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }

  }

  private void open() {
    try {
      datastoreAccess = cf.getAppEngineUtils().open();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void close() {
    cf.getAppEngineUtils().close(datastoreAccess);
  }

  private Entity getAppEngineEntity(Key key) {
    Entity entity = null;    
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      entity = ds.get(key);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("error", e);
    }  
    return entity;
  }

  private Entity getFileEntity(Key key) {

    String skey = ObjectUtils.convertObjectToString(key);

    String regex = "(" + skey + ")";
    regex = regex.replaceAll("\\{", "\\\\{");
    regex = regex.replaceAll("\\}", "\\\\}");

    File file = new File(path); //cf.getFile(kind, date);

    String line = FileUtil.findInFileAndReturnLine(file, regex);

    line = line.replaceAll("\\.0", "");
    
    Entity e = ObjectUtils.stringToObject(Entity.class, line);
    return e;
  }





}
