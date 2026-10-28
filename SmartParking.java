import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SmartParking {

    // Vehicle information
    static class Vehicle {
        String number;
        int slot;
        int hours;
        boolean active;

        Vehicle(String number, int slot, int hours, boolean active) {
            this.number = number;
            this.slot = slot;
            this.hours = hours;
            this.active = active;
        }
    }

    // Node for Linked List
    static class Node {
        Vehicle data;
        Node next;

        Node(Vehicle data) {
            this.data = data;
            this.next = null;
        }
    }

    // Head of Linked List
    static Node head = null;

    // Add vehicle record
    static void addRecord(Vehicle v) {

        Node newNode = new Node(v);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;

            while (temp.next != null) {
                temp = temp.next;
            }

            temp.next = newNode;
        }
    }

    // Search active vehicle
    static Node searchVehicle(String number) {

        Node temp = head;

        while (temp != null) {

            if (temp.data.number.equals(number)
                    && temp.data.active) {
                return temp;
            }

            temp = temp.next;
        }

        return null;
    }

    // Calculate parking fee
    static int calculateFee(int hours) {
        return hours * 20;
    }

    // Display parking status
    static void displayParking(Vehicle[] parking) {

        System.out.println("\n========== PARKING STATUS ==========");

        for (int i = 0; i < parking.length; i++) {

            System.out.print("Slot " + (i + 1) + " : ");

            if (parking[i].active) {
                System.out.println(parking[i].number);
            } else {
                System.out.println("EMPTY");
            }
        }
    }

    // Display all records
    static void displayRecords() {

        System.out.println("\n========== PARKING RECORDS ==========");

        if (head == null) {
            System.out.println("No records available.");
            return;
        }

        Node temp = head;

        while (temp != null) {

            System.out.println(
                    "Vehicle Number : " + temp.data.number);

            System.out.println(
                    "Parking Slot   : " + temp.data.slot);

            System.out.println(
                    "Status         : "
                    + (temp.data.active ? "Parked" : "Exited"));

            System.out.println("-------------------------------------");

            temp = temp.next;
        }
    }

    // Display waiting queue
    static void displayWaitingQueue(Queue<String> waitingQueue) {

        if (waitingQueue.isEmpty()) {
            System.out.println("\nWaiting queue is empty.");
            return;
        }

        System.out.println("\n========== WAITING QUEUE ==========");

        for (String vehicle : waitingQueue) {
            System.out.println(vehicle);
        }
    }

    // Main method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        final int totalSlots = 5;

        // Array for parking slots
        Vehicle[] parking = new Vehicle[totalSlots];

        // Initialize parking slots
        for (int i = 0; i < totalSlots; i++) {

            parking[i] = new Vehicle(
                    "",
                    i + 1,
                    0,
                    false
            );
        }

        // Queue for waiting vehicles
        Queue<String> waitingQueue = new LinkedList<>();

        int choice;

        do {

            System.out.println("\n========================================");
            System.out.println("      SMART PARKING MANAGEMENT SYSTEM");
            System.out.println("========================================");

            System.out.println("1. Vehicle Entry");
            System.out.println("2. Vehicle Exit");
            System.out.println("3. Display Parking Status");
            System.out.println("4. Search Vehicle");
            System.out.println("5. Display Waiting Queue");
            System.out.println("6. Calculate Parking Fee");
            System.out.println("7. Display All Records");
            System.out.println("8. Exit");

            System.out.println("========================================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            // --------------------------------
            // 1. VEHICLE ENTRY
            // --------------------------------

            if (choice == 1) {

                System.out.print("\nEnter Vehicle Number: ");
                String number = sc.next();

                // Check whether already parked
                if (searchVehicle(number) != null) {

                    System.out.println(
                            "Vehicle is already parked!");

                    continue;
                }

                int freeSlot = -1;

                // Find empty slot
                for (int i = 0; i < totalSlots; i++) {

                    if (!parking[i].active) {

                        freeSlot = i;
                        break;
                    }
                }

                // If slot available
                if (freeSlot != -1) {

                    parking[freeSlot].number = number;
                    parking[freeSlot].slot = freeSlot + 1;
                    parking[freeSlot].hours = 0;
                    parking[freeSlot].active = true;

                    addRecord(parking[freeSlot]);

                    System.out.println(
                            "\nVehicle parked successfully!");

                    System.out.println(
                            "Vehicle Number : " + number);

                    System.out.println(
                            "Parking Slot   : "
                            + (freeSlot + 1));

                } else {

                    // Parking full
                    waitingQueue.add(number);

                    System.out.println(
                            "\nParking is FULL!");

                    System.out.println(
                            "Vehicle added to waiting queue.");
                }

            }

            // --------------------------------
            // 2. VEHICLE EXIT
            // --------------------------------

            else if (choice == 2) {

                System.out.print("\nEnter Vehicle Number: ");
                String number = sc.next();

                Node record = searchVehicle(number);

                if (record == null) {

                    System.out.println(
                            "Vehicle not found!");

                    continue;
                }

                System.out.print(
                        "Enter parking hours: ");

                int hours = sc.nextInt();

                int fee = calculateFee(hours);

                int slot = record.data.slot;

                // Make parking slot empty
                parking[slot - 1].active = false;

                // Update record
                record.data.hours = hours;
                record.data.active = false;

                System.out.println(
                        "\nVehicle exited successfully!");

                System.out.println(
                        "Vehicle Number : " + number);

                System.out.println(
                        "Slot " + slot + " is now EMPTY.");

                System.out.println(
                        "Parking Fee    : Rs. " + fee);

                // Move waiting vehicle
                if (!waitingQueue.isEmpty()) {

                    String nextVehicle =
                            waitingQueue.remove();

                    parking[slot - 1].number =
                            nextVehicle;

                    parking[slot - 1].slot =
                            slot;

                    parking[slot - 1].hours = 0;

                    parking[slot - 1].active = true;

                    addRecord(parking[slot - 1]);

                    System.out.println(
                            "\nWaiting vehicle moved to parking!");

                    System.out.println(
                            "Vehicle Number : "
                            + nextVehicle);

                    System.out.println(
                            "Parking Slot   : " + slot);
                }
            }

            // --------------------------------
            // 3. DISPLAY PARKING
            // --------------------------------

            else if (choice == 3) {

                displayParking(parking);
            }

            // --------------------------------
            // 4. SEARCH VEHICLE
            // --------------------------------

            else if (choice == 4) {

                System.out.print(
                        "\nEnter Vehicle Number: ");

                String number = sc.next();

                Node record =
                        searchVehicle(number);

                if (record != null) {

                    System.out.println(
                            "\nVehicle Found!");

                    System.out.println(
                            "Vehicle Number : "
                            + record.data.number);

                    System.out.println(
                            "Parking Slot   : "
                            + record.data.slot);

                } else {

                    System.out.println(
                            "\nVehicle not found!");
                }
            }

            // --------------------------------
            // 5. WAITING QUEUE
            // --------------------------------

            else if (choice == 5) {

                displayWaitingQueue(waitingQueue);
            }

            // --------------------------------
            // 6. PARKING FEE
            // --------------------------------

            else if (choice == 6) {

                System.out.print(
                        "\nEnter Vehicle Number: ");

                String number = sc.next();

                Node record =
                        searchVehicle(number);

                if (record != null) {

                    System.out.print(
                            "Enter parking hours: ");

                    int hours = sc.nextInt();

                    System.out.println(
                            "Parking Fee = Rs. "
                            + calculateFee(hours));

                } else {

                    System.out.println(
                            "Vehicle not found!");
                }
            }

            // --------------------------------
            // 7. ALL RECORDS
            // --------------------------------

            else if (choice == 7) {

                displayRecords();
            }

            // --------------------------------
            // 8. EXIT
            // --------------------------------

            else if (choice == 8) {

                System.out.println(
                        "\nThank you for using "
                        + "Smart Parking System!");
            }

            else {

                System.out.println(
                        "\nInvalid choice!");
            }

        } while (choice != 8);

        sc.close();
    }
}