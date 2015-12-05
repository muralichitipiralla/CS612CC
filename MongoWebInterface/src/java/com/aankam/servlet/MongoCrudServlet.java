/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aankam.servlet;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aankam
 */
@WebServlet(name = "MongoCrudServlet", urlPatterns = {"/MongoCrudServlet"})
public class MongoCrudServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        MongoClientURI uri  = new MongoClientURI("mongodb://Username:Pasword@ds061721.mongolab.com:61721/cs612"); 
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB(uri.getDatabase());
        
        response.setContentType("text/html;charset=UTF-8");
        
        String serialNo = request.getParameter("serialNo");
        String name = request.getParameter("name");
        String emailId = request.getParameter("emailId");
        String operation = request.getParameter("operation");
        
        BasicDBObject customer;
        BasicDBObject updateQuery;
        BasicDBObject deleteQuery;
        BasicDBObject searchQuery;
        DBCollection customersCollection;
        customersCollection = db.getCollection("Customers");
        DBObject searchResult = null;
        boolean flag = false;
        DBCursor cursor;
        switch (operation){
            case "create":  
                customer = new BasicDBObject();
                customer.put("serialNo", serialNo);
                customer.put("name", name);
                customer.put("emailId", emailId);
                customersCollection.insert(customer);
                break;
            case "update":  
                updateQuery = new BasicDBObject();
                updateQuery.append("serialNo", serialNo);
                cursor =customersCollection.find(updateQuery);
                customer = new BasicDBObject();
                customer.put("serialNo", serialNo);
                customer.put("name", name);
                customer.put("emailId", emailId);
                if(cursor.hasNext()){
                    customersCollection.update(updateQuery, customer);
                    flag = true;
                }
                
                break;
            case "delete":  
                deleteQuery = new BasicDBObject();
                deleteQuery.append("serialNo", serialNo);
                customersCollection.remove(deleteQuery);
                break;
            case "search":  
                searchQuery = new BasicDBObject();
                searchQuery.append("serialNo", serialNo);
                cursor =customersCollection.find(searchQuery);
                while(cursor.hasNext()){
                    searchResult = cursor.next();
                }
                break;    
            default : break;
                
        }       
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MongoCrudServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            
            switch (operation){
                case "create":
                    out.println("<h3>Customer was successfully created.</h3>");
                    break;
                case "update":  
                    if(flag == true){
                        out.println("<h3>Customer was successfully updated.</h3>");
                    }else{
                        out.println("<h3>Customer was not found to update.</h3>");
                    }
                    
                    break;
                case "delete":  
                    out.println("<h3>Customer was successfully deleted.</h3>");
                    break;
                case "search":  
                    if(searchResult !=null){
                        out.println("<h3>The following customer was found:</h3>");
                        out.println("<h4>Serial No :" + searchResult.get("serialNo") + "</h4>");
                        out.println("<h4>Name :" + searchResult.get("name") + "</h4>");
                        out.println("<h4>Email Id :" + searchResult.get("emailId") + "</h4>");
                    }else{
                        out.println("<h3>Customer not found.</h3>");
                    }
                    break;    
                default : break;
            }
            out.println("<a href=\"index.html\">Back to Main Form</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
