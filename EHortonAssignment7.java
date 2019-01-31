//Programmer: Elizabeth Nocon
//Class: CS 1450.001 MW
//Assignment Number: 7
//Due Date: 10/15/2018
//Description: 
//This program reads a text file containing information on trains. 
//The program then creates train objects and assigns them to an array. 
//The program then reads an additional text file for information on rail cars. 
//The rail cars are assigned to a queue. 
//The program will then assign the rail cars to trains based on destination.
//Then, the program will move the trains from the sorting yard to the departure track. 
//The train with the least number of rail cars will depart first. 

import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.io.IOException;

public class EHortonAssignment7 
{
	public static void main(String[] args) throws IOException
	{
		//Create a file instance for file containing trains. 
		java.io.File file = new java.io.File("SortingYardTrains7.txt");
				
		//Create a scanner for the trains text file. 
		Scanner input = new Scanner(file);
		
		//Read first string and convert the numbers to integer form.
		int numberTracks = input.nextInt();
		
		//Call RailroadYard method. 
		RailroadYard yard = new RailroadYard(numberTracks+1);
		
		//Print heading for table of trains in sorting yard.
		System.out.println("Loading trains onto tracks in sorting yard...");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%35s\t%s\t\t%s\t%s\t\n", "Engine", "Company", "Rail Cars", "Destination");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		//Create RailroadYard Array
		while(input.hasNext()) 
		{
			//Read values for trains from the text file, and assign to variables.
			int inputTrackNumber = input.nextInt();
			int inputEngineNumber = input.nextInt();
			String inputCompany = input.next();
			int inputNumberRailCars = input.nextInt();
			String inputDestinationCity = input.next();
			
			//Create Train. 
			Train thisNewTrain = new Train(inputEngineNumber, inputCompany, inputDestinationCity);
			
			//Add train to sortingYard array. 
			yard.addTrainToSortingYard(inputTrackNumber, thisNewTrain);
			
			//Print train. 
			System.out.println("Loading sorting track " + inputTrackNumber + ":" + thisNewTrain.print());
		}
		//Close Scanner.
		input.close();
		
		//Print heading for the second table of rail cars on receiving track. 
		System.out.println("\nLoading rail cars on receiving track...");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%30s\t%s\n", "Number", "Destination");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		//Create instance of second file. 
		java.io.File file2 = new java.io.File("ReceivingYardRailCars7.txt");
		
		//Create new scanner to read second file. 
		Scanner inputOfText = new Scanner(file2);
		
		while(inputOfText.hasNext())
		{
			//Read the needed values for Rail Cars from the text file.
			int railCarNumber = inputOfText.nextInt();
			String inputFileFreight = inputOfText.next();
			int inputFileWeight = inputOfText.nextInt();
			String inputFileDestination = inputOfText.next();
			
			//Create rail car object with acquired information. 
			RailCar thisRailCar = new RailCar(railCarNumber, inputFileFreight, inputFileWeight, inputFileDestination);
			
			//Add rail car to receiving track in yard. 
			yard.addRailCarToReceivingTrack(thisRailCar);
			
			//Print rail car details. 
			System.out.print("Loading rail car "+ thisRailCar.print());
		}
		
		inputOfText.close();
		
		//Print that the simulation is beginning. 
		System.out.print("\n...Start railroad yard simulation...\n\n");
		
		//Move rail cars to trains with matching destinations using method in RailroadYard Controller Class.
		RailroadYardController.moveRailCarsToTrains(yard);
		
		//Move trains from sorting yard to departure track. 
		RailroadYardController.moveTrainsToDepartureTrack(yard);
		
		//Clear trains for departure. 
		RailroadYardController.clearedForDeparture(yard);
		
		//Notify that the railroad yard simulation is complete. 
		System.out.println("\n...End railroad yard simulation...");
		
	}//Main
}//Assignment7

//Create print interface. 
interface Printable
{
	String print();
}

//Create RailroadYard Class. 
class RailroadYard
{
	//Declare Variables. 
	private int numberTracks;
	private int currentTrack;
	private Train[] sortingYard;
	private Queue<RailCar> receivingTrack = new LinkedList<>();
	private PriorityQueue<Train> departureTrack = new PriorityQueue<>();
	
	//Create RailroadYard Constructor. 
	public RailroadYard(int numberTracks) 
	{
		this.numberTracks = numberTracks;
		currentTrack = 0;
		sortingYard =new Train[numberTracks]; 
	}
	
	//Create numberTracks getter.
	public int getNumberTracks()
	{
		return numberTracks;
	}
	
	//Create addTrainToSortingYard method. 
	public Train[] addTrainToSortingYard(int trackNumber, Train train) 
	{
		int i = trackNumber;
		sortingYard[i] = train;
		return sortingYard;
	}
	
	//Create getNextTrainInSortingYard method.
	public Train getNextTrainInSortingYard() 
	{
		if (sortingYard[currentTrack] == null) 
		{
			currentTrack++;
			return null;
		}
		else 
		{
			Train train = sortingYard[currentTrack];
			currentTrack++;
			return train;
		}
	}
	
	//Create isReceivingTrackEmpty method. 
	public boolean isReceivingTrackEmpty ()
	{
		return receivingTrack.isEmpty();
	}
	
	//Create addRailCarToReceivingTrack method.
	public void addRailCarToReceivingTrack(RailCar railCar)
	{
		receivingTrack.offer(railCar);
	}
	
	//Create removeRailCarFromReceivingTrack method.
	public RailCar removeRailCarFromReceivingTrack()
	{
		return receivingTrack.remove();
	}
	
	//Create isDepartureTrackEmpty method.
	public boolean isDepartureTrackEmpty()
	{
		return departureTrack.isEmpty();
	}
	
	//Create addTrainToDepartureTrack method. 
	public void addTraintoDepartureTrack(Train train)
	{
		departureTrack.offer(train);
	}
	
	//Create removeTrainFromDepartureTrack method. 
	public Train removeTrainFromDepartureTrack()
	{
		return departureTrack.poll();
	}
	
	//Create method findTrain. Will return index of train in sorting yard.
	public int findTrain(RailCar railCar)
	{
		//Get destination city of the rail car.
		String railCarDestination = railCar.getDestination();
		
		//Create a loop to iterate through each train to find a match. 
		for (int i = 0; i < sortingYard.length;i++)
		{
			//Get train from sorting yard.
			Train searchTrain = sortingYard[i];
			
			//Check for if the train is null!
			if(searchTrain != null)
			{
					int compareValue = searchTrain.getDestinationCity().compareTo(railCarDestination);
					
					if( compareValue == 0)
					{
						return i;
					}
			}
		}
		return -1;
	}
	
	//Create method addRailCarToTrainInSortingYard
	public void addRailCarToTrainInSortingYard(RailCar railCar1, int trackNumber1)
	{
		//Add the rail car to the train's queue of rail cars.
		Train addTrain = sortingYard[trackNumber1];
		addTrain.addRailCar(railCar1);
	}
	
	//Create method removeTrainFromSortingYard.
	public void removeTrainFromSortingYard(int indexOfTheTrain)
	{
		//Removes train from first index. 
		sortingYard[indexOfTheTrain] = null;
	}	
}//RailroadYard

//Create Train Class. 
class Train implements Printable, Comparable<Train>
{
	//Create data fields. 
	private int engineNumber; 
	private String company;
	private Queue<RailCar> railCars = new LinkedList<>();
	private String destinationCity;
	
	//Create Train constructor. 
	public Train(int engineNumber, String company, String destinationCity) 
	{
		this.engineNumber = engineNumber;
		this.company = company;
		this.destinationCity = destinationCity;
	}
	
	//Create engine getter. 
	public int getEngineNumber() 
	{
		return engineNumber;
	}
	
	//Create company getter. 
	public String getCompany() 
	{
		return company;
	}
	
	//Create destinationCity getter. 
	public String getDestinationCity() 
	{
		return destinationCity;
	}
	
	//Override Printable. 
	@Override
	public String print() 
	{
		return String.format("%9d\t%-5s\t\t%-10s\t%-16s", engineNumber,company, "0", destinationCity);
	}
	
	//Create getRailCarsSize method.
	public int getRailCarsSize()
	{
		return railCars.size();
	}
	
	//Create addRailCar method. 
	public void addRailCar(RailCar railcar)
	{
		railCars.offer(railcar);
	}
	
	//Create compare for comparator.
	public int compareTo(Train train)
	{
		if(this.getRailCarsSize() < train.getRailCarsSize()) 
		{
			return -1;
		}
		else if (this.getRailCarsSize() > train.getRailCarsSize()) 
		{
			return 1;
		}
		else 
		{
			return 0;
		}
	}
}//Train

//Create RailCar Class. 
class RailCar implements Printable
{
	//Create data fields. 
	int number;
	String freight;
	int weight;
	String destination;
	
	//Create RailCar constructor. 
	public RailCar(int number, String freight, int weight, String destination)
	{
		this.number = number;
		this.freight = freight;
		this.weight = weight;
		this.destination = destination;
	}
	
	//Create number getter. 
	public int getNumber()
	{
		return number;
	}
	
	//Create freight getter. 
	public String getFreight()
	{
		return freight;
	}
	
	//Create weight getter. 
	public int getWeight()
	{
		return weight;
	}
	
	//Create destination getter. 
	public String getDestination()
	{
		return destination;
	}
	
	//Override Printable. 
	@Override
	public String print() 
	{
		return String.format("%9d\t%-5s\n", number, destination);
	}
}//RailCar

//Create RailroadYardController class. 
class RailroadYardController
{
	//No Private Data Fields. 
	
	//Private Constructor. 
	private RailroadYardController()
	{	
		//Empty private constructor. 
	}
	
	//Create moveRailCarsToTrains method. 
	public static void moveRailCarsToTrains (RailroadYard yard) 
	{
		//Print message of what this method is doing. 
		System.out.println("Controller: Moving rail cars from receiving track to sorting yard:");
		System.out.println("-------------------------------------------------------------------");
		
		while(! yard.isReceivingTrackEmpty()) 
		{
			//Get railCar from the receiving track. 
			RailCar thatRailCar = yard.removeRailCarFromReceivingTrack();
			
			//Find the correct train for thisRailCar.
			int theTrackNumber = yard.findTrain(thatRailCar);
			
			//Print the information of the rail car. 
			System.out.println("Rail car " + thatRailCar.getNumber() + " proceed to sorting track " + theTrackNumber);
		
			//Add railCar to the queue of rail cars for the correct train. 
			yard.addRailCarToTrainInSortingYard(thatRailCar, theTrackNumber);
		}	
	}
	
	//Create moveTrainsToDepartureTrack method. 
	public static void moveTrainsToDepartureTrack(RailroadYard yard)
	{
		//Print message of what this method is doing. 
		System.out.println("\nController: Moving trains from sorting yard to departure track:");
		System.out.println("---------------------------------------------------------------");
				
		//Get size of sorting yard.
		int numberOfTracks = yard.getNumberTracks();
		
		for(int i = 0; i < numberOfTracks; i++)
		{
			//Get next train in the sortingYard. 
			Train tempTrain = yard.getNextTrainInSortingYard();
			
			if(tempTrain != null)
			{
			//Remove train from the sortingYard.
			yard.removeTrainFromSortingYard(i);
			
			//Print train information. 
			System.out.println("Train " + tempTrain.getEngineNumber() + " proceed to departure track.");
			
			//Add train to Departure Track. 
			yard.addTraintoDepartureTrack(tempTrain);
			}
		}	
	}
	
	//Create clearedForDeparture method. 
	public static void clearedForDeparture(RailroadYard yard)
	{
		//Print message of what this method is doing. 
		System.out.println("\nController: Moving trains from departure track to main line- smallest trains go first:");
		System.out.println("--------------------------------------------------------------------------------------");
				
		//While the departureTrack is not empty:
		while(! yard.isDepartureTrackEmpty())
		{
			//Remove Train from departure track to clear for departure. 
			Train departingTrain = yard.removeTrainFromDepartureTrack();
			
			//Get the destination, number of Rail Cars and Engine Number for printing. 
			String destination = departingTrain.getDestinationCity();
			int numberRailCars = departingTrain.getRailCarsSize();
			int engineNumber = departingTrain.getEngineNumber();
			
			//Print the train information. 
			System.out.println("Train " + engineNumber + " with " + numberRailCars + " rail cars is departing to " + destination);	
		}
	}
}//RailroadYardController
