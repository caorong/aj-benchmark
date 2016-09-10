package org.cr.aspect;

import org.HdrHistogram.Histogram;

/**
 * desc...
 * @author caorong
 */
public aspect TimeAspect {

  pointcut TimePointCut():execution(* com.ximalaya..*(..)) ;
  //  pointcut TimePointCut():execution(* com.ximalaya..*(..)) ;
  //  pointcut TimePointCut():call(* com.ximalaya.*(..)) ;
  //    pointcut TimePointCut():call(* *(..)) ;

  Object around():TimePointCut(){
    Histogram histogram = MetricFactory.get(thisJoinPoint.getSignature().toString());

//    System.out.println(thisJoinPoint.getSignature());
    long start = System.nanoTime();
    Object retVal = proceed();
    long cost = System.nanoTime() - start;

    try {
      if (cost > 0 && cost < 1000000000 * 100) {
        histogram.recordValue(cost);
      }
    } catch (Throwable e) {
    }

    return retVal;
  }

}
