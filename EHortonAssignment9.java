//Programmer: Elizabeth Nocon
//Class: CS 1450.001 MW
//Assignment Number: 9
//Due Date: 11/12/2018
//Description:
//This program reads a text file, and utilizes the information read in the file to create railCar objects. 
//The railCar objects are then added to a linked list that symbolizes a train. 
//The railCars are added to the train in order of their final destination, using a compareTo method. 
//After the train is assembled, the program prints the train's railCars. 
//The train will stop in Denver, Colorado Springs, Pueblo, and Santa Fe. 
//At each stop, the program will remove the appropriate railCars based on cargo and destination. 
//Then, the program will print the train linked list. 
//The program completes after railCars have been delivered to Santa Fe. 

import java.util.Scanner;
import java.io.IOException;
import java.lang.Comparable;

public class EHortonAssignment9 
{
	public static void main(String[] args) throws IOException
	{
		//Create an instance of railCarLinkedList. 
		RailCarLinkedList train9 = new RailCarLinkedList();
		
		//Create file instance of railCars.txt.
		java.io.File file = new java.io.File("railcars.txt");
		
		//Create scanner for the file. 
		Scanner fileScanner = new Scanner(file);
		
		//Read values from file. 
		while (fileScanner.hasNext())
		{
			//Read values from the file and assign to variables. 
			int number = fileScanner.nextInt();
			String destination = fileScanner.next();
			String freight = fileScanner.next();
			
			//Create a new rail car from the read values. 
			RailCar9 newRailCar = new RailCar9(number, destination, freight);
			
			//Add the new railcar to train. 
			train9.addInOrder(newRailCar);
		}
		//close scanner. 
		fileScanner.close();
		
		//Print Chicago departure. 
		System.out.println("Train departs Chicago:\n");
		System.out.println("RailCar\tDestination City\tFreight");
		System.out.println("-----------------------------------------------------------------");
		train9.printList();
		
		//Print Denver departure. 
		System.out.println("\nStop 1: Train Arrives in Denver");
		System.out.println("Remove: Denver rail cars\n");
		System.out.println("RailCar\tDestination City\tFreight");
		System.out.println("-----------------------------------------------------------------");
		//Remove trains with final destination of Denver, then print list. 
		train9.removeCarWithDestination("DEN");
		train9.printList();
		
		//Print Colorado Springs departure. 
		System.out.println("\nStop 2: Train Arrives in Colorado Springs");
		System.out.println("Remove: Colorado Springs rail cars");
		System.out.println("Remove: Rail cars with parrots\n");
		System.out.println("RailCar\tDestination City\tFreight");
		System.out.println("-----------------------------------------------------------------");
		//Remove Colorado Springs cars. 
		train9.removeCarWithDestination("COS");
		//Remove rail cars with parrots. 
		train9.removeCarsWithFreight("parrots");
		//Print updated list. 
		train9.printList();
		
		//Print Pueblo departure. 
		System.out.println("\nStop 3: Train Arrives in Pueblo");
		System.out.println("Remove: Pueblo rail cars \n");
		System.out.println("RailCar\tDestination City\tFreight");
		System.out.println("-----------------------------------------------------------------");
		//Remove Pueblo rail cars. 
		train9.removeCarWithDestination("PBO");
		//Reprint list. 
		train9.printList();
		
		//Print Santa Fe stop. 
		System.out.println("\nStop 4: Train Arrives in Santa Fe");
		System.out.println("Remove: Santa Fe rail cars-train should be empty!\n");
		System.out.println("RailCar\tDestination City\tFreight");
		System.out.println("-----------------------------------------------------------------");
		//Remove Santa Fe cars. 
		train9.removeCarWithDestination("SFE");
		//Reprint list. 
		train9.printList();
		
	}
}//Assignment9

//RailCar Class
class RailCar9 implements Comparable<RailCar9>
{
	//Private Data Fields. 
	private int number;
	private String destination;
	private String freight;
	
	RailCar9(int number, String destination, String freight)
	{
		//Set the passed in values equal to the private data field values.
		this.number = number;
		this.destination = destination;
		this.freight = freight;
	}
	
	//Get freight. 
	public String getFreight()
	{
		return freight;
	}
	
	//Get destination.
	public String getDestination()
	{
		return destination;
	}
	
	//print method. 
	public String print()
	{
		String printString = String.format("%d\t%-10s\t\t%-10s\t\n", number, destination, freight);
		return printString;
	}
	
	//compareTo Method
	@Override
	public int compareTo(RailCar9 other)
	{
		//If the railCars are going to the same destination. 
		if(this.getDestination().equals(other.getDestination()))
		{
			return 0;
		}
		
		//If the other railCar is going to Denver. 
		else if(this.getDestination().equals("DEN"))
		{
			return -1;
		}
		
		//If the railCar is going to Colorado Springs. 
		else if(this.getDestination().equals("COS"))
		{
			if(other.getDestination().equals("SFE")|| other.getDestination().equals("PBO"))
			{
				return -1;
			}
		}
		
		//If the railCar is going to Pueblo. 
		else if(this.getDestination().equals("PBO"))
		{
			if(other.getDestination().equals("SFE"))
			{
				return -1;
			}
		}
		
			return 1;
		
	}
	
}//RailCar9 Class

//RailCarLinkedListClass
class RailCarLinkedList
{
	//Private data fields. 
	private Node head;
	
	//Constructor. 
	RailCarLinkedList()
	{
		head = null;
	}
	
	//addInOrder method. 
	public void addInOrder(RailCar9 railCarToAdd)
	{
		//These variables will help keep track of nodes as we move through the list. 
		Node newNode = new Node(railCarToAdd);
		Node current = head; 
		Node previous = null;
			
			
		boolean foundLocation = false;
		//While the node has not yet been placed into the list, perform the while loop. 
		while(current != null && !foundLocation)
		{
			//If returned value is negative one, then the car needs to go
			//between the previous and current. variables
			if(current.data.compareTo(railCarToAdd) == -1)
			{
				foundLocation = true;
			}
			//search to the end of the list. 
			//Move current and previous forward. 
			else
			{
				previous = current;
				current = current.next;
			}
		}//while
		
		//If the head does not have a value yet, then the node will be put in first.
		if(previous == null)
		{
			//Update the head to the new node.
			head = newNode;
			//Reassign current to the updated head value. 
			current = head;
		}
		//If previous is not null, then the car needs to be inserted between. 
		if(previous != null)
		{
			//Change previous.next to the newNode. 
			previous.next = newNode; 
			//change newNode to the current. 
			newNode.next = current;
		}
	}//addInOrder Method
	
	public void removeCarWithDestination(String destination)
	{
		//Create variable node to move head as needed in while loop. 
		Node previousNode = null;
		Node currentNode = head;
		
		//While you are not at the end of the linked list, compare values. 
		while(currentNode != null)
		{
			//If you have a match. 
			if (currentNode.data.getDestination().equals(destination))
			{
				if (currentNode.next == null && previousNode != null)
				{
					previousNode.next = null;
				}
				else if(currentNode == head)
				{
					head = currentNode.next;
				}
				else
				{
					//Update the temporary Node's next to be the current.next. 
					previousNode.next= currentNode.next;
				}
			}
			else
			{
				previousNode = currentNode;
			}
			//move current and previous down the linked list. 
			currentNode = currentNode.next;
		}//While
	}//remove car with destination
	
	public void removeCarsWithFreight(String freight)
	{
		//Create variable node to move head as needed in while loop. 
		Node previousNode = null;
		Node currentNode = head;
		
		//While you are not at the end of the linked list, compare values. 
		while(currentNode != null)
		{
			//If you have a match. 
			if (currentNode.data.getFreight().equals(freight))
			{
				if (currentNode.next == null && previousNode != null)
				{
					previousNode.next = null;
				}
				else if(currentNode == head)
				{
					head = currentNode.next;
				}
				else
				{
					//Update the temporary Node's next to be the current.next. 
					previousNode.next= currentNode.next;
				}
			}
			else
			{
				previousNode = currentNode;
			}
			//move current and previous down the linked list. 
			currentNode = currentNode.next;
		}//While
	}//remove car with freight
	
	public void printList()
	{	
		Node thisNode = head;
		
		//While not at the end of the list
		while(thisNode != null)
		{
			//Call print method to print current Node. 
			System.out.print(thisNode.data.print());
			//Move current forward to the next node. 
			thisNode = thisNode.next;
		}
	}
	
	//Private node class. 
	private static class Node
	{
		//Private data fields. 
		RailCar9 data;
		Node next;
		
		//Constructor. 
		Node(RailCar9 passedInRailCar)
		{
			this.data = passedInRailCar;
			this.next = null;
		}
	} //Private node class
}//RailCarLinkedListClass
