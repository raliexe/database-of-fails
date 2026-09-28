import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router} from '@angular/router';
import { UserRegister } from '../../dtos/user/userRegister';
import { NotificationService } from '../../services/notification/notification.service';
import { ManagementService } from '../../services/management/management.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(private formBuilder: FormBuilder,
              private managementService: ManagementService,
              private router: Router,
              private notificationService: NotificationService,
              private location: Location
  ) {
    this.registerForm = this.formBuilder.group({
      nickname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(32)]],
      email: ['', [Validators.required, Validators.pattern('^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$')]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      admin: [null, [Validators.required]]
    });
  }

  registerUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const user: UserRegister = new UserRegister(this.registerForm.controls.nickname.value,
        this.registerForm.controls.email.value,
        this.registerForm.controls.password.value,
        this.registerForm.controls.password.value,
        this.registerForm.controls.admin.value);
      console.log('Try to register new user: ' + user.email);
      this.managementService.createUser(user).subscribe({
        next: () => {
          this.registerForm.reset();
          const popup = this.notificationService.createMixinNotificationPopup();
          popup.fire({
            icon: 'success',
            text: 'User has been created successfully!'
          });
          console.log('Successfully signed up user: ' + user.email);
          this.router.navigate(['/users/add']);
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

    } else {
      console.error('Invalid input');
    }
  }


  ngOnInit() {
  }

  goBack(): void {
    this.location.back();
  }
}
