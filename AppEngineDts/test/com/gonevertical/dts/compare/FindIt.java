package com.gonevertical.dts.compare;

import java.io.File;

import org.gonevertical.dts.lib.FileUtil;
import org.gonevertical.dts.lib.FoundData;

public class FindIt {

  public static void main(String[] args) {
    new FindIt().run();
  }
  
  public FindIt() {
    
  }
  
  private void run() {
    
    // elizabeth dickson - 37023
    // learning plan - "id":36024
    // \"ClassJdo\",\"id\":1067
    // \"CourseJdo\",\"id\":231
    
    File dir = new File("/Users/branflake2267/tmp/slp2012-3-12");
    String regex = "(\"CourseJdo\",\"id\":5166)";
    
    FoundData[] fd = FileUtil.findAllDataInDir(dir, regex);
    for (int i=0; i < fd.length; i++) {
      System.out.println("file=" + fd[i].getFile() + " ::: " + fd[i].getFound());
    }
  }
}
