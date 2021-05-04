package org.comroid.poste;

import org.comroid.api.ContextualProvider;
import org.comroid.mutatio.model.RefContainer;
import org.comroid.poste.entity.Domain;
import org.comroid.poste.entity.Inbox;
import org.comroid.poste.entity.PosteEntity;
import org.comroid.poste.rest.EndpointLibrary;
import org.comroid.poste.rest.EndpointScope;
import org.comroid.restless.CommonHeaderNames;
import org.comroid.restless.REST;
import org.comroid.restless.body.BodyBuilderType;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.Base64;
import org.comroid.varbind.DataContainerCache;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class PosteIO implements ContextualProvider.Underlying {
    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String DOMAIN_PATTERN = "\\w[\\w\\d]+\\.\\w+";
    public static ContextualProvider CONTEXT;
    private final ContextualProvider context;
    private final String urlPrefix;
    private final String username;
    private final String password;
    private final EndpointLibrary endpointLibrary;
    private final DataContainerCache<String, PosteEntity> entityCache;
    private final REST rest;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return context;
    }

    public RefContainer<String, Domain> getDomains() {
        return entityCache.flatMap(Domain.class);
    }

    public RefContainer<String, Inbox> getInboxes() {
        return entityCache.flatMap(Inbox.class);
    }

    private String getAuthorizationHeader() {
        return Base64.encode("Basic " + username + ':' + password);
    }

    public PosteIO(ContextualProvider context, String host, String username, String password) {
        this.context = context.plus("PosteIO Wrapper - " + host, this);
        this.urlPrefix = "https://" + host + "/admin/api/v1";
        this.username = username;
        this.password = password;
        this.endpointLibrary = new EndpointLibrary(this);
        this.rest = context.getFromContext(REST.class).orElseGet(() -> new REST(context));
        this.entityCache = new DataContainerCache<>(context, 250, PosteEntity.REFERENCE_ID);
    }

    public CompletableFuture<List<Inbox>> requestInboxes() {
        return request(REST.Method.GET, EndpointScope.MAILBOXES, obj -> obj.put("paging", Integer.MAX_VALUE))
                .thenApply(response -> {
                    assert response.get("last_page").asInt(-1) == response.get("page").asInt(-2) : "too many pages";

                    List<Inbox> yields = response.get("results").asArrayNode()
                            .stream()
                            .map(UniNode::asObjectNode)
                            .flatMap(data -> entityCache.autoUpdate(Inbox::new, data).stream())
                            .collect(Collectors.toList());
                    return Collections.unmodifiableList(yields);
                });
    }

    private CompletableFuture<UniNode> request(REST.Method method, EndpointScope scope, Object... args) {
        return request(method, scope, null, args);
    }

    private CompletableFuture<UniNode> request(REST.Method method, EndpointScope scope, Consumer<UniObjectNode> bodyBuilder, Object... args) {
        return rest.request()
                .method(REST.Method.GET)
                .endpoint(endpointLibrary.getEndpoint(EndpointScope.MAILBOXES), args)
                .addHeader(CommonHeaderNames.AUTHORIZATION, getAuthorizationHeader())
                .buildBody(BodyBuilderType.OBJECT, bodyBuilder)
                .execute$deserializeSingle();
    }
}
