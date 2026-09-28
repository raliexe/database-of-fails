import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {NotificationService} from '../../services/notification/notification.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ManagementService} from '../../services/management/management.service';
import {UserFilterDto} from '../../dtos/user/UserFilterDto';
import {User} from '../../dtos/user/user';
import {UserPagedDto} from '../../dtos/user/UserPagedDto';
import {isUndefined} from 'lodash';

@Component({
  selector: 'app-users-management',
  templateUrl: './users-management.component.html',
  styleUrls: ['./users-management.component.scss']
})
export class UsersManagementComponent implements OnInit {

  userFilterDto: UserFilterDto = new UserFilterDto();
  users: User[];
  userPagedDto: UserPagedDto;
  page = 0; // Page number sent to the backend
  currentPage = 0; // Page number displayed on the bottom of the page (off by one)
  totalElements: number;
  actualPageSize: number;
  startingItem = 1;
  endingItem: number;

  error = false;
  errorMessage = '';

  constructor(public authService: AuthService, private managementService: ManagementService,
              private notificationService: NotificationService, private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userFilterDto.email = params.email;
      this.userFilterDto.isLocked = params.isLocked;
    });
    this.userFilterDto.size = 10;
    this.search(true);
  }

  search(newSearch: boolean) {
    if (newSearch) {
      this.page = 0;
      this.currentPage = 0;
    }
    this.userFilterDto.page = this.page;
    this.managementService.getUsersPaged(this.userFilterDto).subscribe({
        next: userPagedDto => {
          this.userPagedDto = userPagedDto;
          this.users = userPagedDto.values;
          this.totalElements = userPagedDto.totalElements;
          this.actualPageSize = this.users.length;
          this.startingItem = userPagedDto.pageNumber * userPagedDto.pageSize + 1;
          this.endingItem = this.startingItem + this.actualPageSize - 1;
        },
        error: error => {
          this.error = true;
          if (typeof error.error === 'object') {
            this.errorMessage = error.error.error;
          } else {
            this.errorMessage = error.error;
          }
          this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
          this.error = false;
        }
      }
    );
  }

  onTableDataChange(event: any) {
    this.currentPage = event;
    this.page = this.currentPage - 1; // off by one
    this.search(false);
  }

  updateQueryParams() {
    const params = new URLSearchParams();
    params.append('by', 'email');
    if (this.userFilterDto.email !== null && this.userFilterDto.email !== '' && !isUndefined(this.userFilterDto.email)) {
      params.append('email', this.userFilterDto.email);
    }
    if (this.userFilterDto.isLocked !== null && !isUndefined(this.userFilterDto.isLocked)) {
      params.append('isLocked', String(this.userFilterDto.isLocked));
    }
    const queryString = params.toString();

    this.router.navigateByUrl('users?' + queryString)
      .catch(reason => this.notificationService.notifyUserOnErrorGeneric(reason.toString()));
  }

  resetFilters() {
    this.userFilterDto.email = '';
    this.userFilterDto.isLocked = null!;
    this.updateQueryParams();
  }

  changeLockStatus(id: number) {
    this.managementService.changeLockStatus(id).subscribe({
      next: () => {
        this.search(false);
        const popup = this.notificationService.createMixinNotificationPopup();
        popup.fire({
          icon: 'success',
          text: 'User status is changed!'
        });
      },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
        this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
        this.error = false;
      }
    });
  }

  resetPassword(id: number) {
    this.managementService.resetUserPassword(id).subscribe({
      next: () => {
        const popup = this.notificationService.createMixinNotificationPopup();
        popup.fire({
          icon: 'success',
          text: 'Reset password email has been sent!'
        });
      },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
        this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
        this.error = false;
      }
    });
  }

  createUser() {
    this.router.navigate(['/users/add']);
  }

}
