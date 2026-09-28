import { TestBed } from '@angular/core/testing';

import { FailsService } from './fails.service';

describe('FailsService', () => {
  let service: FailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
