

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Servlet implementation class certificadoAlumno
 */
public class certificadoAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public certificadoAlumno() {
        super();
    }
    
    private static final String preface = "<!DOCTYPE html>\n<html lang=\"es-es\">\n" +
      		 "<head><meta charset=\"utf-8\" />" +
      		 "<title>Certificado</title>\n</head>\n<body>\n";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		String dni=(String)session.getAttribute("dni");
		String nombre=request.getParameter("nombre");
		String apellidos=request.getParameter("apellidos");
		String stringAsig=request.getParameter("asigJson");
		
		//falta la imagen
		out.println("<h1>DEW - Centro Educativo</h1>");
		out.println("<br>todo el resto...");
		out.println("<br>DEW - Centro Educativo certifica que " + nombre + " " + apellidos + ", con DNI " + dni + ", matriculado...");
		
		//faltan los acronimos
		JSONArray jsonAsig = null;
		try {
			jsonAsig = new JSONArray(stringAsig.toString());

			//we print the JSON objects
			for(int i=0; i<jsonAsig.length(); i++) {
				String asig=jsonAsig.getJSONObject(i).getString("asignatura");
				out.println("<br><h2>" + asig + "</h2>");
				String nota=jsonAsig.getJSONObject(i).getString("nota");
				if(nota=="")
					nota="Sin calificacion";
				out.println("<h3>Nota: " + nota + "</h3>");
			}
	
		}catch (JSONException e) {
				e.printStackTrace();
		}
		out.println("</body></html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
