

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;


public class detallesProfesor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String preface = "<!DOCTYPE html>\n"
			+ "<html lang=\"es\">\n"
			+ "\n"
			+ "<head>\n"
			+ "    <title>Notas OnLine</title>\n"
			+ "    <meta charset=\"utf-8\">\n"
			+ "    <meta grupo=\"DEW G4\">\n"
			+ "    <link rel=\"stylesheet\" href=\"estilo.css\">\n"
			+ "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\"\n"
			+ "        integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">\n"
			+ "</head>\n"
			+ "\n"
			+ "<body>\n"
			+ "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"\n"
			+ "        integrity=\"sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=\" crossorigin=\"anonymous\"></script>\n"
			+ "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js\"\n"
			+ "        integrity=\"sha384-w1Q4orYjBQndcko6MimVbzY0tgp4pWB4lZ7lr30WKz0vr/aWKhXdBNmNb5D92v7s\"\n"
			+ "        crossorigin=\"anonymous\"></script>\n"
			+ "\n"
			+ "    <img src=\"https://a-static.besthdwallpaper.com/chica-lofi-estudiando-papel-pintado-3200x900-81037_74.jpg\"\n"
			+ "        style=\"width: 100%; max-height: 190px;\" alt=\"chicaEstudiando\">\n"
			+ "    <div class=\"texto\">\n"
			+ "        <h3>Cambia la nota del alumno</h3>\n"
			+ "    </div>\n"
			+ "    <div style=\"display: flex;\">\n"
			+ "        <div class=\"grupo\">\n"
			+ "            <h6 id=\"titulo\"><strong>Creado por el grupo G4</strong></h6>\n"
			+ "            <ul class=\"list-group list-group-flush\" id=\"listaGrupo\">\n"
			+ "                <li class=\"list-group-item\">David Sanfélix Enguídanos</li>\n"
			+ "                <li class=\"list-group-item\">Jorge Martín Barreto</li>\n"
			+ "                <li class=\"list-group-item\">Alejandro Duque Segura</li>\n"
			+ "                <li class=\"list-group-item\">Vicent Ivorra Espasa</li>\n"
			+ "                <li class=\"list-group-item\">Davide Romano</li>\n"
			+ "            </ul>\n"
			+ "            <button type=\"button\" class=\"btn btn-danger\" style=\"margin-top:10px;background-color:brown;\"><a style=\"text-decoration:none; color:white;\" href=\"/CentroEducativo/logout\"> <i class=\"bi bi-door-open\"></i> Cerrar Sesión</a></button>   \n"
			+ "        </div>\n"
			+ "            <div class=\"card mr-5\" style=\"width: 18rem; margin-top:100px; margin-left:300px;\">\n"
			+ "            	<img class=\"card-img-top\" src=\"https://us.123rf.com/450wm/yupiramos/yupiramos1711/yupiramos171106114/89548810-dise%C3%B1o-de-ilustraci%C3%B3n-de-vector-de-personajes-de-avatar-de-ni%C3%B1o-peque%C3%B1o.jpg?ver=6\" alt=\"Alumno\" style=\"border-radius: 50%; width: 290px; height: 300px;\">\n"
			+ "	            <div class=\"card-body\">\n";
	
    public detallesProfesor() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//get session attributes
		HttpSession session = request.getSession(true);
		String key=(String)session.getAttribute("key");
		String dni=(String)session.getAttribute("dni");
		List<HttpCookie> cookies=(List<HttpCookie>)session.getAttribute("cookie");
		
		String dniAlu = request.getParameter("dni");
		String nota = request.getParameter("nota");
		
		//I create a cookieManager and I add the cookie saved in the session
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		cookieManager.getCookieStore().add(null, cookies.get(0));
		
		//First connection: get the name and surname of the student
		String RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/alumnos/" + dniAlu + "?key=" + key;
		HttpURLConnection con = (HttpURLConnection) new URL(RequestURL).openConnection();

        //add reuqest header
        con.setRequestMethod("GET");

        // Send get request
        con.setDoInput(true);

        int responseCode = con.getResponseCode();
        
        StringBuilder answer = new StringBuilder();
        if(responseCode>299) {
        	out.println(preface);
        	out.println("<br>CODIGO SERVLET alumno<br>");
            System.out.println("\nSending 'GET' request to URL : " + RequestURL);
    		out.println("dni" + session.getAttribute("dni"));
    		out.println("password" + session.getAttribute("password"));
    		out.println("key" + session.getAttribute("key"));
    		out.println("<br>Something is wrong! Error code:" + responseCode);
        	out.println("</body></html>");
        }
        else {
        	try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                
                while ((line = in.readLine()) != null) {
                    answer.append(line);
                }
                
        	}
        }
        
        out.println(preface);
        
        //I have the answer in string format so we save it into a JSON object
        JSONObject json;
        String nombre = "";
        String apellidos = "";
		try {
			json = new JSONObject(answer.toString());
			nombre = json.getString("nombre");
	        apellidos = json.getString("apellidos");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
		out.println("<h5 class=\"card-title\">" + nombre + " " + apellidos + "</h5>");
		out.println("<p class=\"card-text\">" + dniAlu + "</p>");
		out.println("</div>\n"
				+ "            </div>\n"
				+ "            <form style=\"margin-top:200px;\">");
		out.println("<h3>Nota actual del alumno: " + nota + "</h3>");
		out.println("<div class=\"input-group mb-3\" style=\"margin-top: 50px;\">\n"
				+ "				  <input type=\"text\" class=\"form-control\" placeholder=\"nota\" aria-label=\"nota\" aria-describedby=\"calificar\">\n"
				+ "				  <div class=\"input-group-append\">\n"
				+ "				    <button class=\"btn btn-outline-danger\" type=\"button\" id=\"calificar\">Calificar</button>\n"
				+ "				  </div>\n"
				+ "				</div>\n"
				+ "            </form>\n"
				+ "    </div>\n"
				+ "\n"
				+ "\n"
				+ "</body>\n"
				+ "\n"
				+ "</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
