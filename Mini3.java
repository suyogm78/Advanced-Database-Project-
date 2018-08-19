import java.sql.*;
import java.util.Scanner;
import java.io.*;
/**
 * @Course CS522
 * @Assignment Third Mini Project
 * @DueDate 05/02/2018
 * @author Suyog Mankar
 * @author Kevin Leehan
 * @author Aaron Barr
 * 
 * 
 */
	public class Mini3 
	{ 
		static BufferedReader keyboard;
	    static Connection conn; // A connection to the DB must be established
	                            // before requests can be handled.  You should
	                            // have only one connection.
	    static Statement stmt; 	// Requests are sent via Statements.  You need
	                            // one statement for every request you have
	                            // open at the same time.

	    static Scanner sc = new Scanner(System.in); 
	  
	    // "main" is where the connection to the database is made, and
	    // where I/O is presented to allow the user to direct requests to
	    // the methods that actually do the work.

	    public static void main (String args [])
		throws IOException
	    {
	    	String username="spm122", password = "dz19oLOt";
	    	keyboard = new BufferedReader(new InputStreamReader (System.in));

	    	try { 

	    	   DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

	                System.out.println("Registered the driver...");

	    	    conn = DriverManager.getConnection (
	                       "jdbc:oracle:thin:@oracle1.wiu.edu:1521/toolman.wiu.edu",
	    		    username, password);

	    	    conn.setAutoCommit(false);

	                System.out.println("logged into oracle as " + username);

	    	    stmt = conn.createStatement ();

		    	while(true){
		    		 System.out.println("\n\tProject Name: CS522 Mini Project III");
		    		 System.out.println("\n\tMenu");
		     		 System.out.println("1. Add a new product");
		     		 System.out.println("2. Display all review information");
			         System.out.println("3. Display product and averge review scores");
			         System.out.println("4. Display all customer information.");
			         System.out.println("5. Display total sales per product.");
			         System.out.println("6. To Exit");		
		             
		     		  // Prompt user to select Menu 
		     		  System.out.println("\nPlease make a selection: ");  // user choice 
		     		  int choice = sc.nextInt();		
				
		    		  switch(choice)
			    	  {
		    		  			// Allows user to insert new products into product table
			    	  	case 1:	InsertIntoProduct();
			    	  				System.out.println("\n");
			    	  				break;
			    	  			// Displays products with their average review score
			    	  	case 2: AvgReview();
		    	  					System.out.println("\n");
		    	  					break;
	                            // Display all reviews
			    	  	case 3:	ReviewInfo();
		    	  					System.out.println("\n");
		    	  					break;
			    			    // Get all the customer details 
			    	  	case 4:	GetCustomerInfo();
		    	  					System.out.println("\n");
		    	  					break;
					          	// Find the total sales per product for all time
			    	  	case 5:	PerProductSales();
			    	  				System.out.println("\n");
			    	  				break;
			          			// Exits the program  
			    	  	case 6:	CloseConnection();
			    	  				System.exit(0);
			    	  				break;
		               
			    	  	default: System.out.println("Please enter a valid choice!!!");
			    	  			System.out.println("\n");
			     	}// end-of-switch
		     	  }// end-of-while
	     	  }
	    		catch(SQLException e){
	    			System.out.println("Caught SQL Exception: \n     " + e);
	    		}
	     }
		 
	/**
	 * Prompts user for Product name and product price
	 * Automatically grabs max product# from table increments by 1
	 * Sends SQL insert command containing id, name, price to the global user product table
	 * Trigger on product table replicates to remote tables
	 * This function verifies the remote tables were updated.
	 */
   public static void InsertIntoProduct(){
	   String sql;
	   String name;
       String price;
       int id=0;
       try{
         sql = "select max(prod_id) from product";
         ResultSet rs = stmt.executeQuery(sql);
         while(rs.next())
            id = rs.getInt("max(prod_id)");
         id=id+1;
        
	     System.out.print("\nEnter the product name: ");
         sc.nextLine();
         name = sc.nextLine();
         System.out.print("Enter the product price: ");
	     price = sc.nextLine();
	     System.out.println();                
         sql = "INSERT INTO PRODUCT VALUES("+id+",'"+name+"',"+price+")";
         rs = stmt.executeQuery(sql);
         sql="commit";
         rs = stmt.executeQuery(sql);
         System.out.println("New entry created: Id="+id+" Name="+name+" Price="+price);
         sql = "SELECT * FROM Product "
		  		+"MINUS "
		   		+"SELECT * FROM Product@LinkA";
		 rs = stmt.executeQuery(sql);
		 	if(!rs.next())
		 		System.out.println("LinkA synchronized.");
		 	else
         		System.out.println("LinkK not synchronized");
         sql = "SELECT * FROM Product "
           		+"MINUS "
           		+"SELECT * FROM Product@LinkK";
         rs = stmt.executeQuery(sql);
         	if(!rs.next())
         		System.out.println("LinkK synchronized.");
         	else
         		System.out.println("LinkK not synchronized");
	    }     
	     catch(SQLException e ){
	    	   System.out.println("InsertIntoProduct() - Caught SQL exception: " +e);
	     }
   }
   
	/**
	 * Displays all reviews from customers
	 * Displayed in  the format CustomerName, DateOfReview, ProductReviewed, Comment, Rating
	 */
   public static void ReviewInfo(){
		
	    String sql;	
	    try{
	              // Show review information on the basis of customer type
			sql = "SELECT Cust_Name, R_DATE, Prod_NAME, R_COMMENT, R_RATING "
				+ "From Product Join (Review Join  "
				+ "(SELECT Cust_Id, Cust_Name FROM Customer@LinkA " 
				+ "UNION " 
				+ "SELECT Cust_Id, Cust_Name FROM Customer@LinkK "
			    + "ORDER BY cust_id) "
			    + "USING (cust_id)) "
			    + "using (prod_id) "
			    + "order by r_date";
	  
		      // to execute select query  
	 		ResultSet rs = stmt.executeQuery(sql);
		    System.out.println("Name      DATE           Product    COMMENT                RATING");
		      //print the result of query
		    while(rs.next()){
	 			//print the details 
	 			System.out.printf("%-10s",rs.getString("Cust_Name"));
	 			System.out.printf("%-15s",rs.getDate("R_DATE"));
	 			System.out.printf("%-10s",rs.getString("Prod_Name"));
	 			System.out.printf("%-25s",rs.getString("R_COMMENT"));
	 			System.out.printf("%-2s",rs.getString("R_RATING"));
		    	System.out.println();
	 		}
	    }     
	    catch(SQLException e ){
	    	System.out.println("Inside ReviewInfo() - Caught SQL exception: " +e);
	    }
	}
   
   /**
	 * Displays the Product name and it's average review score
	 * if the product has atleast one review
	 */
	public static void AvgReview(){
		String sql;
		try{
			sql = "Select Prod_Name, AVG(R_Rating) AS Average "   
				+ "FROM Product  Join Review "
	 	   		+ "ON Product.Prod_Id = Review.Prod_Id "
	 	   		+ "GROUP BY Prod_name";
		   
        	  //to execute above sql query  	      
			ResultSet rs = stmt.executeQuery(sql);
		   
			System.out.println("Product      Average Rating");
		     //print the result of query  
            while(rs.next()){
            	System.out.printf("%-10s",rs.getString("Prod_Name"));
            	float n = rs.getFloat("Average");
            	System.out.print("   "+n);
				System.out.println();
		     }
             
          }     
          catch(SQLException e ){
        	  System.out.println("AvgReview() - Caught SQL exception: " +e);
          }		
	}
	   
   /**
    * Displays all customer information from all remote sites
    */
	public static void GetCustomerInfo(){
		String sql="";
		try{
			sql = "SELECT * FROM Customer@LinkA "
				+"UNION "
		 	   	+ "SELECT * FROM Customer@LinkK ";                         
              
	        	  //to execute above sql query  	      
			   ResultSet rs = stmt.executeQuery(sql);
			      
			   System.out.println("ID#       NAME      ADDRESS             Phone#    Email");   			    
			   //print the result of query 
	           while(rs.next()){
	            	//print the details 	  
				   System.out.printf("%-10d",rs.getInt("CUST_ID"));
				   System.out.printf("%-10s",rs.getString("CUST_NAME")); 
				   System.out.printf("%-20s",rs.getString("CUST_ADDRESS"));
				   System.out.printf("%-10d",rs.getInt("CUST_PH"));
				   System.out.printf("%-20s",rs.getString("CUST_EMAIL")); 
				   System.out.println();
	             }
		}
		catch(SQLException e ){
			System.out.println("GetCustomerInfo() - Caught SQL exception: " +e);
	    }		
	}             
	
	/**
	 * Displays the number of items sold per product
	 */
	public static void PerProductSales(){
		String sql;
	    try{
	    	sql = "Select Prod_Name AS Item, Count(Prod_Id) AS \"Total Sold\" "
	 		   + "FROM Product Join "
	 		   + "(SELECT Prod_Id FROM Buy@LinkK " 
	 		   + "UNION ALL "
	 		   + "SELECT Prod_Id FROM Buy@Linka "
	 		   + "ORDER BY Prod_Id) "
	 		   + "using (Prod_Id)  "
	 		   + "group by Prod_Name";
	             
		      // to execute select query  
	 		ResultSet rs = stmt.executeQuery(sql);
	 		
	 		System.out.println("Item      Total Sold");	  
	 		//print the result of query
	 		while(rs.next()){
			      //print the details  
		    	  System.out.printf("%-15s",rs.getString("Item"));
		    	  System.out.printf("%-5s",rs.getString("Total Sold"));
		    	  System.out.println();
	 		}  	  
	    }
	    catch(SQLException e ){
	    	System.out.println("PerProductSales() - Caught SQL exception: " +e);
	    }	
	}    
	
	/**
	 * Closes the sql connection
	 */
	public static void CloseConnection(){
		try{
			conn.close();
	    }
		catch(SQLException e ){
			System.out.println("CloseConnection() - Caught SQL exception: " +e);
		}

	}
}// end-of-class




