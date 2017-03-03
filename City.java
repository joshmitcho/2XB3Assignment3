package cas2xb3_A3_mitchell_jpm;

import java.util.ArrayList;

public class City {
	public int index = 0;
	String name;
	double[] coords = new double[2];
	ArrayList<String> states = new ArrayList<String>();
	boolean mcd = false;
	boolean wendys = false;
	boolean bking = false;
	double foodCost = 5;

	ArrayList<City> connected = new ArrayList<City>();
	ArrayList<Double> cost = new ArrayList<Double>();

	public City(){
		
	}
	
	public City(String[] s) {

		name = s[0];
		
		for (int i = s.length - 1; i > 0; i--) {
			states.add(s[i]);
		}
	}

	public String toString() {

		String s = name;
	
		for (String t : states) {
			if (t != null)
				s += t;
		}
		
		s += " " + index;
		
		return s;
	}

	public void addConnection(City c) {
		connected.add(c);
	}
	public ArrayList<City> getConnected() {
		return connected;

	}
	
	public void addCost(double d){
		cost.add(d);
	}
	public ArrayList<Double> getCost() {
		return cost;

	}
	

	public String getName() {
		return name;
	}

	public void setCoords(double lon, double lat) {
		coords[0] = lon;
		coords[1] = lat;
	}

	public ArrayList<String> getStates() {
		return states;
	}
	
	public double[] getCoords() {
		return coords;
	}

}
