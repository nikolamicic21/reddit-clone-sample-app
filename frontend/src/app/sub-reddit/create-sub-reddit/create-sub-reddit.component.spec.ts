import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSubRedditComponent } from './create-sub-reddit.component';

describe('CreateSubRedditComponent', () => {
  let component: CreateSubRedditComponent;
  let fixture: ComponentFixture<CreateSubRedditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateSubRedditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateSubRedditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
