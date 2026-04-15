import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { authConfig } from '../landing/auth.config';
import { OAuthService } from 'angular-oauth2-oidc';
import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private router: Router, private oauthService: OAuthService, private location: Location) { }

  async configureOAuth() {
    this.oauthService.configure(authConfig);
    await this.oauthService.loadDiscoveryDocumentAndTryLogin();

    if (this.hasValidAccessToken()) {
      const claims = this.oauthService.getIdentityClaims();
      const username = claims['preferred_username'].toUpperCase();
      localStorage.setItem('username', username);
      this.oauthService.setupAutomaticSilentRefresh();
      this.oauthService.events.subscribe((event) => {
        if (event.type === 'token_refresh_error') {
          this.logout();
        }
      });
      this.cleanUrl();
    }
  }

  cleanUrl() {
    this.location.replaceState(window.location.pathname);
  }

  login() {
    if (this.hasValidAccessToken()) {
      this.router.navigate(['/price']);
    } else {
      this.oauthService.initCodeFlow();
    }
  }

  logout() {
    localStorage.clear();
    this.oauthService.logOut();
  }

  signedUp() {
    const customParams = {
      prompt: 'create'
    };
    this.oauthService.initCodeFlow('', customParams);

  }

  hasValidAccessToken(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  refreshToken() {
    return this.oauthService.refreshToken();
  }
}
