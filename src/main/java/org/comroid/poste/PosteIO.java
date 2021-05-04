package org.comroid.poste;

import org.comroid.api.ContextualProvider;
import org.comroid.poste.rest.EndpointLibrary;
import org.comroid.poste.rest.EndpointScope;
import org.comroid.restless.endpoint.AccessibleEndpoint;

public final class PosteIO implements ContextualProvider.Underlying {
    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String DOMAIN_PATTERN = "\\w[\\w\\d]+\\.\\w+";
    private final ContextualProvider context;
    private final String urlPrefix;
    private final EndpointLibrary endpointLibrary;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context;
    }

    public PosteIO(ContextualProvider context, String host) {
        this.context = context.plus("PosteIO Wrapper - " + host, this);
        this.urlPrefix = host + "/admin/api/v1";
        this.endpointLibrary = new EndpointLibrary(this);
    }

    public AccessibleEndpoint getEndpoint(EndpointScope scope) {
        return endpointLibrary.getEndpoint(scope);
    }
}
