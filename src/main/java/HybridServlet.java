import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/HybridServlet", description = "search by hybrid")
public class HybridServlet extends HttpServlet {

    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, XPathExpressionException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        double min = Double.parseDouble(request.getParameter("min"));
        double max = Double.parseDouble(request.getParameter("max"));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile("//car[consumption >= " + min + " and consumption <= " + max + " and consumption[@type = 'hybrid']]/manufacturer | //car[consumption >= " + min + " and consumption <= " + max + " and consumption[@type = 'hybrid']]/model | //car[consumption >= " + min + " and consumption <= " + max + " and consumption[@type = 'hybrid']]/production-year | //car[consumption >= " + min + " and consumption <= " + max + " and consumption[@type = 'hybrid']]/horsepower | //car[consumption >= " + min + " and consumption <= " + max + " and consumption[@type = 'hybrid']]/consumption | //car[consumption >= " + min + " and consumption <= " + max + " and consumption[@type = 'hybrid']]/price");

        File file = new File("/Users/heavengriffin/Documents/Idea Projects/Cars/cars.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        Object object = xPathExpression.evaluate(inputSource, XPathConstants.NODESET);
        NodeList nodeList = (NodeList) object;

        if (nodeList.getLength() > 0) {
            out.println("<html><body>");
            out.println("<table>" +
                    "<tr>" +
                    "<th>Manufacturer</th>" +
                    "<th>Model</th>" +
                    "<th>Production year</th>" +
                    "<th>Horsepower</th>" +
                    "<th>Consumption</th>" +
                    "<th>Price</th>" +
                    "</tr>" +
                    "<tr>");

            for (int i = 0; i < nodeList.getLength(); i++) {
                out.println("<td>" + nodeList.item(i).getFirstChild().getNodeValue() + "</td>");
                if (nodeList.item(i).getNodeName().equals("price") && i < nodeList.getLength() - 1) {
                    out.println("</tr>" +
                            "<tr>");
                    i++;
                    out.println("<td>" + nodeList.item(i).getFirstChild().getNodeValue() + "</td>");
                }
                if (i == nodeList.getLength() - 1) {
                    out.println("</tr>" +
                            "</table>");
                    out.println("<br><br>");
                    out.println("<a href='search_by_consumption.html'>Back to search</a>");
                    out.println("<br><br>");
                    out.println("<a href='index.jsp'>Back to main menu</a>");
                    out.println("</body></html>");
                }

            }
        } else {
            out.println("<html><body>");
            out.println("<h3>There is no such car.</h3>");
            out.println("<a href='search_by_consumption.html'>Back to search</a>");
            out.println("<br><br>");
            out.println("<a href='index.jsp'>Back to main menu</a>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            process(req, resp);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            process(req, resp);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getServletInfo() {
        return super.getServletInfo();
    }
}
