import { TestBed } from '@angular/core/testing';

import { SubRedditService } from './sub-reddit.service';

describe('SubRedditService', () => {
  let service: SubRedditService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubRedditService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
