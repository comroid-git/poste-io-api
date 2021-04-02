package org.comroid.poste;

import org.comroid.api.ContextualProvider;
import org.comroid.poste.rest.EndpointLibrary;

public final class PosteIO implements ContextualProvider.Underlying {
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
        this.urlPrefix = host + "/admin/api/v1/";
        this.endpointLibrary = new EndpointLibrary(this);
    }
}
