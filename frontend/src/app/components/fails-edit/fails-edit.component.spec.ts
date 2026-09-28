import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FailsEditComponent } from './fails-edit.component';

describe('FailsEditComponent', () => {
  let component: FailsEditComponent;
  let fixture: ComponentFixture<FailsEditComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FailsEditComponent]
    });
    fixture = TestBed.createComponent(FailsEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
