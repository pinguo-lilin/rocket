package pinguo.rocket.mq.consumer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Test {
	public static void main(String[] args) {
//		InputStream inStream = ClassLoader.getSystemResourceAsStream("src/main/resources/config/production/rmq.properties");
		 InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream("src/main/resources/config/production/rmq.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			ResourceBundle rb = new PropertyResourceBundle(in);
			System.out.println(rb.getString("rmq.namesrvAddr"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
