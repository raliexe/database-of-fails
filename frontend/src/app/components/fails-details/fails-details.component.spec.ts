import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FailsDetailsComponent } from './fails-details.component';

describe('FailsDetailsComponent', () => {
  let component: FailsDetailsComponent;
  let fixture: ComponentFixture<FailsDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FailsDetailsComponent]
    });
    fixture = TestBed.createComponent(FailsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
