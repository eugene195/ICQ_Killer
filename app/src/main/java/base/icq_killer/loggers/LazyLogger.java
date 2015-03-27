package base.icq_killer.loggers;

import android.os.Environment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * This is Logger implementation that writes to memory until LazyLogger.dumpLog() is called.
 */
public class LazyLogger implements Logger {
    private String source;
    private static final String directory = Environment.getExternalStorageDirectory() + "/Download/";
    private static ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
    private static Date launched = new Date();
    protected LazyLogger(String className) {
        this.source = className;
    }
    protected LazyLogger() {}

    public static Logger getInstance(String className) {
        return new LazyLogger(className);
    }

    @Override
    public void log(String tag, String msg) {

    }

    @Override
    public void log(String msg) {
        //Trying Java 1.7 features
        PrintStream printStream = new PrintStream(new BufferedOutputStream(memoryStream));
        try {
            printStream.println(this.source + " " + msg + " " +
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        }
        finally {
            printStream.close();
        }
    }

    @Override
    public ByteArrayOutputStream getLog() {
        return memoryStream;
    }


    public void dumpLog() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY MM dd HH mm ss");
            String fileName = directory + simpleDateFormat.format(launched) + " - " + simpleDateFormat.format(new Date()) + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            memoryStream.writeTo(new FileOutputStream(file, true));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
