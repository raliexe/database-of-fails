import { TestBed } from '@angular/core/testing';

import { GeneralSearchService } from './general-search.service';

describe('GeneralSearchService', () => {
  let service: GeneralSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GeneralSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
