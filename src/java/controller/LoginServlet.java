/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author Bob Naessens
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
 
    @PersistenceUnit
    //The emf corresponding to
    private EntityManagerFactory emf; 
    
    @Resource
    private UserTransaction utx;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;
        
        //Get the data from loginForm
        String email     = (String) request.getParameter("emailaddress");
        String password  = (String) request.getParameter("password");
        
        System.out.println(email);
            
        try {
          
                //Create a user instance
                User user = new User();

                em = emf.createEntityManager();

                Query q = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
                q.setParameter("email", email);
                
                try {
                    user = (User)q.getSingleResult();
                } catch (Exception e) {
                    e.printStackTrace();

                    // Redirect user to the index page
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
                
                if((user.getEmail() != null) && (password.equals(user.getPword()))) {
                    HttpSession session = request.getSession();
                    session.setAttribute("loggedInId", user.getId());
                    session.setAttribute("currentUsername", user.getFname());
                    
                    // Redirect the user to the index page
                    response.sendRedirect("index.jsp");
                } else {
                    
                    // Redirect user to the index page
                    //request.getRequestDispatcher("index.jsp").forward(request, response);
                    //Redirect to index
                    response.sendRedirect("index.jsp");
                }
            }
        
        catch(Exception e)
        {
            throw new ServletException(e);
        } finally {   
            if(em != null) {
                em.close();
            }
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
