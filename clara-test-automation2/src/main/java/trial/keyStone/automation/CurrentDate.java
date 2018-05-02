package trial.keyStone.automation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate {
	public static void main(String args[]) {

		        Date date = new Date();
	
		        System.out.println("Today's date is: "+date.toString());

		        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
	
		        System.out.println("Today's date is: "+dateFormat.format(date));
	
		        SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		
		        String strdate2 = "02-04-2013 11:35:42";
	
		        try {
		
		            Date newdate = dateformat2.parse(strdate2);
	
		            System.out.println(newdate);

		        } catch (ParseException e) {

		            e.printStackTrace();

		        }
	}

}
