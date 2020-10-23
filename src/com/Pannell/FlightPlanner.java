package com.Pannell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import acm.program.ConsoleProgram;
import acm.util.ErrorException;

public class FlightPlanner extends ConsoleProgram {

	private ArrayList<String> destinations = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> cityAndDestinations = 
			new HashMap<String, ArrayList<String>>();
	private String input;

	public void run() {
		demonstration();
	}

	public void demonstration() {

		// greet user
		println(greeting());

		// load data from file
		loadDataFromFile();

		// print list of available cities
		println(getStringOfDestinations());
		// prompt user for entering a city
		println("let's plan a round trip route.");

		// receive selection from user
		getCityFromUser();

		// handle selection from user
		handleDestinationSelection(input);
		// print available flight paths from selected city
	}

	public void handleDestinationSelection(String destination) {
		ArrayList<String> route = new ArrayList<String>();
		route.add(destination);
		String currentDestination = destination;

		while (true) {
			String nextDestination = getconnectingCity(currentDestination);
			route.add(nextDestination);

			if (nextDestination.equals(destination)) {
				break;
			}
			currentDestination = nextDestination;

			println("The route you've chosen is: ");
			for (int i = 0; i < route.size(); i++) {
				if (i > 0)
					print(" -> ");
				print(route.get(i));
			}
			println();
		}
	}

	public String getconnectingCity(String city) {
		ArrayList<String> destinations = cityAndDestinations.get(city);
		String nextCity = null;
		while (true) {
			println("From " + city + " you can fly directly to:");

			for (int i = 0; i < destinations.size(); i++) {
				String theCity = destinations.get(i);
				println(" " + theCity);
			}

			String prompt = "Where do you want to go from " + city + "? ";
			nextCity = readLine(prompt);
			if (destinations.contains(nextCity))
				break;
			println("You can't get to that city by a direct flight.");
		}

		return nextCity;
	}

	public void getCityFromUser() {
		input = readLine("Enter the starting city: ");
	}

	public String greeting() {
		return "Welcome to flight planner!.";
	}

	public String getStringOfDestinations() {
		StringBuilder str = new StringBuilder();
		str.append("Here's a list of cities in our database: ");

		for (int i = 0; i < destinations.size(); i++) {
			str.append(destinations.get(i) + "\n");
		}

		return str.toString();
	}

	public BufferedReader getBufferedReaderFromFile(String fileName) {

		try {
			FileReader file = new FileReader(fileName);
			return new BufferedReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("A FileNotFoundException was thrown");
			return null;
		}
	}

	public void loadDataFromFile() {

		BufferedReader br = getBufferedReaderFromFile("textFiles/flights.txt");
		if (br == null) {
			System.out.println("null value returned from BufferedReader");
		}

		try {
			while (true) {

				String line = br.readLine();
				if (line == null) {
					break;
				}
				parseLine(line);
			}

			br.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void parseLine(String line) {
		if (!line.contains("->")) {
			throw new ErrorException("Invalid data entered for flight and destination");
		}

		String[] lineArray = line.split("->");

		String startingCity = lineArray[0].trim();
		String endingCity = lineArray[1].trim();

		destinationsContainsCity(startingCity);
		destinationsContainsCity(endingCity);
		getValueOfCityKey(startingCity).add(endingCity);
	}

	public void destinationsContainsCity(String city) {
		if (!destinations.contains(city)) {
			destinations.add(city);
			cityAndDestinations.put(city, new ArrayList<String>());
		}
	}

	public ArrayList<String> getValueOfCityKey(String string) {
		return cityAndDestinations.get(string);

		// return flights.get(fromCity);
	}
}
