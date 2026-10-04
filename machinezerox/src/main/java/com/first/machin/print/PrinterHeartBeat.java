package com.first.machin.print;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.first.machin.service.RequestStatusUpdateService;

@Service
public class PrinterHeartBeat {
	private Logger logger = LoggerFactory.getLogger(PrinterHeartBeat.class);
	
	@Value("${printername}")
	private String printername ="HLL2320D";
	
	@Value("${machineId}")
	private String machineId;
	
	
	Runtime runtime = Runtime.getRuntime();
	
	private String currentAddress; 
	 
	@Autowired
	RequestStatusUpdateService requestStatusUpdateService;

	
	@Scheduled(fixedDelay = 30000)
	public void printHeartBeat() {
     
		System.out.println("This is pending ");
		Process process = null;
		try {
			 
			 process = runtime.exec("lpstat -p "+printername);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			process.destroy();
		}
	}
	

	@Scheduled(fixedDelay = 30000)
	public void updateIPAddress() {
     	try {
			 
     		String address = getLocalIpAddress();
     		requestStatusUpdateService.updateIPStatusOnCloud(machineId, address);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}
	
	
	public boolean getPrintJobId() {
		
		// check printer is online or offline
		Process process = null;
		try {
			process = runtime.exec("lpstat -p "+printername);
			process.getInputStream();
			isPrinterReadyToPrint(process, printername);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
						System.out.println(" Printer Ready to is unavailable ");
						//notify cloud with machineId and status
						
					}
					if(status.contains("Sending data to printer")) {
						System.out.println(" Printer Ready to Print");
						// sending request to Cloud priner available
						
					}
				}else {
					System.out.println(" Printer no staus Online");
				}
				System.out.println(System.currentTimeMillis());
			}
		}
	}
	public boolean cancelJobByJobName(Runtime runtime, String command) {
		System.out.println("========= Cancel Print Jobs ==============");
		try {
			Process process = runtime .exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s= null;
			int index =0;
			String jobId = null;
			while ((s = br.readLine()) != null) {
				System.out.println(" Command result"+s);
				 index = s.charAt('\f');
				 jobId = s.substring(0, index-1);
				 System.out.println("jobId :"+ jobId);
				 
			}
			// cancell Last Print command
			if(jobId !=null) {
			runtime.exec("cancel "+jobId);
			return true;
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  false;
	}

	public boolean checkPrinerIsUnavailable(Runtime runtime, String printerName, String command, String machineId) throws IOException {
		System.out.println(" Checking printer status ============"+ command + " :     "+printerName);
		Process process = runtime .exec(command);
		String s;
		try {
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
						// FIXME
						// sending update cloud about offline 
						// cancel Print Job other where Printer available online will print
						// intiate refund
						return false;
						
					}
					if(status.contains("Sending data to printer")) {
						System.out.println(" Printer Ready to Print");
						// sending request to Cloud priner available
						notifyCloud(machineId, true);
						return true;
					}
				}else {
					System.out.println(" Printer no staus Online");
					return false;
				}
				System.out.println(System.currentTimeMillis());
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("failed to get error :", e);
		}finally {
			process.destroy();
		}
		return true;
	}

	
	
	private void notifyCloud(String machineId, boolean b) {
		
		
	}

	public static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
	                    return inetAddress.getHostAddress();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}

	public static void main(String[] args) {
		System.out.println(" this is ========");
		PrinterHeartBeat hb = new PrinterHeartBeat();
		hb.getPrintJobId();
		
	}

}
