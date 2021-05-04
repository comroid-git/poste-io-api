package org.comroid.poste.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.common.io.FileHandle;
import org.comroid.mail.EMailAddress;
import org.comroid.mutatio.model.Ref;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class Inbox extends PosteEntity {
    public static final GroupBind<Inbox> Type
            = PosteEntity.Type.subGroup("inbox");
    public static final VarBind<Inbox, String, EMailAddress, EMailAddress> ADDRESS
            = Type.createBind("address")
            .extractAs(StandardValueType.STRING)
            .andRemap(EMailAddress::parse)
            .build();
    public static final VarBind<Inbox, String, String, String> USER
            = Type.createBind("user")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, String, String> DOMAIN
            = Type.createBind("domain")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, String, String> PASSWORD
            = Type.createBind("password")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, String, String> PASSWORD_PLAINTEXT
            = Type.createBind("passwordPlaintext")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, String, FileHandle, FileHandle> HOME
            = Type.createBind("home")
            .extractAs(StandardValueType.STRING)
            .andRemap(FileHandle::new)
            .build();
    public static final VarBind<Inbox, String, Instant, Instant> CREATED
            = Type.createBind("created")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Inbox, String, Instant, Instant> UPDATED
            = Type.createBind("updated")
            .extractAs(StandardValueType.STRING)
            .andRemap(Instant::parse)
            .build();
    public static final VarBind<Inbox, String, String, String> NAME
            = Type.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_DISABLED
            = Type.createBind("disabled")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_DOMAIN_ADMIN
            = Type.createBind("domain_admin")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_SUPER_ADMIN
            = Type.createBind("super_admin")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_STRICT_FROM_HEADER_DISABLED
            = Type.createBind("strict_from_disabled")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_REDIRECT_ONLY
            = Type.createBind("redirect_only")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Inbox, String, EMailAddress, List<EMailAddress>> REDIRECT_TO
            = Type.createBind("redirect_to")
            .extractAsArray(StandardValueType.STRING)
            .andRemap(EMailAddress::parse)
            .intoCollection((Supplier<List<EMailAddress>>) ArrayList::new)
            .build();
    public static final VarBind<Inbox, Boolean, Boolean, Boolean> IS_DISCARD
            = Type.createBind("discard")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public final Ref<EMailAddress> address = getComputedReference(ADDRESS);
    public final Ref<String> user = getComputedReference(USER);
    public final Ref<String> domain = getComputedReference(DOMAIN);
    public final Ref<String> password = getComputedReference(PASSWORD);
    public final Ref<String> passwordPlaintext = getComputedReference(PASSWORD_PLAINTEXT);
    public final Ref<FileHandle> home = getComputedReference(HOME);
    public final Ref<Instant> created = getComputedReference(CREATED);
    public final Ref<Instant> updated = getComputedReference(UPDATED);
    public final Ref<String> accountName = getComputedReference(NAME);
    public final Ref<Boolean> isDisabled = getComputedReference(IS_DISABLED);
    public final Ref<Boolean> isDomainAdmin = getComputedReference(IS_DOMAIN_ADMIN);
    public final Ref<Boolean> isSuperAdmin = getComputedReference(IS_SUPER_ADMIN);
    public final Ref<Boolean> isStrictFromDisabled = getComputedReference(IS_STRICT_FROM_HEADER_DISABLED);
    public final Ref<Boolean> isRedirectOnly = getComputedReference(IS_REDIRECT_ONLY);
    public final Ref<List<EMailAddress>> redirectTo = getComputedReference(REDIRECT_TO);
    public final Ref<Boolean> isDiscard = getComputedReference(IS_DISCARD);

    public EMailAddress getEmailAddress() {
        return address.assertion("Email Address");
    }

    public String getUserPart() {
        return user.orElseGet(getEmailAddress()::getUser);
    }

    public @Nullable String getDomain() {
        return domain.get();
    }

    public @Nullable String getPassword() {
        return password.get();
    }

    public @Nullable String getPasswordPlaintext() {
        return passwordPlaintext.get();
    }

    public Instant getCreatedTimestamp() {
        return created.assertion("Creation Timestamp");
    }

    public @Nullable Instant getLastUpdatedTimestamp() {
        return updated.get();
    }

    public @Nullable String getAccountName() {
        return accountName.get();
    }

    public boolean isDisabled() {
        return isDisabled.orElse(false);
    }

    public boolean isDomainAdmin() {
        return isDomainAdmin.orElse(false);
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin.orElse(false);
    }

    public boolean isStrictFromDisabled() {
        return isStrictFromDisabled.orElse(false);
    }

    public boolean isRedirectOnly() {
        return isRedirectOnly.orElse(false);
    }

    public List<EMailAddress> getRedirectTargets() {
        return redirectTo.orElseGet(Collections::emptyList);
    }

    public boolean isDiscard() {
        return isDiscard.orElse(false);
    }

    public Inbox(ContextualProvider context, UniObjectNode initialData) {
        super(context, initialData);
    }
}
