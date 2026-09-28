import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user/user.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { User } from '../../../dtos/user/user';

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.scss']
})
export class ProfileViewComponent implements OnInit {

  error = false;
  errorMessage = '';
  currentUser: User;

  constructor(
      private router: Router,
      private userService: UserService,
      private notificationService: NotificationService) {
  }

  ngOnInit() {
    this.userService.getCurrentUserDetails().subscribe({
      next: user => {
        this.currentUser = user;
      },
      error: error => {
        this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }

    });

  }

  edit() {
    this.router.navigate(['/profile-edit']);
  }

}
