import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../../../services/user/user.service';
import {UserRegister} from '../../../dtos/user/userRegister';
import {NotificationService} from '../../../services/notification/notification.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router,
              private notificationService: NotificationService) {
    this.registerForm = this.formBuilder.group({
      nickname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(32)]],
      email: ['', [Validators.required, Validators.pattern('^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$')]],
      passwordConfirmation: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  registerUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const user: UserRegister = new UserRegister(this.registerForm.controls.nickname.value,
        this.registerForm.controls.email.value,
        this.registerForm.controls.password.value,
        this.registerForm.controls.passwordConfirmation.value,
        false);

      this.userService.registerUserNoImg(user).subscribe({
        next: newUser => {
          console.log('Successfully created user: ' + newUser.nickname);
          this.router.navigate(['/login']);
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
}
