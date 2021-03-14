package com.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.usermanagement.bean.User;
import com.usermanagement.dao.UserDao;;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;    
	private UserDao userDao;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		userDao = new UserDao();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		switch(action){
		case "/new":
			showNewForm(request, response);
			break;
			
		case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		
		case "/delete":
			try {
				deleteUser(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} 
			break;
			
		case "/edit":
			try {
				showEditForm(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			break;
		
		case "/update":
			try {
				updateUser(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		default:
			try {
				listUser(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			break;
		}
		
	}
		
		private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
			dispatcher.forward(request, response);
		}
			
		private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String country = request.getParameter("country");
			
			User newUser = new User(name,email,country);
				userDao.insertQuery(newUser);
			response.sendRedirect("list");
		}
		
		private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
			int id = Integer.parseInt(request.getParameter("id"));
			
			try {
				userDao.deleteQuery(id);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			response.sendRedirect("list");
		}
		
		private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
			int id = Integer.parseInt(request.getParameter("id"));
			User existingUser;
			try {
				existingUser = userDao.selectByIdQuery(id);
				RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
				request.setAttribute("user", existingUser);
				dispatcher.forward(request, response);
			}
			catch(Exception e) {
				e.printStackTrace();
			}			
		}
		
		private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String country = request.getParameter("country");
			
			User user = new User(id,name,email,country);
			userDao.updateQuery(user);
			response.sendRedirect("list");					
		}
		
		//default call
		private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
			try {
				List<User> listUser = userDao.selectAllQuery();
				request.setAttribute("listUser", listUser);
				RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");				
				dispatcher.forward(request, response);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
}
