package cas2xb3_A3_mitchell_jpm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class functions {
	public ArrayList<String> readFile(String i) {

		try {
			BufferedReader input = new BufferedReader(new FileReader(i));
			String str;

			ArrayList<String> fileContents = new ArrayList<String>();
			try {
				while ((str = input.readLine()) != null) {

					fileContents.add(str);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileContents;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void writeFile(String o, String message) {
		File output = new File(o);
		PrintWriter writer;
		try {
			writer = new PrintWriter(output);
			writer.print(message);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double p = 0.017453292519943295; // Math.PI / 180
		double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2
				+ Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p)) / 2;

		return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
	}

	public String[] parseConnectedCities(String s) {
		int i = 0;
		while (s.charAt(i++) != '(')
			;

		String cityName1 = s.substring(0, i - 2);

		int j = i;
		while (s.charAt(j++) != ')')
			;

		String[] states1 = s.substring(i, j - 1).split(", ");

		i = j;

		while (s.charAt(i++) != '(')
			;

		String cityName2 = s.substring(j + 2, i - 2);

		j = i;
		while (s.charAt(j++) != ')')
			;

		String[] states2 = s.substring(i, j - 1).split(", ");

		String[] out = new String[8];

		out[0] = cityName1;
		int k = 0;
		while (k < states1.length) {
			out[k + 1] = states1[k];
			k++;
		}
		out[4] = cityName2;
		int l = 0;
		while (l < states2.length) {
			out[5 + l] = states2[l];
			l++;
		}

		return out;

	}

	public int search(ArrayList<City> c, String s) {

		int out = 0;

		for (City d : c) {
			if (d.toString().equals(s))
				return out;
			out++;
		}

		return -1;
	}

	public ArrayList<City> buildCities() {
		ArrayList<City> cities = new ArrayList<City>();

		ArrayList<String> citiesData = readFile("data/connectedCities.txt");

		for (int i = 0; i < citiesData.size(); i++) {
			String[] s = parseConnectedCities(citiesData.get(i));

			String c1 = "";
			String c2 = "";

			for (int j = 0; j < 4; j++)
				if (s[j] != null)
					c1 += s[j];

			for (int j = 4; j < 8; j++)
				if (s[j] != null)
					c2 += s[j];

			int a1 = search(cities, c1);
			int a2 = search(cities, c2);

			if (a1 == -1) {
				City tempc1 = new City(Arrays.copyOfRange(s, 0, 4));
				tempc1.index = cities.size();
				cities.add(tempc1);

				if (a2 == -1) {
					City tempc2 = new City(Arrays.copyOfRange(s, 4, 8));
					tempc2.index = cities.size();
					cities.add(tempc2);
					cities.get(cities.size() - 2).addConnection(cities.get(cities.size() - 1));
					cities.get(cities.size() - 1).addConnection(cities.get(cities.size() - 2));
				} else {
					cities.get(cities.size() - 1).addConnection(cities.get(a2));
					cities.get(a2).addConnection(cities.get(cities.size() - 1));
				}

			}

			else if (a2 == -1) {
				City tempc2 = new City(Arrays.copyOfRange(s, 4, 8));
				tempc2.index = cities.size();
				cities.add(tempc2);

				if (a1 == -1) {
					City tempc1 = new City(Arrays.copyOfRange(s, 0, 4));
					tempc1.index = cities.size();
					cities.add(tempc1);
					cities.get(cities.size() - 2).addConnection(cities.get(cities.size() - 1));
					cities.get(cities.size() - 1).addConnection(cities.get(cities.size() - 2));
				} else {
					cities.get(cities.size() - 1).addConnection(cities.get(a1));
					cities.get(a1).addConnection(cities.get(cities.size() - 1));
				}
			}
		}
		return cities;
	}

	public ArrayList<City> addCoords(ArrayList<City> cities) {
		ArrayList<String> zipsData = readFile("data/zips1990.csv");
		String[][] zips = new String[zipsData.size()][8];

		for (int i = 0; i < zips.length; i++)
			zips[i] = zipsData.get(i).split(",");

		for (City c : cities) {
			for (int i = 0; i < zips.length; i++) {
				if (c.getName().toUpperCase().equals(zips[i][3])) {
					for (int q = 0; q < c.getStates().size(); q++) {
						c.setCoords(Double.parseDouble(zips[i][4]), Double.parseDouble(zips[i][5]));

					}
				}
			}
		}

		return cities;

	}

	public ArrayList<City> addGasPrices(ArrayList<City> cities) {

		ArrayList<String> gasData = readFile("data/StateGasPrice.csv");
		String[][] gas = new String[gasData.size()][2];

		int p1;
		int p2;
		double price = 0.0;

		for (int i = 0; i < gas.length; i++)
			gas[i] = gasData.get(i).split(", ");

		for (City c1 : cities) {
			for (City c2 : c1.getConnected()) {
				p1 = 100;
				p2 = 100;
				double d = distance(c1.getCoords()[0], c1.getCoords()[1], c2.getCoords()[0], c2.getCoords()[1]);
				// System.out.println(c1.toString() + " -> " + c2.toString() +
				// ": " + d);

				for (String s1 : c1.getStates()) {
					for (String s2 : c2.getStates()) {
						if (s1 != null && s2 != null) {
							// System.out.println(s1 + " " + s2);
							for (String[] g : gas) {
								if (s1.equals(g[0]) && Integer.parseInt(g[1]) < p1)
									p1 = Integer.parseInt(g[1]);
								if (s2.equals(g[0]) && Integer.parseInt(g[1]) < p2)
									p2 = Integer.parseInt(g[1]);
							}
							price = (d / 100.0) * 8.2 * ((p1 + p2) / 200.0);

						}
					}
				}
			}
			c1.addCost(price);
		}

		return cities;
	}

	public void dfs(ArrayList<City> cities, City source, City dest) {

		boolean[] marked = new boolean[cities.size()];
		int i = 0;

		ArrayList<City> path = new ArrayList<City>();

		path.add(source);

		City c = source;

		while (!c.getName().equals(dest.getName())) {
			if (c.getConnected().get(i) != null) {
				c = c.getConnected().get(i);
				path.add(c);
				System.out.println(c + " added");
				// i = 0;
			} else {
				path.remove(c);
				System.out.println(c + " removed");
				i++;
			}

		}

		String s = "";

		for (City d : path) {
			s += d.getName();
		}

		writeFile("data/a3_out.txt", s);
	}

}
