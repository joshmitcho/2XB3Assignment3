package cas2xb3_A3_mitchell_jpm;

import java.util.ArrayList;

public class RoadTrip {

	public static void main(String[] args) {

		functions f = new functions();

		String out = "";
		
		ArrayList<String> instruct = f.readFile("data/a3_in.txt");
		City source = new City();
		City dest = new City();

		ArrayList<City> cities = f.buildCities();

		cities = f.addGasPrices(f.addCoords(cities));

		for (City c : cities) {
			if (c.getName().equals(instruct.get(0)))
				source = c;
			else if (c.getName().equals(instruct.get(1)))
				dest = c;
		}

		for (City c : cities) {
			for (City d : c.getConnected()) {
				
				out += c + " -> " + d + '\n' + '\n';
			}
			
		}
		out += "----------------------------------------------------------------" + '\n';
		for (City c : cities) {
			for (int i = 0; i < c.getCost().size(); i++) {


				out += c + " -> " + c.getConnected().get(i) + " costs " + c.getCost().get(i) + '\n' + '\n';
			}
		}
		
		f.writeFile("data/a3_out.txt", out);

	}
}
