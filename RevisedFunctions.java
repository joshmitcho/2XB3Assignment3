//functions.java
//Original Author: Josh p Mitchell
//Secondary Author: Erin Varey


//This program takes an input file connectedCities.csv and parses the data so it is usable in code. 
//Additionally it fixes spelling errors in the original csv so it will work universally without modifying the original file.
//It also contained additional functions such as distance that will be useful later but are not used.

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

	//The first function is a file reader. This will be used to read the csv file and loads them an array list.
	public ArrayList<String> readFile(String i) {

		try {
			BufferedReader input = new BufferedReader(new FileReader(i)); //reader that will be parsing connectedCities.csv
			String str;//holder variable that will be returned
			//This segment of the code loads the contents to the string str
			ArrayList<String> fileContents = new ArrayList<String>();//creating an array list that will be returned
			try {
				while ((str = input.readLine()) != null) {//while there is still content to be read in the file put in temporary string

					fileContents.add(str); //load this temporary string into an array list

				}
			//this segment of the code is a mandatory error cather
			} catch (IOException e) {
				e.printStackTrace();//error message that is returned if an issue is encountered
			}
			//a string of connectedCities.csv
			return fileContents;//the final array that is returned

		} catch (FileNotFoundException e) {//if the folder does not contain the requested file will give error message
			e.printStackTrace();//error message that is shown in the console
			return null;//ensures the function returns nothing if an error occurs.
		}
	}
	//This is a file writing function. It will be used to re-upload the corrected connectedCities.csv
	public void writeFile(String o, String message) {//input consist of file name o, and string message which is the content being loaded in.
		File output = new File(o);//This overwrites the errors in connectedCities.csv
		PrintWriter writer;//this allow us to write into a file
		//this segment creates a print writer object and copies the message input to the file o.
		try {
			writer = new PrintWriter(output);//the writer is writing to the input file (connectedCities.csv)
			writer.print(message);//overwriting the contents with the new text
			writer.close();//closing object to prevent errors
		} catch (Exception e) {
			e.printStackTrace();//the error that occurs is returned to the console if it occurs
		}
	}
	//this solves for the distance between two points on a map.
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double p = 0.017453292519943295; // Constant that is used for conversion to degrees formula is PI/180
		double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2 //these 3 lines are the formula. This converts the longitude and latitudes
				//this line also converts the longitudes and latitudes to non degree values
				+ Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p)) / 2; //formula is found at http://www.movable-type.co.uk/scripts/latlong.html.
		return 12742 * Math.asin(Math.sqrt(a)); //The earth's radius is 6371km. The diameter is 12742. This returns the distance between the converted values
	}
	//The original file contains many errors. it has extra brackets, spelling errors, incorrect information. this function corrects
	//all of these errors
	//for context the original file contains a series of cities Boston(MA),Newyork(NY, CT, NJ). The values in brackets are the states they are connected
	//with
	public String[] parseConnectedCities(String s) {
		int i = 0;
		//This counts the amount of left brackets in the file which will be used for place holders of each data piece
		while (s.charAt(i++) != '(') //everything to the right of a "(" is the first state name in the dataset.
			;
		//this grabs the name of a city because it is always two places to the left of an open bracket. This grabs everything to the left
		String cityName1 = s.substring(0, i - 2);
		//this saves the value where the bracket is opened
		int j = i;
		//next segment finds where the bracket closes. This contains all the states that a city is connected to. 
		while (s.charAt(j++) != ')')
			;
		//this saves all the state values to an array. there is more than one possible state so an array is needed. T
		//they are separated by a comma in the file
		String[] states1 = s.substring(i, j - 1).split(", ");
		//save the value each state occurs at in the string
		i = j;
		//This loop finds each city that is connected to another
		while (s.charAt(i++) != '(')
			;
		//we know what states are connected to the first city so using these indexes the connected cities are saved
		String cityName2 = s.substring(j + 2, i - 2);
		//the next index is saved where this city name occurs
		j = i;

		//next each connected city has its own set of connected states. This find the states that are connected to the second city
		while (s.charAt(j++) != ')')
			;
		//a second array is used becasue more than one state is possible. Each index is where they are separated by a comma
		String[] states2 = s.substring(i, j - 1).split(", ");
		//This array will store city names and all the states they are connected to
		String[] out = new String[8];
		//the first index contains the first city name
		out[0] = cityName1;
		int k = 0;
		//saves each state connected to a city to the array index. There is a max of 3 connected states
		while (k < states1.length) {
			out[k + 1] = states1[k];
			k++;
		}
		//The second city you are selecting is stored here
		out[4] = cityName2;
		int l = 0;
		//All states that this city is connected too are stored in the rest of the array
		while (l < states2.length) {
			out[5 + l] = states2[l];
			l++;T
		}
		//this function returns an array of cities with what they are connected too
		return out;

	}

	//this function is used to search the array list of cities for the inputted city and its location(index) in the array
	public int search(ArrayList<City> c, String s) {

		int out = 0;
		//Search the array to see if the string occurs
		for (City d : c) {
			if (d.toString().equals(s)) //when a match is found the index this occurs at is returned
				return out;
			out++;
		}

		return -1; //error value returned if it is not able to be found
	}
	//this is going to build an array list of all of the data from connectedCities so it is usable in calculations. Each city and its connections
	//are going to be recorded into this array
	//the original file does not have connections in orders. This will reorder it so it is usable.
	public ArrayList<City> buildCities() {
		ArrayList<City> cities = new ArrayList<City>();
		//The file being read should be stored in this location.
		ArrayList<String> citiesData = readFile("data/connectedCities.txt");

		//Reads every single pieces of data in connectedCities and grabs each city.
		//They are saved in connection order.
		for (int i = 0; i < citiesData.size(); i++) {
			String[] s = parseConnectedCities(citiesData.get(i));//re ordering each data piece as it is read to a usable form

			String c1 = "";
			String c2 = "";
			//Grabs the first city to be used to find the index
			for (int j = 0; j < 4; j++)
				if (s[j] != null)
					c1 += s[j];
			//grabs the second city to find the index
			for (int j = 4; j < 8; j++)
				if (s[j] != null)
					c2 += s[j];
			//using function previously defined that will return the index of these two cities in the original file
			int a1 = search(cities, c1);
			int a2 = search(cities, c2);

			//-1 is returned when a match isn't found. This happens when a connection train ends between cities
			if (a1 == -1) {
				//Save what the parser thought was the correct city and state
				City tempc1 = new City(Arrays.copyOfRange(s, 0, 4));
				//saving where this occurs in the orginal
				tempc1.index = cities.size();
				//saving the city name to the cities array
				cities.add(tempc1);

				//if the second city does not match another city and the first doesnt the index is saved and the array is continued to be searched
				if (a2 == -1) {
					City tempc2 = new City(Arrays.copyOfRange(s, 4, 8)); //save the location of the second city
					tempc2.index = cities.size(); //save its size
					cities.add(tempc2); //add the city at the end of the trail to the array
					cities.get(cities.size() - 2).addConnection(cities.get(cities.size() - 1)); //tell the connection search to skip to the next location
					cities.get(cities.size() - 1).addConnection(cities.get(cities.size() - 2)); //drop this value and go to the next city
				} 
				//this happens when the connection train is able to continue
				else {
					//decrease the connections by one and add this values
					cities.get(cities.size() - 1).addConnection(cities.get(a2));
					//search for connections to city 2
					cities.get(a2).addConnection(cities.get(cities.size() - 1));
				}

			}
			//if city 2 is the end of a connection train
			else if (a2 == -1) {
				City tempc2 = new City(Arrays.copyOfRange(s, 4, 8)); //save the contents
				tempc2.index = cities.size(); //save the size
				cities.add(tempc2); //add the city name
				//if the second city also doesnt have any connections we need to restart the search
				if (a1 == -1) {
					City tempc1 = new City(Arrays.copyOfRange(s, 0, 4)); //save values
					tempc1.index = cities.size(); //save where it occurs
					cities.add(tempc1); //add city name
					cities.get(cities.size() - 2).addConnection(cities.get(cities.size() - 1)); //tell the connection search to skip to the next location
					cities.get(cities.size() - 1).addConnection(cities.get(cities.size() - 2));	//drop this value and go to the next city
				} 
				//otherwise a match is found continue traversing
				else {
					cities.get(cities.size() - 1).addConnection(cities.get(a1)); //decrease by one and continue traversing
					cities.get(a1).addConnection(cities.get(cities.size() - 1)); //grab the city name and keep traversing
				}
			}
		}
		//an array list of all the cities that can be connected is returned
		return cities;
	}
	//this function adds the cordinations of each city to the array
	public ArrayList<City> addCoords(ArrayList<City> cities) {
		ArrayList<String> zipsData = readFile("data/zips1990.csv"); //this contains each cities cordinates and is being read and saved
		String[][] zips = new String[zipsData.size()][8]; //there is 2 sets of cordinates, longitude and latitude thus a 2d array
		//these values are comma separated. To get a longitude and altitude in each index splitting at commas is necessary
		for (int i = 0; i < zips.length; i++)
			zips[i] = zipsData.get(i).split(",");
		//the second set of loops checks to see if the city name is equal to the name in the zips1990 files and saves each set of coordinates for the city
		for (City c : cities) {
			for (int i = 0; i < zips.length; i++) {
				if (c.getName().toUpperCase().equals(zips[i][3])) {//where the name matches in the second file
					for (int q = 0; q < c.getStates().size(); q++) {
						c.setCoords(Double.parseDouble(zips[i][4]), Double.parseDouble(zips[i][5])); //load values into array

					}
				}
			}
		}
		//the city array now contains the cordinates of each of those cities
		return cities;

	}
	//each city cost gas to drive through. the average gas prive in each state is contained in StateGasPrice.csv and is loaded into the array
	public ArrayList<City> addGasPrices(ArrayList<City> cities) {

		ArrayList<String> gasData = readFile("data/StateGasPrice.csv"); //loading the list of gas prices
		String[][] gas = new String[gasData.size()][2]; //using a 2d array that contains all the data points and the prices
		//these values will be used to find the average distance for calculating cheapest price
		int p1;
		int p2;
		double price = 0.0;
		//load the gas values to the first index of the array. Each is comma separated. Need to split by comma to keep separate cost separate.
		for (int i = 0; i < gas.length; i++)
			gas[i] = gasData.get(i).split(", ");
		//the second loop traversing the array to find what cities are connected. Once the links are found the coordinates are used to find the 
		//distance between the two locations. 
		for (City c1 : cities) {
			for (City c2 : c1.getConnected()) { //traversing the array
				p1 = 100;
				p2 = 100;
				double d = distance(c1.getCoords()[0], c1.getCoords()[1], c2.getCoords()[0], c2.getCoords()[1]); //finding the distance
				//next step is the find the states that connect the locations and convert these values to calculatable values
				for (String s1 : c1.getStates()) { //first set of state connections
					for (String s2 : c2.getStates()) { //second set of state connections
						if (s1 != null && s2 != null) { //if they exist and have connections
							for (String[] g : gas) {
								if (s1.equals(g[0]) && Integer.parseInt(g[1]) < p1) //when it matches the first cities cordinates
									p1 = Integer.parseInt(g[1]); //save this coordinate
								if (s2.equals(g[0]) && Integer.parseInt(g[1]) < p2) //when it matches the secondcities coordinates
									p2 = Integer.parseInt(g[1]); //save this coordinate
							}
							price = (d / 100.0) * 8.2 * ((p1 + p2) / 200.0); //calculate the average cost of gas between these two distances using
							//the average of the prices

						}
					}
				}
			}
			//load all the average gas cost to the array to be used for calculations later
			c1.addCost(price);
		}
		//the array list is updated to contain average gas price between each city
		return cities;
	}
	//this is the main portion of the assignment. Find the cheapest cost between the given data using DFS
	//dfs requires a node name and a cost of path. using the distance calculations and city array we can follow this algorithm
	//dfs works by searching every node in the array and seeig how many connections can be made from the starting node
	//segments of this code are sourced from http://algs4.cs.princeton.edu/41graph/DepthFirstSearch.java.html
	public void dfs(ArrayList<City> cities, City source, City dest) {
		//first step of dfs is know to mark if a node has been reached. there will need to be a mark for each city
		boolean[] marked = new boolean[cities.size()];
		int i = 0;
		//the path can be constructed by having a set of cities
		ArrayList<City> path = new ArrayList<City>();
		//the starting node is your first step. it will then be searched
		path.add(source);

		City c = source;
		//the while loop searches until you reach the desired destination city.
		while (!c.getName().equals(dest.getName())) { //if you have not reached your destination keep traversing
			if (c.getConnected().get(i) != null) { //ass long as you have connections you dont need to back track
				c = c.getConnected().get(i);//continues if there are connections and adds the city to the array
				path.add(c);
				System.out.println(c + " added");
				// i = 0;
			} else { //however once an end has been reached and you are NOT at your destination you need to backtrack
				path.remove(c);//remove the reached nodes that do not lead to the desired node
				System.out.println(c + " removed");
				i++; //incriment and keep trying
			}

		}

		String s = "";
		//this grabs the names of the cities that are in the final path to the desired end city. It prints all the connections and their names.
		for (City d : path) {
			s += d.getName();
		}
		//finally the results are written to a file for marking
		writeFile("data/a3_out.txt", s);
	}

}
