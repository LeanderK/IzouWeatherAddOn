package leanderk.izou.weatheraddon;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.identification.IllegalIDException;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.resource.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Finds the woeid for a specific location.
 */
@SuppressWarnings("ALL")
public class WOEIDFinder {
    private static final String PERSONAL_INFORMATION_ID = "leanderk.izou.personalinformation.InformationCG";
    private static final String PERSONAL_INFORMATION_Resource_ID =
            "leanderk.izou.personalinformation.InformationCG.ResourceInfo";
    private Context context;

    public WOEIDFinder(Context context) {
        this.context = context;
    }

    public String getWOEIDLocation() {
        Resource resourceRequest = new Resource(PERSONAL_INFORMATION_Resource_ID);
        Optional<Identification> providerID = IdentificationManager.getInstance()
                .getIdentification(PERSONAL_INFORMATION_ID);
        if (providerID.isPresent())
            resourceRequest = resourceRequest.setProvider(providerID.get());

        final HashMap[] result = new HashMap[1];
        final Object lock = new Object();
        try {
            context.getResources().generateResource(resourceRequest)
                    .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                    .thenAccept(list -> list.stream()
                        .filter(resource -> resource.getResource() instanceof HashMap<?, ?>)
                        .map(resource -> (HashMap) resource.getResource())
                        .filter(hashMap -> hashMap.containsKey("postalcode") && hashMap.containsKey("country"))
                        .findFirst()
                        .ifPresent(hashMap -> {
                            result[0] = hashMap;
                            synchronized (lock) {
                                lock.notify();
                            }
                        })
                    );
        } catch (IllegalIDException e) {
            context.getLogger().error("IllegalId", e);
        }

        synchronized (lock) {
            try {lock.wait(20);} catch (Exception ignored) {}
        }
        if(result[0] == null) return null;
        return getWOEIDforLocation(result[0]);
    }

    public String getWOEIDforLocation(HashMap<String, String> location) {
        URL url;
        try {
            url = getYahooURL(location);
        } catch (MalformedURLException | URISyntaxException e) {
            context.getLogger().error("Unable to create WOEID-Query URL", e);
            return null;
        }
        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            context.getLogger().error("Unable to open WOEID-Query URL", e);
            return null;
        }

        try {
            return parseXML(connection.getInputStream());
        } catch (IOException e) {
            context.getLogger().error("Unable to get WOEID-Query Input-Stream", e);
            e.printStackTrace();
        }
        return null;
    }

    private String parseXML(InputStream inputStream) {
        DocumentBuilderFactory objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder objDocumentBuilder;
        Document doc;
        try {
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
            doc = objDocumentBuilder.parse(inputStream);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            context.getLogger().error("Error while trying to parse Yahoo's xml-response", e);
            try {
                inputStream.close();
            } catch (IOException e1) {
                context.getLogger().error(e1);
            }
            return null;
        }
        String result = doc.getElementsByTagName("woeid").item(0).getTextContent();
        try {
            inputStream.close();
        } catch (IOException e) {
            context.getLogger().error(e);
        }
        return result;
    }

    private URL getYahooURL(HashMap<String, String> location) throws MalformedURLException, URISyntaxException {
        return new URI("http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20geo.placefinder"
                + "%20where%20postal=%22"+ location.get("postalcode").toLowerCase()+ "%22%20and%20country=%22"
                + location.get("country").toLowerCase() +"%22&diagnostics=true").toURL();
    }
}
