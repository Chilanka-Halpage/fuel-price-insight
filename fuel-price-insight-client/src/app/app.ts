import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FuelDashboard } from "./fuel-dashboard/fuel-dashboard";
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('fuel-price-insight-client');

  // constructor(private authService: AuthService) {
  //   this.authService.configureOAuth();
  // }
}
