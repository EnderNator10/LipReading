package edu.lipreading;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.core.xml.XStream;

public class XMLVerifier {

	/**
	 * This tool scans a directory that is given as an argument for XML files
	 * that represent recorded Samples. It builds a Sample from each file and
	 * scans for <0,0> entries in the vectors, and presents some statistics.
	 * 
	 * @param args
	 *            should contain one argument that is a directory with XML
	 *            files.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("please supply location of XML files to verify");
			return;
		}


		List<String> invalidXMLs = new ArrayList<String>();
		String xmlsLocation = args[0];
		int numOfInconsistencies = 0;
		int biggestInconsistency = 0;
		int numOfFiles = 0;
		int numOfInvalidXMLs = 0;
		String biggestInconsistentSample = "";

		File xmlPath = new File(xmlsLocation);

		for (File xml : xmlPath.listFiles()) {
			if (xml.getName().endsWith(".xml")) {
				try {
					Sample samp = (Sample) XStream.read(xml);
					numOfFiles++;
					int sampInconsistency = 0;
					for (List<Integer> vector : samp.getMatrix()) {
						for (int coord : vector) {
							if (coord == 0) {
								sampInconsistency++;
								numOfInconsistencies++;
							}
						}
					}
					if (biggestInconsistency == 0
							|| sampInconsistency > biggestInconsistency) {
						biggestInconsistency = sampInconsistency;
						biggestInconsistentSample = xml.getName();
					}
				} catch (Exception e) {
					invalidXMLs.add(xml.getName());
					numOfInvalidXMLs++;
				}
			}
		}

		numOfInconsistencies = numOfInconsistencies / 2;
		biggestInconsistency = biggestInconsistency / 2;

		for(String badXML : invalidXMLs) {
			System.out.println("Error parsing XML file: "
					+ badXML);
		}
		System.out.println("Number of XMLs scanned: " + numOfFiles);
		System.out.println("Number of Invalid XML Files: " + numOfInvalidXMLs);
		System.out.println("Number of Inconsistencies found: "
				+ numOfInconsistencies);
		System.out.println("Sample '" + biggestInconsistentSample
				+ "' with most inconsistencies contained: "
				+ biggestInconsistency);
	}

}