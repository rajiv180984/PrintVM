package om.first.machin.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.first.machin.print.PrintSide;

@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PrintProperties implements Serializable {

	private int numberOfCopies;

	private String printSide;

	private boolean isCollate;

	
	private String pexpn;

	private boolean color;

	private String printQuality;
	
	private int pageCount;
	
	private String pSize;

	private String servercertkey;
	
	// Portrait, Landscape
	private String orientation;

	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(int numberOfCopies) {
		if(numberOfCopies == 0) {
			numberOfCopies = 1;
		}
		this.numberOfCopies = numberOfCopies;
	}

	public String getPrintSide() {
		return printSide;
	}

	public void setPrintSide(String printSide) {
		if(printSide== null) {
			printSide = PrintSide.DUPLEX.name();
		}
		
		this.printSide = printSide;
	}

	public boolean isCollate() {
		return isCollate;
	}

	public void setCollate(boolean isCollate) {
		this.isCollate = isCollate;
	}

	
	public String getPexpn() {
		return pexpn;
	}

	public void setPexpn(String pexpn) {
		this.pexpn = pexpn;
	}


	public String getServercertkey() {
		return servercertkey;
	}

	public void setServercertkey(String servercertkey) {
		this.servercertkey = servercertkey;
	}
	
	public boolean isColoured() {
		return color;
	}

	public void setColoured(boolean isColoured) {
		this.color = isColoured;
	}

	public String getPrintQuality() {
		return printQuality;
	}

	public void setPrintQuality(String printQuality) {
		this.printQuality = printQuality;
	}

	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public String getpSize() {
		return pSize;
	}

	public void setpSize(String pSize) {
		this.pSize = pSize;
	}
	
	

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	@Override
	public String toString() {
		return "PrintProperties [numberOfCopies=" + numberOfCopies + ", printSide=" + printSide + ", isCollate="
				+ isCollate + ", pexpn=" + pexpn + ", color=" + color + ", printQuality=" + printQuality
				+ ", pageCount=" + pageCount + ", pSize=" + pSize + ", servercertkey=" + servercertkey
				+ ", orientation=" + orientation + "]";
	}

	

}
