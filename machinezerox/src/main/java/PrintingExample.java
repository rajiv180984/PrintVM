import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterInfo;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobAdapter;

import com.first.machin.print.ZeroxPrintJobListener;

public class PrintingExample {

	public static void main(String[] args) throws PrintException, IOException {
		  DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		  PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
		  patts.add(Sides.DUPLEX);
		  patts.add(new PageRanges(1));
		 // patts.add(MediaSize.ISO.A4);
		  PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
		  if (ps.length == 0) {
		    throw new IllegalStateException("No Printer found");
		  }
		  System.out.println("Available printers: " + Arrays.asList(ps));

		  PrintService myService = null;
		  for (PrintService printService : ps) {
		    if (printService.getName().contains("HLL2320D")) {
		      myService = printService;
		      break;
		    }
		  }
		  
		 
		  if (myService == null) {
		    throw new IllegalStateException("Printer not found");
		  }
		  PrintServiceAttributeSet printServiceAttributeSet =  myService.getAttributes();
		  System.out.println(" this is dkdfj :"+ printServiceAttributeSet.toString());
		  Attribute[] atr = printServiceAttributeSet.toArray();
		  for(Attribute at : atr) {
			  System.out.println(" ========= this is value "+ at.toString());
		  }
		  
		  printServiceAttributeSet.get(PrinterState.class);
		  String printerStateReason = printServiceAttributeSet.get(PrinterStateReason.class).toString();
		  
		  DocFlavor[] docFlours =   myService.getSupportedDocFlavors();
		  System.out.println(" docflouvour : "+docFlours.toString() );
		  // doc flouvour 
		  for(DocFlavor docfl : docFlours) {
			//  System.out.println(" ========== "+ docfl.toString());
		  }
		  PrintServiceAttributeSet printerState1 = myService.getAttributes();
		  
		  AttributeSet attributes = myService.getAttributes();
		  

		  
		  Attribute printerStatus = attributes.get(PrinterIsAcceptingJobs.class);
		  
		//  String printerStateReason = attributes.get(PrinterStateReason.class).toString();

		  System.out.println("printerState = " + printerStatus.getName()+ printerStatus.toString()); // May be IDLE, PROCESSING, STOPPED or UNKNOWN
		 // System.out.println("printerStateReason = " + printerStateReason); // If your printer state returns STOPPED, for example, you can identify the reason 

		  
		  for(Attribute attr : printerState1.toArray()) {
			 System.out.println(" his is ======= "+ attr.getName()+" :"+ attr.getCategory().getName()); 
			 
		  }
				  //.get(PrinterState.class)
			
			   
		  FileInputStream fis = new FileInputStream("/home/rajeev/ZeroxVM/test.pdf");
		  Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
		  DocPrintJob printJob = myService.createPrintJob();
		  printJob.addPrintJobListener(new ZeroxPrintJobListener());
		  printJob.print(pdfDoc, new HashPrintRequestAttributeSet()) ;
		 
		  fis.close();        
		}
}

class PrintJobWatcher {
	  boolean done = false;

	  PrintJobWatcher(DocPrintJob job) {
	    job.addPrintJobListener(new PrintJobAdapter() {
	      public void printJobCanceled(PrintJobEvent pje) {
	        synchronized (PrintJobWatcher.this) {
	          done = true;
	          PrintJobWatcher.this.notify();
	        }
	      }

	      public void printJobCompleted(PrintJobEvent pje) {
	    	  System.out.println(" Hello this is compleed ========");
	        synchronized (PrintJobWatcher.this) {
	          done = true;
	          System.out.println(" Hello this is compleed ========1");
	          PrintJobWatcher.this.notify();
	        }
	      }

	      public void printJobFailed(PrintJobEvent pje) {
	        synchronized (PrintJobWatcher.this) {
	          done = true;
	          PrintJobWatcher.this.notify();
	        }
	      }

	      public void printJobNoMoreEvents(PrintJobEvent pje) {
	        synchronized (PrintJobWatcher.this) {
	          done = true;
	          PrintJobWatcher.this.notify();
	        }
	      }
	    });
	  }

	  public synchronized void waitForDone() {
	    try {
	      while (!done) {
	        wait();
	      }
	    } catch (InterruptedException e) {
	    }
	  }
	}