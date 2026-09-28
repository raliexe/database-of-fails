import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { NotificationService } from '../../../services/notification/notification.service';
import { User } from '../../../dtos/user/user';
import { UserRegister } from '../../../dtos/user/userRegister';
import { Location } from '@angular/common';

@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.scss']
})
export class ProfileEditComponent implements OnInit {

  error = false;
  errorMessage = '';
  currentUser: User;
  userUpdate: UserRegister;
  //showPasswordInputFields: boolean;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private router: Router,
              private notificationService: NotificationService,
              private location: Location
  ) {
    //this.showPasswordInputFields = false;
  }

  ngOnInit() {
    this.userService.getCurrentUserDetails().subscribe({
      next: user => {
        this.currentUser = user;
        this.userUpdate = new UserRegister(this.currentUser.nickname,
          this.currentUser.email, null!, null!, null!);
      },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
        this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }


  /* public showPasswordFields() {
    this.showPasswordInputFields = !this.showPasswordInputFields;
  } */

  public updateUser() {
    this.userService.updateUser(this.userUpdate).subscribe({
      next: user => {
        if (user.token) {
          localStorage.setItem('authToken', user.token);
        }
        this.router.navigate(['/profile']);
      },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
        this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
