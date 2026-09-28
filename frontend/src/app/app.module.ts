import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { httpInterceptorProviders } from './interceptors';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgxPaginationModule } from 'ngx-pagination';

import { CdkTreeModule } from '@angular/cdk/tree';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';

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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    ProfileEditComponent,
    ProfileViewComponent,
    CreateUserComponent,
    UsersManagementComponent,
    FailsComponent,
    FailsCreateComponent,
    FailsDetailsComponent,
    FailsEditComponent,
    DatabaseComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    NgSelectModule,
    NgxPaginationModule,
    CdkTreeModule,
    MatIconModule,
    MatButtonModule,
    AppRoutingModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
