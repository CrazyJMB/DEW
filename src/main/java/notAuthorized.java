

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class notAuthorized
 */
public class notAuthorized extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private static final String preface = "<!DOCTYPE html>\n<html lang=\"es-es\">\n" +
	   		 "<head><meta charset=\"utf-8\" />" +
	   		 "<title>NOT AUTHORIZED</title>\n</head>\n<body>\n";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public notAuthorized() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(request.isUserInRole("rolalu")) {
			out.println(preface);
			out.println("<h1>You are not a professor! </h1><br>");
			out.println("Go to student page: <br><a href=\"alumno\">Here</a>");
			out.println("<body></html>");
		}
		else {
			out.println(preface);
        	out.println("<h1>You are not a student! </h1><br>");
        	out.println("Go to professor page: <br><a href=\"profesor\">Here</a>");
        	out.println("<body></html>");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
