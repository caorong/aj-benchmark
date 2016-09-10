package org.cr.aspect;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.HdrHistogram.ConcurrentHistogram;
import org.HdrHistogram.Histogram;

/**
 * desc...
 *
 * @author caorong
 */
public class MetricFactory {

  static Map<String, Histogram> localMap = new ConcurrentHashMap<String, Histogram>(200);

  public static Map<String, Histogram> getLocalMap() {
    return localMap;
  }

  public static Histogram get(String method) {
    Histogram histogram = localMap.get(method);
    if (histogram == null) {
      // 纪录最大100 sec
      histogram = new ConcurrentHistogram(1000000000 * 100, 1);
      localMap.put(method, histogram);
    }
    return histogram;
  }

  // add in runtime
//  Runtime.getRuntime().addShutdownHook(new Thread() {
//      @Override
//      public void run() {
//        System.out.println("Shutdown hook ran!");
//        printMetric();
//      }
//    });
//
  public static void printMetric() {
    final TreeMap<String, Histogram> treeMap = new TreeMap<String, Histogram>(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        if (MetricFactory.getLocalMap().get(o1).getTotalCount() > MetricFactory.getLocalMap().get(o2)
            .getTotalCount()) {
          return 1;
        } else if (MetricFactory.getLocalMap().get(o1).getTotalCount() == MetricFactory.getLocalMap().get(o2)
            .getTotalCount()) {
          // total 相同，比较时间花费
          if (MetricFactory.getLocalMap().get(o1).getMean() > MetricFactory.getLocalMap().get(o2).getMean()) {
            return 1;
          }
          return -1;
        }
        return -1;
      }
    });
    treeMap.putAll(MetricFactory.getLocalMap());

    for (Map.Entry<String, Histogram> entry : treeMap.entrySet()) {
      Histogram histogram = entry.getValue();
      if (histogram.getTotalCount() > 10) {
        System.out.println(entry.getKey() + " count:" + histogram.getTotalCount());
        System.out.println(entry.getKey() + " 99%:" + histogram.getValueAtPercentile(99));
        System.out.println(entry.getKey() + " 95%:" + histogram.getValueAtPercentile(95));
        System.out.println(entry.getKey() + " max:" + histogram.getMaxValue());
        System.out.println(entry.getKey() + " avg:" + histogram.getMean());
      }
    }
  }

}
