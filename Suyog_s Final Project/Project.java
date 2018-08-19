	/* 
	 * Title: Voice of Customer 
	 * File Name (Project.java) 
	 * Author: Suyog Mankar & Avilash Chary
	 *
	 */

	// You need to import the java.sql package to use JDBC
import java.sql.*;
import java.util.Scanner;
import java.io.*;

	public class Project 
	{ 
		
		static BufferedReader keyboard;
	      
		static Connection conn; // A connection to the DB must be established
	                            // before requests can be handled.  You should
	                            // have only one connection.
	    static Statement stmt; // Requests are sent via Statements.  You need
	                            // one statement for every request you have
	                            // open at the same time.

	    
	    static Scanner sc = new Scanner(System.in); 
	  


	    // "main" is where the connection to the database is made, and
	    // where I/O is presented to allow the user to direct requests to
	    // the methods that actually do the work.

	    public static void main (String args [])
		throws IOException
	    {
	    	String username="spm122", password = "ku13lBDu";
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

	    	ResultSet rset=stmt.executeQuery ("select TNAME from TAB");
	    	while (rset.next()) {
		       	   System.out.println( rset.getString(1) );
		    	   }

		    	rset.close();
		    	while(true){
		     		 
		    		 System.out.println("\n\tProject Name: Voice of Customer");
		    		 System.out.println("\n\tMenu");
		     		 System.out.println("1. Retrieve all information about third party");
				     System.out.println("2. Display reviews whose market review is greater than 2");
			         System.out.println("3. Show review information on the basis of customer type");
				     System.out.println("4. Retrieve order of customer license.");
				     System.out.println("5. Get all the customer details by order date.");  
				     System.out.println("6. Update Rating by customer Name");  
		             System.out.println("7. To Exit");		

		     		  // Prompt user to select Menu 
		     		  System.out.println("\nPlease enter your choice");  // user choice 
		     		  int choice = sc.nextInt();		
				
		    		  switch(choice)
			    	  {
			    	     case 1:   
				                  getThirdParty();         // Finding third party table details
			    			      System.out.println("\n");
				        	      break;
				        		
		    		     case 2:  selectReview();             // Display reviews whose market review is greater than 2 
	                              System.out.println("\n");
				             	  break;
				        		
			    		 case 3:  ReviewInfo();              // Show review information on the basis of customer type 
			    			      System.out.println("\n"); 
		        		          break;
		        		         
			          	 case 4:  CustomerInfo();           // Get all the customer details by order date.
			          	          System.out.println("\n");
			          	          break;
			          	   
			          	 case 5:  getProdDetails();       // Get all product details 
	                              System.exit(0);
		                          break;     
		                          
			          	 case 6 : showCustomer();           // Update Rating by customer Name
			          		 		break;
			          		 		
			          	 case 7:  closeConnection();       // call to closeConnection method 
		                          System.exit(0);
				                  break;
		                
			    	     
			    		 default: System.out.println("Please enter valid choice!!!");   
		                          System.out.println("\n"); 
		 
				
			     	}// end-of-switch
		     	  }// end-of-while

	    	
	     	  }
	     	 catch(SQLException e)
	 	    {
	 		System.out.println("Caught SQL Exception: \n     " + e);
	 	    }
	     }

	
        
	  public static void showCustomer()   // Update Rating by customer Name
	  {
		  String sql="";  // input from user 
		  String sql2="";  // input from user
		     String sname="";  
			
		    try{
		            // accept ItemType from user 
		             System.out.println("\n Enter Customer Name (tom,john,sam,john1,sam1,tom1): ");
		             sc.nextLine();
		             sname = sc.nextLine();

		             System.out.println("\n Give rating : ");
		             int rating = sc.nextInt();

		             // sql query to get customer id and customer name   
				sql = "select Customer.Cust_Id,Customer.Cust_Name " 
						+"FROM Customer " 
						+"WHERE Customer.Cust_Name='"+sname+"'";

			      // to execute query  
		 		ResultSet rs = stmt.executeQuery(sql);
		 		int cust_id=0;
		 		while(rs.next())
		 		{
		 	       cust_id = rs.getInt("CUST_ID");  // Customer id as input for give table 
			    	  
		 		}
		 		sql2 = "select R_Id FROM GIVE WHERE "  // sql query to find R_id for customer 
		 				+ "CUST_ID="+cust_id;
		 		
		 		ResultSet rs1 = stmt.executeQuery(sql2);
		 		int r_id = 0;
		 		while(rs1.next())
		 		{
		 	       r_id = rs1.getInt("R_ID");  // R_id input to update in rating table 
			    	  
		 		}
		 		
		 		String sql3 = "update review set M_RATING="+rating+" where R_ID="+r_id;
		 		ResultSet rs2 = stmt.executeQuery(sql3);
		 		try{
		    		conn.commit(); //commit transaction
		    	}
		    	catch(SQLException e)
		    	{
		    		System.out.println("Caught sql exception : \n" +e);
		    	}
		 		
		    }     
		       catch(SQLException e ){
					 
			 System.out.println("Inside ReviewInfo() ");
			 System.out.println("Caught SQL exception: " +e);
		       }

		  
	  }

	// to get information of third party table 
	public static void  getThirdParty() {
		
	     String sql="";
		
	    try{
	                                              
		   	sql = "SELECT * From ThirdParty";  // Retrieve details from third party table 
			      	  
		      // to execute select query  
	 		ResultSet rs = stmt.executeQuery(sql);
		    
	 		while(rs.next()){
			   
	 	       //print  
	 				System.out.printf("%-15s",rs.getString("COMP_NAME"));  // display company name
				   System.out.printf("%4d",rs.getInt("PROD_ID"));          // display production id 
				   System.out.printf("%-15s",rs.getString("POSITIVE_COMM"));  // display positive comments 
				   System.out.printf("%-15s",rs.getString("NEGATIVE_COMM"));   // display negative comments 
				   System.out.printf("%-15s",rs.getString("T_RATING"));         // display rating 
				   System.out.println();   
	 		   
	 		}
		    	 
		}     
	       catch(SQLException e ){
				 
		 System.out.println("Inside getThirdParty()");
		 System.out.println("Caught SQL exception: " +e);
	       }
			
		
	}

	//to get review 
	public static void selectReview(){
			
		String sql;
		
	        try{
                 
	         sql = "SELECT R_ID,M_RATING, R_COMMENT "   // Display reviews whose market review is greater than 2
		 	   +"FROM  Review "
			   +"WHERE (M_RATING > 2.0)";                         
              
	        	  //to execute above sql query  	      
			   ResultSet rs = stmt.executeQuery(sql);
			      
			   
              // System.out.println("---------------------- \t---------- \t----------");	
			   System.out.println("\n Reviews are: ");
			   System.out.println("\nR_ID\tM_RATING    R_COMMENT");
			   System.out.println("---------------------------------- ");
			    
	             //print the result of query  
	             while(rs.next()){
	            	//print the details 	  
			   System.out.printf("%-10d",rs.getInt("R_ID"));
			   System.out.printf("%-10d",rs.getInt("M_RATING"));
			   System.out.printf("%-15s",rs.getString("R_COMMENT")); 
			   System.out.println();
			   
			   			   
	              }
	             
	          }     
	          catch(SQLException e ){
		
			 System.out.println("Review doesn't exist ");
		         System.out.println("Caught SQL exception: " +e);
	          }		
	}
	
	public static void ReviewInfo(){
		
	     String sql;
	     String sname="";
		
	    try{
	            // accept customer Type from user 
	             System.out.println("\n Enter the Customer Type (deal) : ");
	             sc.nextLine();
	             sname = sc.nextLine();

	              // Show review information on the basis of customer type
			sql = "SELECT * From Review,Customer,Give "
			      +"Where Customer.CUST_ID = Give.CUST_ID "
			      +"And give.R_ID=Review.R_ID AND CUST_TYPE='"+sname+"'";
	  
		      // to execute select query  
	 		ResultSet rs = stmt.executeQuery(sql);
		    	  System.out.println("CUST_TYPE    R_DATE           S_PRODFEATURE      S_PRODPRICE       M_RATING     R_COMMENT");
	 		while(rs.next())
	 		{
	 			//print the details 
		    	  //System.out.printf("%-5d",rs.getInt("R_ID"));
	 			  System.out.printf("%-10s",rs.getString("CUST_TYPE"));
		    	  System.out.printf("%-15s",rs.getDate("R_DATE"));
		    	  System.out.printf("%-20s",rs.getString("S_PRODFEATURE"));
		    	  System.out.printf("%-10d",rs.getInt("S_PRODPRICE"));
		    	  System.out.printf("%-8s",rs.getString("M_RATING"));
		    	  System.out.printf("%-10s",rs.getString("R_COMMENT"));
		    	  //System.out.printf("%-5d",rs.getInt("CUST_ID"));
		    	   // System.out.printf("%-10s",rs.getString("CUST_NAME"));
		    	  //System.out.printf("%-12s",rs.getString("CUST_ADDRESS"));
		    	 // System.out.printf("%-10d",rs.getInt("CUST_PH"));
		    	 // System.out.printf("%-10s",rs.getString("CUST_EMAIL"));
		    	 // System.out.printf("%-5d",rs.getInt("CUST_LICENSE"));
		    	 // System.out.printf("%-2d",rs.getInt("CUST_ID"));
		    	  //System.out.printf("%-3d",rs.getInt("R_ID"));
		    	  System.out.println();
	 		}
	    }     
	       catch(SQLException e ){
				 
		 System.out.println("Inside ReviewInfo() ");
		 System.out.println("Caught SQL exception: " +e);
	       }
			
	
	}

	public static void CustomerInfo()
	{
		
		String sql="";
		

	        try{
	        		      
	           sql = "SELECT * FROM Customer "
		 	   	   +"ORDER BY CUST_LICENSE DESC";                         
              
	        	  //to execute above sql query  	      
			   ResultSet rs = stmt.executeQuery(sql);
			      
			   System.out.println("CUST_ID  CUST_TYPE  CUST_NAME  CUST_ADDRESS     CUST_PH    CUST_EMAIL   CUST_LICENSE");   			    
	             //print the result of query  
	             while(rs.next()){
	            	//print the details 	  
			   System.out.printf("%-10d",rs.getInt("CUST_ID"));
			   System.out.printf("%-10s",rs.getString("CUST_TYPE"));
			   System.out.printf("%-10s",rs.getString("CUST_NAME")); 
			   System.out.printf("%-15s",rs.getString("CUST_ADDRESS"));
			   System.out.printf("%-8d",rs.getInt("CUST_PH"));
			   System.out.printf("%-20s",rs.getString("CUST_EMAIL")); 
			   System.out.printf("%-8d",rs.getInt("CUST_LICENSE")); 
			   System.out.println();
	             }
	             
	          }     
	          catch(SQLException e ){
		
			 System.out.println("Details doesn't exist ");
		         System.out.println("Caught SQL exception: " +e);
	          }		
	}             
	
	public static void getProdDetails(){
		
	     String sql;
	    String sname="";
	    
		
	    try{
	            // accept date from user 
	             System.out.println("\n Enter the order date:(dd-MMM-yy)");
	             sc.nextLine();
	             sname = sc.nextLine();
	            
	             // Get all the customer details by order date  
	 			sql = "SELECT CUST_NAME,CUST_ADDRESS,CUST_PH,PROD_ID,B_ORDERDATE "
	 			      +"FROM BUY,CUSTOMER "
	 			      +"WHERE BUY.CUST_ID=CUSTOMER.CUST_ID " 
	 			      +"AND BUY.B_OrderDate='"+sname+"'"; 
	               
	            
		      // to execute select query  
	 		ResultSet rs = stmt.executeQuery(sql);
	 		System.out.println("CUST_NAME  CUST_ADDRESS  CUST_PH  PROD_ID     B_ORDERDATE");	  
	 		while(rs.next()){
			      
	 			
				   
	 	       //print the details  
		    	  System.out.printf("%-10s",rs.getString("CUST_NAME"));
		    	  System.out.printf("%-10s",rs.getString("CUST_ADDRESS"));
		    	  System.out.printf("%-10d",rs.getInt("CUST_PH"));
		    	  System.out.printf("%-10d",rs.getInt("PROD_ID"));
		    	  System.out.printf("%-15s",rs.getDate("B_ORDERDATE"));
		    	  System.out.println();
	 		}  	  
	    }     
	       catch(SQLException e ){
				 
		 System.out.println("Inside getProdDetails() ");
		 System.out.println("Caught SQL exception: " +e);
	       }
			
	
	}    
	
	// to close sql connection 	
	public static void closeConnection(){
	               
	         try{
	                conn.close();
	            }
	         catch(SQLException e ){
			
	        	 System.out.println("Inside closeConnection");
		         System.out.println("Caught SQL exception: " +e);
		
	         }

	}

	}// end-of-class




