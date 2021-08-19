import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListSubRedditComponent } from './list-sub-reddit.component';

describe('ListSubRedditComponent', () => {
  let component: ListSubRedditComponent;
  let fixture: ComponentFixture<ListSubRedditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListSubRedditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListSubRedditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
