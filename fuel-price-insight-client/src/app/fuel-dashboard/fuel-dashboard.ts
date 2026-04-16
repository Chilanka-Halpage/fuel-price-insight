import { CommonModule, DatePipe } from '@angular/common';
import { Component, effect, Signal, signal } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../services/auth-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelPriceService } from '../services/fuel-price-service';
import { Notification } from '../services/notification';

@Component({
  selector: 'app-fuel-dashboard',
  imports: [FormsModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatSelectModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIcon,
    DatePipe,
    CommonModule,
    MatTableModule],
  templateUrl: './fuel-dashboard.html',
  styleUrl: './fuel-dashboard.scss',
})
export class FuelDashboard {
  fuelTypes!: Signal<FuelType[]>;
  predictionTypes!: Signal<PredictionType[]>;

  selectedFuelSignal!: Signal<any>;
  selectedPredictionTypeSignal!: Signal<string | null>;

  selectedFuel = new FormControl('WTI_USD');
  selectedPredictionType = new FormControl('WEEK');

  comments = signal<any[]>([]);
  newComment = '';

  currentFromatedPrice = signal<string>('0.00');
  currentPrice = signal<number>(0.00);
  predictedPrice = signal<any>(null);

  priceHistory = signal<any[]>([]);
  displayedColumns: string[] = ['datetime', 'currency', 'price', 'type', 'unit'];

  username = signal<string>('');

  constructor(private authService: AuthService, private fuelPriceService: FuelPriceService, private notification: Notification) {
    this.init();
    this.setEffects();
  }


  ngOnInit() {
    this.loadComments();
  }

  init() {
    this.username.set(localStorage.getItem('username') || 'Guest');

    this.fuelTypes = signal<FuelType[]>([
      { code: 'WTI_USD', decription: 'West Texas Intermediate Crude Oil' },
      { code: 'BRENT_CRUDE_USD', decription: 'Brent Crude Oil' },
      { code: 'NATURAL_GAS_USD', decription: 'Henry Hub Natural Gas' },
      { code: 'DIESEL_USD', decription: 'Ultra Low Sulfur Diesel' },
      { code: 'HEATING_OIL_USD', decription: 'Heating Oil No. 2' },
      { code: 'JET_FUEL_USD', decription: 'Jet Fuel (Kerosene)' },
      { code: 'GASOLINE_USD', decription: 'RBOB Gasoline' },
      { code: 'COAL_USD', decription: '	Thermal Coal (Newcastle)' },
    ]);

    this.predictionTypes = signal<PredictionType[]>([
      { code: 'DAY', decription: 'Tommorrow' },
      { code: 'WEEK', decription: 'Next Week' },
      { code: 'TWO_WEEK', decription: 'Two Weeks' },
      { code: 'THREE_WEEK', decription: 'Three Weeks' },
      { code: 'MONTH', decription: 'Next Month' },
    ]);

    this.selectedFuelSignal = toSignal(this.selectedFuel.valueChanges, { initialValue: this.selectedFuel.value });
    this.selectedPredictionTypeSignal = toSignal(this.selectedPredictionType.valueChanges, { initialValue: this.selectedPredictionType.value });
  }

  setEffects() {
    effect(() => {
      this.getPrice(this.selectedFuelSignal());
      this.getPriceHistory(this.selectedFuelSignal());
      this.selectedPredictionType.setValue('WEEK');
    });

    effect(() => {
      this.getPrediction(this.selectedFuelSignal(), this.selectedPredictionTypeSignal());
    });
  }

  getPrice(fuelType: any) {
    try {
      this.fuelPriceService.getPrice(fuelType).subscribe({
        next: result => {
          if (result.success) {
            this.currentFromatedPrice.set(result.data.formatted);
            this.currentPrice.set(result.data.price);
          } else {
            console.error('Failed to retrieve price data:', result.error);
            this.notification.error(`Oops! We couldn't fetch the current price right now`);
            this.setDefaultPrice();
          }

        }, error: (error) => {
          console.error('Error retrieving price data:', error);
          this.notification.error(`Oops! We couldn't fetch the current price right now`);
          this.setDefaultPrice();
        }
      });
    } catch (error) {
      console.error('Error retrieving price data:', error);
      this.notification.error(`Oops! We couldn't fetch the current price right now`);
      this.setDefaultPrice();
    }
  }

  private setDefaultPrice() {
    this.currentFromatedPrice.set('0.00');
    this.currentPrice.set(0.00);
  }

  getPriceHistory(fuelType: any) {
    try {
      this.fuelPriceService.getPriceHistory(fuelType).subscribe({
        next: result => {
          if (result.success) {
            this.priceHistory.set(result.data);
          } else {
            console.error('Failed to retrieve price history data:', result.error);
            this.notification.error(`Oops! We couldn't fetch the price history right now`);
            this.setDefaultPriceHistory();
          }
        },
        error: (error) => {
          console.error('Error retrieving price history data:', error);
          this.notification.error(`Oops! We couldn't fetch the price history right now`);
          this.setDefaultPriceHistory();
        }
      });
    } catch (error) {
      console.error('Error retrieving price history data:', error);
      this.notification.error(`Oops! We couldn't fetch the price history right now`);
      this.setDefaultPriceHistory();
    }
  }

  private setDefaultPriceHistory() {
    this.priceHistory.set([]);
  }

  getPrediction(fuelType: any, predictionType: any) {
    try {
      this.fuelPriceService.getPrediction(fuelType, predictionType).subscribe({
        next: result => {
          if (result.success) {
            this.predictedPrice.set(result.data);
          } else {
            console.error('Failed to retrieve price prediction data:', result.error);
            this.notification.error(`Oops! We couldn't fetch the price prediction right now`);
            this.setDefaultPrediction();
          }

        }, error: (error) => {
          console.error('Error retrieving price prediction data:', error);
          this.notification.error(`Oops! We couldn't fetch the price prediction right now`);
          this.setDefaultPrediction();
        }
      })
    } catch (error) {
      console.error('Error retrieving price prediction data:', error);
      this.notification.error(`Oops! We couldn't fetch the price prediction right now`);
      this.setDefaultPrediction();
    }
  }

  private setDefaultPrediction() {
    this.predictedPrice.set(null);
  }

  loadComments() {
    try {
      this.fuelPriceService.getAllComments().subscribe({
        next: result => {
          if (result.success) {
            this.comments.set(result.data);
          } else {
            console.error('Failed to load comments:', result.error);
            this.notification.error(`Oops! We couldn't fetch the comments right now`);
          }
        }, error: (error) => {
          console.error('Error loading comments:', error);
          this.notification.error(`Oops! We couldn't fetch the comments right now`);
        }
      });
    } catch (error) {
      console.error('Error loading comments:', error);
      this.notification.error(`Oops! We couldn't fetch the comments right now`);
    }
  }

  addComment() {
    try {
      if (!this.newComment.trim()) return;
      this.fuelPriceService.addComment(this.newComment).subscribe({
        next: (reuslt: any) => {
          if (reuslt.success) {
            this.comments.update(comments => [{ userName: this.username(), description: this.newComment, lastUpdatedAt: new Date() }, ...comments ?? []]);
            this.notification.success('Comment posted successfully');
            this.newComment = '';
          } else {
            this.notification.error('Failed to post comment. Please try again.');
          }
        },
        error: (error) => {
          console.error('Error adding comment:', error);
          this.notification.error('Failed to post comment. Please try again.');
        }
      });
    } catch (error) {
      console.error('Error adding comment:', error);
      this.notification.error('Failed to post comment. Please try again.');
    }
  }

  getFuelName() {
    const fuel = this.fuelTypes().find(f => f.code === this.selectedFuelSignal());
    return fuel ? fuel.decription : this.selectedFuelSignal();
  }

  logout() {
    this.authService.logout();
  }

}