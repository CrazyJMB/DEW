

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

/**
 * Servlet implementation class detallesAlumno
 */
public class detallesAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public detallesAlumno() {
        super();
        // TODO Auto-generated constructor stub
    }

    private static final String preface = "<!DOCTYPE html>\n"
    		+ "<html lang=\"es\">\n"
    		+ "\n"
    		+ "<head>\n"
    		+ "    <title>Notas</title>\n"
    		+ "    <meta charset=\"utf-8\">\n"
    		+ "    <meta grupo=\"DEW G4\">\n"
    		+ "    <link rel=\"stylesheet\" href=\"estilo.css\">\n"
    		+ "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\"\n"
    		+ "        integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">\n"
    		+ "        <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.7.0/css/all.css\" integrity=\"sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ\" crossorigin=\"anonymous\">\n"
    		+ "\n"
    		+ "    </head>\n"
    		+ "\n"
    		+ "<body>\n"
    		+ "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"\n"
    		+ "        integrity=\"sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=\" crossorigin=\"anonymous\"></script>\n"
    		+ "    \n"
    		+ "\n"
    		+ "    <img src=\"https://a-static.besthdwallpaper.com/chica-lofi-estudiando-papel-pintado-3200x900-81037_74.jpg\"\n"
    		+ "        style=\"width: 100%; max-height: 190px;\" alt=\"chicaEstudiando\">\n"
    		+ "        <div class=\"container-fluid\" style=\"margin-left: 380px;\" >";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		String key=(String)session.getAttribute("key");
		String dni=(String)session.getAttribute("dni");
		List<HttpCookie> cookies=(List<HttpCookie>)session.getAttribute("cookie");
		
		//I create a cookieManager and I add the cookie saved in the session
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		cookieManager.getCookieStore().add(null, cookies.get(0));
		
		//I DONT KNOW HOW TO PASS THE ACRONIMO OF THE ASIGNATURA
		String reqURL=request.getRequestURI();
		String asig=reqURL.substring(reqURL.lastIndexOf("/") + 1);
		
		//First connection: get the name and surname of the student
		String RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/asignaturas/" + asig + "?key=" + key;
		HttpURLConnection con = (HttpURLConnection) new URL(RequestURL).openConnection();
		
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
        
        //I have the answer in string format so we save it into a JSON object
        JSONObject json;
        String acronimo = "";
        String creditos = "";
        String cuatrimestre = "";
        String curso = "";
        String nombre = "";
		try {
			json = new JSONObject(answer.toString());
			nombre = json.getString("nombre");
			acronimo = json.getString("acronimo");
			creditos = json.get("creditos").toString();
			cuatrimestre = json.get("cuatrimestre").toString();
			curso = json.get("curso").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		out.println(preface);
		out.println("<h1 class=\"display-3\" style=\"color: brown;\">" + acronimo +  " detalles</h1>");
		out.println("<h3 class=\"font-weight-bolder\">Acronimo: <mark>" + acronimo + "</mark></h3>\n"
				+ "            <h3 class=\"font-weight-bolder\">Creditos: <mark> " + creditos + "</mark></h3> \n"
				+ "            <h3 class=\"font-weight-bolder\">Cuatrimestre: <mark>" + cuatrimestre + "</mark></h3>\n"
				+ "            <h3 class=\"font-weight-bolder\">Curso: <mark>" + curso + "</mark></h3>\n"
				+ "            <h3 class=\"font-weight-bolder\">Nombre: <mark>" + nombre + "</mark></h3>");
		out.println("<div style=\"margin-right: 180px;\">\n"
				+ "                <button type=\"submit\" class=\"btn btn-primary\" style=\"background-color: brown;\" >Volver</button>\n"
				+ "            </div>          \n"
				+ "          </div>\n"
				+ "        \n"
				+ "        \n"
				+ "\n"
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
				+ "        </div>\n"
				+ "\n"
				+ "        \n"
				+ "    </div>\n"
				+ "            \n"
				+ "    </div>\n"
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
