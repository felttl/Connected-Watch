import java.text.SimpleDateFormat;
import java.util.Date;

public class Watch {

    private char[] id; // []
    private double heartRate;
    private double temp;
    private char[] wdate; // [21]

    public Watch(){
        id = new char[34];
        UUID uuid = UUID.randomUUID();
        addToCharArray(uuid.toString(), myString);
        double hR = Math.random() * 180;
        int tmp = (int) (Math.random() * 40);
        this.heartRate = hR;        
        this.temp = tmp;
        this.wdate = this.getFormatDate(Date());
    }

    private String getFormatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy mm:ss:SSSS");
        return dateFormat.format(date);
    }

    // get set

    public char[] getId(){ // char[?]
        return this.id;
    }
    public double getHeartRate(){
        return this.heartRate;
    }
    public double getTemp(){
        return this.temp;
    }
    public char[] getDate(){ // char[21]
        return this.wdate;
    } 

    public void setId(char[] id){ // char[?]
        this.id = id;
    }
    public void setHeartRate(double hrate){
        this.heartRate = hrate;
    }
    public void setTemp(double tmp){
        this.temp = tmp;
    }
    public void getDate(char[] wdate){ // char[21]
        this.wdate = wdate;
    } 
    
    public String toString() {
        return "Watch{" +
                "id='" + id + '\'' +
                ", fc=" + heartRate +
                ", temp=" + temp +
                ", date=" + this.getFormatDate(this.wdate) +
                '}';
    }
}
