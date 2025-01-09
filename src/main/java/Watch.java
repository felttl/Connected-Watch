package main.java;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.print.DocFlavor.STRING;


public class Watch {

    private String id; 
    private double heartRate;
    private double temp;
    private String wdate; 

    public Watch(){
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        double hR = Math.random() * 180;
        int tmp = (int) (Math.random() * 40);
        this.heartRate = hR;        
        this.temp = tmp;
        this.wdate = this.getFormatDate(new Date());
    }

    private String getFormatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy mm:ss:SSSS");
        return dateFormat.format(date);
    }

    // get set

    public String getId(){ // char[?]
        return this.id;
    }
    public double getHeartRate(){
        return this.heartRate;
    }
    public double getTemp(){
        return this.temp;
    }
    public String getDate(){ // char[21]
        return this.wdate;
    } 

    public void setId(String id){ // char[?]
        this.id = id;
    }
    public void setHeartRate(double hrate){
        this.heartRate = hrate;
    }
    public void setTemp(double tmp){
        this.temp = tmp;
    }
    public void getDate(String wdate){ // char[21]
        this.wdate = wdate;
    } 
    
    public String toString() {
        return "Watch{" +
                "id='" + id + '\'' +
                ", fc=" + heartRate +
                ", temp=" + temp +
                ", date=" + this.wdate +
                '}';
    }
}
