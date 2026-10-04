package com.first.machin.print;

import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;

public class PrintAttributeUtility {
	public static PageRanges evaluatePageRange(String printPageRange) {
		PageRanges range = null;
		int indexOfHipphon = printPageRange.indexOf("-");

		if (indexOfHipphon > 0) {
			int lowerBound = Integer.parseInt(printPageRange.substring(0, indexOfHipphon - 1).trim());
			int upperBound = Integer
					.parseInt(printPageRange.substring(indexOfHipphon + 1, printPageRange.length()).trim());
			range = new PageRanges(lowerBound, upperBound);
			return range;
		}

		int indexOfComms = printPageRange.indexOf(",");
		if (indexOfComms > 0) {
			range = new PageRanges(printPageRange);
		}

		if (indexOfHipphon < 0 || indexOfComms < 0) {

			range = new PageRanges(Integer.parseInt(printPageRange.trim()));
		}

		// range with space
		range = new PageRanges(printPageRange);
		return range;
	}

	public static PrintQuality evaluatePrintQuality(String printQuality) {
		if(printQuality == null) {
			return PrintQuality.NORMAL;
		}
		if (printQuality.equalsIgnoreCase("low")) {
			return PrintQuality.DRAFT;
		} else if (printQuality.equalsIgnoreCase("high")) {
			return PrintQuality.HIGH;
		}
		return PrintQuality.NORMAL;
	}

}
