import { TestBed } from '@angular/core/testing';

import { FuelPriceService } from './fuel-price-service';

describe('FuelPriceService', () => {
  let service: FuelPriceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuelPriceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
