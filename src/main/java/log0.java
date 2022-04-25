import java.io.*;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class log0
 */
public class log0 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public log0() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<html><head><title>Info</title></head><body><table>");
		out.println("<tr><td colspan=2><b>Info Path</b></td><tr>");
		out.println("<tr><td>Request URL</td><td>"+request.getRequestURL()+"</td><tr>");
		out.println("<tr><td>Context Path</td><td>"+request.getContextPath()+"</td><tr>");out.println("<tr><td>Servlet Path</td><td>"+request.getServletPath()+"</td><tr>");out.println("<tr><td>Path Info</td><td>"+request.getPathInfo()+"</td><tr>");
		out.println("<tr><td>Query String</td><td>"+request.getQueryString()+"</td><tr>");out.println("<tr><td colspan=2><b>Protocolo HTTP</b></td><tr>");
		out.println("<tr><td>Metodo</td><td>"+request.getMethod()+"</td><tr>");
		out.println("<tr><td>Remote Addr</td><td>"+request.getRemoteAddr()+"</td><tr>");
		out.println("<tr><td>Remote Host</td><td>"+request.getRemoteHost()+"</td><tr>");
		out.println("<tr><td>Remote Port</td><td>"+request.getRemotePort()+"</td><tr>");
		out.println("<tr><td colspan=2><b>Cabeceras HTTP</b></td><tr>");
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			out.println("<tr><td>"+name+"</td><td>"+request.getHeader(name)+"</td><tr>");
		}
		out.println("<tr><td colspan=2><b>Parametros</b></td><tr>");
		names = request.getParameterNames();
		
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			out.println("<tr><td>"+name+"</td><td>"+request.getParameter(name)+"</td><tr>");
		}
		out.println("</table></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}