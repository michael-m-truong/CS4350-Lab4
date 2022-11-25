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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

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
        String trDate="2022-11-23";  
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
		System.out.println();

        try {
 
            String dbURL = "jdbc:sqlserver://LAPTOP-KF9OCC19\\sqlexpress;databaseName=Lab4;portNumber=1433;encrypt=true;trustServerCertificate=true;"; //"jdbc:sqlserver:1433;databaseName=SchoolDB;integratedSecurity=true";
            String user = "mmt";
            String pass = "123";
            conn = DriverManager.getConnection(dbURL, user, pass);
            if (conn != null) {
                displayAllSchedules();
                displayStops();
                displayWeeklySchedule();
                //insertTripOffering();
                /** Put menu i n here */
                
                // String sql = "SELECT * FROM [SchoolDB].[dbo].[Student]";
                // String test = null;
                // String sqlTest = "INSERT INTO bday values ('2012-2-22')";
                // PreparedStatement statement = conn.prepareStatement(sql);
                // statement.setString(1, test);
            
                // ResultSet result = statement.executeQuery();
                // System.out.printf("%-20s %-20s %-20s\n", "SSN", "Name", "Major");
                // while (result.next()) {
                //     System.out.printf("%-20s %-20s %-20s\n", result.getString("SSN"), result.getString("Name"), result.getString("Major"));
                // }
               

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

            statement.executeUpdate();
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTripOffering() {
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        String user_ScheduledArrivalTime = "";
        String user_DriverName = "testing";
        String user_BusID = null;               //null in java is NULL in sql
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

            statement.executeUpdate();
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void changeDriver() {
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        String sql_DeleteTripOffering = """
                UPDATE TripOffering 
                SET DriverName = ?
                WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DeleteTripOffering);
            statement.setString(1, user_TripNumber);
            statement.setString(2, user_Date);
            statement.setString(3, user_ScheduledStartTime);

            statement.executeUpdate();
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void changeBus() {
        String user_TripNumber = "";
        String user_Date = "";
        String user_ScheduledStartTime = "";
        String sql_DeleteTripOffering = """
                UPDATE TripOffering 
                SET BusID = ?
                WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_DeleteTripOffering);
            statement.setString(1, user_TripNumber);
            statement.setString(2, user_Date);
            statement.setString(3, user_ScheduledStartTime);

            statement.executeUpdate();
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void displayStops() {
        String user_TripNumber = "1";
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
    

    public static void displayWeeklySchedule() {
        String user_DriverName = "Nhi";
        String user_StartDate = "2022-01-01";
        String user_EndDate = "2022-01-07";
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
        String sql_InsertDriver = """
                INSERT 
                INTO Driver
                VALUES (?, ?)
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertDriver);
            statement.setString(1, user_DriverName);
            statement.setString(2, user_DriverTelephoneNumber);

            statement.executeUpdate();
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

            statement.executeUpdate();
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    public static void deleteBus() {
        String user_BusID = "";
        String sql_InsertBus = """
                DELETE 
                FROM Bus
                WHERE BusID = ?
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertBus);
            statement.setString(1, user_BusID);

            statement.executeUpdate();
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
        String sql_InsertBus = """
                INSERT
                INTO ActualTripStopInfo 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            PreparedStatement statement = conn.prepareStatement(sql_InsertBus);
            statement.setString(1, user_TripNumber);
            statement.setString(2, user_Date);
            statement.setString(3, user_ScheduledStartTime);
            statement.setString(4, user_StopNumber);
            statement.setString(5, user_ScheduledArrivalTime);
            statement.setString(6, user_ActualStartTime);
            statement.setString(7, user_ActualArrivalTime);
            statement.setString(8, user_NumberOfPassengerIn);
            statement.setString(9, user_NumberofPassengerOut);

            statement.executeQuery();
            System.out.println("Query sucessfully executed");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }
    
}
