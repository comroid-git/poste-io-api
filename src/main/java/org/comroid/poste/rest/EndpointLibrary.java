package org.comroid.poste.rest;

import org.comroid.poste.PosteIO;
import org.comroid.restless.endpoint.AccessibleEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public final class EndpointLibrary {
    private final Map<EndpointScope, CombinedEndpoint> cache = new ConcurrentHashMap<>();
    private final PosteIO poste;

    public EndpointLibrary(PosteIO poste) {
        this.poste = poste;
    }

    public AccessibleEndpoint getEndpoint(EndpointScope scope) {
        return cache.computeIfAbsent(scope, CombinedEndpoint::new);
    }

    private final class CombinedEndpoint implements AccessibleEndpoint {
        private final EndpointScope scope;
        private final Pattern pattern;

        @Override
        public String getUrlBase() {
            return poste.getUrlPrefix();
        }

        @Override
        public String getUrlExtension() {
            return scope.getExtension();
        }

        @Override
        public String[] getRegExpGroups() {
            return scope.getRegExp();
        }

        @Override
        public Pattern getPattern() {
            return pattern;
        }

        private CombinedEndpoint(EndpointScope scope) {
            this.scope = scope;
            this.pattern = buildUrlPattern();
        }
    }
}
