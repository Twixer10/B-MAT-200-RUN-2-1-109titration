package re.fazan.titration;

import java.util.Comparator;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
	
	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("-h")) {
			printHelp();
			System.exit(0);
		}
		if (args.length != 1) {
			printErr();
			System.exit(84);
		}
		init_titration(args);
	}

	public static void init_titration(String[] args) {
		String sb = open_file(args);
		String[] line = sb.split("\n");
		ArrayList<Double> vol = new ArrayList<Double>();
		ArrayList<Double> ph = new ArrayList<Double>();
		for (int i = 0; i != line.length; i++) {
			String[] splitted = line[i].split(";");
			vol.add(Double.parseDouble(splitted[0]));
			ph.add(Double.parseDouble(splitted[1]));
		}
		derivative(vol, ph);
	}
	
	public static void derivative(ArrayList<Double> vol, ArrayList<Double> ph) {
		ArrayList<Double> backward = new ArrayList<Double>();
		ArrayList<Double> forward = new ArrayList<Double>();
		ArrayList<Double> h = new ArrayList<Double>();
		ArrayList<Double> hp = new ArrayList<Double>();
		ArrayList<Double> cpt1 = new ArrayList<Double>();
		ArrayList<Double> cpt2 = new ArrayList<Double>();
		ArrayList<Double> cptf = new ArrayList<Double>();
		cptf = do_derivatives(ph, vol, backward, forward, h, hp, cpt1, cpt2, cptf);
		System.out.println("Derivative:");
		for (int j = 0; j != vol.size()-2; j++)
			System.out.printf("%.1f ml -> %.2f\n", vol.get(j+1), cptf.get(j));
		double ed1 = ph.get(equi_index(cptf, ph));
		System.out.printf("Equivalence point at %.1f ml\n", ed1);
		clear_derivatives(ph, vol, backward, forward, h, hp, cpt1, cpt2, cptf);
		cptf = do_derivatives(ph, vol, backward, forward, h, hp, cpt1, cpt2, cptf);
		System.out.println();
		System.out.println("Second derivative:");
		for (int m = 0; m != vol.size()-2; m++)
			System.out.printf("%.1f ml -> %.2f\n", vol.get(m+1), cptf.get(m));
		System.out.println("\nSecond derivative estimated:");
		System.out.println();
		double ed2 = ph.get(equi_index(cptf, ph));
		System.out.printf("Equivalence point at %.1f ml\n", ed2);
	}
	
	public static ArrayList<Double> do_derivatives(ArrayList<Double> ph, ArrayList<Double> vol,
			ArrayList<Double> backward, ArrayList<Double> forward, ArrayList<Double> h, ArrayList<Double> hp,
			ArrayList<Double>  cpt1, ArrayList<Double> cpt2, ArrayList<Double> cptf) {
		int i = 1;
		
		while (i != ph.size()-1) {
			backward.add(ph.get(i)-ph.get(i-1));
			forward.add(ph.get(i+1)-ph.get(i));
			h.add(vol.get(i)-vol.get(i-1));
			hp.add(vol.get(i+1)-vol.get(i));
			cpt1.add(backward.get(i-1)/h.get(i-1));
			cpt2.add(forward.get(i-1)/hp.get(i-1));
			cptf.add(((cpt1.get(i-1)*hp.get(i-1))+(cpt2.get(i-1)*h.get(i-1)))/(h.get(i-1)+hp.get(i-1)));
			i++;
		}
		return (cptf);
	}
	
	public static void clear_derivatives(ArrayList<Double> ph, ArrayList<Double> vol,
			ArrayList<Double> backward, ArrayList<Double> forward, ArrayList<Double> h, ArrayList<Double> hp,
			ArrayList<Double>  cpt1, ArrayList<Double> cpt2, ArrayList<Double> cptf) {
		ph.clear();
		for (int k = 0; k != cptf.size(); k++)
			ph.add(cptf.get(k));
		vol.remove(0);
		vol.remove(vol.size()-1);
		backward.clear();
		forward.clear();
		h.clear();
		hp.clear();
		cpt1.clear();
		cpt2.clear();
		cptf.clear();
	}
	
	public static int equi_index(ArrayList<Double> cptf, ArrayList<Double> ph) {
		double max = cptf.get(0);
		int index = 0;
		
		for (int id = 0; id < cptf.size(); id++) {
			if (max < cptf.get(id)) {
				max = cptf.get(id);
				index = id;
			}
		}
		return (index + 1);
	}
	
	public static String open_file(String[] args) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = Files.newBufferedReader(Paths.get(args[0]))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} catch (IOException err) {
			System.out.println("IOException: " + err);
		}
		return (sb.toString());
	}

	public static void printHelp() {
	    System.out.println("USAGE");
	    System.out.println("    ./109trigo file");
	    System.out.println();
	    System.out.println("DESCRIPTION");
	    System.out.println("    file    a csv file containing \"vol;php\" lines");
	}
	
	public static void printErr() {
		System.out.println("Wrong arguments.");
		System.out.println("Try './109titration -h' to have more information.");
	}
}