import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FailsCreateComponent } from './fails-create.component';

describe('FailsCreateComponent', () => {
  let component: FailsCreateComponent;
  let fixture: ComponentFixture<FailsCreateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FailsCreateComponent]
    });
    fixture = TestBed.createComponent(FailsCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
