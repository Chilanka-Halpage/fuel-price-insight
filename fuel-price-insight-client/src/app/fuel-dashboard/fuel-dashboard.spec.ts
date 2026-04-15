import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelDashboard } from './fuel-dashboard';

describe('FuelDashboard', () => {
  let component: FuelDashboard;
  let fixture: ComponentFixture<FuelDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuelDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
