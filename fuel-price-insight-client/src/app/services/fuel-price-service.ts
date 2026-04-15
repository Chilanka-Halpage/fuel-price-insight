import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class FuelPriceService {
  API_BASE_URL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getPrice(fuelType: string) {
    return this.http.get<any>(`${this.API_BASE_URL}/fuel/price?code=${fuelType}`);
  }

  getPriceHistory(fuelType: string) {
    return this.http.get<any>(`${this.API_BASE_URL}/fuel/price-history?code=${fuelType}`);
  }

  getPrediction(fuelType: string, predictionType: string) {
    return this.http.get<any>(`${this.API_BASE_URL}/fuel/price-prediction/${fuelType}/${predictionType}`);
  }

  getAllComments() {
    return this.http.get<any>(`${this.API_BASE_URL}/price-comments`);
  }

  addComment(comment: string) {
    const post = { description: comment }; ``
    return this.http.post(`${this.API_BASE_URL}/price-comments`, post);
  }
}
