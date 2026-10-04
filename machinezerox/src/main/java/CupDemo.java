import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.cups4j.PrintRequestResult;

public class CupDemo {
	public static void main(String[] args) {

		System.out.println(" this is testing ");

		CupsClient cupsClient;
		try {

			Process process = Runtime.getRuntime().exec("lpstat -p HLL2320D");

			isPrinterReadyToPrint(process, "HLL2320D");
			process.waitFor();
			System.out.println("exit: " + process.exitValue());
			process.destroy();

		//System.exit(0);
			cupsClient = new CupsClient("localhost", 631);
			CupsPrinter cupsPrinter = cupsClient.getPrinter(new URL("http://localhost:631/printers/HLL2320D"));
			if (cupsClient == null) {
				System.out.println(" Sorry ");
			}

			List<CupsPrinter> list = cupsClient.getPrinters();
			
			if (cupsPrinter == null) {
				System.out.println(" Ye kya BC Hai");
			}
			InputStream inputStream = new FileInputStream("/home/rajeev/Gauri Classes/gauri");
			PrintJob printJob = new PrintJob.Builder(inputStream).build();
	
			PrintRequestResult printRequestResult = cupsPrinter.print(printJob);
			System.out.println(" ===## =="+printRequestResult.getJobId());
			
			System.out.println(" ============" + printRequestResult.toString());
			cupsPrinter.getDescription();

			System.out.println("=================================" + cupsPrinter.getDescription());
			
			System.out.println("************ :"+cupsPrinter.getJobStatus(584758).name());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void isPrinterReadyToPrint(Process process, String printerName) throws IOException {
		String s;
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((s = br.readLine()) != null) {
			System.out.println("line: " + s);
			if (s.contains(printerName)) {
				System.out.println(System.currentTimeMillis());
				String status = br.readLine();
				System.out.println(status);
				if(status !=null) {
					//
					if(status.contains("Waiting for printer to become available")) {
						// sending update cloud about offline 
						
						// cancel request 
					}
					if(status.contains("Sending data to printer")) {
						System.out.println(" Printer Ready to Print");
						// sending request to Cloud priner available
						
					}
				}else {
					System.out.println(" Printer Online");
				}
				System.out.println(System.currentTimeMillis());
			}
		}
	}
	
	// cancelPrintJob by ID 
	
	// getJOB Print job Status
	
}
