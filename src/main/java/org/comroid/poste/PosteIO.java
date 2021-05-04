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
import org.comroid.uniform.model.Serializable;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.Base64;
import org.comroid.varbind.DataContainerCache;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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

    public DataContainerCache<String, PosteEntity> getEntityCache() {
        return entityCache;
    }

    public PosteIO(String host, String username, String password) {
        this.context = CONTEXT.plus("PosteIO Wrapper - " + host, this);
        this.urlPrefix = "https://" + host + "/admin/api/v1";
        this.username = username;
        this.password = password;
        this.endpointLibrary = new EndpointLibrary(this);
        this.rest = context.getFromContext(REST.class).orElseGet(() -> new REST(context));
        this.entityCache = new DataContainerCache<>(context, 250, PosteEntity.REFERENCE_ID);
    }

    public CompletableFuture<List<Domain>> requestDomains() {
        return request(REST.Method.GET, EndpointScope.DOMAINS, obj -> obj.put("paging", Integer.MAX_VALUE), 200)
                .thenApply(response -> {
                    assert response.get("last_page").asInt(-1) == response.get("page").asInt(-2) : "too many pages";

                    List<Domain> yields = response.get("results").asArrayNode()
                            .stream()
                            .map(UniNode::asObjectNode)
                            .flatMap(data -> entityCache.autoUpdate(Domain::new, data).stream())
                            .collect(Collectors.toList());
                    return Collections.unmodifiableList(yields);
                });
    }

    public CompletableFuture<List<Inbox>> requestInboxes() {
        return request(REST.Method.GET, EndpointScope.MAILBOXES, obj -> obj.put("paging", Integer.MAX_VALUE), 200)
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

    public CompletableFuture<Inbox> createInbox(
            String emailAddress
    ) {
        return createInbox(null, emailAddress, null);
    }

    public CompletableFuture<Inbox> createInbox(
            @Nullable String name,
            String emailAddress,
            @Nullable String passwordPlaintext
    ) {
        return createInbox(name, emailAddress, passwordPlaintext, (String[]) null);
    }

    public CompletableFuture<Inbox> createInbox(
            @Nullable String name,
            String emailAddress,
            @Nullable String passwordPlaintext,
            @Nullable String... redirectTargets
    ) {
        return createInbox(name, emailAddress, passwordPlaintext, false, false, redirectTargets);
    }

    public CompletableFuture<Inbox> createInbox(
            @Nullable String name,
            String emailAddress,
            @Nullable String passwordPlaintext,
            boolean disabled,
            boolean superAdmin,
            @Nullable String... redirectTargets
    ) {
        return createInbox(name, emailAddress, passwordPlaintext, disabled, superAdmin, null, redirectTargets);
    }

    public CompletableFuture<Inbox> createInbox(
            @Nullable String name,
            String emailAddress,
            @Nullable String passwordPlaintext,
            boolean disabled,
            boolean superAdmin,
            @Nullable String referenceId,
            @Nullable String... redirectTargets
    ) {
        return request(REST.Method.POST, EndpointScope.MAILBOXES, obj -> {
            if (name != null) obj.put("name", name);
            obj.put("email", emailAddress);
            if (passwordPlaintext != null) obj.put("passwordPlaintext", passwordPlaintext);
            obj.put("disabled", disabled);
            obj.put("superAdmin", superAdmin);
            if (redirectTargets != null && redirectTargets.length != 0)
                obj.put("redirectTo", Arrays.asList(redirectTargets));
            if (referenceId != null)
                obj.put("referenceId", referenceId);
        }, 201).thenApply(inbox -> entityCache.autoUpdate(Inbox::new, inbox.asObjectNode()).get());
    }

    public CompletableFuture<Domain> requestDomain(String name) {
        return request(REST.Method.GET, EndpointScope.DOMAIN_ID, 200, name)
                .thenApply(data -> entityCache.autoUpdate(Domain::new, data.asObjectNode()).assertion("faulty domain response data"));
    }

    public CompletableFuture<Inbox> requestInbox(String email) {
        return request(REST.Method.GET, EndpointScope.MAILBOX_ID, 200, email)
                .thenApply(data -> entityCache.autoUpdate(Inbox::new, data.asObjectNode()).assertion("faulty inbox response data"));
    }

    @Internal
    public CompletableFuture<UniNode> request(REST.Method method, EndpointScope scope, int expectCode, Object... args) {
        return request(method, scope, null, expectCode, args);
    }

    @Internal
    public <K> CompletableFuture<UniNode> request(REST.Method method, EndpointScope scope, Consumer<UniObjectNode> bodyBuilder, int expectCode, Object... args) {
        return rest.request()
                .method(REST.Method.GET)
                .endpoint(endpointLibrary.getEndpoint(EndpointScope.MAILBOXES), args)
                .addHeader(CommonHeaderNames.AUTHORIZATION, getAuthorizationHeader())
                .buildBody(BodyBuilderType.OBJECT, bodyBuilder)
                .execute()
                .thenApply(response -> {
                    if (response.getStatusCode() == expectCode)
                        return response.getBody().into(Serializable::toUniNode);
                    throw response.toException();
                });
    }
}
