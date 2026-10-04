package com.first.machin.service;

import java.util.concurrent.Callable;

import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

public class PrintJobCallable implements Callable<PrintJobAttributeSet> {

	DocPrintJob printJob;
	Doc mydoc;
	PrintRequestAttributeSet printRequestAttributeSet;

	public PrintJobCallable(DocPrintJob printJob, Doc mydoc, PrintRequestAttributeSet printRequestAttributeSet) {
		super();
		this.printJob = printJob;
		this.mydoc = mydoc;
		this.printRequestAttributeSet = printRequestAttributeSet;

	}

	@Override
	public PrintJobAttributeSet call() throws Exception {
		// TODO Auto-generated method stub
		try {
			printJob.print(mydoc, printRequestAttributeSet);
			return printJob.getAttributes();
		} catch (Exception e) {
			return null;
		}

	}

	private static class JobCompleteMonitor extends PrintJobAdapter {

		private boolean completed = false;

		@Override
		public void printJobCanceled(PrintJobEvent pje) {
			System.out.println(" This is 1");
			signalCompletion();
		}

		@Override
		public void printJobCompleted(PrintJobEvent pje) {
			System.out.println(" This is 2");
			signalCompletion();
			System.out.println(" This is 3");
		}

		@Override
		public void printJobFailed(PrintJobEvent pje) {
			System.out.println(" This is 4");
			signalCompletion();
		}

		@Override
		public void printJobNoMoreEvents(PrintJobEvent pje) {
			System.out.println(" This is 5");
			signalCompletion();
		}

		private void signalCompletion() {

			synchronized (JobCompleteMonitor.this) {

				completed = true;

				JobCompleteMonitor.this.notify();

			}

		}

		public synchronized void waitForJobCompletion() {

			try {

				while (!completed) {

					wait();

				}

			} catch (InterruptedException e) {

			}
		}

	}

}
