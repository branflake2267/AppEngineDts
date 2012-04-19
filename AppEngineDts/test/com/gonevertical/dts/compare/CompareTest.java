package com.gonevertical.dts.compare;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.download.kind.DownloadKind;
import com.gonevertical.dts.utils.AppEngineUtils;
import com.gonevertical.dts.utils.SqlUtils;

public class CompareTest {

  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/dev/dev.properties";

  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDownloadPeopleJdo() {

    ClientFactory cf = null;
    try {
      cf = new ClientFactory(new File(configfilepath));
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (cf == null) {
      fail("no client factory");
    }

    if (1==1) {
      return;
    }

    
    String kind = "PeopleJdo";
    
    
    long id = 117003;
    
    String path = "";
    
    File dir = new File("/Users/branflake2267/tmp/slp2012-2-1");
    
    
    CompareEntity ce = new CompareEntity(cf);
    ce.setEntity(kind, id);
    //ce.run(path);
    ce.runFindKeysInDir(dir);
    
    
    
    System.out.println("finished test");
  }


  public void finished() {
    
    //35081 kirby /Users/branflake2267/tmp/slp2012-2-1/2012-01-04__00-00-11___PeopleJdo
    //36070 alexander j /Users/branflake2267/tmp/slp2012-2-1/2012-01-09__00-00-09___PeopleJdo
    //75003 karen /Users/branflake2267/tmp/slp2012-2-1/2012-01-27__00-00-08___PeopleJdo
    //112001 /Users/branflake2267/tmp/slp2012-2-1/2012-02-01__00-00-08___PeopleJdo
    //115001 /Users/branflake2267/tmp/slp2012-2-1/2012-02-01__00-00-08___PeopleJdo
    // 116002 /Users/branflake2267/tmp/slp2012-2-1/2012-02-01__00-00-08___PeopleJdo
    // 117002 /Users/branflake2267/tmp/slp2012-2-1/2012-02-01__00-00-08___PeopleJdo
    
    //117003
  }

}
