import { TestBed } from '@angular/core/testing';

import { TradeServiceService } from './trade-service.service';

describe('TradeServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TradeServiceService = TestBed.get(TradeServiceService);
    expect(service).toBeTruthy();
  });
});
