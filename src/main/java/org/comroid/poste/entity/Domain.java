package org.comroid.poste.entity;

import org.comroid.api.ContextualProvider;
import org.comroid.api.EMailAddress;
import org.comroid.api.Named;
import org.comroid.mutatio.model.Ref;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class Domain extends PosteEntity implements Named {
    public static final GroupBind<Domain> Type
            = PosteEntity.Type.subGroup("domain");
    public static final VarBind<Domain, String, String, String> NAME
            = Type.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Domain, Boolean, Boolean, Boolean> IS_DOMAIN_BIN
            = Type.createBind("domain_bin")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Domain, String, EMailAddress, EMailAddress> DOMAIN_BIN_TARGET
            = Type.createBind("domain_bin_address")
            .extractAs(StandardValueType.STRING)
            .andRemap(EMailAddress::parse)
            .build();
    public static final VarBind<Domain, Boolean, Boolean, Boolean> IS_FORWARD
            = Type.createBind("forward")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Domain, String, String, String> FORWARD_DOMAIN
            = Type.createBind("forward_domain")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Domain, Boolean, Boolean, Boolean> IS_FORCE_ROUTE
            = Type.createBind("force_route")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<Domain, String, String, String> FORCE_ROUTE_HOST
            = Type.createBind("force_route_host")
            .extractAs(StandardValueType.STRING)
            .build();
    public final Ref<String> name = getComputedReference(NAME);
    public final Ref<Boolean> isDomainBin = getComputedReference(IS_DOMAIN_BIN);
    public final Ref<EMailAddress> domainBinTarget = getComputedReference(DOMAIN_BIN_TARGET);
    public final Ref<Boolean> isForward = getComputedReference(IS_FORWARD);
    public final Ref<String> forwardDomain = getComputedReference(FORWARD_DOMAIN);
    public final Ref<Boolean> isForceRoute = getComputedReference(IS_FORCE_ROUTE);
    public final Ref<String> forceRouteHost = getComputedReference(FORCE_ROUTE_HOST);

    @Override
    public String getName() {
        return name.assertion("Name");
    }

    public boolean isDomainBin() {
        return isDomainBin.orElse(false);
    }

    public @Nullable EMailAddress getDomainBinTarget() {
        return domainBinTarget.get();
    }

    public boolean isForward() {
        return isForward.orElse(false);
    }

    public @Nullable String getForwardDomain() {
        return forwardDomain.get();
    }

    public boolean isForceRoute() {
        return isForceRoute.orElse(false);
    }

    public @Nullable String getForceRouteHost() {
        return forceRouteHost.get();
    }

    public Domain(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
