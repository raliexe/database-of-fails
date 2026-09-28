import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { FailsService } from '../../services/fails/fails.service';
import { Fail } from '../../dtos/fails/fail';
import { Image } from '../../dtos/image/image';

@Component({
  selector: 'app-fails',
  templateUrl: './fails.component.html',
  styleUrls: ['./fails.component.css']
})
export class FailsComponent {

  error = false;
  errorMessage = '';

  fails: Fail[];

  constructor(private formBuilder: FormBuilder,
              private failsService: FailsService) {
  }

  ngOnInit() {
    this.loadFails();
  }

  public loadFails() {
    this.failsService.getFailsByCurrentUser().subscribe({
      next: (fails: Fail[]) => {
        console.log('fails');
        console.log(fails);
        this.fails = fails;
        },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  deleteFailsById(id: number) {
    this.failsService.deleteFailsById(id).subscribe({
      next: () => {
        this.loadFails();
      },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  scrollToTop() {
    window.scroll({
      top: 0,
      behavior: 'smooth'
    });
  }

}
