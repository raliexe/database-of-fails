import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FailsComponent } from './fails.component';

describe('FailsComponent', () => {
  let component: FailsComponent;
  let fixture: ComponentFixture<FailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FailsComponent]
    });
    fixture = TestBed.createComponent(FailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
