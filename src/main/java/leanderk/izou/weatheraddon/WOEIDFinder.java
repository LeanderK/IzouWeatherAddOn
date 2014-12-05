package leanderk.izou.weatheraddon;

import intellimate.izou.exampleaddon.ExampleContentGeneratorResource;
import intellimate.izou.resource.Resource;
import intellimate.izou.system.Context;
import intellimate.izou.system.Identification;
import intellimate.izou.system.IdentificationManager;

import java.util.HashMap;
import java.util.Optional;

/**
 * Finds the woeid for a specific location.
 */
public class WOEIDFinder {
    private static final String PERSONAL_INFORMATION_ID = "leanderk.izou.personalinformation.InformationCG";
    private static final String PERSONAL_INFORMATION_Resource_ID =
            "leanderk.izou.personalinformation.InformationCG.ResourceInfo";
    private static final String URL = "http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20geo.placefinder"
                                    + "%20where%20postal=%2276229%22%20and%20country=%22germany%22&diagnostics=true";
    private IdentificationManager identificationManager = IdentificationManager.getInstance();
    private Context context;

    public WOEIDFinder(Context context) {
        this.context = context;
    }

    public String getWOIDforPostalCode(String postalcode) {
        return null;
    }

    public String getWOEIDLocation() {
        Resource resourceRequest = new Resource(PERSONAL_INFORMATION_Resource_ID);
        Optional<Identification> providerID = identificationManager.getIdentification(ExampleContentGeneratorResource.ID);
        providerID.ifPresent(resourceRequest::setProvider);

        final String[] result = new String[1];
        context.resources.generateResource(resourceRequest, resourceList -> resourceList.stream()
                        .filter(resource -> resource.getResource() instanceof HashMap<?, ?>)
                        .map(resource -> (HashMap) resource.getResource())
                        .filter(hashMap -> hashMap.containsKey("postalcode"))
                        .findFirst()
                        .ifPresent(hashMap -> {
                            result[0] = getWOIDforPostalCode((String) hashMap.get("postalcode"));
                            result.notify();
                        })
        );

        try {result.wait(20);} catch (InterruptedException ignored) {}
        return result[0];
    }
}
