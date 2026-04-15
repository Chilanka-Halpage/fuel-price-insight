import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-landing',
  imports: [MatToolbar, MatButton],
  templateUrl: './landing.html',
  styleUrl: './landing.scss',
})
export class Landing {
  constructor(private router: Router, private authService: AuthService) {}

  goToLogin() {
    this.authService.login();
  }

  goToSignup() {
    this.authService.signedUp();
  }
}