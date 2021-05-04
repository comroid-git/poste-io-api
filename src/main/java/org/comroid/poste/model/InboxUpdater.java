package org.comroid.poste.model;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Updater;
import org.comroid.poste.PosteIO;
import org.comroid.poste.entity.Inbox;
import org.comroid.poste.rest.EndpointScope;
import org.comroid.restless.REST;

import java.util.concurrent.CompletableFuture;

public final class InboxUpdater implements Updater<Inbox> {
    private final ContextualProvider context;
    private final CharSequence email;
    private String name;
    private String password;
    private boolean disabled;
    private boolean superAdmin;
    private String referenceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public InboxUpdater(Inbox inbox) {
        this(inbox.getParentContext(), inbox.getEmailAddress());

        this.disabled = inbox.isDisabled();
        this.superAdmin = inbox.isSuperAdmin();
    }

    public InboxUpdater(ContextualProvider context, CharSequence email) {
        this.context = context;
        this.email = email;
    }

    public void setEnabled() {
        setDisabled(false);
    }

    public void setDisabled() {
        setDisabled(true);
    }

    @Override
    public CompletableFuture<Inbox> update() {
        PosteIO posteIO = context.requireFromContext(PosteIO.class);
        return posteIO.request(REST.Method.PATCH, EndpointScope.MAILBOX_ID, obj -> {
            if (name != null) obj.put("name", name);
            if (password != null) obj.put("passwordPlaintext", password);
            obj.put("disabled", disabled);
            obj.put("superAdmin", superAdmin);
            if (referenceId != null) obj.put("referenceId", referenceId);
        }, 204, email)
                .thenApply(data -> posteIO.getEntityCache()
                        .autoUpdate(Inbox::new, data.asObjectNode())
                        .assertion("faulty response"));
    }

}
