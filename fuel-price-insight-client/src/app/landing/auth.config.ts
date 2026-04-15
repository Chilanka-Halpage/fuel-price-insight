import { AuthConfig } from "angular-oauth2-oidc";

export const authConfig: AuthConfig = {
    issuer: 'http://localhost:9090/realms/csh-apps',
    redirectUri: `${window.location.origin}/price`,
    clientId: 'fuel-price-insight',
    responseType: 'code',
    scope: 'openid profile email',
    showDebugInformation: true,
    postLogoutRedirectUri: `${window.location.origin}`,
    useSilentRefresh: false
};