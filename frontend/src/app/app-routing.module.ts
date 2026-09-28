import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './guards/auth.guard';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/user/register/register.component';
import { ProfileViewComponent } from './components/user/profile-view/profile-view.component';
import { ProfileEditComponent } from './components/user/profile-edit/profile-edit.component';
import { UsersManagementComponent } from './components/users-management/users-management.component';
import { CreateUserComponent } from './components/create-user/create-user.component';

import { FailsComponent } from './components/fails/fails.component';
import { FailsCreateComponent } from './components/fails-create/fails-create.component';
import { FailsDetailsComponent } from './components/fails-details/fails-details.component';
import { FailsEditComponent } from './components/fails-edit/fails-edit.component';
import { DatabaseComponent } from './components/database/database.component';

const routes: Routes = [
  { path: '', redirectTo: 'database', pathMatch: 'full' },
  { path: 'database', component: DatabaseComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', canActivate: [AuthGuard], component: ProfileViewComponent },
  { path: 'profile-edit', canActivate: [AuthGuard], component: ProfileEditComponent },
  { path: 'users', canActivate: [AuthGuard], component: UsersManagementComponent },
  { path: 'users/add', canActivate: [AuthGuard], component: CreateUserComponent },
  { path: 'fails', canActivate: [AuthGuard], component: FailsComponent },
  { path: 'fails/add', canActivate: [AuthGuard], component: FailsCreateComponent },
  { path: 'fails/:id', canActivate: [AuthGuard], component: FailsDetailsComponent },
  { path: 'fails/:id/edit', canActivate: [AuthGuard], component: FailsEditComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
