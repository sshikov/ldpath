package at.newmedialab.ldpath.model.transformers;

import at.newmedialab.lmf.search.rdfpath.model.selectors.PropertySelector;
import kiwi.core.api.triplestore.ResourceService;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.Constants;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiUriResource;
import kiwi.core.util.KiWiContext;
import org.apache.solr.schema.LatLonType;

import java.util.NoSuchElementException;

/**
 * Transforms a node into the external value for {@link LatLonType}.
 * 
 * @author Jakob Frank <jakob.frank@salzburgresearch.at>
 * 
 */
public class LatLonTransformer implements NodeTransformer<String> {

    private static final String DEFAULT_LON_PROPERTY_URI = Constants.NS_GEO + "long",
            DEFAULT_LAT_PROPERTY_URI = Constants.NS_GEO + "lat";

    private static final StringTransformer stringer = new StringTransformer();

    final private String latPropertyUri, lonPropertyUri;
    private PropertySelector latProperty = null, lonProperty = null;

    public LatLonTransformer(String latUri, String lonUri) {
        latPropertyUri = latUri;
        lonPropertyUri = lonUri;
    }

    public LatLonTransformer(KiWiUriResource latProp, KiWiUriResource lonProp) {
        this(latProp.getUri(), lonProp.getUri());
        latProperty = new PropertySelector(latProp);
        lonProperty = new PropertySelector(lonProp);
    }

    public LatLonTransformer() {
        this(DEFAULT_LAT_PROPERTY_URI, DEFAULT_LON_PROPERTY_URI);
    }

    /**
     * Transform the node into the external value for {@link LatLonType}. This is done by retrieving
     * the lat/lon properties of the node from the tripleStore.
     * 
     * @return The external representation of a LatLon position.
     * @see LatLonType
     */
    @Override
    public String transform(KiWiNode node, TripleStore tripleStore) throws IllegalArgumentException {
        if (latProperty == null || lonProperty == null) {
            ResourceService rs = KiWiContext.getInstance(ResourceService.class);

            final KiWiUriResource latUR = rs.getUriResource(latPropertyUri);
            final KiWiUriResource lonUR = rs.getUriResource(lonPropertyUri);
            if (latUR != null && lonUR != null) {
                latProperty = new PropertySelector(latUR);
                lonProperty = new PropertySelector(lonUR);
            }
        }
        if (latProperty == null || lonProperty == null) { throw new IllegalArgumentException("lat/lon properties not found in triplestore."); }

        try {
            KiWiNode lat = latProperty.select(tripleStore, node).iterator().next(), lon = lonProperty.select(tripleStore, node).iterator().next();

            // Using a StringTransformer here to avoid loosing precision.
            String latStr = stringer.transform(lat, tripleStore), lonStr = stringer.transform(lon, tripleStore);
            // Check for valid doubles
            Double.parseDouble(latStr);
            Double.parseDouble(lonStr);

            // That's the externalVal of LatLonType
            return latStr + "," + lonStr;
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("cannot transform node without lat/lon property to LatLonType");
        }
    }

}
