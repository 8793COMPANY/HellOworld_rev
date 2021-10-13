package com.corporation.helloworld;

import android.content.Context;
import android.os.Environment;

import java.io.PrintWriter;
import java.io.StringWriter;

import androidx.annotation.NonNull;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler{



    private final String LINE_SEPARATOR="\n";

    private org.apache.log4j.Logger mLogger = org.apache.log4j.Logger.getLogger(ExceptionHandler.class);
    String pattern = "[%d{yyyy-MM-dd HH:mm:ss}] %-5p [%l] - %m%n";

   public ExceptionHandler(Context context){

       final LogConfigurator logConfigurator = new LogConfigurator();
       logConfigurator.setFileName(context.getExternalFilesDir(null).getPath()+ "/logfile.log");
    //   logConfigurator.setFilePattern(pattern);
       logConfigurator.configure();

   }
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
       StringWriter stackTrace = new StringWriter();
       e.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append(stackTrace.toString());

        mLogger.error(errorReport);



   //   android.os.Process.killProcess(android.os.Process.myPid());
   //   System.exit(10);
    }
}
