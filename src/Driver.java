import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import javax.print.attribute.standard.Destination;

public class Driver {

    private static Connection conn = null;
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        /*Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String str = formatter.format(date);
        System.out.println(str);
        //System.out.println("Hello, World!");
        Calendar c = Calendar.getInstance(); */
        //Date date = new Date();
        /*String trDate="2022-11-23";  
        trDate = trDate.replaceAll("-", "");
        Date tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(trDate);
        Calendar c = Calendar.getInstance();
        c.setTime(tradeDate);
     // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        System.out.println();
     // Print dates of the current week starting on Sunday
       DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
       String startDate = df.format(c.getTime());
       System.out.println(startDate);
        for (int i = 0; i <6; i++) {
         c.add(Calendar.DATE, 1);
           }
        String endDate = df.format(c.getTime());
        System.out.println(endDate);
		System.out.println(); */

        try {
 
            String dbURL = "jdbc:sqlserver://LAPTOP-KF9OCC19\\sqlexpress;databaseName=Lab4;portNumber=1433;encrypt=true;trustServerCertificate=true;"; //"jdbc:sqlserver:1433;databaseName=SchoolDB;integratedSecurity=true";
            String user = "mmt";
            String pass = "123";
            conn = DriverManager.getConnection(dbURL, user, pass);
            String menuLoop = "";
            String userSelection = "";
            if (conn != null) {
                while (menuLoop != "no") {
                    System.out.println("Select an option");
                    System.out.println("1. Display all schedule of all trips");
                    System.out.println("2. Delete a trip offering");
                    System.out.println("3. Add a set of trip offerings");
                    System.out.println("4. Change the driver for a given trip offering");
                    System.out.println("5. Change the bus for a given trip offering");
                    System.out.println("6. Display the stops of a given trip");
                    System.out.println("7. Display a weekly schedule");
                    System.out.println("8. Add a driver");
                    System.out.println("9. Add a bus");
                    System.out.println("10. Delete a bus");
                    System.out.println("11. Insert the actual data of a given trip offering");
                    System.out.print("\nUser selection: ");
                    userSelection = input.nextLine();
                    switch (userSelection) {
                        case "1":
                            displayAllSchedules();
                            break;
                        case "2":
                            deleteTripOffering();
                            break;
                        case "3":
                            insertTripOfferingMenu();
                            break;
                        case "4":
                            changeDriver();
                            break;
                        case "5":
                            changeBus();
                            break;
                        case "6":
                            displayStops();
                            break;
                        case "7":
                            displayWeeklySchedule();
                            break;
                        case "8":
                            insertDriver();
                            break;
                        case "9":
                            insertBus();
                            break;
                        case "10":
                            deleteBus();
                            break;
                        case "11":
                            insertActualTripStopInfo();
                            break;
                        default:
                            System.out.println("Invalid option\n");
                    }

                }
                //displayAllSchedules();    //verified
                //displayStops();           //verified
                //displayWeeklySchedule();  //verified
                //insertTripOfferingMenu();    //verified
                //deleteTripOffering();         //verified
                //changeBus();                    //verified
                //insertActualTripStopInfo();      //verified
                //changeDriver();                  //verified
                //insertDriver();                 //verified
                //insertBus();            //verified
                //deleteBus();          //verified
                /** Put menu in here */
                
                
               

            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        input.close();


    }

    public static void displayAllSchedules() {
        String user_StartLocationName = "Pomona";
        String user_DestinationName = "Anaheim";
        String user_Date = "2022-01-01";

        System.out.print("Enter StartLocationName: ");
        user_StartLocationName = input.nextLine();
        user_StartLocationName = user_StartLocationName.equals("") ? null : user_StartLocationName;  // if empty string, then make null
        System.out.print("Enter DestinationName: ");
        user_DestinationName = input.nextLine();
        user_DestinationName = user_DestinationName.equals("") ? null : user_DestinationName;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_Date = input.nextLine();
        user_Date = user_Date.equals("") ? null : user_Date;  // if empty string, then make null
        //System.out.println("Input: " + user_StartLocationName);
        /*String sql_DisplayAllSchedules = """
            SELECT Date, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID
                FROM TripOffering
                WHERE TripNumber IN
                    (
                        SELECT TripNumber
                        FROM Trip
                        WHERE StartLocationName = ? AND DestinationName = ?
                    )
                AND Date = ?
        """; */
        String test2 = """
            SELECT t1.StartLocationName, t1.DestinationName, t2.Date, t2.ScheduledStartTime, t2.ScheduledArrivalTime, t2.DriverName, t2.BusID
                FROM (
                    Select *
                    From Trip
                    Where StartLocationName = ? AND DestinationName = ?
                ) t1
                JOIN TripOffering t2
                ON t1.TripNumber = t2.TripNumber
                WHERE t2.Date = ?
                                """;
        try {
            PreparedStatement statement = conn.prepareStatement(test2);
            statement.setString(1, user_StartLocationName);
            statement.setString(2, user_DestinationName);
            statement.setString(3, user_Date);

            ResultSet result = statement.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "StartLocationName", "DestinationName", "Date", "ScheduledStartTime", "ScheduledArrivalTime", "DriverName", "BusID");
            while (result.next()) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", result.getString("StartLocationName"), result.getString("DestinationName"), result.getString("Date"), result.getString("ScheduledStartTime"), result.getString("ScheduledArrivalTime"), result.getString("DriverName"), result.getString("BusID"));
            }
            System.out.println();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }      
    }

    public static void deleteTripOffering() {
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        System.out.print("Enter TripNumber: ");
        user_TripNumber = input.nextLine();
        user_TripNumber = user_TripNumber.equals("") ? null : user_TripNumber;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_Date = input.nextLine();
        user_Date = user_Date.equals("") ? null : user_Date;  // if empty string, then make null
        System.out.print("Enter ScheduledStartTime: ");
        user_ScheduledStartTime = input.nextLine();
        user_ScheduledStartTime = user_ScheduledStartTime.equals("") ? null : user_ScheduledStartTime;  // if empty string, then make null
        String sql_DeleteTripOffering = """
                DELETE 
                FROM TripOffering
                WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DeleteTripOffering);
            statement.setString(1, user_TripNumber);
            statement.setString(2, user_Date);
            statement.setString(3, user_ScheduledStartTime);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTripOfferingMenu() {
        String exit = "";
        while (!exit.equals("no")) {
            insertTripOffering();
            System.out.print("Enter 'yes' to enter more trip offerings or 'no' to stop: ");
            exit = input.nextLine();
        }
    }

    public static void insertTripOffering() {
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        String user_ScheduledArrivalTime = "";
        String user_DriverName = "testing";
        String user_BusID = null;               //null in java is NULL in sql
        System.out.print("Enter TripNumber: ");
        user_TripNumber = input.nextLine();
        user_TripNumber = user_TripNumber.equals("") ? null : user_TripNumber;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_Date = input.nextLine();
        user_Date = user_Date.equals("") ? null : user_Date;  // if empty string, then make null
        System.out.print("Enter ScheduledStartTime: ");
        user_ScheduledStartTime = input.nextLine();
        user_ScheduledStartTime = user_ScheduledStartTime.equals("") ? null : user_ScheduledStartTime;  // if empty string, then make null
        System.out.print("Enter ScheduledArrivalTime: ");
        user_ScheduledArrivalTime = input.nextLine();
        user_ScheduledArrivalTime = user_ScheduledArrivalTime.equals("") ? null : user_ScheduledArrivalTime;  // if empty string, then make null
        System.out.print("Enter DriverName: ");
        user_DriverName = input.nextLine();
        user_DriverName = user_DriverName.equals("") ? null : user_DriverName;  // if empty string, then make null
        System.out.print("Enter BusID: ");
        user_BusID = input.nextLine();
        user_BusID = user_BusID.equals("") ? null : user_BusID;  // if empty string, then make null
        String sql_InsertTripOffering = """
                INSERT 
                INTO TripOffering
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertTripOffering);
            statement.setString(1, user_TripNumber);
            statement.setString(2, user_Date);
            statement.setString(3, user_ScheduledStartTime);
            statement.setString(4, user_ScheduledArrivalTime);
            statement.setString(5, user_DriverName);
            statement.setString(6, user_BusID);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void changeDriver() {
        String user_DriverName = "";
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        System.out.print("Enter DriverName: ");
        user_DriverName = input.nextLine();
        user_DriverName = user_DriverName.equals("") ? null : user_DriverName;  // if empty string, then make null
        System.out.print("Enter TripNumber: ");
        user_TripNumber = input.nextLine();
        user_TripNumber = user_TripNumber.equals("") ? null : user_TripNumber;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_Date = input.nextLine();
        user_Date = user_Date.equals("") ? null : user_Date;  // if empty string, then make null
        System.out.print("Enter ScheduledStartTime: ");
        user_ScheduledStartTime = input.nextLine();
        user_ScheduledStartTime = user_ScheduledStartTime.equals("") ? null : user_ScheduledStartTime;  // if empty string, then make null
        String sql_DeleteTripOffering = """
                UPDATE TripOffering 
                SET DriverName = ?
                WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DeleteTripOffering);
            statement.setString(1, user_DriverName);
            statement.setString(2, user_TripNumber);
            statement.setString(3, user_Date);
            statement.setString(4, user_ScheduledStartTime);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void changeBus() {
        String user_BusID = "";
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        System.out.print("Enter BusID: ");
        user_BusID = input.nextLine();
        user_BusID = user_BusID.equals("") ? null : user_BusID;  // if empty string, then make null
        System.out.print("Enter TripNumber: ");
        user_TripNumber = input.nextLine();
        user_TripNumber = user_TripNumber.equals("") ? null : user_TripNumber;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_Date = input.nextLine();
        user_Date = user_Date.equals("") ? null : user_Date;  // if empty string, then make null
        System.out.print("Enter ScheduledStartTime: ");
        user_ScheduledStartTime = input.nextLine();
        user_ScheduledStartTime = user_ScheduledStartTime.equals("") ? null : user_ScheduledStartTime;  // if empty string, then make null
        String sql_DeleteTripOffering = """
                UPDATE TripOffering 
                SET BusID = ?
                WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DeleteTripOffering);
            statement.setString(1, user_BusID);
            statement.setString(2, user_TripNumber);
            statement.setString(3, user_Date);
            statement.setString(4, user_ScheduledStartTime);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void displayStops() {
        String user_TripNumber = "1";
        System.out.print("Enter TripNumber: ");
        user_TripNumber = input.nextLine();
        user_TripNumber = user_TripNumber.equals("") ? null : user_TripNumber;  // if empty string, then make null
        String sql_DisplayStops = """
                SELECT * 
                FROM TripStopInfo
                WHERE TripNumber = ? 
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DisplayStops);
            statement.setString(1, user_TripNumber);

            ResultSet result = statement.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s %-20s\n", "TripNumber", "StopNumber", "SequenceNumber", "DrivingTime");
            while (result.next()) {
                System.out.printf("%-20s %-20s %-20s %-20s\n", result.getString("TripNumber"), result.getString("StopNumber"), result.getString("SequenceNumber"), result.getString("DrivingTime"));
            }
            System.out.println();
            
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }
    

    public static void displayWeeklySchedule() throws ParseException {
        String user_DriverName = "Nhi";
        String user_StartDate = "2022-01-01";
        String user_EndDate = "2022-01-07";
        System.out.print("Enter DriverName: ");
        user_DriverName = input.nextLine();
        user_DriverName = user_DriverName.equals("") ? null : user_DriverName;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_StartDate = input.nextLine();
        user_StartDate = user_StartDate.equals("") ? null : user_StartDate;  // if empty string, then make null
        
        user_StartDate = user_StartDate.replaceAll("-", "");
        Date currDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(user_StartDate);
        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
     // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println();
     // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        user_StartDate = df.format(c.getTime());
        System.out.println(user_StartDate);
        for (int i = 0; i <6; i++) {
            c.add(Calendar.DATE, 1);
        }
        user_EndDate = df.format(c.getTime());
        System.out.println("Weekly schedule for " + user_StartDate + " - " + user_EndDate + "\n");

        String sql_DisplayWeeklySchedule = """
            SELECT t1.StartLocationName, t1.DestinationName, t2.Date, t2.ScheduledStartTime, t2.ScheduledArrivalTime, t2.DriverName, t2.BusID
                FROM (
                    Select *
                    From TripOffering
                    Where DriverName = ? AND Date BETWEEN ? AND ?
                ) t2
                JOIN Trip t1
                ON t2.TripNumber = t1.TripNumber
                                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DisplayWeeklySchedule);
            statement.setString(1, user_DriverName);
            statement.setString(2, user_StartDate);
            statement.setString(3, user_EndDate);

            ResultSet result = statement.executeQuery();
            
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "StartLocationName", "DestinationName", "Date", "ScheduledStartTime", "ScheduledArrivalTime", "DriverName", "BusID");
            while (result.next()) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", result.getString("StartLocationName"), result.getString("DestinationName"), result.getString("Date"), result.getString("ScheduledStartTime"), result.getString("ScheduledArrivalTime"), result.getString("DriverName"), result.getString("BusID"));
            }
            System.out.println();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void insertDriver() {
        String user_DriverName = "";
        String user_DriverTelephoneNumber = "";
        System.out.print("Enter DriverName: ");
        user_DriverName = input.nextLine();
        user_DriverName = user_DriverName.equals("") ? null : user_DriverName;  // if empty string, then make null
        System.out.print("Enter DriverTelephoneNumber: ");
        user_DriverTelephoneNumber = input.nextLine();
        user_DriverTelephoneNumber = user_DriverTelephoneNumber.equals("") ? null : user_DriverTelephoneNumber;  // if empty string, then make null
        String sql_InsertDriver = """
                INSERT 
                INTO Driver
                VALUES (?, ?)
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertDriver);
            statement.setString(1, user_DriverName);
            statement.setString(2, user_DriverTelephoneNumber);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void insertBus() {
        String user_BusID = "";
        String user_Model = "";
        String user_Year = "";
        System.out.print("Enter BusID: ");
        user_BusID = input.nextLine();
        user_BusID = user_BusID.equals("") ? null : user_BusID;  // if empty string, then make null
        System.out.print("Enter Model: ");
        user_Model = input.nextLine();
        user_Model = user_Model.equals("") ? null : user_Model;  // if empty string, then make null
        System.out.print("Enter Year: ");
        user_Year = input.nextLine();
        user_Year = user_Year.equals("") ? null : user_Year;  // if empty string, then make null
        String sql_InsertBus = """
                INSERT 
                INTO Bus
                VALUES (?, ?, ?)
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertBus);
            statement.setString(1, user_BusID);
            statement.setString(2, user_Model);
            statement.setString(3, user_Year);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void deleteBus() {
        String user_BusID = "";
        System.out.print("Enter BusID: ");
        user_BusID = input.nextLine();
        user_BusID = user_BusID.equals("") ? null : user_BusID;  // if empty string, then make null
        String sql_DeleteBus = """
                DELETE 
                FROM Bus
                WHERE BusID = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DeleteBus);
            statement.setString(1, user_BusID);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void insertActualTripStopInfo() {
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        String user_StopNumber = "";
        String user_ScheduledArrivalTime = "";
        String user_ActualStartTime = "";
        String user_ActualArrivalTime = "";
        String user_NumberOfPassengerIn = "";
        String user_NumberofPassengerOut = "";
        System.out.print("Enter TripNumber: ");
        user_TripNumber = input.nextLine();
        user_TripNumber = user_TripNumber.equals("") ? null : user_TripNumber;  // if empty string, then make null
        System.out.print("Enter Date: ");
        user_Date = input.nextLine();
        user_Date = user_Date.equals("") ? null : user_Date;  // if empty string, then make null
        System.out.print("Enter ScheduledStartTime: ");
        user_ScheduledStartTime = input.nextLine();
        user_ScheduledStartTime = user_ScheduledStartTime.equals("") ? null : user_ScheduledStartTime;  // if empty string, then make null
        System.out.print("Enter StopNumber: ");
        user_StopNumber = input.nextLine();
        user_StopNumber = user_StopNumber.equals("") ? null : user_StopNumber;  // if empty string, then make null
        System.out.print("Enter ScheduledArrivalTime: ");
        user_ScheduledArrivalTime = input.nextLine();
        user_ScheduledArrivalTime = user_ScheduledArrivalTime.equals("") ? null : user_ScheduledArrivalTime;  // if empty string, then make null
        System.out.print("Enter ActualStartTime: ");
        user_ActualStartTime = input.nextLine();
        user_ActualStartTime = user_ActualStartTime.equals("") ? null : user_ActualStartTime;  // if empty string, then make null
        System.out.print("Enter ActualArrivalTime: ");
        user_ActualArrivalTime = input.nextLine();
        user_ActualArrivalTime = user_ActualArrivalTime.equals("") ? null : user_ActualArrivalTime;  // if empty string, then make null
        System.out.print("Enter NumberOfPassengerIn: ");
        user_NumberOfPassengerIn = input.nextLine();
        user_NumberOfPassengerIn = user_NumberOfPassengerIn.equals("") ? null : user_NumberOfPassengerIn;  // if empty string, then make null
        System.out.print("Enter NumberOfPassengerOut: ");
        user_NumberofPassengerOut = input.nextLine();
        user_NumberofPassengerOut = user_NumberofPassengerOut.equals("") ? null : user_NumberofPassengerOut;  // if empty string, then make null
        String sql_InsertActualTripStopInfo = """
                INSERT
                INTO ActualTripStopInfo 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertActualTripStopInfo);
            statement.setString(1, user_TripNumber);
            statement.setString(2, user_Date);
            statement.setString(3, user_ScheduledStartTime);
            statement.setString(4, user_StopNumber);
            statement.setString(5, user_ScheduledArrivalTime);
            statement.setString(6, user_ActualStartTime);
            statement.setString(7, user_ActualArrivalTime);
            statement.setString(8, user_NumberOfPassengerIn);
            statement.setString(9, user_NumberofPassengerOut);

            System.out.println(statement.executeUpdate() + " rows affected");
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }
    
}
