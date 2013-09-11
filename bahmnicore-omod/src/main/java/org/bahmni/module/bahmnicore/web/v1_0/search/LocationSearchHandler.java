package org.bahmni.module.bahmnicore.web.v1_0.search;

import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class LocationSearchHandler  implements SearchHandler{

    private LocationService locationService;

    @Autowired
    public LocationSearchHandler(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public SearchConfig getSearchConfig() {
        return new SearchConfig("byTags", RestConstants.VERSION_1 + "/location", Arrays.asList("1.9.*"),
                new SearchQuery.Builder("Allows you to find locations by tags attached to the location").withRequiredParameters("tags").build());

    }

    @Override
    public PageableResult search(RequestContext requestContext) throws ResponseException {
        String query = requestContext.getParameter("q");
        List<LocationTag> tags = new ArrayList<>();
        for(String tag : query.split(",")){
           tags.add(locationService.getLocationTagByName(tag));
        }

        List<Location> locations =  locationService.getLocationsHavingAllTags(tags);
        return new AlreadyPaged<>(requestContext, locations, false);
    }
}


