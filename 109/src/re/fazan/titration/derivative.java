package re.fazan.titration;

import java.util.ArrayList;

public class derivative {

	public static void derivative(ArrayList<Double> vol, ArrayList<Double> ph) {
		ArrayList<Double> backward = new ArrayList<Double>();
		ArrayList<Double> forward = new ArrayList<Double>();
		ArrayList<Double> h = new ArrayList<Double>();
		ArrayList<Double> hp = new ArrayList<Double>();
		int i = 1;
		while (i != vol.size()-1) {
			backward.add(ph.get(i)-ph.get(i-1));
			forward.add(ph.get(i+1)-ph.get(i));
			h.add(vol.get(i)-vol.get(i-1));
			hp.add(vol.get(i+1)-vol.get(i));
			i++;
		}
		
	}
}
