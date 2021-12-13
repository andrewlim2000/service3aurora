package lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import saaf.Inspector;
import java.util.HashMap;
import java.util.List;

/**
 * 
 */
public class QueryAurora implements RequestHandler<Request, HashMap<String, Object>> {

    /**
     * Lambda Function Handler
     * 
     * @param request Request POJO with defined variables from Request.java
     * @param context 
     * @return HashMap that Lambda will automatically convert into JSON.
     */
    public HashMap<String, Object> handleRequest(Request request, Context context) {

        // Create logger
        LambdaLogger logger = context.getLogger();        

        //Collect inital data.
        Inspector inspector = new Inspector();
        //inspector.inspectAll();
        
        Response r = new Response();

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("db.properties"));
            
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");
            
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement(request.getQuery());
            
            LinkedList<String> ll = new LinkedList<>();
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                String row = "";
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    if (i == md.getColumnCount()) {
                        row += rs.getString(md.getColumnName(i));
                    } else {
                        row += rs.getString(md.getColumnName(i)) + ", ";
                    }
                }
                ll.add(row);
            }
            
            rs.close();
            con.close();
            
            r.setRows(ll);
        } 
        catch (Exception e) {
            logger.log("Got an exception working with MySQL! ");
            logger.log(e.getMessage());
        }
        
        inspector.consumeResponse(r);
        
        //Collect final information such as total runtime and cpu deltas.
        //inspector.inspectAllDeltas();
        return inspector.finish();
    }
}
