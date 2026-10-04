import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;

public class PrintPSDemo {

        public static void main(String args[]) {
                PrintPSDemo ps = new PrintPSDemo();
        }
        public PrintPSDemo() {
                /* Construct the print request specification.
                 * The print data is Postscript which will be 
                 * supplied as a stream.  The media size 
                 * required is A4, and 2 copies are to be printed
                 */
        	
        	HashDocAttributeSet docAttr=new HashDocAttributeSet();
        	HashPrintRequestAttributeSet reqAttr=new HashPrintRequestAttributeSet();

                DocFlavor flavor  = DocFlavor.INPUT_STREAM.PNG;
                PrintRequestAttributeSet aset = 
                        new HashPrintRequestAttributeSet();
                aset.add(MediaSizeName.ISO_A4);
                aset.add(new Copies(2));
                aset.add(Sides.ONE_SIDED);
//                aset.add(Finishings.STAPLE);

                /* locate a print service that can handle it */
                PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
                System.out.println(ps.toString());
                PrintService[] pservices =
                        PrintServiceLookup.lookupPrintServices(flavor, aset);
                if (pservices.length > 0) {
                        System.out.println("selected printer " + pservices[0].getName());

                    //    DocPrintJob job = ps.createPrintJob();
                  //      DocFlavor flavor1 = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                        /* create a print job for the chosen service */
                        DocPrintJob pj =pservices[0].createPrintJob();
                        try {
                                /* 
                                * Create a Doc object to hold the print data.
                                * Since the data is postscript located in a disk file,
                                * an input stream needs to be obtained
                                * BasicDoc is a useful implementation that will if requested
                                * close the stream when printing is completed.
                                */
                                FileInputStream fis = new FileInputStream("/home/rajeev/Downloads/selection.png");
                                System.out.println(" =========="+ fis.available());
                                Doc doc = new SimpleDoc(fis, flavor, null);
                             
                                /* print the doc as specified */
                                pj.print(doc, aset);
                                /*
                                * Do not explicitly call System.exit() when print returns.
                                * Printing can be asynchronous so may be executing in a
                                * separate thread.
                                * If you want to explicitly exit the VM, use a print job 
                                * listener to be notified when it is safe to do so.
                                */

                        } catch (IOException ie) { 
                                System.err.println(ie);
                                ie.printStackTrace();
                                
                        } catch (PrintException e) { 
                        	e.printStackTrace();
                        	
                                System.err.println(e);
                                
                        }
                }
        }
}