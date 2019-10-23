import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.print.Doc;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Project SiteMap
 * Created by End on окт., 2019
 */
public class Main {
    public static void main(String[] args) {
        String my_site = "skillbox.ru";
        LinkReader obj = new LinkReader(my_site);
            obj.getLinks("https://skillbox.ru/", 0,"");
            obj.getDomenURL().forEach(System.out::println);
    }


    }


