package com.ximalaya.demo;

import org.cr.aspect.MetricFactory;

/**
 * Hello world!
 */
public class App {

  public void xx() {
    System.nanoTime();
  }

  public static void main(String[] args) {
    System.out.println("Hello World!");
    //    System.out.println(Long.MAX_VALUE);
    //    System.out.println(1000000000);
    // 默认最大 10sec
    //    ConcurrentHistogram histogram = new ConcurrentHistogram(1000000000 * 100, 1);
    //    System.out.println(histogram.getEstimatedFootprintInBytes());
    new App().xx();

    System.out.println(MetricFactory.getLocalMap().keySet());
  }
}
