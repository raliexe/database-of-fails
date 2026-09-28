import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';

import { AuthService } from '../../services/auth/auth.service';
import { UserService } from '../../services/user/user.service';
import { FailsService } from '../../services/fails/fails.service';
import { LikesService } from '../../services/likes/likes.service';
import { User } from '../../dtos/user/user';
import { Fail } from '../../dtos/fails/fail';
import { Image } from '../../dtos/image/image';

@Component({
  selector: 'app-database',
  templateUrl: './database.component.html',
  styleUrls: ['./database.component.css']
})
export class DatabaseComponent {

  error = false;
  errorMessage = '';

  fails: Fail[];

  constructor(public authService: AuthService,
              private router: Router,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private failsService: FailsService,
              private likesService: LikesService) {
  }

  ngOnInit() {
    this.loadFails();
  }

  public loadFails() {
    this.failsService.getAll().subscribe({
      next: (fails: Fail[]) => {
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

   likeFail(id: number) {
    this.likesService.likeFail(id).subscribe({
      next: () => {
        this.loadFails();
        //this.router.navigate(['']);
      },
      error: (error: any) => {
        this.error = true;
        //console.log(JSON.stringify(error));
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = error.error;
        }
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

   unlikeFail(id: number) {
    this.likesService.unlikeFail(id).subscribe({
      next: () => {
        this.loadFails();
        //this.router.navigate(['']);
      },
      error: (error: any) => {
        this.error = true;
        //console.log(JSON.stringify(error));
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = error.error;
        }
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  loadPdf(id: number) {
    this.failsService.getFailPDF(id).subscribe(
    response => {
      const file = new Blob([response], {type: 'application/pdf'});
      const fileURL = URL.createObjectURL(file);
      window.open(fileURL);
    },
    error => {
      this.error = true;
      if (typeof error.error === 'object') {
        this.errorMessage = error.error.error;
      } else {
        this.errorMessage = error.error;
      }
      //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
    });
  }

  scrollToTop() {
    window.scroll({
      top: 0,
      behavior: 'smooth'
    });
  }

}
