package org.ovirt.engine.ui.userportal.system;

import org.ovirt.engine.ui.common.auth.CurrentUser;
import org.ovirt.engine.ui.common.logging.ApplicationLogManager;
import org.ovirt.engine.ui.common.system.BaseApplicationInit;
import org.ovirt.engine.ui.common.system.LockInteractionManager;
import org.ovirt.engine.ui.common.uicommon.FrontendEventsHandlerImpl;
import org.ovirt.engine.ui.common.uicommon.FrontendFailureEventListener;
import org.ovirt.engine.ui.common.widget.AlertManager;
import org.ovirt.engine.ui.frontend.Frontend;
import org.ovirt.engine.ui.uicommonweb.ITypeResolver;
import org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalLoginModel;
import org.ovirt.engine.ui.userportal.ApplicationDynamicMessages;
import org.ovirt.engine.ui.userportal.auth.UserPortalCurrentUserRole;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ApplicationInit extends BaseApplicationInit<UserPortalLoginModel> {

    private final UserPortalCurrentUserRole userRole;
    private final ApplicationDynamicMessages dynamicMessages;

    @Inject
    public ApplicationInit(ITypeResolver typeResolver,
            FrontendEventsHandlerImpl frontendEventsHandler,
            FrontendFailureEventListener frontendFailureEventListener,
            CurrentUser user,
            Provider<UserPortalLoginModel> loginModelProvider,
            LockInteractionManager lockInteractionManager,
            Frontend frontend,
            ApplicationLogManager applicationLogManager,
            AlertManager alertManager,
            UserPortalCurrentUserRole userRole,
            ApplicationDynamicMessages dynamicMessages) {
        super(typeResolver, frontendEventsHandler, frontendFailureEventListener, user,
                loginModelProvider, lockInteractionManager, frontend, userRole,
                applicationLogManager, alertManager);
        this.userRole = userRole;
        this.dynamicMessages = dynamicMessages;
    }

    @Override
    protected void performBootstrap() {
        super.performBootstrap();
        Window.setTitle(dynamicMessages.applicationTitle());
    }

    @Override
    protected void beforeLogin() {
    }

    @Override
    protected boolean filterFrontendQueries() {
        return true;
    }

    @Override
    protected void initLoginModel() {
        super.initLoginModel();

        final UserPortalLoginModel loginModel = getLoginModel();

        // Login model "IsENGINEUser" property determines the availability
        // of the "Extended" main tab and starts the actual login operation
        loginModel.getIsENGINEUser().getPropertyChangedEvent().addListener((ev, sender, args) -> {
            Boolean isEngineUser = loginModel.getIsENGINEUser().getEntity();

            if (isEngineUser != null) {
                userRole.setEngineUser(isEngineUser);

                // Proceed with login operation
                performLogin(loginModel);
            }
        });
    }

    @Override
    protected void onLogin(UserPortalLoginModel loginModel) {
        // Instead of performing login now, request update for "IsENGINEUser" property
        loginModel.updateIsENGINEUser(loginModel.getLoggedUser());
    }

}
