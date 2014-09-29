package netty.bess.stat;


import java.text.SimpleDateFormat;
import java.util.Date;

public class IpData {

    private int count;
    private String time;


    public IpData() {
        this.time=CurrentTimeStamp();
        count=1;
    }

    public void incrementCount ()  {
        count++;
    }

    public void updateTime() {
        time=CurrentTimeStamp();
    }

    public  String CurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String formatCurrentTime = sdfDate.format(currentTime);
        return formatCurrentTime;
    }

    public int getCount() {
        return count;
    }

    public String getTime() {
        return time;
    }
}
