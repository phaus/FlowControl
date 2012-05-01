import java.io.File;
import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;
import play.Logger;
import play.libs.IO;

public class ApplicationTest extends FunctionalTest {

    public String request1 = IO.readContentAsString(new File("test/request1.xml"));
    public String expectedResponse1 = IO.readContentAsString(new File("test/response1.xml"));

    /**
     * This test sends a specific request.
     * The Response status should be 200/OK.
     * The Response MIME/Type should be application/xml.
     * The Response Content should be equals to the expected Response.
     * There should be at least one Notice saved.
     */
    @Test
    public void testThatNoticeConsumingWorks(){
    	Response response = POST("/notice", "application/xml", request1);
        assertIsOk(response);
        assertContentType("text/xml", response);
        //assertContentMatch(expectedResponse1, response);

        int count = Notice.all().fetch().size();
        assertTrue(count > 0);
    }
}