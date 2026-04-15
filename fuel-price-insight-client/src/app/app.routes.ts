import { Routes } from '@angular/router';
import { authGuard } from './guard/auth-guard';

export const routes: Routes = [
    { path: '', loadComponent: () => import('./landing/landing').then(m => m.Landing) },
    { path: 'price', loadComponent: () => import('./fuel-dashboard/fuel-dashboard').then(m => m.FuelDashboard), },
];
